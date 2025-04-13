/*
    *******************************************************************************
    ToDoListPanel Class
    Last Updated 04/12/2025
    Developers: Matthew Tome

    This class includes the entire panel and all methods to make
    and add functionality to the to do list.

    Please remember to update the version date if any changes
    are made to this file.
    *******************************************************************************
 */
package com.example.pickitup.ui;

import com.example.pickitup.services.models.ToDoItem;
import com.example.pickitup.services.dao.ToDoItemDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;


public class ToDoListPanel extends JPanel {

    private JTable todoTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton completeButton;
    private JButton deleteButton;
    private JTextField taskInput;
    private JTextField dateInput; // Simple text field for date input
    private ToDoItemDAO todoItemDAO;
    private ThemeManager themeManager;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private JFrame frame; // Main frame for the popup

    /**
     * Constructor initializes the To-Do List panel
     */
    public ToDoListPanel() {
        this(false);
    }

    /**
     * Constructor with option to create as a popup
     *
     * @param asPopup Whether to create as a popup window
     */
    public ToDoListPanel(boolean asPopup) {
        // Initialize the DAO
        todoItemDAO = new ToDoItemDAO();

        // Get theme manager instance
        themeManager = ThemeManager.getInstance();

        // Set up layout
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Create a title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("To-Do List", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        // Create the table model with columns
        String[] columnNames = {"Task", "Due Date", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };

        // Create the table and set properties
        todoTable = new JTable(tableModel);
        todoTable.setRowHeight(25);
        todoTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        todoTable.setFont(new Font("Arial", Font.PLAIN, 14));

        // Create scroll pane for the table
        JScrollPane tableScrollPane = new JScrollPane(todoTable);
        tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Create input panel for adding new tasks
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        // Task input field
        taskInput = new JTextField();
        taskInput.setFont(new Font("Arial", Font.PLAIN, 14));

        // Date input field
        dateInput = new JTextField(dateFormat.format(new Date())); // Set to current date by default
        dateInput.setFont(new Font("Arial", Font.PLAIN, 14));
        JButton datePickerButton = new JButton("...");
        datePickerButton.addActionListener(e -> showDatePicker());

        // Create panel for task input and date chooser
        JPanel taskPanel = new JPanel(new BorderLayout());
        taskPanel.add(new JLabel("Task: "), BorderLayout.WEST);
        taskPanel.add(taskInput, BorderLayout.CENTER);

        JPanel datePanel = new JPanel(new BorderLayout());
        datePanel.add(new JLabel("Due Date (yyyy-MM-dd): "), BorderLayout.WEST);
        datePanel.add(dateInput, BorderLayout.CENTER);
        datePanel.add(datePickerButton, BorderLayout.EAST);

        JPanel formPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        formPanel.add(taskPanel);
        formPanel.add(datePanel);

        // Create buttons
        addButton = new JButton("Add Task");
        completeButton = new JButton("Mark Complete");
        deleteButton = new JButton("Delete Task");

        // Create panel for buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 5, 0));
        buttonPanel.add(addButton);
        buttonPanel.add(completeButton);
        buttonPanel.add(deleteButton);

        // Add components to input panel
        inputPanel.add(formPanel, BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add all components to main panel
        add(titlePanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        // Setup event handlers
        setupEventHandlers();

        // Load and display existing to-do items
        loadTodoItems();

        // Set preferred size for panel
        setPreferredSize(new Dimension(500, 400));

        // Register subcomponents with theme manager
        registerComponentsWithThemeManager();

        // Apply custom button styling AFTER registering with theme manager
        // This ensures our custom styling takes precedence
        styleButtonsForCurrentTheme();

        // If creating as a popup, set up the frame
        if (asPopup) {
            frame = new JFrame("To-Do List");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(600, 500);
            frame.add(this);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }

    /**
     * Static method to create and show a ToDoList as a popup
     */
    public static void showAsPopup() {
        SwingUtilities.invokeLater(() -> new ToDoListPanel(true));
    }

    /**
     * Show a simple date picker dialog
     */
    private void showDatePicker() {
        // Create a simple date picker using JOptionPane
        String currentDate = dateInput.getText();
        String newDate = (String) JOptionPane.showInputDialog(
                this,
                "Enter date (yyyy-MM-dd):",
                "Date Picker",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                currentDate);

        if (newDate != null && !newDate.isEmpty()) {
            try {
                // Validate the date format
                Date date = dateFormat.parse(newDate);
                dateInput.setText(dateFormat.format(date));
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Invalid date format. Please use yyyy-MM-dd",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Applies button styling based on the current theme
     */
    private void styleButtonsForCurrentTheme() {
        // Use the same colors regardless of theme to maintain the original look
        // Add Task button (green)
        addButton.setBackground(new Color(46, 125, 50));
        addButton.setForeground(Color.WHITE);

        // Mark Complete button (blue)
        completeButton.setBackground(new Color(33, 150, 243));
        completeButton.setForeground(Color.WHITE);

        // Delete button (red)
        deleteButton.setBackground(new Color(211, 47, 47));
        deleteButton.setForeground(Color.WHITE);

        // Common styling
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        completeButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteButton.setFont(new Font("Arial", Font.BOLD, 14));

        // Ensure buttons don't get overridden by the theme manager
        addButton.setOpaque(true);
        completeButton.setOpaque(true);
        deleteButton.setOpaque(true);

        // Ensure focus doesn't change the colors
        addButton.setFocusPainted(false);
        completeButton.setFocusPainted(false);
        deleteButton.setFocusPainted(false);
    }

    /**
     * Registers all components with the theme manager
     * Note: We exclude the buttons from theme management to keep their colors
     */
    private void registerComponentsWithThemeManager() {
        themeManager.registerComponent(this);
        themeManager.registerComponent(todoTable);
        themeManager.registerComponent((JComponent)todoTable.getTableHeader());
        themeManager.registerComponent(taskInput);
        themeManager.registerComponent(dateInput);

        // Do NOT register the buttons with the theme manager
        // This ensures they keep their original colors
    }

    /**
     * Loads to-do items from the DAO and displays them in the table
     */
    private void loadTodoItems() {
        // Clear the current table data
        tableModel.setRowCount(0);

        // Get all items from the DAO
        List<ToDoItem> items = todoItemDAO.getAllItems();

        // Add each item to the table
        for (ToDoItem item : items) {
            Vector<String> row = new Vector<>();
            row.add(item.getTask());
            row.add(dateFormat.format(item.getDueDate()));
            row.add(item.getStatusText());

            tableModel.addRow(row);
        }
    }

    /**
     * Sets up event handlers for buttons and other components
     */
    private void setupEventHandlers() {
        // Add task button action
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });

        // Mark complete button action
        completeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                markTaskComplete();
            }
        });

        // Delete task button action
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteTask();
            }
        });

