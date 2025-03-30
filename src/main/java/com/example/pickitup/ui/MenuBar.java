package com.example.pickitup.ui;

import javax.swing.*;
import java.awt.*;

public class MenuBar extends JMenuBar {
    private JMenuBar menuBar = null;
    private JMenu fileMenu = null;
    private JMenu saveMenu = null;
    private JMenu fontMenu = null;
    private JMenu fontSizeMenu = null;
    private JMenu calendarMenu = null;
    private final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 16); // constant

    // constructor
    public MenuBar() {
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        saveMenu = new JMenu("Save");
        fontMenu = new JMenu("Fonts");
        fontSizeMenu = new JMenu("Font Size");
        calendarMenu = new JMenu("Calendar");

        // set to the default font
        fileMenu.setFont(DEFAULT_FONT);
        saveMenu.setFont(DEFAULT_FONT);
        fontMenu.setFont(DEFAULT_FONT);
        fontSizeMenu.setFont(DEFAULT_FONT);
        calendarMenu.setFont(DEFAULT_FONT);

        // Set up the calendar menu
        setupCalendarMenu();
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
    public void makeMenuBar() {
        menuBar.add(fileMenu);
        menuBar.add(saveMenu);
        menuBar.add(fontMenu);
        menuBar.add(fontSizeMenu);
        menuBar.add(calendarMenu);

    }

    // returns the menu bar to be added to a panel
    public JMenuBar getMenuBar()
    {
        return menuBar;
    }
}