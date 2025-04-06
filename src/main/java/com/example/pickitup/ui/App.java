/*
    *******************************************************************************
    App Class
    Last Updated 04/03/2025
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

    // constructor
    public App()
    {
        // Initialize theme manager first
        themeManager = ThemeManager.getInstance();
        themeManager.initializeTheme();

        // make the objects
        appFrame = new AppFrame();
        journalsPane = new JournalsPane();
//        aiAssistantPanel = new AIAssistantPanel(journalsPane);
        toDoListPanel = new ToDoListPanel();
        clockPanel = new ClockPanel();
    }


    // This is the method that adds everything to the main frame
    // to create the entire app
    public void runApp()
    {
        // make the Main App Frame
        appFrame.makeMainAppFrame();

        // Create a top panel for menu and clock
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(menuBar.getMenuBar(), BorderLayout.NORTH);
        topPanel.add(clockPanel, BorderLayout.CENTER);

        // Add top panel to the main frame
        appFrame.add(topPanel, BorderLayout.NORTH);

        // Create a center panel to hold the note editor and to-do list
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0));

        // Register with theme manager
        themeManager.registerComponent(centerPanel);
        themeManager.registerComponent(topPanel);

        // make and add the tabbed pane
        // adds a single tab by default
        journalsPane.addJournalTab();
        centerPanel.add(journalsPane.getJournalsPane());

        appFrame.add(centerPanel, BorderLayout.CENTER);

        // make the menu bar and add it to the main frame
        // this MUST go after making the journals pane to avoid
        // errors
        menuBar = new MenuBar(journalsPane);
        menuBar.makeMenuBar();

        appFrame.add(menuBar.getMenuBar(), BorderLayout.NORTH);

        // Initialize the AI Assistant Panel with the current NoteEditor
        NotesPane selectedNotesPane = journalsPane.getSelectedNotesPane();
        if (selectedNotesPane != null) {
            NoteEditor currentNoteEditor = selectedNotesPane.getCurrentNoteEditor();
            if (currentNoteEditor != null) {
                aiAssistantPanel = new AIAssistantPanel(currentNoteEditor);
                AppFrame.aiAssistantPanel = aiAssistantPanel;
                appFrame.add(aiAssistantPanel, BorderLayout.EAST);
            }
        }

        registerComponentsWithThemeManager();

        // show the frame
        // this should stay as the last thing done in this method
        appFrame.setVisible(true);
    } // end runApp()

} // end App class