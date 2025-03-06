package com.example.pickitup.ai;

import com.example.pickitup.services.ChatMemoryService;
import com.example.pickitup.services.models.DocumentData;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.document.splitter.DocumentSplitters; // Fixed incorrect import
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

import java.util.ArrayList;
import java.util.List;

/**
 * RAG (Retrieval Augmented Generation) Agent that uses langchain4j to retrieve relevant information
 * and augment LLM responses
 * 
 * @author Maaz Haque
 * @version 1.0
 */
public class RagAgent {

    private final ChatLanguageModel chatModel;
    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;
    private final ChatMemoryService chatMemoryService;
    private final List<DocumentData> documentDataList;
    private Assistant assistant;
    
    /**
     * Interface defining the assistant capabilities
     */
    interface Assistant {
        @SystemMessage(LargeLanguageModelParameters.SYSTEM_MESSAGE)
        String chat(String userMessage);
    }
    
    /**
     * Constructor initializes the RAG agent with the required models
     */
    public RagAgent() {
        chatMemoryService = new ChatMemoryService();
        documentDataList = new ArrayList<>();
        
        // Initialize the chat model from parameters
        chatModel = LargeLanguageModel.createChatModel();
        
        // Initialize OpenAI embedding model
        embeddingModel = OpenAiEmbeddingModel.builder()
                .apiKey(LargeLanguageModelParameters.API_KEY)
                .modelName("text-embedding-3-small")
                .build();
        
        // Create in-memory embedding store
        embeddingStore = new InMemoryEmbeddingStore<>();
        
        // Initialize the assistant
        initializeAssistant();
    }
    
    /**
     * Initializes the assistant with the chat model, memory, and content retriever
     */
    private void initializeAssistant() {
        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(3)
                .minScore(0.6)
                .build();
        
        // Create the assistant using AiServices from langchain4j
        assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(chatModel)
                .contentRetriever(contentRetriever)
                .chatMemory(chatMemoryService.getChatMemory())
                .build();
    }
    
    /**
     * Processes a document for RAG retrieval by splitting it into chunks and embedding
     * 
     * @param document The document to add to the retrieval system
     */
    public void addDocument(DocumentData document) {

        String documentContent = document.getContent();

        // Add to tracking list
        documentDataList.add(document);
        
        // Convert to langchain4j Document
        Document langchainDoc = Document.from(
                documentContent,
                Metadata.from("source", document.getSource())
        );
        
        // Split document into segments for embedding
        List<TextSegment> segments = DocumentSplitters.recursive(500, 50)
                .split(langchainDoc);
        // Embed and store text segments
        embeddingStore.addAll(
                embeddingModel.embedAll(segments).content(),
                segments);
    }
    
    /**
     * Processes user message, stores it in memory, and gets AI response
     * 
     * @param userMessage The user message to process
     * @return The AI's response
     */
    public String processMessage(String userMessage) {
        // Add user message to memory
        chatMemoryService.addUserMessage(userMessage);
        
        // Get response from assistant
        String response = assistant.chat(userMessage);
        
        // Store AI response in memory
        chatMemoryService.addAiMessage(response);
        
        return response;
    }
    
    /**
     * Clears all documents from the embedding store
     */
    public void clearDocuments() {
        // Fixed: Now clears the embedding store properly
        embeddingStore.removeAll();
        documentDataList.clear();
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
     * Gets the list of document data
     * 
     * @return The list of document data
     */
    public List<DocumentData> getDocumentDataList() {
        return documentDataList;
    }
}
