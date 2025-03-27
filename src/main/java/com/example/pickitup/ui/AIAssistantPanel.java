package com.example.pickitup.ui;

import com.example.pickitup.ai.RagAgent;
import com.example.pickitup.services.models.DocumentData;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Panel for AI Assistant functionality in the UI
 */
public class AIAssistantPanel extends JPanel {

    private final RagAgent ragAgent;
    private final JTextArea chatHistoryArea;
    private final JTextField userInputField;
    private final JButton sendButton;
    private final JButton clearButton;
    private final JButton shareWithAIButton;
    private final JButton uploadPdfButton;
    private JFileChooser fileChooser;
    private ScrollPane noteEditor;
    private JLabel statusLabel;

    /**
     * Constructor initializes the AI Assistant panel
     */
    public AIAssistantPanel(ScrollPane noteEditor) {
        // Initialize RAG Agent
        ragAgent = new RagAgent();
        // Set note editor
        this.noteEditor = noteEditor;
        // Set up layout
        setLayout(new BorderLayout());

        // Initialize file chooser
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));

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
        uploadPdfButton = new JButton("Upload PDF");
        
        // Make buttons more visible with colors and fonts
        shareWithAIButton.setBackground(new Color(0, 150, 136));
        shareWithAIButton.setForeground(Color.WHITE);
        shareWithAIButton.setFont(new Font("Arial", Font.BOLD, 14));

        uploadPdfButton.setBackground(new Color(255, 87, 34));
        uploadPdfButton.setForeground(Color.WHITE);
        uploadPdfButton.setFont(new Font("Arial", Font.BOLD, 14));

        sendButton.setBackground(new Color(63, 81, 181));
        sendButton.setForeground(Color.WHITE);

        // Create panel for the share and upload buttons (top of the panel)
        JPanel sharePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        sharePanel.add(shareWithAIButton);
        sharePanel.add(uploadPdfButton);
        titlePanel.add(sharePanel, BorderLayout.SOUTH);
        
        // Add status label for indicating background processing
        statusLabel = new JLabel("");
        statusLabel.setForeground(new Color(33, 150, 243));
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        
        // Create panel for input components
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(userInputField, BorderLayout.CENTER);
        
        // Create panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(statusLabel);
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
        
        // Upload PDF button action
        uploadPdfButton.addActionListener(e -> uploadPdfDocument());
    }
    
    /**
     * Uploads and processes a PDF document
     */
    private void uploadPdfDocument() {
        int result = fileChooser.showOpenDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            
            // Show loading message
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            JOptionPane pane = new JOptionPane("Processing PDF document...", 
                    JOptionPane.INFORMATION_MESSAGE, 
                    JOptionPane.DEFAULT_OPTION, 
                    null, 
                    new Object[]{}, 
                    null);
            JDialog dialog = pane.createDialog(this, "Please Wait");
            dialog.setModal(false);
            dialog.setVisible(true);
            
            // Process in background thread to avoid UI freeze
            SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() {
                    return ragAgent.addPdfDocument(selectedFile);
                }

                @Override
                protected void done() {
                    dialog.setVisible(false);
                    setCursor(Cursor.getDefaultCursor());
                    try {
                        boolean success = get();
                        if (success) {
                            JOptionPane.showMessageDialog(AIAssistantPanel.this,
                                    "PDF document successfully added to AI Assistant's knowledge base.",
                                    "Document Added",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(AIAssistantPanel.this,
                                    "Failed to process PDF document.",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(AIAssistantPanel.this,
                                "An error occurred: " + e.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    }
                }
            };
            worker.execute();
        }
    }
    
    /**
     * Sends user message to RAG agent and displays response
     */
    private void sendMessage() {
        String userMessage = userInputField.getText().trim();
        if (!userMessage.isEmpty()) {
            // Disable input and show processing status
            userInputField.setEnabled(false);
            sendButton.setEnabled(false);
            statusLabel.setText("Processing...");
            
            // Temporarily display user message for better UX
            String currentChat = chatHistoryArea.getText();
            chatHistoryArea.setText(currentChat + (currentChat.isEmpty() ? "" : "\n\n") + "You: " + userMessage);
            
            // Clear input field
            userInputField.setText("");
            
            // Process message through RAG agent in background
            SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() {
                    // Process message and get AI response
                    return ragAgent.processMessage(userMessage);
                }

                @Override
                protected void done() {
                    try {
                        // Get AI response
                        String aiResponse = get();
                        
                        // Update display with AI response
                        updateChatDisplay();
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                        // Handle error in AI processing
                        chatHistoryArea.append("\n\nAI Assistant: Sorry, an error occurred while processing your request.\n");
                    } finally {
                        // Re-enable input
                        userInputField.setEnabled(true);
                        sendButton.setEnabled(true);
                        statusLabel.setText("");
                        
                        // Request focus back to the input field
                        userInputField.requestFocus();
                    }
                }
            };
            worker.execute();
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
     */
    public void addNoteAsDocument() {
        String noteContent = noteEditor.getTextInTextEditor();
        if (noteContent != null && !noteContent.trim().isEmpty()) {
            // Show processing indicator
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            shareWithAIButton.setEnabled(false);
            statusLabel.setText("Processing note...");
            
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() {
                    DocumentData document = new DocumentData(noteContent, "Current Note");
                    ragAgent.addDocument(document);
                    return null;
                }

                @Override
                protected void done() {
                    setCursor(Cursor.getDefaultCursor());
                    shareWithAIButton.setEnabled(true);
                    statusLabel.setText("");
                    JOptionPane.showMessageDialog(AIAssistantPanel.this, 
                            "Note added to AI Assistant's knowledge base.",
                            "Document Added", 
                            JOptionPane.INFORMATION_MESSAGE);
                }
            };
            worker.execute();
        }
    }
}
