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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MenuBar extends JMenuBar
{
    // fields
    private JMenuBar menuBar = null;
    private TabbedPane tabbedPane = null;
    private final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 16); // constant

    // menu bar buttons
    private JMenu fileMenu = null;
    private JMenu saveMenu = null;
    private JMenu fontMenu = null;
    private JMenu fontSizeMenu = null;

    // menu item buttons for fileMenu
    private JMenuItem newPage = null;
    private JMenuItem deletePage = null;



    // constructor
    public MenuBar(TabbedPane tabbedPane)
    {
        menuBar = new JMenuBar();
        this.tabbedPane = tabbedPane; // reference to the main frame's tabbed pane

        // menu bar tabs
        fileMenu = new JMenu("File");
        saveMenu = new JMenu("Save");
        fontMenu = new JMenu("Fonts");
        fontSizeMenu = new JMenu("Font Size");

        // menu items for fileMenu
        newPage = new JMenuItem("New Page");
        deletePage = new JMenuItem("Delete Page");


        // default font for the menus
        fileMenu.setFont(DEFAULT_FONT);
        saveMenu.setFont(DEFAULT_FONT);
        fontMenu.setFont(DEFAULT_FONT);
        fontSizeMenu.setFont(DEFAULT_FONT);

        // for menu items
        newPage.setFont(DEFAULT_FONT);
        deletePage.setFont(DEFAULT_FONT);
    }


    // adds the menus to the menu bar
    public void makeMenuBar()
    {
        // menu items for file menu
        newPage.addActionListener(e -> tabbedPane.addTab());
        deletePage.addActionListener(e -> tabbedPane.deleteTab());
        fileMenu.add(newPage);
        fileMenu.add(deletePage);


        // add the menus to the menu bar
        // keep these at the bottom of this method
        menuBar.add(fileMenu);
        menuBar.add(saveMenu);
        menuBar.add(fontMenu);
        menuBar.add(fontSizeMenu);
    }

    // returns the menu bar to the App class
    public JMenuBar getMenuBar()
    {
        return menuBar;
    } // end getMenuBar

} // end MenuBar class
