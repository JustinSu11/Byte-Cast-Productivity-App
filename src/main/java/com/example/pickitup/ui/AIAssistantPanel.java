package com.example.pickitup.ui;

import com.example.pickitup.ai.RagAgent;
import com.example.pickitup.services.models.DocumentData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel for AI Assistant functionality in the UI
 * @author Maaz Haque
 * @version 1.0
 */
public class AIAssistantPanel extends JPanel
{

    private final RagAgent ragAgent;
    private final JTextArea chatHistoryArea;
    private final JTextField userInputField;
    private final JButton sendButton;
    private final JButton clearButton;
    private final JButton shareWithAIButton;
    private NoteEditor noteEditor;

    /**
     * Constructor initializes the AI Assistant panel
     */
    public AIAssistantPanel(NoteEditor noteEditor) {
        // Initialize RAG Agent
        ragAgent = new RagAgent();
        // Set note editor
        this.noteEditor = noteEditor;
        // Set up layout
        setLayout(new BorderLayout());

        // Create a title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("AI Assistant", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        // Create chat history display area
        chatHistoryArea = new JTextArea();
        chatHistoryArea.setEditable(false);
        chatHistoryArea.setLineWrap(true);
        chatHistoryArea.setWrapStyleWord(true);
        chatHistoryArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(chatHistoryArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        // Create user input field and send button
        userInputField = new JTextField();
        userInputField.setFont(new Font("Arial", Font.PLAIN, 14));
        userInputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLUE, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        // Create buttons
        sendButton = new JButton("Send");
        clearButton = new JButton("Clear Chat");
        shareWithAIButton = new JButton("Share Note with AI");
        // Make buttons more visible with colors and fonts
        shareWithAIButton.setBackground(new Color(0, 150, 136));
        shareWithAIButton.setForeground(Color.WHITE);
        shareWithAIButton.setFont(new Font("Arial", Font.BOLD, 14));

        sendButton.setBackground(new Color(63, 81, 181));
        sendButton.setForeground(Color.WHITE);

        // Create panel for the share button (top of the panel)
        JPanel sharePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        sharePanel.add(shareWithAIButton);
        titlePanel.add(sharePanel, BorderLayout.SOUTH);
        
        // Create panel for input components
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(userInputField, BorderLayout.CENTER);
        
        // Create panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(sendButton);
        buttonPanel.add(clearButton);
        inputPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Add components to main panel
        add(titlePanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
        
        // Set up event handlers
        setupEventHandlers();

        //set preferred size for better visibility
        setPreferredSize(new Dimension(350, 600));

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

        // Share with AI button action
        shareWithAIButton.addActionListener(e -> addNoteAsDocument());
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
     */
    public void addNoteAsDocument() {
        String noteContent = noteEditor.getTextInTextEditor();
        if (noteContent != null && !noteContent.trim().isEmpty()) {
            DocumentData document = new DocumentData(noteContent, "Current Note");
            ragAgent.addDocument(document);
            JOptionPane.showMessageDialog(this, 
                    "Note added to AI Assistant's knowledge base.",
                    "Document Added", 
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Updates the note editor reference
     * 
     * @param noteEditor The new note editor to use
     */
    public void setNoteEditor(NoteEditor noteEditor) {
        this.noteEditor = noteEditor;
    }
}
