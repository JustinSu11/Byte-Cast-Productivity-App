package com.example.pickitup.ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * ThemeManager handles the application's theme (dark/light mode)
 * It provides methods to switch themes and stores the user's preference
 *
 * @version 1.0
 */
public class ThemeManager {
    // Theme constants
    public static final int LIGHT_MODE = 0;
    public static final int DARK_MODE = 1;

    // Light mode colors
    public static final Color LIGHT_BACKGROUND = new Color(245, 245, 245);
    public static final Color LIGHT_FOREGROUND = new Color(33, 33, 33);
    public static final Color LIGHT_PANEL_BACKGROUND = new Color(255, 255, 255);
    public static final Color LIGHT_SELECTION_BACKGROUND = new Color(197, 218, 250);
    public static final Color LIGHT_BUTTON_BACKGROUND = new Color(225, 225, 225);
    public static final Color LIGHT_TABLE_GRID = new Color(180, 180, 180);
    public static final Color LIGHT_TABLE_HEADER = new Color(230, 230, 230);

    // Dark mode colors
    public static final Color DARK_BACKGROUND = new Color(43, 43, 43);
    public static final Color DARK_FOREGROUND = new Color(220, 220, 220);
    public static final Color DARK_PANEL_BACKGROUND = new Color(60, 63, 65);
    public static final Color DARK_SELECTION_BACKGROUND = new Color(75, 110, 175);
    public static final Color DARK_BUTTON_BACKGROUND = new Color(80, 80, 80);
    public static final Color DARK_TABLE_GRID = new Color(100, 100, 100);
    public static final Color DARK_TABLE_HEADER = new Color(70, 70, 70);

    // Singleton instance
    private static ThemeManager instance;

    // Current theme
    private int currentTheme;

    // Preferences for storing user theme choice
    private final Preferences prefs;

    // List of components to update when theme changes
    private final List<JComponent> registeredComponents;

    /**
     * Private constructor for singleton pattern
     */
    private ThemeManager() {
        prefs = Preferences.userNodeForPackage(ThemeManager.class);
        currentTheme = prefs.getInt("theme", LIGHT_MODE); // Default to light mode
        registeredComponents = new ArrayList<>();
    }

    /**
     * Gets the singleton instance of ThemeManager
     *
     * @return The ThemeManager instance
     */
    public static synchronized ThemeManager getInstance() {
        if (instance == null) {
            instance = new ThemeManager();
        }
        return instance;
    }

    /**
     * Gets the current theme
     *
     * @return The current theme (LIGHT_MODE or DARK_MODE)
     */
    public int getCurrentTheme() {
        return currentTheme;
    }

    /**
     * Sets the theme for the application
     *
     * @param theme The theme to set (LIGHT_MODE or DARK_MODE)
     */
    public void setTheme(int theme) {
        if (theme != LIGHT_MODE && theme != DARK_MODE) {
            throw new IllegalArgumentException("Invalid theme: " + theme);
        }

        currentTheme = theme;
        prefs.putInt("theme", theme);

        // Apply theme to all registered components
        applyThemeToRegisteredComponents();

        // Apply theme to future Swing components
        setupUIManagerDefaults();
    }

    /**
     * Toggles between light and dark mode
     */
    public void toggleTheme() {
        setTheme(currentTheme == LIGHT_MODE ? DARK_MODE : LIGHT_MODE);
    }

    /**
     * Registers a component to receive theme updates
     *
     * @param component The component to register
     */
    public void registerComponent(JComponent component) {
        if (!registeredComponents.contains(component)) {
            registeredComponents.add(component);
            applyThemeToComponent(component);
        }
    }

    /**
     * Applies the current theme to all registered components
     */
    private void applyThemeToRegisteredComponents() {
        for (JComponent component : registeredComponents) {
            applyThemeToComponent(component);
        }
    }

