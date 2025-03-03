package com.example.pickitup.ui;

import com.example.pickitup.dao.CalendarEventDAO;
import com.example.pickitup.database.DatabaseSetup;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Locale;
import java.util.List;

/**
 * CalendarApp is a simple Swing-based calendar application.
 * It allows users to navigate through months and view a calendar layout.
 * The current day is highlighted for better visibility.
 */
public class CalendarApp {
    private JFrame frame;            // Main application window
    private JPanel calendarPanel;    // Panel to display the calendar
    private JLabel monthLabel;       // Label to display current month and year
    private Calendar calendar;       // Calendar instance to manage date operations
    private Calendar today;          // Tracks the current date
    private CalendarEventDAO eventDAO = new CalendarEventDAO(); // DAO for events
    /**
     * Constructor initializes the calendar UI components and sets up the frame.
     */
    public CalendarApp() {
        // Create the main frame
        frame = new JFrame("Calendar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        // Initialize calendar and track today's date
        calendar = Calendar.getInstance();
        today = Calendar.getInstance();  // Keeps today's date unchanged

        monthLabel = new JLabel("", SwingConstants.CENTER);

        // Create navigation buttons for previous and next months
        JButton preButton = new JButton("<");
        JButton nextButton = new JButton(">");
        preButton.addActionListener(e -> updateMonth(-1));
        nextButton.addActionListener(e -> updateMonth(1));

        // Create header panel containing navigation buttons and month label
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(preButton, BorderLayout.WEST);
        headerPanel.add(monthLabel, BorderLayout.CENTER);
        headerPanel.add(nextButton, BorderLayout.EAST);

        // Panel for the calendar grid (7x7 layout for days and dates)
        calendarPanel = new JPanel(new GridLayout(7, 7, 2, 2));

        // Add components to the frame
        frame.setLayout(new BorderLayout());
        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(calendarPanel, BorderLayout.CENTER);

        // Populate calendar with the current month's data
        updateCalendar();

        // Make the frame visible
        frame.setVisible(true);
    }

    /**
     * Updates the calendar by changing the month based on user navigation.
     *
     * @param change The number of months to shift (negative for previous, positive for next).
     */
    private void updateMonth(int change) {
        calendar.add(Calendar.MONTH, change);
        updateCalendar();
    }

    /**
     * Updates the calendar display for the current month.
     * Clears the panel and repopulates it with the correct day labels and date buttons.
     */
    private void updateCalendar() {
        // Clear previous calendar content
        calendarPanel.removeAll();

        // Set month and year in the header
        monthLabel.setText(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) +
                " " + calendar.get(Calendar.YEAR));

        // Add day labels (Sunday to Saturday)
        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : days) {
            JLabel dayLabel = new JLabel(day, SwingConstants.CENTER);
            dayLabel.setFont(new Font("Arial", Font.BOLD, 12));
            calendarPanel.add(dayLabel);
        }

        // Get the first day of the month and total number of days
        Calendar temp = (Calendar) calendar.clone();
        temp.set(Calendar.DAY_OF_MONTH, 1);
        int firstDay = temp.get(Calendar.DAY_OF_WEEK) - 1; // Convert to 0-based index
        int maxDays = temp.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Add empty labels for alignment before the first day of the month
        for (int i = 0; i < firstDay; i++) {
            calendarPanel.add(new JLabel(""));
        }

        // Add buttons for each day of the month
        for (int day = 1; day <= maxDays; day++) {
            JButton dayButton = new JButton(String.valueOf(day));
            dayButton.setFont(new Font("Arial", Font.PLAIN, 10));
            dayButton.setPreferredSize(new Dimension(30, 30));

            // Highlight today's date
            if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                    calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                    day == today.get(Calendar.DAY_OF_MONTH)) {
                dayButton.setBackground(Color.WHITE);
                dayButton.setForeground(Color.BLACK);
            }

            // Add click event to open event entry dialog
            int selectedDay = day;
            dayButton.addActionListener(e -> {
                String formattedDate = String.format("%d-%02d-%02d 00:00:00",
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, selectedDay);

                // Call the DAO to fetch events
                List<String> events = eventDAO.getEvents(formattedDate);

                if (!events.isEmpty()) {
                    String eventDetails = String.join("\n", events);
                    JOptionPane.showMessageDialog(frame, "Events for " + formattedDate + ":\n" + eventDetails,
                            "Event Details", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    openEventDialog(selectedDay);
                }
            });

            calendarPanel.add(dayButton);
        }

        // Refresh the frame to reflect updates
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Opens a dialog to enter an event for the selected date.
     *
     * @param day The selected day of the month.
     */
    private void openEventDialog(int day) {
        String title = JOptionPane.showInputDialog(frame, "Enter event title:", "New Event", JOptionPane.PLAIN_MESSAGE);
        if (title == null || title.trim().isEmpty()) return;

        String description = JOptionPane.showInputDialog(frame, "Enter event description:", "Event Details", JOptionPane.PLAIN_MESSAGE);
        if (description == null || description.trim().isEmpty()) return;

        // Format the date for storing
        String formattedDate = String.format("%d-%02d-%02d 00:00:00",
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, day);

        saveEvent(title, description, formattedDate);  // Call the saveEvent method
    }

    /**
     * Saves the event details to the database or memory.
     *
     * @param title       The title of the event.
     * @param description The description of the event.
     * @param startTime   The starting date and time of the event.
     */
    public void saveEvent(String title, String description, String startTime) {
        // Implement the logic to save the event details to the database or memory
        // For now, we just print the event details
        System.out.println("Event saved: " + title + ", " + description + ", " + startTime);
    }

    public static void main(String[] args)
    {
        DatabaseSetup.createTables();
        SwingUtilities.invokeLater(CalendarApp::new);
    }
}

