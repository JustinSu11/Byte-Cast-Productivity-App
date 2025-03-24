/*
    *******************************************************************************
    App Class
    Last Updated 03/21/2025


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
    private ToDoListPanel toDoListPanel = null;
    private JPanel mainPanel = null;

    // constructor
    public App()
    {
        // make the objects
        appFrame = new AppFrame();
        menuBar = new MenuBar();
        scrollPane = new ScrollPane();
        aiAssistantPanel = new AIAssistantPanel(scrollPane);
        toDoListPanel = new ToDoListPanel();
    }


    // This is the method that adds everything to the main frame
    // to create the entire app
    public void runApp()
    {
        // make the Main App Frame
        appFrame.makeMainAppFrame();


        // make the menu bar and add it to the main frame
        menuBar.makeMenuBar();
        appFrame.add(menuBar.getMenuBar(), BorderLayout.NORTH);


        // Create a center panel to hold the note editor and to-do list
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0));

        // make and add the scroll pane (text area)
        scrollPane.makeScrollPane();
        centerPanel.add(scrollPane.getScrollPane());

        // Add the to-do list panel
        centerPanel.add(toDoListPanel);

        // Add the center panel to the main frame
        appFrame.add(centerPanel, BorderLayout.CENTER);

        // add the AI Assistant Panel
        appFrame.add(aiAssistantPanel, BorderLayout.EAST);


        // show the frame
        // this should stay as the last thing done in this method
        appFrame.setVisible(true);
    } // end runApp()

} // end App class