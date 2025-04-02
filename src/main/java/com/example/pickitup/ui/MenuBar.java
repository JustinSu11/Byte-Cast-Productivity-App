/*
    *******************************************************************************
    MenuBar Class
    Updated 04/2/2025
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
    private JMenu saveMenu = null;
    private JMenu fontMenu = null;
    private JMenu pagesMenu = null;
    private JMenu journalsMenu = null;

    // menu item buttons for fileMenu
    private JMenuItem newPage = null;
    private JMenuItem deletePage = null;
    private JMenuItem newJournal = null;
    private JMenuItem deleteJournal = null;
    private JMenuItem renameJournal = null;
    private JMenuItem renamePage = null;
    private JMenuItem EXIT = null;

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
        menuBar = new JMenuBar();
        this.journalsPane = journalsPane; // reference to the main frame's journal pane

        // menu bar tabs
        fileMenu = new JMenu("File");
        saveMenu = new JMenu("Save");
        fontMenu = new JMenu("Font");

        // menu items for fileMenu
        newPage = new JMenuItem("New Page");
        deletePage = new JMenuItem("Delete Page");
        newJournal = new JMenuItem("New Journal");
        deleteJournal = new JMenuItem("Delete Journal");
        renameJournal = new JMenuItem("Rename Journal");
        renamePage = new JMenuItem("Rename Page");
        pagesMenu = new JMenu("Pages");
        journalsMenu = new JMenu("Journals");
        EXIT = new JMenuItem("Exit");

        // menu items for fontMenu
        comicSans = new JMenuItem("Comic Sans");
        timesRoman = new JMenuItem("Times New Roman");
        arial = new JMenuItem("Arial");
        fontSize = new JMenuItem("Font Size");
        fontColor = new JMenuItem("Font Color");
        backgroundColor = new JMenuItem("Background Color");
        fontTypeMenu = new JMenu("Fonts");


        // default font for the menus
        fileMenu.setFont(DEFAULT_FONT);
        saveMenu.setFont(DEFAULT_FONT);
        fontMenu.setFont(DEFAULT_FONT);

        // for file menu items
        newPage.setFont(DEFAULT_FONT);
        deletePage.setFont(DEFAULT_FONT);
        newJournal.setFont(DEFAULT_FONT);
        deleteJournal.setFont(DEFAULT_FONT);
        renameJournal.setFont(DEFAULT_FONT);
        renamePage.setFont(DEFAULT_FONT);
        pagesMenu.setFont(DEFAULT_FONT);
        journalsMenu.setFont(DEFAULT_FONT);
        EXIT.setFont(DEFAULT_FONT);

        // for font menu items
        fontTypeMenu.setFont(DEFAULT_FONT);
        comicSans.setFont(DEFAULT_FONT);
        timesRoman.setFont(DEFAULT_FONT);
        arial.setFont(DEFAULT_FONT);
        fontSize.setFont(DEFAULT_FONT);
        fontColor.setFont(DEFAULT_FONT);
        backgroundColor.setFont(DEFAULT_FONT);
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

        renamePage.addActionListener(e ->{
            NotesPane selectedNotesPane = journalsPane.getSelectedNotesPane();
            String input = JOptionPane.showInputDialog("Enter New Name:");
            selectedNotesPane.setNewPageName(input);
        });

        renameJournal.addActionListener(e ->{
            String input = JOptionPane.showInputDialog("Enter New Journal Name:");
            journalsPane.setNewJournalName(input);
        });

        //menu items for fontMenu
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

        EXIT.addActionListener(e ->{
            System.exit(0);
        });

        //fileMenu creation
        newJournal.addActionListener(e -> journalsPane.addJournalTab());
        deleteJournal.addActionListener(e -> journalsPane.deleteJournalTab());
        fileMenu.add(journalsMenu);
        fileMenu.add(pagesMenu);
        pagesMenu.add(renamePage);
        pagesMenu.add(newPage);
        pagesMenu.add(deletePage);
        journalsMenu.add(renameJournal);
        journalsMenu.add(newJournal);
        journalsMenu.add(deleteJournal);
        fileMenu.add(EXIT);

        //fontMenu creation
        fontMenu.add(fontSize);
        fontMenu.add(fontColor);
        fontMenu.add(backgroundColor);
        fontTypeMenu.add(comicSans);
        fontTypeMenu.add(timesRoman);
        fontTypeMenu.add(arial);
        fontMenu.add(fontTypeMenu);

        // add the menus to the menu bar
        // keep these at the bottom of this method
        menuBar.add(fileMenu);
        menuBar.add(saveMenu);
        menuBar.add(fontMenu);
    }

    // returns the menu bar to the App class
    public JMenuBar getMenuBar()
    {
        return menuBar;
    } // end getMenuBar

} // end MenuBar class
