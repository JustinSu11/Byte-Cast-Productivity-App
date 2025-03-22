/*
    *******************************************************************************
    MenuBar Class
    Updated 03/21/2025


    This class creates the menu bar for the app with various options.


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
    private JMenu viewMenu = null; // New menu for view options

    // constructor
    // makes the file menu and save menu with the default font
    public MenuBar()
    {
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        saveMenu = new JMenu("Save");
        fontMenu = new JMenu("Fonts");
        fontSizeMenu = new JMenu("Font Size");
        viewMenu = new JMenu("View"); // Initialize new view menu

        // set to the default font
        // constant
        Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 16);
        fileMenu.setFont(DEFAULT_FONT);
        saveMenu.setFont(DEFAULT_FONT);
        fontMenu.setFont(DEFAULT_FONT);
        fontSizeMenu.setFont(DEFAULT_FONT);
        viewMenu.setFont(DEFAULT_FONT);

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
    }


    // adds the menus to the menu bar
    public void makeMenuBar()
    {
        menuBar.add(fileMenu);
        menuBar.add(saveMenu);
        menuBar.add(viewMenu); // Add the view menu
        menuBar.add(fontMenu);
        menuBar.add(fontSizeMenu);
    }

    // returns the menu bar to be added to a panel
    public JMenuBar getMenuBar()
    {
        return menuBar;
    } // end getMenuBar

} // end MenuBar class