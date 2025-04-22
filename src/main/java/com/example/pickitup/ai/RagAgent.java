/**
 * RAG (Retrieval Augmented Generation) Agent that uses langchain4j to retrieve relevant information
 * and augment LLM responses with support for multiple knowledge bases
 *
 * @author Maaz Haque
 * @date 04/23/2025
 */
package com.example.pickitup.ai;

import com.example.pickitup.services.ChatMemoryService;
import com.example.pickitup.services.models.DocumentData;
import com.example.pickitup.services.KnowledgeBaseService;
import com.example.pickitup.services.models.KnowledgeBase;
import com.example.pickitup.utils.PdfUtils;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RagAgent {
    
    // Constants for embedding model configuration
    private static final String EMBEDDING_MODEL_NAME = "text-embedding-3-small";
    private static final int DOCUMENT_CHUNK_SIZE = 500;
    private static final int DOCUMENT_CHUNK_OVERLAP = 50;
    private static final int MAX_RETRIEVAL_RESULTS = 3;
    private static final double MIN_RETRIEVAL_SCORE = 0.6;
    
    // Constants for source metadata
    private static final String SOURCE_METADATA_KEY = "source";
    private static final String PDF_SOURCE_PREFIX = "PDF: ";
    
    // Member variables for RAG functionality
    private final ChatLanguageModel chatModel;
    private final EmbeddingModel embeddingModel;
    private final ChatMemoryService chatMemoryService;
    private final KnowledgeBaseService knowledgeBaseService;
    
    // Map of knowledge base ID to its embedding store
    private final Map<String, EmbeddingStore<TextSegment>> knowledgeBaseEmbeddings;
    
    // The current active assistant tied to the active knowledge base
    private Assistant assistant;
    
    /**
     * Interface defining the assistant capabilities
     */
    interface Assistant {
        @SystemMessage(LargeLanguageModelParameters.SYSTEM_MESSAGE)
        String chat(String userMessage);
    }
    
    /**
     * Constructor initializes the RAG agent with the required models and services
     */
    public RagAgent() {
        // Initialize services
        this.chatMemoryService = new ChatMemoryService();
        this.knowledgeBaseService = new KnowledgeBaseService();
        this.knowledgeBaseEmbeddings = new HashMap<>();
        
        // Initialize the chat model from parameters
        this.chatModel = LargeLanguageModel.createChatModel();
        
        // Initialize OpenAI embedding model
        this.embeddingModel = OpenAiEmbeddingModel.builder()
                .apiKey(LargeLanguageModelParameters.API_KEY)
                .modelName(EMBEDDING_MODEL_NAME)
                .build();
        
        try {
            // Load existing knowledge bases and initialize embedding stores
            loadKnowledgeBases();
            
            // Initialize the assistant with the active knowledge base
            initializeAssistant();
        } catch (Exception e) {
            // Log the error
            System.err.println("Error initializing RagAgent: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize RagAgent", e);
        }
    }
    
    /**
     * Loads existing knowledge bases and initializes their embedding stores
     */
    private void loadKnowledgeBases() {
        // Get all knowledge bases from the service
        List<KnowledgeBase> knowledgeBases = knowledgeBaseService.getAllKnowledgeBases();
        
        // Process each knowledge base
        for (KnowledgeBase kb : knowledgeBases) {
            // Skip null knowledge bases
            if (kb == null) {
                System.err.println("Null knowledge base found during loading");
                continue;
            }
            
            try {
                // Create embedding store for this knowledge base
                EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
                knowledgeBaseEmbeddings.put(kb.getId(), embeddingStore);
                
                // Process and embed all documents in this knowledge base
                List<DocumentData> documents = kb.getDocuments();
                for (DocumentData doc : documents) {
                    // Skip null documents
                    if (doc == null) {
                        continue;
                    }
                    
                    // Embed the document
                    embedDocument(embeddingStore, doc);
                }
            } catch (Exception e) {
                // Log the error but continue processing other knowledge bases
                System.err.println("Error loading knowledge base " + kb.getName() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Initializes the assistant with the chat model, memory, and content retriever
     * using the active knowledge base
     */
    private void initializeAssistant() {
        // Get the active knowledge base
        KnowledgeBase activeKnowledgeBase = knowledgeBaseService.getActiveKnowledgeBase();
        if (activeKnowledgeBase == null) {
            // This should not happen as the service creates a default knowledge base if none exists
            throw new IllegalStateException("No active knowledge base available");
        }
        
        // Get or create the embedding store for the active knowledge base
        EmbeddingStore<TextSegment> activeEmbeddingStore = knowledgeBaseEmbeddings.get(activeKnowledgeBase.getId());
        if (activeEmbeddingStore == null) {
            // Create a new embedding store if it doesn't exist yet
            activeEmbeddingStore = new InMemoryEmbeddingStore<>();
            knowledgeBaseEmbeddings.put(activeKnowledgeBase.getId(), activeEmbeddingStore);
        }
        
        try {
            // Create a content retriever for the active embedding store
            ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                    .embeddingStore(activeEmbeddingStore)
                    .embeddingModel(embeddingModel)
                    .maxResults(MAX_RETRIEVAL_RESULTS)
                    .minScore(MIN_RETRIEVAL_SCORE)
                    .build();
            
            // Create the assistant using AiServices from langchain4j
            assistant = AiServices.builder(Assistant.class)
                    .chatLanguageModel(chatModel)
                    .contentRetriever(contentRetriever)
                    .chatMemory(chatMemoryService.getChatMemory())
                    .build();
        } catch (Exception e) {
            // Log the error
            System.err.println("Error initializing assistant: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize assistant", e);
        }
    }
    
    /**
     * Embeds a document in the specified embedding store
     * 
     * @param embeddingStore The embedding store to use
     * @param document The document to embed
     */
    private void embedDocument(EmbeddingStore<TextSegment> embeddingStore, DocumentData document) {
        // Validate parameters
        if (embeddingStore == null || document == null) {
            System.err.println("Cannot embed document: null embedding store or document");
            return;
        }
        
        // Check for empty content
        String content = document.getContent();
        if (content == null || content.trim().isEmpty()) {
            System.err.println("Cannot embed document with empty content");
            return;
        }
        
        try {
            // Convert to langchain4j Document
            Document langchainDoc = Document.from(
                    content,
                    Metadata.from(SOURCE_METADATA_KEY, document.getSource())
            );
            
            // Split document into segments for embedding
            List<TextSegment> segments = DocumentSplitters.recursive(DOCUMENT_CHUNK_SIZE, DOCUMENT_CHUNK_OVERLAP)
                    .split(langchainDoc);
            
            // Skip if no segments were created
            if (segments == null || segments.isEmpty()) {
                System.err.println("No segments created from document: " + document.getSource());
                return;
            }
            
            // Embed and store text segments
            embeddingStore.addAll(
                    embeddingModel.embedAll(segments).content(),
                    segments);
        } catch (Exception e) {
            // Log the error
            System.err.println("Error embedding document: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Creates a new knowledge base
     * 
     * @param name Name of the knowledge base
     * @param description Description of the knowledge base
     * @return The created knowledge base or null if creation failed
     */
    public KnowledgeBase createKnowledgeBase(String name, String description) {
        // Validate parameters
        if (name == null || name.trim().isEmpty()) {
            System.err.println("Cannot create knowledge base with null or empty name");
            return null;
        }
        
        try {
            // Create the knowledge base using the service
            KnowledgeBase kb = knowledgeBaseService.createKnowledgeBase(name, description);
            
            if (kb != null) {
                // Create embedding store for new knowledge base
                knowledgeBaseEmbeddings.put(kb.getId(), new InMemoryEmbeddingStore<>());
            }
            
            return kb;
        } catch (Exception e) {
            // Log the error
            System.err.println("Error creating knowledge base: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Sets the active knowledge base and reinitializes the assistant
     * 
     * @param knowledgeBaseId ID of the knowledge base to set as active
     * @return true if successful, false if the ID is invalid
     */
    public boolean setActiveKnowledgeBase(String knowledgeBaseId) {
        // Validate parameter
        if (knowledgeBaseId == null || knowledgeBaseId.trim().isEmpty()) {
            System.err.println("Cannot set active knowledge base with null or empty ID");
            return false;
        }
        
        try {
            // Set the active knowledge base using the service
            boolean success = knowledgeBaseService.setActiveKnowledgeBase(knowledgeBaseId);
            
            if (success) {
                // Reinitialize the assistant with the new active knowledge base
                initializeAssistant();
            }
            
            return success;
        } catch (Exception e) {
            // Log the error
            System.err.println("Error setting active knowledge base: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Gets the active knowledge base
     * 
     * @return The active knowledge base
     */
    public KnowledgeBase getActiveKnowledgeBase() {
        return knowledgeBaseService.getActiveKnowledgeBase();
    }
    
    /**
     * Gets all knowledge bases
     * 
     * @return List of all knowledge bases
     */
    public List<KnowledgeBase> getAllKnowledgeBases() {
        return knowledgeBaseService.getAllKnowledgeBases();
    }
    
    /**
     * Adds a document to a specific knowledge base
     * 
     * @param knowledgeBaseId ID of the knowledge base to add the document to
     * @param document The document to add
     * @return true if successful, false otherwise
     */
    public boolean addDocumentToKnowledgeBase(String knowledgeBaseId, DocumentData document) {
        // Validate parameters
        if (knowledgeBaseId == null || knowledgeBaseId.trim().isEmpty()) {
            System.err.println("Cannot add document to knowledge base with null or empty ID");
            return false;
        }
        
        if (document == null) {
            System.err.println("Cannot add null document to knowledge base");
            return false;
        }
        
        try {
            // Add the document to the knowledge base using the service
            boolean success = knowledgeBaseService.addDocumentToKnowledgeBase(knowledgeBaseId, document);
            
            if (success) {
                // Get the embedding store for this knowledge base
                EmbeddingStore<TextSegment> embeddingStore = knowledgeBaseEmbeddings.get(knowledgeBaseId);
                if (embeddingStore == null) {
                    // Create a new embedding store if it doesn't exist yet
                    embeddingStore = new InMemoryEmbeddingStore<>();
                    knowledgeBaseEmbeddings.put(knowledgeBaseId, embeddingStore);
                }
                
                // Embed the document
                embedDocument(embeddingStore, document);
                
                // If this is the active knowledge base, reinitialize the assistant
                KnowledgeBase activeKnowledgeBase = knowledgeBaseService.getActiveKnowledgeBase();
                if (activeKnowledgeBase != null && knowledgeBaseId.equals(activeKnowledgeBase.getId())) {
                    initializeAssistant();
                }
            }
            
            return success;
        } catch (Exception e) {
            // Log the error
            System.err.println("Error adding document to knowledge base: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Adds a document to the active knowledge base
     * 
     * @param document The document to add
     * @return true if successful, false otherwise
     */
    public boolean addDocument(DocumentData document) {
        // Validate parameter
        if (document == null) {
            System.err.println("Cannot add null document");
            return false;
        }
        
        // Get the active knowledge base
        KnowledgeBase activeKnowledgeBase = knowledgeBaseService.getActiveKnowledgeBase();
        if (activeKnowledgeBase == null) {
            System.err.println("No active knowledge base available");
            return false;
        }
        
        // Add document to the active knowledge base
        return addDocumentToKnowledgeBase(activeKnowledgeBase.getId(), document);
    }
    
    /**
     * Adds a PDF document to a specific knowledge base
     * 
     * @param knowledgeBaseId ID of the knowledge base to add the PDF to
     * @param pdfFile The PDF file to process
     * @return true if successful, false otherwise
     */
    public boolean addPdfDocumentToKnowledgeBase(String knowledgeBaseId, File pdfFile) {
        // Validate parameters
        if (knowledgeBaseId == null || knowledgeBaseId.trim().isEmpty()) {
            System.err.println("Cannot add PDF to knowledge base with null or empty ID");
            return false;
        }
        
        if (pdfFile == null || !pdfFile.exists() || !pdfFile.isFile()) {
            System.err.println("Invalid PDF file: file is null or does not exist");
            return false;
        }
        
        try {
            // Extract text from PDF
            String pdfContent = PdfUtils.extractTextFromPdf(pdfFile);
            
            // Check if content was extracted
            if (pdfContent == null || pdfContent.trim().isEmpty()) {
                System.err.println("No content extracted from PDF file: " + pdfFile.getName());
                return false;
            }
            
            // Create document data
            DocumentData documentData = new DocumentData(
                pdfContent,
                PDF_SOURCE_PREFIX + pdfFile.getName()
            );
            
            // Add document to the specified knowledge base
            return addDocumentToKnowledgeBase(knowledgeBaseId, documentData);
        } catch (IOException e) {
            // Log the error
            System.err.println("Error reading PDF file: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            // Log the error
            System.err.println("Error processing PDF document: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Adds a PDF document to the active knowledge base
     * 
     * @param pdfFile The PDF file to process
     * @return true if successful, false otherwise
     */
    public boolean addPdfDocument(File pdfFile) {
        // Validate parameter
        if (pdfFile == null || !pdfFile.exists() || !pdfFile.isFile()) {
            System.err.println("Invalid PDF file: file is null or does not exist");
            return false;
        }
        
        // Get the active knowledge base
        KnowledgeBase activeKnowledgeBase = knowledgeBaseService.getActiveKnowledgeBase();
        if (activeKnowledgeBase == null) {
            System.err.println("No active knowledge base available");
            return false;
        }
        
        // Add PDF to the active knowledge base
        return addPdfDocumentToKnowledgeBase(activeKnowledgeBase.getId(), pdfFile);
    }
    
    /**
     * Processes user message, stores it in memory, and gets AI response
     * 
     * @param userMessage The user message to process
     * @return The AI's response or an error message if processing fails
     */
    public String processMessage(String userMessage) {
        // Validate parameter
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return "Message cannot be empty";
        }
        
        // Check if assistant is initialized
        if (assistant == null) {
            return "AI assistant is not initialized";
        }
        
        // Add user message to memory
        chatMemoryService.addUserMessage(userMessage);
        
        try {
            // Get response from assistant
            String response = assistant.chat(userMessage);
            
            // Store AI response in memory
            chatMemoryService.addAiMessage(response);
            
            return response;
        } catch (Exception e) {
            // Log the exception for debugging
            System.err.println("Error processing message: " + e.getMessage());
            e.printStackTrace();
            
            // Return a user-friendly error message
            return "Error generating response, check connection or message content";
        }
    }
    
    /**
     * Clears all documents from a specific knowledge base
     * 
     * @param knowledgeBaseId ID of the knowledge base to clear
     * @return true if successful, false otherwise
     */
    public boolean clearKnowledgeBase(String knowledgeBaseId) {
        // Validate parameter
        if (knowledgeBaseId == null || knowledgeBaseId.trim().isEmpty()) {
            System.err.println("Cannot clear knowledge base with null or empty ID");
            return false;
        }
        
        try {
            // Get the knowledge base
            KnowledgeBase kb = knowledgeBaseService.getKnowledgeBase(knowledgeBaseId);
            if (kb == null) {
                System.err.println("Knowledge base not found: " + knowledgeBaseId);
                return false;
            }
            
            // Clear the embedding store
            EmbeddingStore<TextSegment> embeddingStore = knowledgeBaseEmbeddings.get(knowledgeBaseId);
            if (embeddingStore != null) {
                embeddingStore.removeAll();
            }
            
            // Clear documents from the knowledge base
            List<DocumentData> documents = new ArrayList<>(kb.getDocuments());
            for (DocumentData doc : documents) {
                kb.removeDocument(doc);
            }
            
            // Save the empty knowledge base
            knowledgeBaseService.addDocumentToKnowledgeBase(knowledgeBaseId, new DocumentData("", "Empty Document"));
            
            // If this is the active knowledge base, reinitialize the assistant
            KnowledgeBase activeKnowledgeBase = knowledgeBaseService.getActiveKnowledgeBase();
            if (activeKnowledgeBase != null && knowledgeBaseId.equals(activeKnowledgeBase.getId())) {
                initializeAssistant();
            }
            
            return true;
        } catch (Exception e) {
            // Log the error
            System.err.println("Error clearing knowledge base: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Clears all documents from the active knowledge base
     */
    public void clearDocuments() {
        // Get the active knowledge base
        KnowledgeBase activeKnowledgeBase = knowledgeBaseService.getActiveKnowledgeBase();
        if (activeKnowledgeBase == null) {
            System.err.println("No active knowledge base available");
            return;
        }
        
        // Clear the active knowledge base
        clearKnowledgeBase(activeKnowledgeBase.getId());
    }
    
    /**
     * Gets the chat memory service
     * 
     * @return The chat memory service
     */
    public ChatMemoryService getChatMemoryService() {
        return chatMemoryService;
    }
    
    /**
     * Gets the knowledge base service
     * 
     * @return The knowledge base service
     */
    public KnowledgeBaseService getKnowledgeBaseService() {
        return knowledgeBaseService;
    }
}
