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
    private final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 16); // constant
    private JFrame colorFrame = null;
    private final Color INITIAL_FONT_COLOR = new Color(0,0,0);
    private final Color INITIAL_BACKGROUND_COLOR = new Color(255,255,255);

    // menu bar buttons
    private JMenu fileMenu = null;
    private JMenu fontMenu = null;
    private JMenu fontSizeMenu = null;
    private JMenu viewMenu = null; // Menu for view options
    private JMenu themeMenu = null; // Menu for theme options
    private JMenu calendarMenu = null;
    private JMenuItem lightModeItem = null;
    private JMenuItem darkModeItem = null;
    private JMenuItem clockTimerItem = null; // New menu item for clock/timer
    private ThemeManager themeManager;

    //----------Begin - File Menu Options----------//
    // menu item buttons for journalsMenu
    private JMenu journalsMenu = null;
    private JMenuItem newJournal = null;
    private JMenuItem deleteJournal = null;
    private JMenuItem renameJournal = null;

    //menu item buttons for pagesMenu
    private JMenu pagesMenu = null;
    private JMenuItem newPage = null;
    private JMenuItem deletePage = null;
    private JMenuItem renamePage = null;

    // menu item buttons for saveMenu
    private JMenu saveMenu = null;
    private JMenuItem saveNotes = null;
    private JMenuItem saveAnExit = null;
    private JMenuItem EXIT = null;
    //----------End - File Menu Options----------//

    // menu item buttons for fontMenu
    private JMenu fontTypeMenu = null;
    private JMenuItem comicSans = null;
    private JMenuItem timesRoman = null;
    private JMenuItem arial = null;
    private JMenuItem fontSize = null;
    private JMenuItem fontColor = null;
    private JMenuItem backgroundColor = null;


    // constructor
    public MenuBar(JournalsPane journalsPane)
    {
        themeManager = ThemeManager.getInstance();
        menuBar = new JMenuBar();
        this.journalsPane = journalsPane; // reference to the main frame's journal pane

        // menu bar tabs
        fileMenu = new JMenu("File");
        saveMenu = new JMenu("Save");
        fontMenu = new JMenu("Font");
        fontSizeMenu = new JMenu("Font Size");
        viewMenu = new JMenu("View"); // Initialize new view menu
        calendarMenu = new JMenu("Calendar");
        themeMenu = new JMenu("Theme");

        // menu items for fileMenu
        newPage = new JMenuItem("New Page");
        deletePage = new JMenuItem("Delete Page");
        newJournal = new JMenuItem("New Journal");
        deleteJournal = new JMenuItem("Delete Journal");

        // menu items for fontMenu
        fontTypeMenu = new JMenu("Fonts");
        comicSans = new JMenuItem("Comic Sans");
        timesRoman = new JMenuItem("Times New Roman");
        arial = new JMenuItem("Arial");
        fontSize = new JMenuItem("Font Size");
        fontColor = new JMenuItem("Font Color");
        backgroundColor = new JMenuItem("Background Color");

        // menu items for saveMenu
        EXIT = new JMenuItem("Exit");
        saveNotes = new JMenuItem("Save Notes");
        saveAnExit = new JMenuItem("Save An Exit");

        // menu items for journalsMenu
        newJournal = new JMenuItem("New Journal");
        deleteJournal = new JMenuItem("Delete Journal");
        renameJournal = new JMenuItem("Rename Journal");
        journalsMenu = new JMenu("Journals");

        // menu items for pagesMenu
        newPage = new JMenuItem("New Page");
        deletePage = new JMenuItem("Delete Page");
        renamePage = new JMenuItem("Rename Page");
        pagesMenu = new JMenu("Pages");

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

        // for file menu items
        newPage.setFont(DEFAULT_FONT);
        deletePage.setFont(DEFAULT_FONT);
        newJournal.setFont(DEFAULT_FONT);
        deleteJournal.setFont(DEFAULT_FONT);
        calendarMenu.setFont(DEFAULT_FONT);
        notesItem.setFont(DEFAULT_FONT);
        todoItem.setFont(DEFAULT_FONT);
        clockTimerItem.setFont(DEFAULT_FONT);
        themeMenu.setFont(DEFAULT_FONT);
        renameJournal.setFont(DEFAULT_FONT);
        renamePage.setFont(DEFAULT_FONT);
        pagesMenu.setFont(DEFAULT_FONT);
        journalsMenu.setFont(DEFAULT_FONT);
        EXIT.setFont(DEFAULT_FONT);
        saveNotes.setFont(DEFAULT_FONT);
        saveAnExit.setFont(DEFAULT_FONT);

        viewMenu.add(notesItem);
        viewMenu.add(todoItem);
        viewMenu.add(clockTimerItem);

        // for font menu items
        fontTypeMenu.setFont(DEFAULT_FONT);
        comicSans.setFont(DEFAULT_FONT);
        timesRoman.setFont(DEFAULT_FONT);
        arial.setFont(DEFAULT_FONT);
        fontSize.setFont(DEFAULT_FONT);
        fontColor.setFont(DEFAULT_FONT);
        backgroundColor.setFont(DEFAULT_FONT);

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
        //fontMenu Creation
        fontSize.addActionListener(e ->{
            int number = 0;
            String input = JOptionPane.showInputDialog("Enter font size:");

            //if user doesn't input
            if(input == null){
                return;
            }

            try {
                number = Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Input: " + ex.getMessage() + " is not a number", "Error", JOptionPane.ERROR_MESSAGE);
            }

            NotesPane selectedNotesPane = journalsPane.getSelectedNotesPane();
            selectedNotesPane.setFontSize(number);
        });
        comicSans.addActionListener(e ->{
            NotesPane selectedNotesPane = journalsPane.getSelectedNotesPane();
            selectedNotesPane.setFontType("Comic Sans");
        });
        arial.addActionListener(e ->{
            NotesPane selectedNotesPane = journalsPane.getSelectedNotesPane();
            selectedNotesPane.setFontType("Arial");
        });
        timesRoman.addActionListener(e ->{
            NotesPane selectedNotesPane = journalsPane.getSelectedNotesPane();
            selectedNotesPane.setFontType("Times New Roman");
        });
        fontColor.addActionListener(e ->{
            NotesPane selectedNotesPane = journalsPane.getSelectedNotesPane();
            Color selectedColor = JColorChooser.showDialog(colorFrame, "Pick a color :D", INITIAL_FONT_COLOR);
            if (selectedColor != null){
                selectedNotesPane.setFontColor(selectedColor);
            }
        });
        backgroundColor.addActionListener(e ->{
            NotesPane selectedNotesPane = journalsPane.getSelectedNotesPane();
            Color selectedColor = JColorChooser.showDialog(colorFrame, "Pick a color :D", INITIAL_BACKGROUND_COLOR);
            if (selectedColor != null) {
                selectedNotesPane.setBackgroundColor(selectedColor);
            }
        });
        fontMenu.add(fontSize);
        fontMenu.add(fontColor);
        fontMenu.add(backgroundColor);
        fontTypeMenu.add(comicSans);
        fontTypeMenu.add(timesRoman);
        fontTypeMenu.add(arial);
        fontMenu.add(fontTypeMenu);

        //journalsMenu Creation
        newJournal.addActionListener(e -> journalsPane.addJournalTab());
        deleteJournal.addActionListener(e -> journalsPane.deleteJournalTab());
        renameJournal.addActionListener(e ->{
            String input = JOptionPane.showInputDialog("Enter New Journal Name:");
            if(input == null || input.isEmpty() || input.equals("")){
                JOptionPane.showMessageDialog(
                        null,
                        "No name was provided!",
                        "ERROR",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            journalsPane.setNewJournalName(input);
        });
        journalsMenu.add(renameJournal);
        journalsMenu.add(newJournal);
        journalsMenu.add(deleteJournal);

        //pagesMenu
        newPage.addActionListener(e -> {
            NotesPane selectedNotesPane = JournalsPane.getSelectedNotesPane();
            if (selectedNotesPane != null)
            {
                selectedNotesPane.addPageTab();
            }
        });
        deletePage.addActionListener(e -> {
            int yesNo = JOptionPane.showConfirmDialog(
                    null,
                    "Are you sure you want to delete this page!?",
                    "Delete Page?",
                    JOptionPane.YES_NO_OPTION
            );
            if (yesNo == JOptionPane.YES_OPTION){
                NotesPane selectedNotesPane = JournalsPane.getSelectedNotesPane();
                if (selectedNotesPane != null)
                {
                    selectedNotesPane.deletePageTab();
                }
            }
        });
        renamePage.addActionListener(e -> {
            NotesPane selectedNotesPane = JournalsPane.getSelectedNotesPane();
            String input = JOptionPane.showInputDialog("Enter New Name:");
            if(input == null || input.isEmpty() || input.equals("")){
                JOptionPane.showMessageDialog(
                        null,
                        "No name was provided!",
                        "ERROR",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            selectedNotesPane.setNewPageName(input);
        });
        pagesMenu.add(renamePage);
        pagesMenu.add(newPage);
        pagesMenu.add(deletePage);

        //saveMenu Creation
        EXIT.addActionListener(e ->{
            int yesNo = JOptionPane.showConfirmDialog(
                    null,
                    "Are you sure you want to exit?\n"+
                            "Any unsaved work may be lost!!!",
                    "Exit?",
                    JOptionPane.YES_NO_OPTION
            );
            if (yesNo == JOptionPane.YES_OPTION){System.exit(0);}
        });
        saveNotes.addActionListener(e ->{
            System.out.println("Notes Saved!");
        });
        saveAnExit.addActionListener(e ->{
            System.out.println("Notes Saved!");
            System.exit(0);
        });
        saveMenu.add(saveNotes);
        saveMenu.add(saveAnExit);
        saveMenu.add(EXIT);


        //fileMenu creation
        fileMenu.add(journalsMenu);
        fileMenu.add(pagesMenu);
        fileMenu.add(saveMenu);
        fileMenu.add(saveMenu);


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
