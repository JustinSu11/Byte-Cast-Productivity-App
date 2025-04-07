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

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;


public class AppFrame extends JFrame
{
    private JPanel mainPanel = null;
    private final String TITLE = "Pick It Up"; // constant
    public static AIAssistantPanel aiAssistantPanel;


    // Constructor: Creates the objects and sets Look and Feel
    public AppFrame() {
        // fields
        JFrame mainFrame = new JFrame(TITLE);
        mainPanel = new JPanel();

        try {
            // Simple setup without checking for UIScale
            FlatLightLaf.setup();
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
            // Fall back to system look and feel if FlatLaf fails
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // This method sets basic attributes of the main app frame
    public void makeMainAppFrame()
    {
        // Force revalidation and repainting
        revalidate();
        repaint();

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