    /**
     * Applies the current theme to a specific component
     *
     * @param component The component to apply the theme to
     */
    private void applyThemeToComponent(JComponent component) {
        // Skip components that should maintain custom colors
        if (component instanceof JButton) {
            JButton button = (JButton) component;
            // If the button already has a non-default background color, don't change it
            if (button.isOpaque() && !button.isFocusPainted()) {
                // This button has custom styling, skip theme application
                return;
            }
        }

        if (currentTheme == LIGHT_MODE) {
            applyLightTheme(component);
        } else {
            applyDarkTheme(component);
        }

        // Recursively apply theme to child components
        for (Component child : component.getComponents()) {
            if (child instanceof JComponent) {
                applyThemeToComponent((JComponent) child);
            }
        }

        component.repaint();
    }

    /**
     * Applies light theme to a component
     *
     * @param component The component to apply the theme to
     */
    private void applyLightTheme(JComponent component) {
        component.setBackground(LIGHT_PANEL_BACKGROUND);
        component.setForeground(LIGHT_FOREGROUND);

        if (component instanceof JPanel) {
            component.setBackground(LIGHT_PANEL_BACKGROUND);
        } else if (component instanceof JTextArea || component instanceof JTextField) {
            component.setBackground(LIGHT_PANEL_BACKGROUND);
            component.setForeground(LIGHT_FOREGROUND);
            component.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        } else if (component instanceof JButton) {
            component.setBackground(LIGHT_BUTTON_BACKGROUND);
            component.setForeground(LIGHT_FOREGROUND);
        } else if (component instanceof JTable) {
            JTable table = (JTable) component;
            table.setBackground(LIGHT_PANEL_BACKGROUND);
            table.setForeground(LIGHT_FOREGROUND);
            table.setGridColor(LIGHT_TABLE_GRID);
            table.getTableHeader().setBackground(LIGHT_TABLE_HEADER);
            table.getTableHeader().setForeground(LIGHT_FOREGROUND);
        } else if (component instanceof JScrollPane) {
            JScrollPane scrollPane = (JScrollPane) component;
            scrollPane.getViewport().setBackground(LIGHT_PANEL_BACKGROUND);
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        } else if (component instanceof JMenuBar || component instanceof JMenu) {
            component.setBackground(LIGHT_PANEL_BACKGROUND);
            component.setForeground(LIGHT_FOREGROUND);

            // Apply to menu items
            if (component instanceof JMenu) {
                JMenu menu = (JMenu) component;
                for (int i = 0; i < menu.getItemCount(); i++) {
                    JMenuItem item = menu.getItem(i);
                    if (item != null) {
                        item.setBackground(LIGHT_PANEL_BACKGROUND);
                        item.setForeground(LIGHT_FOREGROUND);
                    }
                }
            }
        }
    }

    /**
     * Applies dark theme to a component
     *
     * @param component The component to apply the theme to
     */
    private void applyDarkTheme(JComponent component) {
        component.setBackground(DARK_PANEL_BACKGROUND);
        component.setForeground(DARK_FOREGROUND);

        if (component instanceof JPanel) {
            component.setBackground(DARK_PANEL_BACKGROUND);
        } else if (component instanceof JTextArea || component instanceof JTextField) {
            component.setBackground(DARK_PANEL_BACKGROUND);
            component.setForeground(DARK_FOREGROUND);
            component.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        } else if (component instanceof JButton) {
            component.setBackground(DARK_BUTTON_BACKGROUND);
            component.setForeground(DARK_FOREGROUND);
        } else if (component instanceof JTable) {
            JTable table = (JTable) component;
            table.setBackground(DARK_PANEL_BACKGROUND);
            table.setForeground(DARK_FOREGROUND);
            table.setGridColor(DARK_TABLE_GRID);
            table.getTableHeader().setBackground(DARK_TABLE_HEADER);
            table.getTableHeader().setForeground(DARK_FOREGROUND);
        } else if (component instanceof JScrollPane) {
            JScrollPane scrollPane = (JScrollPane) component;
            scrollPane.getViewport().setBackground(DARK_PANEL_BACKGROUND);
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        } else if (component instanceof JMenuBar || component instanceof JMenu) {
            component.setBackground(DARK_PANEL_BACKGROUND);
            component.setForeground(DARK_FOREGROUND);

            // Apply to menu items
            if (component instanceof JMenu) {
                JMenu menu = (JMenu) component;
                for (int i = 0; i < menu.getItemCount(); i++) {
                    JMenuItem item = menu.getItem(i);
                    if (item != null) {
                        item.setBackground(DARK_PANEL_BACKGROUND);
                        item.setForeground(DARK_FOREGROUND);
                    }
                }
            }
        }
    }

