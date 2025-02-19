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
import java.awt.*; // for border layout
import java.awt.event.*;
import com.example.pickitup.models.Note;


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
    MenuBar menuBar = new MenuBar();
    Note note;

    // this method is the "main" method of the Notepad class that
    // calls the other methods to make the notepad program
    public void newWindow()
    {
        // make the basic frame
        makeFrame();

        // add the text area to the frame
        addTextArea();

        // add the toolbar
        addToolbar();

        // add scroll pane
        addScrollPane();

        // add menu bar
        frame.setJMenuBar(menuBar);
        menuBar.saveAsItem.addActionListener(new MenuActionListener());
        menuBar.saveItem.addActionListener(new MenuActionListener());

        // show the frame
        frame.setVisible(true);

        note = new Note();
    }

    // make the frame of the window and set its size
    private void makeFrame()
    {
        // set some attributes of the frame
        frame = new JFrame();
        frame.setTitle(title);
        frame.setSize(800, 600);

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

        // add the text area to the frame
        frame.add(textArea);

        // write a line to the editor to guide users
        textArea.setText("Begin writing here: ");

        // set the "cursor line" to appear after the example text
        textArea.setCaretPosition(20);

        // take input from text area as content for note
    }

    // adds the toolbar to the top of the frame
    private void addToolbar()
    {
        // make the toolbar, stop it from moving, add it to frame at the top
        toolbar = new JToolBar();
        toolbar.setFloatable(false);
        frame.add(toolbar, BorderLayout.NORTH);

        // write a text line to the toolbar, and add the toolbar
        toolbarLabel = new JLabel
                ("This is the toolbar; Add buttons here; Also try to make this bigger");
        toolbar.add(toolbarLabel);
    }

    // adds the scroll bar to the text editor
    private void addScrollPane()
    {
        scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane);
    }

    public class MenuActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            if(e.getSource() == menuBar.saveAsItem){
                note.setContent(textArea.getText());
                note.saveNoteWithFileChooser();
            }
//            if(e.getSource() == menuBar.saveItem){
//                note.saveNote(untitled.json, this);
//            }
            if(e.getSource() == menuBar.loadNoteItem){
                loadNoteFromFile();
            }
        }
    }
}
