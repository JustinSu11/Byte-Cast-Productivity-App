/*
    *******************************************************************************
    AppFrame Class
    Updated 03/06/2025


    This class creates the main frame for the app and
    sets some basic attributes for the frame.


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
    private final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 16); // constant

    // constructor
    // makes the file menu and save menu with the default font
    public MenuBar()
    {
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        saveMenu = new JMenu("Save");
        fontMenu = new JMenu("Fonts");
        fontSizeMenu = new JMenu("Font Size");

        // set to the default font
        fileMenu.setFont(DEFAULT_FONT);
        saveMenu.setFont(DEFAULT_FONT);
        fontMenu.setFont(DEFAULT_FONT);
        fontSizeMenu.setFont(DEFAULT_FONT);


    }


    // adds the menus to the menu bar
    public void makeMenuBar()
    {
        menuBar.add(fileMenu);
        menuBar.add(saveMenu);
        menuBar.add(fontMenu);
        menuBar.add(fontSizeMenu);
    }

    // returns the menu bar to be added to a panel
    public JMenuBar getMenuBar()
    {
        return menuBar;
    } // end getMenuBar

} // end MenuBar class
