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


import java.awt.*;


public class App
{
    // fields
    private AppFrame appFrame = null;
    private MenuBar menuBar = null;
    private ScrollPane scrollPane = null;
    private AIAssistantPanel aiAssistantPanel;

    // constructor
    public App()
    {
        // make the objects
        appFrame = new AppFrame();
        menuBar = new MenuBar();
        scrollPane = new ScrollPane();
        aiAssistantPanel = new AIAssistantPanel();
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


        // make and add the scroll pane (text area)
        scrollPane.makeScrollPane();
        appFrame.add(scrollPane.getScrollPane(), BorderLayout.CENTER);

        // add the AI panel to the right of frame
        aiAssistantPanel.setPreferredSize(new Dimension(300, 0));
        appFrame.add(aiAssistantPanel, BorderLayout.EAST);

        // show the frame
        // this should stay as the last thing done in this method
        appFrame.setVisible(true);
    } // end runApp()

} // end App class
