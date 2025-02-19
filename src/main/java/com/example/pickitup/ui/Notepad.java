/*
    ************************************************************
    NotePad Class
    Last Updated 02/19/2025

    This class is for making the text area and the frame

    Please remember to update the version date if any changes
    are made to this file.
    ************************************************************
*/

package com.example.pickitup.ui;

// imports
import javax.swing.*;  // to make the text area and GUI


// makes the popup window with a text area
public class Notepad
{
    // fields
    private JFrame frame = null;
    private JPanel panel = null;
    private JTextArea textArea = null;
    private JScrollPane scrollPane = null;

    // ********************************************************
    // THESE VALUES WILL PROBABLY BE CHANGED AND MOVED LATER
    // These are constant values
    // ********************************************************
    public static final int DEFAULT_WIDTH = 800;
    public static final int DEFAULT_HEIGHT = 600;
    public static final int MAX_WIDTH = 1920;
    public static final int MAX_HEIGHT = 832;
    public static final String TITLE = "Notepad";

    // constructor
    public Notepad()
    {
        frame = new JFrame();
        panel = new JPanel();
        textArea = new JTextArea();
        scrollPane = new JScrollPane(textArea);
    }

    // this is the driver method of the class
    public void newNotepadWindow()
    {
        makeFrame();
        addScrollPane();

        // show the frame
        frame.setVisible(true);
    }


    // make the frame of the window
    private void makeFrame()
    {
        // set some attributes of the frame
        frame.setTitle(TITLE);
        frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        // closes the program when the X is clicked
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // open in fullscreen
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // opens the frame in the center of the screen
        frame.setLocationRelativeTo(null);

        // absolute positioning is used in the panel
        // to have full control over where all items are placed in the panel
        panel.setLayout(null);
        frame.add(panel);
    }


    // adds the scroll pane to the panel. The text area is
    // a part of the scroll pane
    private void addScrollPane()
    {
        // Set the scroll pane's location, and size
        scrollPane.setBounds(300, 0, DEFAULT_WIDTH, MAX_HEIGHT);

        // allows the scroll bar to always be on screen
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // allows the word and text to wrap around to the next line
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        // write a line to the text area to guide users
        textArea.setText("Begin writing here: ");

        // set the "cursor line" to appear after the example text
        textArea.setCaretPosition(20);

        // add the scroll pane to the panel
        panel.add(scrollPane);

    } // end addScrollPane

} // end Notepad class