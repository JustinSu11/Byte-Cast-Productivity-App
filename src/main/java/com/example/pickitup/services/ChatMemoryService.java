package com.example.pickitup.services;

import com.example.pickitup.services.dao.ChatMemoryDAO;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing chat memory and persistence
 * @author Maaz Haque
 * @version 1.0
 */
public class ChatMemoryService {
    
    // Constants for roles
    private static final String ROLE_USER = "user";
    private static final String ROLE_ASSISTANT = "assistant";
    private static final String DISPLAY_USER = "You";
    private static final String DISPLAY_ASSISTANT = "Assistant";
    
    // Constants for configuration
    private static final int MAX_MESSAGES = 20;
    private static final String MESSAGE_SEPARATOR = "\n\n";
    
    // Error messages
    private static final String ERROR_ADDING_USER_MSG = "Error adding user message to memory: ";
    private static final String ERROR_ADDING_AI_MSG = "Error adding AI message to memory: ";
    private static final String ERROR_LOADING_MEMORY = "Error loading chat memory: ";
    private static final String ERROR_CLEARING_MEMORY = "Error clearing chat memory: ";
    
    // Service dependencies
    private final ChatMemoryDAO chatMemoryDAO;
    private ChatMemory chatMemory;
    
    /**
     * Constructor initializes the chat memory and loads existing messages
     */
    public ChatMemoryService() {
        this.chatMemoryDAO = new ChatMemoryDAO();
        initializeMemory();
        
        try {
            // Load existing messages from the database
            loadMemoryFromDatabase();
        } catch (Exception e) {
            System.err.println(ERROR_LOADING_MEMORY + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Initializes a new chat memory instance
     */
    private void initializeMemory() {
        this.chatMemory = MessageWindowChatMemory.builder()
                .maxMessages(MAX_MESSAGES)
                .build();
    }
    
    /**
     * Loads chat messages from the database into the in-memory chat memory
     */
    private void loadMemoryFromDatabase() {
        // Get messages from database
        List<ChatMemoryDAO.ChatMessage> messages = chatMemoryDAO.getAllMessages();
        
        // Skip if no messages
        if (messages == null || messages.isEmpty()) {
            return;
        }
        
        // Process each message
        for (ChatMemoryDAO.ChatMessage message : messages) {
            if (message == null) {
                continue;
            }
            
            String role = message.getRole();
            String content = message.getContent();
            
            // Skip messages with null or empty content
            if (content == null || content.trim().isEmpty()) {
                continue;
            }
            
            // Add to chat memory based on role
            if (ROLE_USER.equals(role)) {
                chatMemory.add(dev.langchain4j.data.message.UserMessage.from(content));
            } else if (ROLE_ASSISTANT.equals(role)) {
                chatMemory.add(dev.langchain4j.data.message.AiMessage.from(content));
            }
        }
    }
    
    /**
     * Adds a user message to memory and persists it
     * 
     * @param userMessage The message from the user
     */
    public void addUserMessage(String userMessage) {
        // Validate input
        if (userMessage == null || userMessage.trim().isEmpty()) {
            System.err.println("Cannot add empty user message to memory");
            return;
        }
        
        try {
            // Add to in-memory chat
            chatMemory.add(dev.langchain4j.data.message.UserMessage.from(userMessage));
            
            // Save to database
            chatMemoryDAO.saveMessage(ROLE_USER, userMessage);
        } catch (Exception e) {
            System.err.println(ERROR_ADDING_USER_MSG + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Adds an AI response to memory and persists it
     * 
     * @param aiResponse The response from the AI
     */
    public void addAiMessage(String aiResponse) {
        // Validate input
        if (aiResponse == null || aiResponse.trim().isEmpty()) {
            System.err.println("Cannot add empty AI message to memory");
            return;
        }
        
        try {
            // Add to in-memory chat
            chatMemory.add(dev.langchain4j.data.message.AiMessage.from(aiResponse));
            
            // Save to database
            chatMemoryDAO.saveMessage(ROLE_ASSISTANT, aiResponse);
        } catch (Exception e) {
            System.err.println(ERROR_ADDING_AI_MSG + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Gets the current chat memory
     * 
     * @return The chat memory object
     */
    public ChatMemory getChatMemory() {
        return chatMemory;
    }
    
    /**
     * Clears all chat memory both in-memory and from the database
     */
    public void clearMemory() {
        try {
            // Reset in-memory chat
            initializeMemory();
            
            // Clear database
            chatMemoryDAO.clearAllMessages();
        } catch (Exception e) {
            System.err.println(ERROR_CLEARING_MEMORY + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Gets all messages as a formatted string for display
     * 
     * @return Formatted chat history
     */
    public String getFormattedChatHistory() {
        try {
            // Get messages from database
            List<ChatMemoryDAO.ChatMessage> messages = chatMemoryDAO.getAllMessages();
            
            // Return empty string if no messages
            if (messages == null || messages.isEmpty()) {
                return "";
            }
            
            // Format the message history
            StringBuilder history = new StringBuilder();
            
            for (ChatMemoryDAO.ChatMessage message : messages) {
                if (message == null) {
                    continue;
                }
                
                String role = ROLE_USER.equals(message.getRole()) ? DISPLAY_USER : DISPLAY_ASSISTANT;
                String content = message.getContent();
                
                if (content != null && !content.trim().isEmpty()) {
                    history.append(role).append(": ").append(content).append(MESSAGE_SEPARATOR);
                }
            }
            
            return history.toString();
        } catch (Exception e) {
            System.err.println("Error formatting chat history: " + e.getMessage());
            e.printStackTrace();
            return "Error retrieving chat history";
        }
    }
}