        // Allow pressing Enter in the task input to add a task
        taskInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });
    }

    /**
     * Adds a new task to the to-do list
     */
    private void addTask() {
        String task = taskInput.getText().trim();
        String dateString = dateInput.getText().trim();

        if (!task.isEmpty() && !dateString.isEmpty()) {
            try {
                // Parse the date
                Date dueDate = dateFormat.parse(dateString);

                // Add to the DAO (which handles persistence)
                todoItemDAO.addItem(task, dueDate);

                // Reload the table to show the updated list
                loadTodoItems();

                // Clear the input field
                taskInput.setText("");
                taskInput.requestFocus();
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this,
                        "Invalid date format. Please use yyyy-MM-dd format.",
                        "Date Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please enter a task and a due date.",
                    "Input Required",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Marks the selected task as complete
     */
    private void markTaskComplete() {
        int selectedRow = todoTable.getSelectedRow();

        if (selectedRow != -1) {
            String currentStatus = (String) tableModel.getValueAt(selectedRow, 2);
            boolean newStatus = currentStatus.equals("Pending"); // Toggle status

            // Update in the DAO (which handles persistence)
            todoItemDAO.updateItemStatus(selectedRow, newStatus);

            // Reload the table to show the updated status
            loadTodoItems();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please select a task to mark as complete.",
                    "Selection Required",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Deletes the selected task from the to-do list
     */
    private void deleteTask() {
        int selectedRow = todoTable.getSelectedRow();

        if (selectedRow != -1) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this task?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                // Remove from the DAO (which handles persistence)
                todoItemDAO.removeItem(selectedRow);

                // Reload the table to show the updated list
                loadTodoItems();
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please select a task to delete.",
                    "Selection Required",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}