    /**
     * Sets up UIManager defaults for the current theme
     * This affects all new Swing components created
     */
    private void setupUIManagerDefaults() {
        UIManager.put("Panel.background", currentTheme == LIGHT_MODE ? LIGHT_PANEL_BACKGROUND : DARK_PANEL_BACKGROUND);
        UIManager.put("Panel.foreground", currentTheme == LIGHT_MODE ? LIGHT_FOREGROUND : DARK_FOREGROUND);

        UIManager.put("TextField.background", currentTheme == LIGHT_MODE ? LIGHT_PANEL_BACKGROUND : DARK_PANEL_BACKGROUND);
        UIManager.put("TextField.foreground", currentTheme == LIGHT_MODE ? LIGHT_FOREGROUND : DARK_FOREGROUND);

        UIManager.put("TextArea.background", currentTheme == LIGHT_MODE ? LIGHT_PANEL_BACKGROUND : DARK_PANEL_BACKGROUND);
        UIManager.put("TextArea.foreground", currentTheme == LIGHT_MODE ? LIGHT_FOREGROUND : DARK_FOREGROUND);

        UIManager.put("Button.background", currentTheme == LIGHT_MODE ? LIGHT_BUTTON_BACKGROUND : DARK_BUTTON_BACKGROUND);
        UIManager.put("Button.foreground", currentTheme == LIGHT_MODE ? LIGHT_FOREGROUND : DARK_FOREGROUND);

        UIManager.put("Menu.background", currentTheme == LIGHT_MODE ? LIGHT_PANEL_BACKGROUND : DARK_PANEL_BACKGROUND);
        UIManager.put("Menu.foreground", currentTheme == LIGHT_MODE ? LIGHT_FOREGROUND : DARK_FOREGROUND);

        UIManager.put("MenuBar.background", currentTheme == LIGHT_MODE ? LIGHT_PANEL_BACKGROUND : DARK_PANEL_BACKGROUND);
        UIManager.put("MenuBar.foreground", currentTheme == LIGHT_MODE ? LIGHT_FOREGROUND : DARK_FOREGROUND);

        UIManager.put("MenuItem.background", currentTheme == LIGHT_MODE ? LIGHT_PANEL_BACKGROUND : DARK_PANEL_BACKGROUND);
        UIManager.put("MenuItem.foreground", currentTheme == LIGHT_MODE ? LIGHT_FOREGROUND : DARK_FOREGROUND);

        UIManager.put("Table.background", currentTheme == LIGHT_MODE ? LIGHT_PANEL_BACKGROUND : DARK_PANEL_BACKGROUND);
        UIManager.put("Table.foreground", currentTheme == LIGHT_MODE ? LIGHT_FOREGROUND : DARK_FOREGROUND);
        UIManager.put("Table.gridColor", currentTheme == LIGHT_MODE ? LIGHT_TABLE_GRID : DARK_TABLE_GRID);

        UIManager.put("TableHeader.background", currentTheme == LIGHT_MODE ? LIGHT_TABLE_HEADER : DARK_TABLE_HEADER);
        UIManager.put("TableHeader.foreground", currentTheme == LIGHT_MODE ? LIGHT_FOREGROUND : DARK_FOREGROUND);

        UIManager.put("ScrollPane.background", currentTheme == LIGHT_MODE ? LIGHT_PANEL_BACKGROUND : DARK_PANEL_BACKGROUND);

        UIManager.put("Label.foreground", currentTheme == LIGHT_MODE ? LIGHT_FOREGROUND : DARK_FOREGROUND);
    }

    /**
     * Initializes the theme for the application
     * Should be called before creating any UI components
     */
    public void initializeTheme() {
        setupUIManagerDefaults();
    }
}