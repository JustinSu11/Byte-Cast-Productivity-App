/*
    *******************************************************************************
    AppFrame Class
    Updated 04/02/2025


    This class creates the main frame for the app and
    sets some basic attributes for the frame.


    Please remember to update the version date if any changes
    are made to this file.
    *******************************************************************************
 */
package com.example.pickitup.ui;

import com.example.pickitup.services.ApplicationStateServices;
import com.example.pickitup.services.database.DatabaseConnection;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

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
        // Save then close app when X is clicked
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                try {
                    ApplicationStateServices.saveApplicationState();
                } catch (Exception error) {
                    error.printStackTrace();
                }
                System.exit(0);
            }
        });
        setExtendedState(JFrame.MAXIMIZED_BOTH); // open in fullscreen
        setLocationRelativeTo(null); // open in the center of the screen

        Image icon = Toolkit.getDefaultToolkit().getImage("coconut.jpg");
        setIconImage(icon);

        // border layout is used for the main panel
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);
    } // end makeMainAppFrame()
} // end Frame class
