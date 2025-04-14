/**
 * Makes the app frame and sets attributes for it
 *
 * @author CJ Quintero
 * @author Aron Rios
 * @date 04/12/2025
 */
package com.example.pickitup.ui;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;


public class AppFrame extends JFrame
{
    private JPanel mainPanel = null;
    private final String TITLE = "Pick It Up"; // constant


    // Constructor: Creates the objects and sets Look and Feel
    public AppFrame()
    {
        // fields
        JFrame mainFrame = new JFrame(TITLE);
        mainPanel = new JPanel();

        try
        {
            // Simple setup without checking for UIScale
            FlatLightLaf.setup();
            UIManager.setLookAndFeel(new FlatLightLaf());
        }
        catch (Exception e)
        {
            e.printStackTrace();

            // Fall back to system look and feel if FlatLaf fails
            try
            {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex)
            {
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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        Image icon = Toolkit.getDefaultToolkit().getImage("coconut.jpg");
        setIconImage(icon);

        // border layout is used for the main panel
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

    } // end makeMainAppFrame()

} // end class
