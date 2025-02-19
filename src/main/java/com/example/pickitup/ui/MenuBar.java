package com.example.pickitup.ui;

import javax.swing.*;

public class MenuBar extends JMenuBar {
    public JMenuItem saveItem, saveAsItem;

    public MenuBar() {
        JMenu fileMenu = new JMenu("File");

        // Create menu items
        saveItem = new JMenuItem("Save");
        saveAsItem = new JMenuItem("Save As");

        // Add menu items to the menu
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);

        // Add file menu to the menu bar
        add(fileMenu);
    }
}
