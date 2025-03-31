/*
    *******************************************************************************
    App Class
    Last Updated 03/26/2025
    Developed by CJ Quintero

    This is the main class that uses all the classes as member variables.
    Launch.java creates an instance of this class and calls runApp()
    which adds everything to the main frame.


    Please remember to update the version date if any changes
    are made to this file.
    *******************************************************************************
 */
package com.example.pickitup.ui;


import java.awt.*;


public class App
{
    // fields
    private AppFrame appFrame = null;
    private MenuBar menuBar = null;
    private JournalsPane journalsPane = null;
    //private AIAssistantPanel aiAssistantPanel = null;

    // constructor
    public App()
    {
        // make the objects
        appFrame = new AppFrame();
        journalsPane = new JournalsPane();
        menuBar = new MenuBar(journalsPane);

        //aiAssistantPanel = new AIAssistantPanel(scrollPane);
    }


    // This is the method that adds everything to the main frame
    // to create the entire app
    public void runApp()
    {
        // make the Main App Frame
        appFrame.makeMainAppFrame();


        // make and add the tabbed pane
        // makes a single tab by default
        journalsPane.makeJournalsPane();
        appFrame.add(journalsPane.getJournalsPane(), BorderLayout.CENTER);


        // make the menu bar and add it to the main frame
        menuBar.makeMenuBar();
        appFrame.add(menuBar.getMenuBar(), BorderLayout.NORTH);


        // add the AI Assistant Panel
        //appFrame.add(aiAssistantPanel, BorderLayout.EAST);


        // show the frame
        // this should stay as the last thing done in this method
        appFrame.setVisible(true);
    } // end runApp()

} // end App class
