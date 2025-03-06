// ************************************************************
// NotePad Class version 1.0:
// This class is for making the notepad popup window
//
// Please remember to update the version number if any changes
// are made to this file.
// ************************************************************

package com.example.pickitup.ui;

// imports
import javax.swing.*;  // to make the text area and GUI
import com.example.pickitup.services.models.NotesDataModel; // to create Notes object
import com.example.pickitup.services.dao.NotesDAO; // to use CRUD methods for notes
import java.awt.*; // for border layout
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


// makes the popup window
public class Notepad extends JFrame
{
    // fields
    JFrame frame = null;
    JTextArea textArea = null;
    JScrollPane scrollPane = null;
    JToolBar toolbar = null;
    JLabel toolbarLabel = null;
    String title = "Notepad";
    private NotesDAO notesDAO;
    private AIAssistantPanel aiAssistantPanel;
    private JTextField titleField;

    // this method is the "main" method of the Notepad class that
    // calls the other methods to make the notepad program
    public void newWindow()
    {
        // Initialize NotesDAO
        notesDAO = new NotesDAO();
        
        // make the basic frame
        makeFrame();

        // add the text area to the frame
        addTextArea();

        // add the toolbar
        addToolbar();

        // add scroll pane
        addScrollPane();
        
        // add AI Assistant Panel
        addAIAssistant();

        // show the frame
        frame.setVisible(true);
    }

    // make the frame of the window and set its size
    private void makeFrame()
    {
        // set some attributes of the frame
        frame = new JFrame();
        frame.setTitle(title);
        frame.setSize(1000, 700);

        // closes the program when the X is clicked
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // opens the frame in the center of the screen
        frame.setLocationRelativeTo(null);
    }

    // adds the text area to the frame
    private void addTextArea()
    {
        textArea = new JTextArea();

        // fill the screen with the text box
        textArea.setSize(1920, 1080);

        // allows the text to wrap around to the next line
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        // write a line to the editor to guide users
        textArea.setText("Begin writing here: ");

        // set the "cursor line" to appear after the example text
        textArea.setCaretPosition(20);
    }

    // adds the toolbar to the top of the frame
    private void addToolbar()
    {
        // make the toolbar, stop it from moving, add it to frame at the top
        toolbar = new JToolBar();
        toolbar.setFloatable(false);
        frame.add(toolbar, BorderLayout.NORTH);

        // Create title input field
        JLabel titleLabel = new JLabel("Note Title: ");
        toolbar.add(titleLabel);
        
        titleField = new JTextField(15);
        toolbar.add(titleField);
        
        toolbar.addSeparator(new Dimension(10, 10));
        

        
        // Add share with AI button
        JButton shareWithAIButton = new JButton("Share with AI");
        shareWithAIButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shareNoteWithAI();
            }
        });
        toolbar.add(shareWithAIButton);
    }

    // adds the scroll bar to the text editor
    private void addScrollPane()
    {
        scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);
    }
    
    // adds the AI Assistant panel to the right side of the frame
    private void addAIAssistant() {
        aiAssistantPanel = new AIAssistantPanel();
        frame.add(aiAssistantPanel, BorderLayout.EAST);
        aiAssistantPanel.setPreferredSize(new Dimension(300, 0));
    }

    
    // Shares the current note with the AI Assistant
    private void shareNoteWithAI() {
        String noteTitle = titleField.getText().trim();
        String noteContent = textArea.getText();
        
        if (noteTitle.isEmpty()) {
            noteTitle = "Untitled Note";
        }
        
        aiAssistantPanel.addNoteAsDocument(noteTitle, noteContent);
    }
}
