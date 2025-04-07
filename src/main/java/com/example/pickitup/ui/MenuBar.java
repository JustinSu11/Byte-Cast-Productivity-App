/*
    *******************************************************************************
    MenuBar Class
    Updated 04/03/2025


    This class creates the menu bar and sets some menus.
    Each menu has its own menu items.


    Please remember to update the version date if any changes
    are made to this file.
    *******************************************************************************
 */
package com.example.pickitup.ui;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MenuBar extends JMenuBar
{
    // fields
    private JMenuBar menuBar = null;
    private JournalsPane journalsPane = null; // Store JournalsPane

    // menu bar buttons
    private JMenu fileMenu = null;
    private JMenu saveMenu = null;
    private JMenu fontMenu = null;
    private JMenu fontSizeMenu = null;
    private JMenu viewMenu = null; // Menu for view options
    private JMenu themeMenu = null; // Menu for theme options
    private JMenu calendarMenu = null;
    private JMenuItem lightModeItem = null;
    private JMenuItem darkModeItem = null;
    private JMenuItem clockTimerItem = null; // New menu item for clock/timer
    private final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 16); // constant
    private ThemeManager themeManager;

    // menu item buttons for fileMenu
    private JMenuItem newPage = null;
    private JMenuItem deletePage = null;
    private JMenuItem newJournal = null;
    private JMenuItem deleteJournal = null;



    // constructor
    public MenuBar(JournalsPane journalsPane)
    {
        themeManager = ThemeManager.getInstance();
        menuBar = new JMenuBar();
        this.journalsPane = journalsPane; // reference to the main frame's journal pane

        // menu bar tabs
        fileMenu = new JMenu("File");
        saveMenu = new JMenu("Save");
        fontMenu = new JMenu("Fonts");
        fontSizeMenu = new JMenu("Font Size");
        viewMenu = new JMenu("View"); // Initialize new view menu
        calendarMenu = new JMenu("Calendar");
        themeMenu = new JMenu("Theme");

        // menu items for fileMenu
        newPage = new JMenuItem("New Page");
        deletePage = new JMenuItem("Delete Page");
        newJournal = new JMenuItem("New Journal");
        deleteJournal = new JMenuItem("Delete Journal");

        // default font for the menus
        fileMenu.setFont(DEFAULT_FONT);
        saveMenu.setFont(DEFAULT_FONT);
        fontMenu.setFont(DEFAULT_FONT);
        fontSizeMenu.setFont(DEFAULT_FONT);
        viewMenu.setFont(DEFAULT_FONT);

        // Add view menu items
        JMenuItem notesItem = new JMenuItem("Notes");
        JMenuItem todoItem = new JMenuItem("To-Do List");
        clockTimerItem = new JMenuItem("Clock Timer");

        // for menu items
        newPage.setFont(DEFAULT_FONT);
        deletePage.setFont(DEFAULT_FONT);
        newJournal.setFont(DEFAULT_FONT);
        deleteJournal.setFont(DEFAULT_FONT);
        calendarMenu.setFont(DEFAULT_FONT);
        notesItem.setFont(DEFAULT_FONT);
        todoItem.setFont(DEFAULT_FONT);
        clockTimerItem.setFont(DEFAULT_FONT);
        themeMenu.setFont(DEFAULT_FONT);

        viewMenu.add(notesItem);
        viewMenu.add(todoItem);
        viewMenu.add(clockTimerItem);

        // Add theme menu items
        lightModeItem = new JMenuItem("Light Mode");
        darkModeItem = new JMenuItem("Dark Mode");

        lightModeItem.setFont(DEFAULT_FONT);
        darkModeItem.setFont(DEFAULT_FONT);

        themeMenu.add(lightModeItem);
        themeMenu.add(darkModeItem);

        // Set up theme switcher actions
        setupThemeActions();

        // Set up view item actions
        setupViewActions();

        // Register with theme manager
        themeManager.registerComponent(menuBar);

        // Set up the calendar menu
        setupCalendarMenu();
        revalidate();
        repaint();
    }


    /**
     * Set up actions for view menu items
     */
    private void setupViewActions() {
        // Here you would add action listeners for the view menu items
        // For now, we'll focus on the clock/timer item
        clockTimerItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // This would typically bring the clock panel into focus
                // In our layout design, it's already visible so we don't need
                // to do anything here, but in a more complex UI you might
                // switch between different panels
            }
        });
    }

    /**
     * Set up actions for theme menu items
     */
    private void setupThemeActions() {
        lightModeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                themeManager.setTheme(ThemeManager.LIGHT_MODE);
                updateThemeMenuState();
            }
        });

        darkModeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                themeManager.setTheme(ThemeManager.DARK_MODE);
                updateThemeMenuState();
            }
        });

        // Initialize menu state based on current theme
        updateThemeMenuState();
    }

    // Method to set up the Calendar menu with actions
    private void setupCalendarMenu()
    {
        JMenuItem openCalendarItem = new JMenuItem("Open Calendar");
        openCalendarItem.setFont(DEFAULT_FONT);
        openCalendarItem.addActionListener(e ->
        {
            new CalendarPanel();
        });
        calendarMenu.add(openCalendarItem);
    }

    /**
     * Updates the state of theme menu items based on current theme
     */
    private void updateThemeMenuState() {
        int currentTheme = themeManager.getCurrentTheme();
        lightModeItem.setEnabled(currentTheme != ThemeManager.LIGHT_MODE);
        darkModeItem.setEnabled(currentTheme != ThemeManager.DARK_MODE);
    }

    // adds the menus to the menu bar
    public void makeMenuBar()
    {
        // menu items for file menu
        newPage.addActionListener(e ->
        {
            NotesPane selectedNotesPane = journalsPane.getSelectedNotesPane();
            if (selectedNotesPane != null)
            {
                selectedNotesPane.addPageTab();
            }
        });
        deletePage.addActionListener(e ->
        {
            NotesPane selectedNotesPane = journalsPane.getSelectedNotesPane();
            if (selectedNotesPane != null)
            {
                selectedNotesPane.deletePageTab();
            }
        });


        newJournal.addActionListener(e -> journalsPane.addJournalTab());
        deleteJournal.addActionListener(e -> journalsPane.deleteJournalTab());
        fileMenu.add(newPage);
        fileMenu.add(deletePage);
        fileMenu.add(newJournal);
        fileMenu.add(deleteJournal);

        // add the menus to the menu bar
        // keep these at the bottom of this method
        menuBar.add(fileMenu);
        menuBar.add(saveMenu);
        menuBar.add(viewMenu); // Add the view menu
        menuBar.add(fontMenu);
        menuBar.add(fontSizeMenu);
        menuBar.add(calendarMenu);
        menuBar.add(themeMenu); // Add the theme menu
    }

    // returns the menu bar to the App class
    public JMenuBar getMenuBar()
    {
        return menuBar;
    } // end getMenuBar

} // end MenuBar class
