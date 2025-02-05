// ************************************************************
// NotePad Class version 1.0:
// This class is for making the notepad popup window
//
// Please remember to update the version number if any changes
// are made to this file.
// ************************************************************

package com.example.pickitup.ui;

// imports
import javax.swing.*;   // to make the text area and GUI


// makes the popup window
public class Notepad extends JFrame
{
    // methods
    public void newWindow()
    {
        JTextArea textArea = new JTextArea();               // make a text area object
        setTitle("Basic Text Editor");                      // title of the window popup in upper left corner
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     // closes program when exited
        setSize(800,600);                      // size of the window popup
        setLocationRelativeTo(null);                        // centers the popup window when opened
        add(textArea);                                      // allows input to write to the frame
        JScrollPane scrollPane = new JScrollPane(textArea); // creates a scroll bar object
        add(scrollPane);                                    // adds the scroll bar to the window popup on the right
    }
}
