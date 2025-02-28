/*
    *******************************************************************************
    AppFrame Class
    Updated 02/28/2025


    This class creates the main frame for the app and
    sets some basic attributes for the frame.


    Please remember to update the version date if any changes
    are made to this file.
    *******************************************************************************
 */
package com.example.pickitup.ui;


import javax.swing.*;
import java.awt.*;


public class AppFrame extends JFrame
{
    // fields
    private JFrame mainFrame = null;
    private JPanel mainPanel = null;
    private final String TITLE = "Pick It Up"; // constant


    // constructor creates the objects
    public AppFrame()
    {
        mainFrame = new JFrame(TITLE);
        mainPanel = new JPanel();
    }


    // This method sets basic attributes of the main app frame
    public void makeMainAppFrame()
    {
        // set some attributes of the frame
        setTitle(TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close app when X is clicked
        setExtendedState(JFrame.MAXIMIZED_BOTH); // open in fullscreen
        setLocationRelativeTo(null); // open in the center of the screen


        // border layout is used for the main panel
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);
    } // end makeMainAppFrame()

} // end Frame class
