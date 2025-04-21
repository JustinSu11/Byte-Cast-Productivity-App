/**
 * Makes the AI functional in the UI
 *
 * @author Maaz Haque
 * @date 04/12/2025
 */
package com.example.pickitup.ui;

import com.example.pickitup.ai.RagAgent;
import com.example.pickitup.services.models.DocumentData;
import com.example.pickitup.services.models.KnowledgeBase;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;


public class AIAssistantPanel extends JPanel {

    private final RagAgent ragAgent;
    private final JTextArea conversationHistoryArea;
    private final JTextField userMessageField;
    private final JButton sendMessageButton;
    private final JButton clearChatButton;
    private final JButton shareNoteButton;
    private final JButton uploadPdfButton;
    private JComboBox<KnowledgeBase> knowledgeBaseSelector;
    private JButton createKnowledgeBaseButton;
    private JButton knowledgeBaseDetailsButton;
    private JFileChooser pdfFileChooser;
    private NoteEditor noteEditor;
    private JLabel processingStatusLabel;

    /**
     * Constructor initializes the AI Assistant panel
     */
    public AIAssistantPanel(JournalsPane journalsPane) {
        // Initialize RAG Agent
        ragAgent = new RagAgent();
        // Set note editor
        this.noteEditor = journalsPane.getSelectedNotesPane().getCurrentNoteEditor();
        // Set up layout
        setLayout(new BorderLayout());

        // Initialize file chooser
        pdfFileChooser = new JFileChooser();
        pdfFileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));

        // Create a title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("AI Assistant", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        // Create knowledge base selector panel
        JPanel knowledgeBasePanel = createKnowledgeBasePanel();
        titlePanel.add(knowledgeBasePanel, BorderLayout.NORTH);

        // Create chat history display area
        conversationHistoryArea = new JTextArea();
        conversationHistoryArea.setEditable(false);
        conversationHistoryArea.setLineWrap(true);
        conversationHistoryArea.setWrapStyleWord(true);
        conversationHistoryArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane conversationScrollPane = new JScrollPane(conversationHistoryArea);
        conversationScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Create user input field and send button
        userMessageField = new JTextField();
        userMessageField.setFont(new Font("Arial", Font.PLAIN, 14));
        userMessageField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLUE, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        // Create buttons
        sendMessageButton = new JButton("Send");
        clearChatButton = new JButton("Clear Chat");
        shareNoteButton = new JButton("Share Note with AI");
        uploadPdfButton = new JButton("Upload PDF");

        // Make buttons more visible with colors and fonts
        shareNoteButton.setBackground(new Color(0, 150, 136));
        shareNoteButton.setForeground(Color.WHITE);
        shareNoteButton.setFont(new Font("Arial", Font.BOLD, 14));

        uploadPdfButton.setBackground(new Color(255, 87, 34));
        uploadPdfButton.setForeground(Color.WHITE);
        uploadPdfButton.setFont(new Font("Arial", Font.BOLD, 14));

        sendMessageButton.setBackground(new Color(63, 81, 181));
        sendMessageButton.setForeground(Color.WHITE);

        // Create panel for the share and upload buttons (top of the panel)
        JPanel documentActionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        documentActionsPanel.add(shareNoteButton);
        documentActionsPanel.add(uploadPdfButton);
        titlePanel.add(documentActionsPanel, BorderLayout.SOUTH);

        // Add status label for indicating background processing
        processingStatusLabel = new JLabel("");
        processingStatusLabel.setForeground(new Color(33, 150, 243));
        processingStatusLabel.setFont(new Font("Arial", Font.ITALIC, 12));

        // Create panel for input components
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(userMessageField, BorderLayout.CENTER);
        
        // Create panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(processingStatusLabel);
        buttonPanel.add(sendMessageButton);
        buttonPanel.add(clearChatButton);
        inputPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Add components to main panel
        add(titlePanel, BorderLayout.NORTH);
        add(conversationScrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
        
        // Set up event handlers
        setupEventHandlers();

        //set preferred size for better visibility
        setPreferredSize(new Dimension(350, 600));

        // Load previous chat history if any
        updateChatDisplay();

        // Add component listener to handle visibility changes
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent evt) {
                userMessageField.requestFocus();
                updateChatDisplay();
            }
        });
    }

    /**
     * Creates the knowledge base selector panel
     */
    private JPanel createKnowledgeBasePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.setBorder(new TitledBorder("Knowledge Base"));

        // Create knowledge base combo box
        knowledgeBaseSelector = new JComboBox<>();
        refreshKnowledgeBases();
        knowledgeBaseSelector.addActionListener(e -> knowledgeBaseSelected());

        // Create new knowledge base button
        createKnowledgeBaseButton = new JButton("+");
        createKnowledgeBaseButton.setToolTipText("Create New Knowledge Base");
        createKnowledgeBaseButton.addActionListener(e -> createNewKnowledgeBase());

        // Create a details button
        knowledgeBaseDetailsButton = new JButton("📋");
        knowledgeBaseDetailsButton.setToolTipText("Show Knowledge Base Details");
        knowledgeBaseDetailsButton.addActionListener(e -> showKnowledgeBaseDetails());

        // Create a clear button
        JButton clearKnowledgeBaseButton = new JButton("🗑️");
        clearKnowledgeBaseButton.setToolTipText("Clear Knowledge Base");
        clearKnowledgeBaseButton.addActionListener(e -> clearKnowledgeBase());

        // Create a panel for the buttons
        JPanel kbActionButtonPanel = new JPanel(new GridLayout(1, 3, 2, 0));
        kbActionButtonPanel.add(createKnowledgeBaseButton);
        kbActionButtonPanel.add(knowledgeBaseDetailsButton);
        kbActionButtonPanel.add(clearKnowledgeBaseButton);

        // Create a panel for the combo box and buttons
        JPanel comboPanel = new JPanel(new BorderLayout());
        comboPanel.add(knowledgeBaseSelector, BorderLayout.CENTER);
        comboPanel.add(kbActionButtonPanel, BorderLayout.EAST);

        panel.add(comboPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Refreshes the knowledge base dropdown with current knowledge bases
     */
    private void refreshKnowledgeBases() {
        knowledgeBaseSelector.removeAllItems();

        List<KnowledgeBase> knowledgeBases = ragAgent.getAllKnowledgeBases();
        for (KnowledgeBase kb : knowledgeBases) {
            knowledgeBaseSelector.addItem(kb);
        }

        // Select active knowledge base
        KnowledgeBase activeKB = ragAgent.getActiveKnowledgeBase();
        if (activeKB != null) {
            knowledgeBaseSelector.setSelectedItem(activeKB);
        }
    }

    /**
     * Handler for knowledge base selection change
     */
    private void knowledgeBaseSelected() {
        KnowledgeBase selectedKB = (KnowledgeBase) knowledgeBaseSelector.getSelectedItem();
        if (selectedKB != null) {
            ragAgent.setActiveKnowledgeBase(selectedKB.getId());
            updateKnowledgeBaseInfo();
        }
    }

    /**
     * Updates the status label with information about the active knowledge base
     */
    private void updateKnowledgeBaseInfo() {
        KnowledgeBase kb = ragAgent.getActiveKnowledgeBase();
    }

    /**
     * Creates a new knowledge base
     */
    private void createNewKnowledgeBase() {
        String name = JOptionPane.showInputDialog(this, "Enter knowledge base name:");
        if (name != null && !name.trim().isEmpty()) {
            String description = JOptionPane.showInputDialog(this, "Enter description (optional):");
            if (description == null) {
                description = "";
            }

            ragAgent.createKnowledgeBase(name, description);
            refreshKnowledgeBases();
        }
    }

    /**
     * Shows a dialog with details about the current knowledge base
     */
    private void showKnowledgeBaseDetails() {
        KnowledgeBase kb = ragAgent.getActiveKnowledgeBase();
        if (kb == null) {
            return;
        }

        StringBuilder details = new StringBuilder();
        details.append("Name: ").append(kb.getName()).append("\n");
        details.append("Description: ").append(kb.getDescription()).append("\n");
        details.append("Document Count: ").append(kb.getDocumentCount()).append("\n\n");
        details.append("Documents:\n");

        List<DocumentData> documents = kb.getDocuments();
        for (int i = 0; i < documents.size(); i++) {
            DocumentData doc = documents.get(i);
            details.append(i + 1).append(". ").append(doc.getSource()).append("\n");
        }

        JTextArea detailsTextArea = new JTextArea(details.toString());
        detailsTextArea.setEditable(false);
        detailsTextArea.setLineWrap(true);
        detailsTextArea.setWrapStyleWord(true);

        JScrollPane detailsScrollPane = new JScrollPane(detailsTextArea);
        detailsScrollPane.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(
                this,
                detailsScrollPane,
                kb.getName() + " Details",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Clears all documents from the selected knowledge base
     */
    private void clearKnowledgeBase() {
        KnowledgeBase kb = ragAgent.getActiveKnowledgeBase();
        if (kb == null) {
            JOptionPane.showMessageDialog(this, "No active knowledge base selected");
            return;
        }

        int result = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to clear all documents from \"" + kb.getName() + "\"?",
                "Confirm Clear", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            boolean success = ragAgent.clearKnowledgeBase(kb.getId());

            if (success) {
                JOptionPane.showMessageDialog(this, "Knowledge base cleared successfully!");
                updateKnowledgeBaseInfo();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to clear knowledge base",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Sets up event handlers for buttons and input field
     */
    private void setupEventHandlers() {
        // Send button action
        sendMessageButton.addActionListener(e -> sendMessage());
        
        // Enter key in the input field also sends message
        userMessageField.addActionListener(e -> sendMessage());
        
        // Clear button action
        clearChatButton.addActionListener(e -> clearChat());

        // Share with AI button action
        shareNoteButton.addActionListener(e -> addNoteAsDocument());

        // Upload PDF button action
        uploadPdfButton.addActionListener(e -> uploadPdfDocument());
    }

    /**
     * Uploads and processes a PDF document
     */
    private void uploadPdfDocument() {
        int result = pdfFileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = pdfFileChooser.getSelectedFile();

            // Get the active knowledge base
            KnowledgeBase activeKB = ragAgent.getActiveKnowledgeBase();
            if (activeKB == null) {
                JOptionPane.showMessageDialog(this,
                        "No active knowledge base found",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Show loading message
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            JOptionPane pdfProcessingPane = new JOptionPane("Processing PDF document for " + activeKB.getName() + "...",
                    JOptionPane.INFORMATION_MESSAGE,
                    JOptionPane.DEFAULT_OPTION,
                    null,
                    new Object[]{},
                    null);
            JDialog processingDialog = pdfProcessingPane.createDialog(this, "Please Wait");
            processingDialog.setModal(false);
            processingDialog.setVisible(true);

            // Process in background thread to avoid UI freeze
            SwingWorker<Boolean, Void> pdfWorker = new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() {
                    return ragAgent.addPdfDocument(selectedFile);
                }

                @Override
                protected void done() {
                    processingDialog.setVisible(false);
                    setCursor(Cursor.getDefaultCursor());
                    try {
                        boolean success = get();
                        if (success) {
                            JOptionPane.showMessageDialog(AIAssistantPanel.this,
                                    "PDF document successfully added to " + activeKB.getName() + ".",
                                    "Document Added",
                                    JOptionPane.INFORMATION_MESSAGE);
                            updateKnowledgeBaseInfo();
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
            pdfWorker.execute();
        }
    }
    
    /**
     * Sends user message to RAG agent and displays response
     */
    private void sendMessage() {
        String userMessage = userMessageField.getText().trim();
        if (!userMessage.isEmpty()) {
            // Disable input and show processing status
            userMessageField.setEnabled(false);
            sendMessageButton.setEnabled(false);
            processingStatusLabel.setText("Processing...");

            // Temporarily display user message for better UX
            String currentConversation = conversationHistoryArea.getText();
            conversationHistoryArea.setText(currentConversation + (currentConversation.isEmpty() ? "" : "\n\n") + "You: " + userMessage);
            
            // Clear input field
            userMessageField.setText("");
            
            // Process message through RAG agent in background
            SwingWorker<String, Void> messageProcessingWorker = new SwingWorker<String, Void>() {
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
                        conversationHistoryArea.append("\n\nAI Assistant: Sorry, an error occurred while processing your request.\n");
                    } finally {
                        // Re-enable input
                        userMessageField.setEnabled(true);
                        sendMessageButton.setEnabled(true);
                        updateKnowledgeBaseInfo();

                        // Request focus back to the input field
                        userMessageField.requestFocus();
                    }
                }
            };
            messageProcessingWorker.execute();
        }
    }
    
    /**
     * Updates the chat display with current chat history
     */
    private void updateChatDisplay() {
        conversationHistoryArea.setText(ragAgent.getChatMemoryService().getFormattedChatHistory());
        
        // Scroll to the bottom of the chat
        conversationHistoryArea.setCaretPosition(conversationHistoryArea.getDocument().getLength());
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

        String noteContent = getTextInTextEditor();

        if (noteContent != null && !noteContent.trim().isEmpty()) {
            // Get the active knowledge base
            KnowledgeBase activeKnowledgeBase = ragAgent.getActiveKnowledgeBase();
            if (activeKnowledgeBase == null) {
                JOptionPane.showMessageDialog(this,
                        "No active knowledge base found",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Show processing indicator
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            shareNoteButton.setEnabled(false);
            processingStatusLabel.setText("Processing note for " + activeKnowledgeBase.getName() + "...");

            SwingWorker<Void, Void> noteProcessingWorker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() {
                    DocumentData document = new DocumentData(noteContent, "Current Note");
                    ragAgent.addDocument(document);
                    return null;
                }

                @Override
                protected void done() {
                    setCursor(Cursor.getDefaultCursor());
                    shareNoteButton.setEnabled(true);
                    updateKnowledgeBaseInfo();
                    JOptionPane.showMessageDialog(AIAssistantPanel.this,
                            "Note added to " + activeKnowledgeBase.getName() + ".",
                            "Document Added",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            };
            noteProcessingWorker.execute();
        }
    }

    /**
     * Gets the text from the current note editor
     * @return The text in the text editor as string
     *
     */
    private String getTextInTextEditor()
    {
        // Get the currently selected notes pane
        NotesPane currentNotesPane = JournalsPane.getSelectedNotesPane();
        //get the text editor object
        JTextArea currentTextArea = currentNotesPane.getCurrentNoteEditor().getTextArea();
        // Check if text area is not null
        if (currentTextArea != null) {
            // Get the text from the current text area and return it
            return currentTextArea.getText();
        }
        //return nothing if tab is not found or selected to run program
        return "";
    }

    /**
     * Gets the RAG Agent for use by other components
     *
     * @return The RAG Agent
     */
    public RagAgent getRagAgent() {
        return ragAgent;
    }
}

