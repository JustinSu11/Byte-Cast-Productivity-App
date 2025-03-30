/*
    *******************************************************************************
    App Class
    Last Updated 02/28/2025


    This is the main class that uses all the classes as member variables.
    Launch.java creates an instance of this class and calls runApp()
    which adds everything to the main frame.


    Please remember to update the version date if any changes
    are made to this file.
    *******************************************************************************
 */
package com.example.pickitup.ui;


import javax.swing.*;
import java.awt.*;


public class App
{
    // fields
    private AppFrame appFrame = null;
    private MenuBar menuBar = null;
    private ScrollPane scrollPane = null;
    private AIAssistantPanel aiAssistantPanel = null;

    // constructor
    public App()
    {
        // make the objects
        appFrame = new AppFrame();
        menuBar = new MenuBar();
        scrollPane = new ScrollPane();
        aiAssistantPanel = new AIAssistantPanel(scrollPane);
    }


    // This is the method that adds everything to the main frame
    // to create the entire app
    public void runApp()
    {
        // Create and set up components first
        menuBar.makeMenuBar();
        scrollPane.makeScrollPane();

        // Add components to the frame
        appFrame.add(menuBar.getMenuBar(), BorderLayout.NORTH);
        appFrame.add(scrollPane.getScrollPane(), BorderLayout.CENTER);
        appFrame.add(aiAssistantPanel, BorderLayout.EAST);

        // Important: Update UI and layout before making visible
        appFrame.validate();
        appFrame.pack();

        // Set to maximized state AFTER pack but BEFORE setVisible
        appFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Now make the frame visible
        appFrame.setVisible(true);
    } // end runApp()

} // end App class
