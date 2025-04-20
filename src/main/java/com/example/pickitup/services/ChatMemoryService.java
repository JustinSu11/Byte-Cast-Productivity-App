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
    private static final String USER_ROLE_IDENTIFIER = "user";
    private static final String ASSISTANT_ROLE_IDENTIFIER = "assistant";
    private static final String USER_DISPLAY_NAME = "You";
    private static final String ASSISTANT_DISPLAY_NAME = "Assistant";
    
    // Constants for configuration
    private static final int MAXIMUM_MESSAGE_COUNT = 20;
    private static final String MESSAGE_DISPLAY_SEPARATOR = "\n\n";
    
    // Error messages
    private static final String USER_MESSAGE_ERROR = "Error adding user message to memory: ";
    private static final String AI_MESSAGE_ERROR = "Error adding AI message to memory: ";
    private static final String MEMORY_LOADING_ERROR = "Error loading chat memory: ";
    private static final String MEMORY_CLEARING_ERROR = "Error clearing chat memory: ";
    
    // Service dependencies
    private final ChatMemoryDAO chatMemoryDAO;
    private ChatMemory activeChatMemory;
    
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
            System.err.println(MEMORY_LOADING_ERROR + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Initializes a new chat memory instance
     */
    private void initializeMemory() {
        this.activeChatMemory = MessageWindowChatMemory.builder()
                .maxMessages(MAXIMUM_MESSAGE_COUNT)
                .build();
    }
    
    /**
     * Loads chat messages from the database into the in-memory chat memory
     */
    private void loadMemoryFromDatabase() {
        // Get messages from database
        List<ChatMemoryDAO.ChatMessage> storedMessages = chatMemoryDAO.getAllMessages();
        
        // Skip if no messages
        if (storedMessages == null || storedMessages.isEmpty()) {
            return;
        }
        
        // Process each message
        for (ChatMemoryDAO.ChatMessage chatMessageEntry : storedMessages) {
            if (chatMessageEntry == null) {
                continue;
            }
            
            String messageRole = chatMessageEntry.getRole();
            String messageContent = chatMessageEntry.getContent();
            
            // Skip messages with null or empty content
            if (messageContent == null || messageContent.trim().isEmpty()) {
                continue;
            }
            
            // Add to chat memory based on role
            if (USER_ROLE_IDENTIFIER.equals(messageRole)) {
                activeChatMemory.add(dev.langchain4j.data.message.UserMessage.from(messageContent));
            } else if (ASSISTANT_ROLE_IDENTIFIER.equals(messageRole)) {
                activeChatMemory.add(dev.langchain4j.data.message.AiMessage.from(messageContent));
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
            activeChatMemory.add(dev.langchain4j.data.message.UserMessage.from(userMessage));
            
            // Save to database
            chatMemoryDAO.saveMessage(USER_ROLE_IDENTIFIER, userMessage);
        } catch (Exception e) {
            System.err.println(USER_MESSAGE_ERROR + e.getMessage());
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
            activeChatMemory.add(dev.langchain4j.data.message.AiMessage.from(aiResponse));
            
            // Save to database
            chatMemoryDAO.saveMessage(ASSISTANT_ROLE_IDENTIFIER, aiResponse);
        } catch (Exception e) {
            System.err.println(AI_MESSAGE_ERROR + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Gets the current chat memory
     * 
     * @return The chat memory object
     */
    public ChatMemory getChatMemory() {
        return activeChatMemory;
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
            System.err.println(MEMORY_CLEARING_ERROR + e.getMessage());
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
            List<ChatMemoryDAO.ChatMessage> storedMessages = chatMemoryDAO.getAllMessages();
            
            // Return empty string if no messages
            if (storedMessages == null || storedMessages.isEmpty()) {
                return "";
            }
            
            // Format the message history
            StringBuilder formattedHistory = new StringBuilder();
            
            for (ChatMemoryDAO.ChatMessage chatMessageEntry : storedMessages) {
                if (chatMessageEntry == null) {
                    continue;
                }
                
                String displayName = USER_ROLE_IDENTIFIER.equals(chatMessageEntry.getRole()) ? USER_DISPLAY_NAME : ASSISTANT_DISPLAY_NAME;
                String messageContent = chatMessageEntry.getContent();
                
                if (messageContent != null && !messageContent.trim().isEmpty()) {
                    formattedHistory.append(displayName).append(": ").append(messageContent).append(MESSAGE_DISPLAY_SEPARATOR);
                }
            }
            
            return formattedHistory.toString();
        } catch (Exception e) {
            System.err.println("Error formatting chat history: " + e.getMessage());
            e.printStackTrace();
            return "Error retrieving chat history";
        }
    }
}
