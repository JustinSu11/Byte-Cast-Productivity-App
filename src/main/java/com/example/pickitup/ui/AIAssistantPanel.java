package com.example.pickitup.ui;

import com.example.pickitup.ai.RagAgent;
import com.example.pickitup.services.models.DocumentData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel for AI Assistant functionality in the UI
 * @author GitHub Copilot
 * @version 1.0
 */
public class AIAssistantPanel extends JPanel {

    private final RagAgent ragAgent;
    private final JTextArea chatHistoryArea;
    private final JTextField userInputField;
    private final JButton sendButton;
    private final JButton clearButton;

    /**
     * Constructor initializes the AI Assistant panel
     */
    public AIAssistantPanel() {
        // Initialize RAG Agent
        ragAgent = new RagAgent();
        
        // Set up layout
        setLayout(new BorderLayout());
        
        // Create chat history display area
        chatHistoryArea = new JTextArea();
        chatHistoryArea.setEditable(false);
        chatHistoryArea.setLineWrap(true);
        chatHistoryArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(chatHistoryArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        // Create user input field and send button
        userInputField = new JTextField();
        sendButton = new JButton("Send");
        clearButton = new JButton("Clear Chat");
        
        // Create panel for input components
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(userInputField, BorderLayout.CENTER);
        
        // Create panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(sendButton);
        buttonPanel.add(clearButton);
        inputPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Add components to main panel
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
        
        // Set up event handlers
        setupEventHandlers();
        
        // Load previous chat history if any
        updateChatDisplay();
    }
    
    /**
     * Sets up event handlers for buttons and input field
     */
    private void setupEventHandlers() {
        // Send button action
        sendButton.addActionListener(e -> sendMessage());
        
        // Enter key in the input field also sends message
        userInputField.addActionListener(e -> sendMessage());
        
        // Clear button action
        clearButton.addActionListener(e -> clearChat());
    }
    
    /**
     * Sends user message to RAG agent and displays response
     */
    private void sendMessage() {
        String userMessage = userInputField.getText().trim();
        if (!userMessage.isEmpty()) {
            // Process message through RAG agent
            String aiResponse = ragAgent.processMessage(userMessage);
            
            // Clear input field
            userInputField.setText("");
            
            // Update chat display
            updateChatDisplay();
            
            // Request focus back to the input field
            userInputField.requestFocus();
        }
    }
    
    /**
     * Updates the chat display with current chat history
     */
    private void updateChatDisplay() {
        chatHistoryArea.setText(ragAgent.getChatMemoryService().getFormattedChatHistory());
        
        // Scroll to the bottom of the chat
        chatHistoryArea.setCaretPosition(chatHistoryArea.getDocument().getLength());
    }
    
    /**
     * Clears the chat history
     */
    private void clearChat() {
        ragAgent.getChatMemoryService().clearMemory();
        updateChatDisplay();
    }
    
    /**
     * Adds current note text as a document to the RAG agent
     * 
     * @param noteTitle The title of the note
     * @param noteContent The content of the note
     */
    public void addNoteAsDocument(String noteTitle, String noteContent) {
        if (noteContent != null && !noteContent.trim().isEmpty()) {
            DocumentData document = new DocumentData(noteContent, noteTitle);
            ragAgent.addDocument(document);
            JOptionPane.showMessageDialog(this, 
                    "Note added to AI Assistant's knowledge base.",
                    "Document Added", 
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
