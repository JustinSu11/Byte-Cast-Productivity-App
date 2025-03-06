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
 * @author GitHub Copilot
 * @version 1.0
 */
public class ChatMemoryService {
    
    private final ChatMemoryDAO chatMemoryDAO;
    private ChatMemory chatMemory;
    
    /**
     * Constructor initializes the chat memory and loads existing messages
     */
    public ChatMemoryService() {
        chatMemoryDAO = new ChatMemoryDAO();
        chatMemory = MessageWindowChatMemory.builder()
                .maxMessages(20)
                .build();
        
        // Load existing messages from the database
        loadMemoryFromDatabase();
    }
    
    /**
     * Loads chat messages from the database into the in-memory chat memory
     */
    private void loadMemoryFromDatabase() {
        List<ChatMemoryDAO.ChatMessage> messages = chatMemoryDAO.getAllMessages();
        
        for (ChatMemoryDAO.ChatMessage message : messages) {
            if ("user".equals(message.getRole())) {
                chatMemory.add(dev.langchain4j.data.message.UserMessage.from(message.getContent()));
            } else if ("assistant".equals(message.getRole())) {
                chatMemory.add(dev.langchain4j.data.message.AiMessage.from(message.getContent()));
            }
        }
    }
    
    /**
     * Adds a user message to memory and persists it
     * 
     * @param userMessage The message from the user
     */
    public void addUserMessage(String userMessage) {
        chatMemory.add(dev.langchain4j.data.message.UserMessage.from(userMessage));
        chatMemoryDAO.saveMessage("user", userMessage);
    }
    
    /**
     * Adds an AI response to memory and persists it
     * 
     * @param aiResponse The response from the AI
     */
    public void addAiMessage(String aiResponse) {
        chatMemory.add(dev.langchain4j.data.message.AiMessage.from(aiResponse));
        chatMemoryDAO.saveMessage("assistant", aiResponse);
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
        chatMemory = MessageWindowChatMemory.builder()
                .maxMessages(20)
                .build();
        chatMemoryDAO.clearAllMessages();
    }
    
    /**
     * Gets all messages as a formatted string for display
     * 
     * @return Formatted chat history
     */
    public String getFormattedChatHistory() {
        List<ChatMemoryDAO.ChatMessage> messages = chatMemoryDAO.getAllMessages();
        StringBuilder history = new StringBuilder();
        
        for (ChatMemoryDAO.ChatMessage message : messages) {
            String role = "user".equals(message.getRole()) ? "You" : "Assistant";
            history.append(role).append(": ").append(message.getContent()).append("\n\n");
        }
        
        return history.toString();
    }
}
