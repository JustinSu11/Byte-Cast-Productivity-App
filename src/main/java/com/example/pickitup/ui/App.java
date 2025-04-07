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


import javax.swing.*;
import java.awt.*;


public class App
{
    // fields
    private AppFrame appFrame = null;
    private MenuBar menuBar = null;
    private JournalsPane journalsPane = null;
    private AIAssistantPanel aiAssistantPanel = null;
    private ToDoListPanel toDoListPanel = null;
    private ClockPanel clockPanel = null;
    private ThemeManager themeManager = null;
    private JButton toggleAiPanelButton = null;
    private boolean aiPanelVisible = true;

    // constructor
    public App()
    {
        // Initialize theme manager first
        themeManager = ThemeManager.getInstance();
        themeManager.initializeTheme();

        // make the objects
        appFrame = new AppFrame();
        journalsPane = new JournalsPane();
        toDoListPanel = new ToDoListPanel();
        clockPanel = new ClockPanel();
        toggleAiPanelButton = new JButton("Hide AI Assistant");
    }


    // This is the method that adds everything to the main frame
    // to create the entire app
    public void runApp()
    {
        // make the Main App Frame
        appFrame.makeMainAppFrame();

        // Create a center panel to hold the note editor and to-do list
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0));

        // Register with theme manager
        themeManager.registerComponent(centerPanel);

        // make and add the tabbed pane
        // adds a single tab by default
        journalsPane.addJournalTab();
        centerPanel.add(journalsPane.getJournalsPane());

        //Make as assistant panel after journal tab is created
        aiAssistantPanel = new AIAssistantPanel(journalsPane);

        appFrame.add(centerPanel, BorderLayout.CENTER);

        // make the menu bar and add it to the main frame
        // this MUST go after making the journals pane to avoid
        // errors
        menuBar = new MenuBar(journalsPane);
        menuBar.makeMenuBar();

        // Create a top panel for menu and clock
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(menuBar.getMenuBar(), BorderLayout.NORTH);
        topPanel.add(clockPanel, BorderLayout.CENTER);

        // Add top panel to the main frame
        appFrame.add(topPanel, BorderLayout.NORTH);

        themeManager.registerComponent(topPanel);
        // Configure toggle button
        toggleAiPanelButton.setFocusable(false);
        toggleAiPanelButton.addActionListener(e -> toggleAiPanel());

        // Create a panel for the bottom of the UI that holds the toggle button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(toggleAiPanelButton);
        appFrame.add(bottomPanel, BorderLayout.SOUTH);

        // add the AI Assistant Panel
        appFrame.add(aiAssistantPanel, BorderLayout.EAST);

        // show the frame
        // this should stay as the last thing done in this method
        appFrame.setVisible(true);

        registerComponentsWithThemeManager();
    } // end runApp()

    private void registerComponentsWithThemeManager() {
        // Register the main frame and its content pane
        themeManager.registerComponent((JComponent)appFrame.getContentPane());

        // Register individual panels
        themeManager.registerComponent(toDoListPanel);
        themeManager.registerComponent(aiAssistantPanel);
        themeManager.registerComponent(journalsPane.getJournalsPane());
        themeManager.registerComponent(clockPanel);
    }

    /**
     * Toggles the visibility of the AI Assistant panel
     */
    private void toggleAiPanel() {
        aiPanelVisible = !aiPanelVisible;
        aiAssistantPanel.setVisible(aiPanelVisible);
        toggleAiPanelButton.setText(aiPanelVisible ? "Hide AI Assistant" : "Show AI Assistant");
        appFrame.revalidate();
    }

} // end App class
