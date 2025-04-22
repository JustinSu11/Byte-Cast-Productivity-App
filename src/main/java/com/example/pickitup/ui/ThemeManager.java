/**
 * Makes the methods to change the theme from light/dark
 *
 * @author Matthew Tomme
 * @date 04/23/2025
 */
package com.example.pickitup.ui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;


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
    public static final Color LIGHT_MENU_HOVER_BACKGROUND = new Color(210, 210, 210);
    public static final Color LIGHT_MENU_HOVER_FOREGROUND = new Color(0, 0, 0);
    public static final Color LIGHT_DIALOG_BACKGROUND = new Color(240, 240, 240);
    public static final Color LIGHT_DIALOG_FOREGROUND = new Color(33, 33, 33);
    public static final Color LIGHT_BORDER = new Color(200, 200, 200);

    // Dark mode colors
    public static final Color DARK_BACKGROUND = new Color(43, 43, 43);
    public static final Color DARK_FOREGROUND = new Color(220, 220, 220);
    public static final Color DARK_PANEL_BACKGROUND = new Color(60, 63, 65);
    public static final Color DARK_SELECTION_BACKGROUND = new Color(75, 110, 175);
    public static final Color DARK_BUTTON_BACKGROUND = new Color(80, 80, 80);
    public static final Color DARK_TABLE_GRID = new Color(100, 100, 100);
    public static final Color DARK_TABLE_HEADER = new Color(70, 70, 70);
    public static final Color DARK_MENU_HOVER_BACKGROUND = new Color(90, 90, 90);
    public static final Color DARK_MENU_HOVER_FOREGROUND = new Color(255, 255, 255);
    public static final Color DARK_DIALOG_BACKGROUND = new Color(60, 63, 65);
    public static final Color DARK_DIALOG_FOREGROUND = new Color(220, 220, 220);
    public static final Color DARK_BORDER = new Color(100, 100, 100);

    // Singleton instance
    private static ThemeManager instance;

    // Current theme
    private int currentTheme;

    // Preferences for storing user theme choice
    private final Preferences prefs;

    // List of components to update when theme changes
    private final List<JComponent> registeredComponents;

    // Map to track components with custom colors that should be preserved
    private final Map<JComponent, Color> customBackgroundColors = new HashMap<>();
    private final Map<JComponent, Color> customForegroundColors = new HashMap<>();

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

        // Configure the popup menu and option pane settings
        configureDialogAndMenuSettings();

        // Apply theme to all registered components
        applyThemeToRegisteredComponents();

        // Apply theme to future Swing components
        setupUIManagerDefaults();
    }

    /**
     * Configures dialog and menu settings for the current theme
     */
    private void configureDialogAndMenuSettings() {
        // Configure dialog colors
        UIManager.put("OptionPane.background", currentTheme == LIGHT_MODE ?
                LIGHT_DIALOG_BACKGROUND : DARK_DIALOG_BACKGROUND);
        UIManager.put("OptionPane.foreground", currentTheme == LIGHT_MODE ?
                LIGHT_DIALOG_FOREGROUND : DARK_DIALOG_FOREGROUND);
        UIManager.put("OptionPane.messageForeground", currentTheme == LIGHT_MODE ?
                LIGHT_DIALOG_FOREGROUND : DARK_DIALOG_FOREGROUND);

        // Configure dialog panel colors
        UIManager.put("Panel.background", currentTheme == LIGHT_MODE ?
                LIGHT_DIALOG_BACKGROUND : DARK_DIALOG_BACKGROUND);
        UIManager.put("Panel.foreground", currentTheme == LIGHT_MODE ?
                LIGHT_DIALOG_FOREGROUND : DARK_DIALOG_FOREGROUND);

        // Configure menu selection colors
        UIManager.put("MenuItem.selectionBackground", currentTheme == LIGHT_MODE ?
                LIGHT_MENU_HOVER_BACKGROUND : DARK_MENU_HOVER_BACKGROUND);
        UIManager.put("MenuItem.selectionForeground", currentTheme == LIGHT_MODE ?
                LIGHT_MENU_HOVER_FOREGROUND : DARK_MENU_HOVER_FOREGROUND);
        UIManager.put("Menu.selectionBackground", currentTheme == LIGHT_MODE ?
                LIGHT_MENU_HOVER_BACKGROUND : DARK_MENU_HOVER_BACKGROUND);
        UIManager.put("Menu.selectionForeground", currentTheme == LIGHT_MODE ?
                LIGHT_MENU_HOVER_FOREGROUND : DARK_MENU_HOVER_FOREGROUND);

        // Configure dialog borders
        Border dialogBorder = BorderFactory.createLineBorder(
                currentTheme == LIGHT_MODE ? LIGHT_BORDER : DARK_BORDER);
        UIManager.put("OptionPane.border", dialogBorder);

        // Configure dialog button colors
        UIManager.put("Button.background", currentTheme == LIGHT_MODE ?
                LIGHT_BUTTON_BACKGROUND : DARK_BUTTON_BACKGROUND);
        UIManager.put("Button.foreground", currentTheme == LIGHT_MODE ?
                LIGHT_DIALOG_FOREGROUND : DARK_DIALOG_FOREGROUND);

        // Configure text field colors in dialogs
        UIManager.put("TextField.background", currentTheme == LIGHT_MODE ?
                Color.WHITE : new Color(70, 73, 75));
        UIManager.put("TextField.foreground", currentTheme == LIGHT_MODE ?
                LIGHT_DIALOG_FOREGROUND : DARK_DIALOG_FOREGROUND);
        UIManager.put("TextField.caretForeground", currentTheme == LIGHT_MODE ?
                LIGHT_DIALOG_FOREGROUND : DARK_DIALOG_FOREGROUND);

        // Configure combo box colors
        UIManager.put("ComboBox.background", currentTheme == LIGHT_MODE ?
                LIGHT_PANEL_BACKGROUND : DARK_PANEL_BACKGROUND);
        UIManager.put("ComboBox.foreground", currentTheme == LIGHT_MODE ?
                LIGHT_FOREGROUND : DARK_FOREGROUND);
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
     * Sets a custom background color for a component that should be preserved
     * This color won't be overridden by theme changes
     *
     * @param component The component
     * @param color The custom color to preserve
     */
    public void setCustomBackgroundColor(JComponent component, Color color) {
        customBackgroundColors.put(component, color);
        component.setBackground(color);
    }

    /**
     * Sets a custom foreground color for a component that should be preserved
     * This color won't be overridden by theme changes
     *
     * @param component The component
     * @param color The custom color to preserve
     */
    public void setCustomForegroundColor(JComponent component, Color color) {
        customForegroundColors.put(component, color);
        component.setForeground(color);
    }

    /**
     * Checks if a component has a custom background color
     *
     * @param component The component to check
     * @return true if the component has a custom background color
     */
    public boolean hasCustomBackgroundColor(JComponent component) {
        return customBackgroundColors.containsKey(component);
    }

    /**
     * Checks if a component has a custom foreground color
     *
     * @param component The component to check
     * @return true if the component has a custom foreground color
     */
    public boolean hasCustomForegroundColor(JComponent component) {
        return customForegroundColors.containsKey(component);
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

        // Check if this component has custom colors that should be preserved
        boolean hasCustomBg = hasCustomBackgroundColor(component);
        boolean hasCustomFg = hasCustomForegroundColor(component);

        // If the component is a text area in NotesPane, preserve its colors
        if (component instanceof JTextArea && component.getParent() != null &&
                component.getParent().getParent() instanceof NotesPane) {
            // This is likely a text area in NotesPane, don't theme it
            return;
        }

        // Apply theme colors to components without custom colors
        if (currentTheme == LIGHT_MODE) {
            if (!hasCustomBg) {
                applyLightThemeBackground(component);
            }
            if (!hasCustomFg) {
                applyLightThemeForeground(component);
            }
        } else {
            if (!hasCustomBg) {
                applyDarkThemeBackground(component);
            }
            if (!hasCustomFg) {
                applyDarkThemeForeground(component);
            }
        }

        // Special handling for menus
        if (component instanceof JMenuBar || component instanceof JMenu) {
            applyMenuTheme(component);
        }

        // Recursively apply theme to child components
        for (Component child : component.getComponents()) {
            if (child instanceof JComponent) {
                JComponent childComponent = (JComponent) child;

                // Check if this component has custom colors that should be preserved
                boolean childHasCustomBg = hasCustomBackgroundColor(childComponent);
                boolean childHasCustomFg = hasCustomForegroundColor(childComponent);

                // Special handling for text components in NotesPane
                boolean isNotesPaneTextComponent = childComponent instanceof JTextComponent &&
                        component instanceof NotesPane;

                if (!isNotesPaneTextComponent) {
                    applyThemeToComponent(childComponent);
                }
            }
        }

        component.repaint();
    }

    /**
     * Apply appropriate theme to menu components
     */
    private void applyMenuTheme(JComponent component) {
        if (component instanceof JMenu) {
            JMenu menu = (JMenu) component;

            // Apply theme to the popup menu
            JPopupMenu popupMenu = menu.getPopupMenu();
            if (popupMenu != null) {
                popupMenu.setBackground(currentTheme == LIGHT_MODE ?
                        LIGHT_PANEL_BACKGROUND : DARK_PANEL_BACKGROUND);
                popupMenu.setForeground(currentTheme == LIGHT_MODE ?
                        LIGHT_FOREGROUND : DARK_FOREGROUND);
                popupMenu.setBorder(BorderFactory.createLineBorder(
                        currentTheme == LIGHT_MODE ? LIGHT_BORDER : DARK_BORDER));

                // Apply theme to all menu items in the popup
                for (Component menuItem : popupMenu.getComponents()) {
                    if (menuItem instanceof JMenuItem) {
                        menuItem.setBackground(currentTheme == LIGHT_MODE ?
                                LIGHT_PANEL_BACKGROUND : DARK_PANEL_BACKGROUND);
                        menuItem.setForeground(currentTheme == LIGHT_MODE ?
                                LIGHT_FOREGROUND : DARK_FOREGROUND);
                    }
                }
            }

            // Set menu hover colors
            menu.setBackground(currentTheme == LIGHT_MODE ?
                    LIGHT_PANEL_BACKGROUND : DARK_PANEL_BACKGROUND);
            menu.setForeground(currentTheme == LIGHT_MODE ?
                    LIGHT_FOREGROUND : DARK_FOREGROUND);
        }
    }

    /**
     * Applies light theme background to a component
     */
    private void applyLightThemeBackground(JComponent component) {
        if (component instanceof JPanel) {
            component.setBackground(LIGHT_PANEL_BACKGROUND);
        } else if (component instanceof JTextArea || component instanceof JTextField) {
            component.setBackground(LIGHT_PANEL_BACKGROUND);
        } else if (component instanceof JButton) {
            component.setBackground(LIGHT_BUTTON_BACKGROUND);
        } else if (component instanceof JTable) {
            JTable table = (JTable) component;
            table.setBackground(LIGHT_PANEL_BACKGROUND);
            table.setGridColor(LIGHT_TABLE_GRID);
            table.getTableHeader().setBackground(LIGHT_TABLE_HEADER);
        } else if (component instanceof JScrollPane) {
            JScrollPane scrollPane = (JScrollPane) component;
            scrollPane.getViewport().setBackground(LIGHT_PANEL_BACKGROUND);
            scrollPane.setBorder(BorderFactory.createLineBorder(LIGHT_BORDER));
        } else if (component instanceof JMenuBar || component instanceof JMenu) {
            component.setBackground(LIGHT_PANEL_BACKGROUND);

            // Apply to menu items
            if (component instanceof JMenu) {
                JMenu menu = (JMenu) component;
                JPopupMenu popup = menu.getPopupMenu();
                popup.setBackground(LIGHT_PANEL_BACKGROUND);

                for (int i = 0; i < menu.getItemCount(); i++) {
                    JMenuItem item = menu.getItem(i);
                    if (item != null) {
                        item.setBackground(LIGHT_PANEL_BACKGROUND);
                    }
                }
            }
        } else {
            component.setBackground(LIGHT_PANEL_BACKGROUND);
        }
    }

    /**
     * Applies light theme foreground to a component
     */
    private void applyLightThemeForeground(JComponent component) {
        if (component instanceof JTable) {
            JTable table = (JTable) component;
            table.setForeground(LIGHT_FOREGROUND);
            table.getTableHeader().setForeground(LIGHT_FOREGROUND);
        } else if (component instanceof JMenuBar || component instanceof JMenu) {
            component.setForeground(LIGHT_FOREGROUND);

            // Apply to menu items
            if (component instanceof JMenu) {
                JMenu menu = (JMenu) component;
                JPopupMenu popup = menu.getPopupMenu();
                popup.setForeground(LIGHT_FOREGROUND);

                for (int i = 0; i < menu.getItemCount(); i++) {
                    JMenuItem item = menu.getItem(i);
                    if (item != null) {
                        item.setForeground(LIGHT_FOREGROUND);
                    }
                }
            }
        } else {
            component.setForeground(LIGHT_FOREGROUND);
        }
    }

    /**
     * Applies dark theme background to a component
     */
    private void applyDarkThemeBackground(JComponent component) {
        if (component instanceof JPanel) {
            component.setBackground(DARK_PANEL_BACKGROUND);
        } else if (component instanceof JTextArea || component instanceof JTextField) {
            component.setBackground(DARK_PANEL_BACKGROUND);
        } else if (component instanceof JButton) {
            component.setBackground(DARK_BUTTON_BACKGROUND);
        } else if (component instanceof JTable) {
            JTable table = (JTable) component;
            table.setBackground(DARK_PANEL_BACKGROUND);
            table.setGridColor(DARK_TABLE_GRID);
            table.getTableHeader().setBackground(DARK_TABLE_HEADER);
        } else if (component instanceof JScrollPane) {
            JScrollPane scrollPane = (JScrollPane) component;
            scrollPane.getViewport().setBackground(DARK_PANEL_BACKGROUND);
            scrollPane.setBorder(BorderFactory.createLineBorder(DARK_BORDER));
        } else if (component instanceof JMenuBar || component instanceof JMenu) {
            component.setBackground(DARK_PANEL_BACKGROUND);

            // Apply to menu items
            if (component instanceof JMenu) {
                JMenu menu = (JMenu) component;
                JPopupMenu popup = menu.getPopupMenu();
                popup.setBackground(DARK_PANEL_BACKGROUND);

                for (int i = 0; i < menu.getItemCount(); i++) {
                    JMenuItem item = menu.getItem(i);
                    if (item != null) {
                        item.setBackground(DARK_PANEL_BACKGROUND);
                    }
                }
            }
        } else {
            component.setBackground(DARK_PANEL_BACKGROUND);
        }
    }

    /**
     * Applies dark theme foreground to a component
     */
    private void applyDarkThemeForeground(JComponent component) {
        if (component instanceof JTable) {
            JTable table = (JTable) component;
            table.setForeground(DARK_FOREGROUND);
            table.getTableHeader().setForeground(DARK_FOREGROUND);
        } else if (component instanceof JMenuBar || component instanceof JMenu) {
            component.setForeground(DARK_FOREGROUND);

            // Apply to menu items
            if (component instanceof JMenu) {
                JMenu menu = (JMenu) component;
                JPopupMenu popup = menu.getPopupMenu();
                popup.setForeground(DARK_FOREGROUND);

                for (int i = 0; i < menu.getItemCount(); i++) {
                    JMenuItem item = menu.getItem(i);
                    if (item != null) {
                        item.setForeground(DARK_FOREGROUND);
                    }
                }
            }
        } else {
            component.setForeground(DARK_FOREGROUND);
        }
    }

    /**
     * Sets up UIManager defaults for the current theme
     * This affects all new Swing components created
     */
    private void setupUIManagerDefaults() {
        // Basic components
        UIManager.put("Panel.background", new ColorUIResource(currentTheme == LIGHT_MODE ?
                LIGHT_PANEL_BACKGROUND : DARK_PANEL_BACKGROUND));
        UIManager.put("Panel.foreground", new ColorUIResource(currentTheme == LIGHT_MODE ?
                LIGHT_FOREGROUND : DARK_FOREGROUND));

        // Text components
        UIManager.put("TextField.background", new ColorUIResource(currentTheme == LIGHT_MODE ?
                LIGHT_PANEL_BACKGROUND : DARK_PANEL_BACKGROUND));
        UIManager.put("TextField.foreground", new ColorUIResource(currentTheme == LIGHT_MODE ?
                LIGHT_FOREGROUND : DARK_FOREGROUND));
        UIManager.put("TextArea.background", new ColorUIResource(currentTheme == LIGHT_MODE ?
                LIGHT_PANEL_BACKGROUND : DARK_PANEL_BACKGROUND));
        UIManager.put("TextArea.foreground", new ColorUIResource(currentTheme == LIGHT_MODE ?
                LIGHT_FOREGROUND : DARK_FOREGROUND));

        // Buttons
        UIManager.put("Button.background", new ColorUIResource(currentTheme == LIGHT_MODE ?
                LIGHT_BUTTON_BACKGROUND : DARK_BUTTON_BACKGROUND));
        UIManager.put("Button.foreground", new ColorUIResource(currentTheme == LIGHT_MODE ?
                LIGHT_FOREGROUND : DARK_FOREGROUND));

        // Menus
        UIManager.put("Menu.background", new ColorUIResource(currentTheme == LIGHT_MODE ?
                LIGHT_PANEL_BACKGROUND : DARK_PANEL_BACKGROUND));
        UIManager.put("Menu.foreground", new ColorUIResource(currentTheme == LIGHT_MODE ?
                LIGHT_FOREGROUND : DARK_FOREGROUND));
        UIManager.put("MenuBar.background", new ColorUIResource(currentTheme == LIGHT_MODE ?
                LIGHT_PANEL_BACKGROUND : DARK_PANEL_BACKGROUND));
        UIManager.put("MenuBar.foreground", new ColorUIResource(currentTheme == LIGHT_MODE ?
                LIGHT_FOREGROUND : DARK_FOREGROUND));
        UIManager.put("MenuItem.background", new ColorUIResource(currentTheme == LIGHT_MODE ?
                LIGHT_PANEL_BACKGROUND : DARK_PANEL_BACKGROUND));
        UIManager.put("MenuItem.foreground", new ColorUIResource(currentTheme == LIGHT_MODE ?
                LIGHT_FOREGROUND : DARK_FOREGROUND));

        // Menu selection colors
        UIManager.put("MenuItem.selectionBackground", new ColorUIResource(currentTheme == LIGHT_MODE ?
                LIGHT_MENU_HOVER_BACKGROUND : DARK_MENU_HOVER_BACKGROUND));
        UIManager.put("MenuItem.selectionForeground", new ColorUIResource(currentTheme == LIGHT_MODE ?
                LIGHT_MENU_HOVER_FOREGROUND : DARK_MENU_HOVER_FOREGROUND));
        UIManager.put("Menu.selectionBackground", new ColorUIResource(currentTheme == LIGHT_MODE ?
                LIGHT_MENU_HOVER_BACKGROUND : DARK_MENU_HOVER_BACKGROUND));
        UIManager.put("Menu.selectionForeground", new ColorUIResource(currentTheme == LIGHT_MODE ?
                LIGHT_MENU_HOVER_FOREGROUND : DARK_MENU_HOVER_FOREGROUND));

        // Tables
        UIManager.put("Table.background", new ColorUIResource(currentTheme == LIGHT_MODE ?
                LIGHT_PANEL_BACKGROUND : DARK_PANEL_BACKGROUND));
        UIManager.put("Table.foreground", new ColorUIResource(currentTheme == LIGHT_MODE ?
                LIGHT_FOREGROUND : DARK_FOREGROUND));
        UIManager.put("Table.gridColor", new ColorUIResource(currentTheme == LIGHT_MODE ?
                LIGHT_TABLE_GRID : DARK_TABLE_GRID));
        UIManager.put("TableHeader.background", new ColorUIResource(currentTheme == LIGHT_MODE ?
                LIGHT_TABLE_HEADER : DARK_TABLE_HEADER));
        UIManager.put("TableHeader.foreground", new ColorUIResource(currentTheme == LIGHT_MODE ?
                LIGHT_FOREGROUND : DARK_FOREGROUND));

        // Scroll panes
        UIManager.put("ScrollPane.background", new ColorUIResource(currentTheme == LIGHT_MODE ?
                LIGHT_PANEL_BACKGROUND : DARK_PANEL_BACKGROUND));

        // Labels
        UIManager.put("Label.foreground", new ColorUIResource(currentTheme == LIGHT_MODE ?
                LIGHT_FOREGROUND : DARK_FOREGROUND));

        // Option panes and dialogs
        UIManager.put("OptionPane.background", new ColorUIResource(currentTheme == LIGHT_MODE ?
                LIGHT_DIALOG_BACKGROUND : DARK_DIALOG_BACKGROUND));
        UIManager.put("OptionPane.foreground", new ColorUIResource(currentTheme == LIGHT_MODE ?
                LIGHT_DIALOG_FOREGROUND : DARK_DIALOG_FOREGROUND));
        UIManager.put("OptionPane.messageForeground", new ColorUIResource(currentTheme == LIGHT_MODE ?
                LIGHT_DIALOG_FOREGROUND : DARK_DIALOG_FOREGROUND));
    }

    /**
     * Initializes the theme for the application
     * Should be called before creating any UI components
     */
    public void initializeTheme() {
        setupUIManagerDefaults();
        configureDialogAndMenuSettings();
    }
}