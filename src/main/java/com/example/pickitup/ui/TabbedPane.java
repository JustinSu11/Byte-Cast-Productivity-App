/*
    *******************************************************************************
    TabbedPane Class
    Last Updated 03/26/2025
    Developer CJ Quintero

    This class makes the tabbed pane and has methods to add or delete tabs.

    Please remember to update the version date if any changes
    are made to this file.
    *******************************************************************************
*/
package com.example.pickitup.ui;

import javax.swing.*;
import java.awt.*;

public class TabbedPane extends JTabbedPane
{
    // fields
    private JTabbedPane tabbedPane = null;
    private String title = null;
    private ScrollPane scrollPane = null;
    private int selectedIndex = 0;
    private final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 16); // constant


    // constructor
    public TabbedPane()
    {
        // initialize the variables
        tabbedPane = new JTabbedPane();
        scrollPane = new ScrollPane();

        // set the default font
        tabbedPane.setFont(DEFAULT_FONT);
    }

    // makes a single tab as an example
    public void makeTabbedPane()
    {
        // sets the tab name, makes the scroll pane (text area), then
        // adds the scroll pane to the new tab
        title = "Page " + (tabbedPane.getTabCount() + 1);
        scrollPane.makeScrollPane();
        tabbedPane.addTab(title, scrollPane.getScrollPane());
    }


    // method to add a new tab to the tabbed pane
    public void addTab()
    {
        // sets the tab name, makes the scroll pane (text area), then
        // adds the scroll pane to the new tab
        title = "Page " + (tabbedPane.getTabCount() + 1);
        ScrollPane newScrollPane = new ScrollPane();
        newScrollPane.makeScrollPane();
        tabbedPane.addTab(title, newScrollPane.getScrollPane());
    }


    // method to remove tabs
    public void deleteTab()
    {
        // get the index of the selected tab
        selectedIndex = tabbedPane.getSelectedIndex();

        // delete the selected tab
        if(tabbedPane.getTabCount() > 0)
        {
            tabbedPane.removeTabAt(tabbedPane.getSelectedIndex());
        }
    }


    // return the tabbed pane to the app class
    public JTabbedPane getTabbedPane()
    {
        return tabbedPane;
    } // end getTabbedPane

} // end class
