/*
    *******************************************************************************
    MenuBar Class
    Updated 03/26/2025
    Developer CJ Quintero


    This class creates the menu bar and sets some menus.
    Each menu has its own menu items.


    Please remember to update the version date if any changes
    are made to this file.
    *******************************************************************************
 */
package com.example.pickitup.ui;


import javax.swing.*;
import java.awt.*;
import com.formdev.flatlaf.FlatLightLaf;


public class MenuBar extends JMenuBar
{
    // fields
    private JMenuBar menuBar = null;
    private NotesPane notesPane = null;
    private JournalsPane journalsPane = null;
    private final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 16); // constant

    // menu bar buttons
    private JMenu fileMenu = null;
    private JMenu saveMenu = null;
    private JMenu fontMenu = null;
    private JMenu fontSizeMenu = null;
    private JMenu calendarMenu = null;
    private final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 16); // constant

    // menu item buttons for fileMenu
    private JMenuItem newPage = null;
    private JMenuItem deletePage = null;
    private JMenuItem newJournal = null;
    private JMenuItem deleteJournal = null;



    // constructor
    public MenuBar(JournalsPane journalsPane)
    {
        menuBar = new JMenuBar();
        this.journalsPane = journalsPane; // reference to the main frame's tabbed pane
//        this.notesPane = journalsPane;

        // menu bar tabs
        fileMenu = new JMenu("File");
        saveMenu = new JMenu("Save");
        fontMenu = new JMenu("Fonts");
        fontSizeMenu = new JMenu("Font Size");
        calendarMenu = new JMenu("Calendar");

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
        calendarMenu.setFont(DEFAULT_FONT);

        // Set up the calendar menu
        setupCalendarMenu();
        revalidate();
        repaint();

        // for menu items
        newPage.setFont(DEFAULT_FONT);
        deletePage.setFont(DEFAULT_FONT);
        newJournal.setFont(DEFAULT_FONT);
        deleteJournal.setFont(DEFAULT_FONT);
    }

    // Method to set up the Calendar menu with actions
    private void setupCalendarMenu()
    {
        JMenuItem openCalendarItem = new JMenuItem("Open Calendar");
        openCalendarItem.setFont(DEFAULT_FONT);
        openCalendarItem.addActionListener(e ->
        {
            new CalendarApp();
        });
        calendarMenu.add(openCalendarItem);
    }

    // adds the menus to the menu bar
    public void makeMenuBar()
    {
        // menu items for file menu
        newPage.addActionListener(e -> notesPane.addTab());
        deletePage.addActionListener(e -> notesPane.deleteTab());
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
        menuBar.add(fontMenu);
        menuBar.add(fontSizeMenu);
        menuBar.add(calendarMenu);

    }

    // returns the menu bar to the App class
    public JMenuBar getMenuBar()
    {
        return menuBar;
    } // end getMenuBar

} // end MenuBar class
