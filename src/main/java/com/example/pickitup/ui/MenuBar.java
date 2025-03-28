/*
    *******************************************************************************
    MenuBar Class
    Updated 03/27/2025 - Matthew Tomme


    This class creates the menu bar for the app with various options.
    Updated to include theme toggle functionality.


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
    private JMenuBar menuBar = null;
    private JMenu fileMenu = null;
    private JMenu saveMenu = null;
    private JMenu fontMenu = null;
    private JMenu fontSizeMenu = null;
    private JMenu viewMenu = null; // Menu for view options
    private JMenu themeMenu = null; // Menu for theme options
    private JMenuItem lightModeItem = null;
    private JMenuItem darkModeItem = null;
    private final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 16); // constant
    private final ThemeManager themeManager;

    // constructor
    // makes the file menu and save menu with the default font
    public MenuBar()
    {
        // Get theme manager instance
        themeManager = ThemeManager.getInstance();

        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        saveMenu = new JMenu("Save");
        fontMenu = new JMenu("Fonts");
        fontSizeMenu = new JMenu("Font Size");
        viewMenu = new JMenu("View"); // Initialize view menu
        themeMenu = new JMenu("Theme"); // Initialize theme menu

        // set to the default font
        fileMenu.setFont(DEFAULT_FONT);
        saveMenu.setFont(DEFAULT_FONT);
        fontMenu.setFont(DEFAULT_FONT);
        fontSizeMenu.setFont(DEFAULT_FONT);
        viewMenu.setFont(DEFAULT_FONT);
        themeMenu.setFont(DEFAULT_FONT);

        // Add view menu items
        JMenuItem notesItem = new JMenuItem("Notes");
        JMenuItem todoItem = new JMenuItem("To-Do List");
        JMenuItem calendarItem = new JMenuItem("Calendar");

        notesItem.setFont(DEFAULT_FONT);
        todoItem.setFont(DEFAULT_FONT);
        calendarItem.setFont(DEFAULT_FONT);

        viewMenu.add(notesItem);
        viewMenu.add(todoItem);
        viewMenu.add(calendarItem);

        // Add theme menu items
        lightModeItem = new JMenuItem("Light Mode");
        darkModeItem = new JMenuItem("Dark Mode");

        lightModeItem.setFont(DEFAULT_FONT);
        darkModeItem.setFont(DEFAULT_FONT);

        themeMenu.add(lightModeItem);
        themeMenu.add(darkModeItem);

        // Set up theme switcher actions
        setupThemeActions();

        // Register with theme manager
        themeManager.registerComponent(menuBar);
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
        menuBar.add(fileMenu);
        menuBar.add(saveMenu);
        menuBar.add(viewMenu); // Add the view menu
        menuBar.add(fontMenu);
        menuBar.add(fontSizeMenu);
        menuBar.add(themeMenu); // Add the theme menu
    }

    // returns the menu bar to be added to a panel
    public JMenuBar getMenuBar()
    {
        return menuBar;
    } // end getMenuBar

} // end MenuBar class