package com.example.pickitup.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 * Panel for To-Do List functionality in the UI
 * @version 1.0
 */
public class ToDoListPanel extends JPanel {

    private JTable todoTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton completeButton;
    private JButton deleteButton;
    private JTextField taskInput;
    private JDateChooser dueDateChooser;

    /**
     * Constructor initializes the To-Do List panel
     */
    public ToDoListPanel() {
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
        tableModel = new DefaultTableModel(columnNames, 0);

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
        taskInput.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        // Due date chooser
        dueDateChooser = new JDateChooser();
        dueDateChooser.setDateFormatString("yyyy-MM-dd");
        dueDateChooser.setDate(new Date()); // Set to current date by default

        // Create panel for task input and date chooser
        JPanel taskPanel = new JPanel(new BorderLayout());
        taskPanel.add(new JLabel("Task: "), BorderLayout.WEST);
        taskPanel.add(taskInput, BorderLayout.CENTER);

        JPanel datePanel = new JPanel(new BorderLayout());
        datePanel.add(new JLabel("Due Date: "), BorderLayout.WEST);
        datePanel.add(dueDateChooser, BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        formPanel.add(taskPanel);
        formPanel.add(datePanel);

        // Create buttons
        addButton = new JButton("Add Task");
        completeButton = new JButton("Mark Complete");
        deleteButton = new JButton("Delete Task");

        // Customize button appearance
        addButton.setBackground(new Color(46, 125, 50));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Arial", Font.BOLD, 14));

        completeButton.setBackground(new Color(33, 150, 243));
        completeButton.setForeground(Color.WHITE);
        completeButton.setFont(new Font("Arial", Font.BOLD, 14));

        deleteButton.setBackground(new Color(211, 47, 47));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFont(new Font("Arial", Font.BOLD, 14));

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

        // Set preferred size for panel
        setPreferredSize(new Dimension(500, 400));
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
        Date dueDate = dueDateChooser.getDate();

        if (!task.isEmpty() && dueDate != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(dueDate);

            Vector<String> row = new Vector<>();
            row.add(task);
            row.add(formattedDate);
            row.add("Pending");

            tableModel.addRow(row);

            // Clear the input field and reset date to today
            taskInput.setText("");
            dueDateChooser.setDate(new Date());
            taskInput.requestFocus();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please enter a task and select a due date.",
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

            // Toggle between Pending and Completed
            if (currentStatus.equals("Pending")) {
                tableModel.setValueAt("Completed", selectedRow, 2);
            } else {
                tableModel.setValueAt("Pending", selectedRow, 2);
            }
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
                tableModel.removeRow(selectedRow);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please select a task to delete.",
                    "Selection Required",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Inner class for date chooser component
     * This is a simplified version. In a real application, you might use a third-party
     * library like JDateChooser from JCalendar.
     */
    private class JDateChooser extends JPanel {
        private JTextField dateField;
        private JButton calendarButton;
        private Date selectedDate;
        private SimpleDateFormat dateFormat;

        public JDateChooser() {
            setLayout(new BorderLayout());

            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            selectedDate = new Date();

            dateField = new JTextField(dateFormat.format(selectedDate));
            dateField.setEditable(false);

            calendarButton = new JButton("...");
            calendarButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // This would typically open a date picker dialog
                    // For simplicity, we're just showing a simple option pane
                    String newDate = JOptionPane.showInputDialog(
                            ToDoListPanel.this,
                            "Enter date (yyyy-MM-dd):",
                            dateFormat.format(selectedDate));

                    if (newDate != null && !newDate.isEmpty()) {
                        try {
                            selectedDate = dateFormat.parse(newDate);
                            dateField.setText(dateFormat.format(selectedDate));
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(
                                    ToDoListPanel.this,
                                    "Invalid date format. Please use yyyy-MM-dd",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });

            add(dateField, BorderLayout.CENTER);
            add(calendarButton, BorderLayout.EAST);
        }

        public void setDateFormatString(String format) {
            dateFormat = new SimpleDateFormat(format);
            dateField.setText(dateFormat.format(selectedDate));
        }

        public Date getDate() {
            return selectedDate;
        }

        public void setDate(Date date) {
            selectedDate = date;
            dateField.setText(dateFormat.format(selectedDate));
        }
    }
}