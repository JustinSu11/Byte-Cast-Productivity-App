package com.example.pickitup.ui;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class AppFrame extends JFrame {
    // Fields
    private final JPanel mainPanel;
    private final String TITLE = "Pick It Up"; // Constant
    public static AIAssistantPanel aiAssistantPanel;

    // Constructor: Creates the objects and sets Look and Feel
    public AppFrame() {
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
        setTitle(TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null); // Center the window

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        add(mainPanel);
    }

    // This method initializes the main frame attributes
    public void makeMainAppFrame() {
        SwingUtilities.updateComponentTreeUI(this);
        // Force revalidation and repainting
        revalidate();
        repaint();

        setVisible(true); // Show the window
    }

    // Main method to launch the app
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppFrame app = new AppFrame();
            app.makeMainAppFrame();
            SwingUtilities.updateComponentTreeUI(app);
            app.revalidate();
            app.repaint();
        });

    }
}
