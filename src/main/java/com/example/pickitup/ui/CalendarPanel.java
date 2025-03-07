package com.example.pickitup.ui;

import com.example.pickitup.services.dao.CalendarEventDAO;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * CalendarApp is a simple Swing-based calendar application.
 * It allows users to navigate through months and view a calendar layout.
 * The current day is highlighted for better visibility.
 */
public class CalendarPanel {
    private JFrame frame;            // Main application window
    private JPanel calendarPanel;    // Panel to display the calendar
    private JLabel monthLabel;       // Label to display current month and year
    private Calendar calendar;       // Calendar instance to manage date operations
    private Calendar today;          // Tracks the current date
    private CalendarEventDAO eventDAO = new CalendarEventDAO(); // DAO for events

    /**
     * Constructor initializes the calendar UI components and sets up the frame.
     */
    public CalendarPanel() {
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

        for (int day = 1; day <= maxDays; day++) {
            JButton dayButton = new JButton(String.valueOf(day));
            dayButton.setFont(new Font("Arial", Font.PLAIN, 10));
            dayButton.setPreferredSize(new Dimension(30, 30));

            // Format date for querying
            String formattedDate = String.format("%d-%02d-%02d 00:00:00",
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, day);

            // Fetch events from the database
            List<String> events = eventDAO.getEvents(formattedDate);

            // Highlight today's date
            if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                    calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                    day == today.get(Calendar.DAY_OF_MONTH)) {
                dayButton.setBackground(Color.WHITE); // Highlight today's date with a different color
                dayButton.setForeground(Color.BLACK);
            }

            // Highlight days with events
            if (!events.isEmpty()) {
                dayButton.setBackground(Color.CYAN); // Change color for event days
                dayButton.setToolTipText("<html>" + String.join("<br>", events) + "</html>"); // Show events on hover
            }

            // Add click event to show event details or open event dialog
            int selectedDay = day;
            dayButton.addActionListener(e -> {
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

        // Revalidate and repaint the calendar panel to update the UI
        calendarPanel.revalidate();
        calendarPanel.repaint();
    }

    /**
     * Opens a dialog to enter an event for the selected date.
     *
     * @param day The selected day of the month
     */
    private void openEventDialog(int day) {
        String title = JOptionPane.showInputDialog(frame, "Enter event title:", "New Event", JOptionPane.PLAIN_MESSAGE);
        if (title == null || title.trim().isEmpty()) return;

        String description = JOptionPane.showInputDialog(frame, "Enter event description:", "Event Details", JOptionPane.PLAIN_MESSAGE);
        if (description == null || description.trim().isEmpty()) return;

        // Time selection using dropdown
        String[] times = {"08:00 AM", "09:00 AM", "10:00 AM", "11:00 AM", "12:00 PM",
                "01:00 PM", "02:00 PM", "03:00 PM", "04:00 PM", "05:00 PM",
                "06:00 PM", "07:00 PM", "08:00 PM", "09:00 PM"};
        String selectedTime = (String) JOptionPane.showInputDialog(frame,
                "Select time:", "Event Time", JOptionPane.QUESTION_MESSAGE, null, times, times[0]);

        if (selectedTime == null) return; // User canceled

        // Convert time to 24-hour format
        String formattedTime = convertTo24Hour(selectedTime);

        // Format date-time string
        String formattedDateTime = String.format("%d-%02d-%02d %s",
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, day, formattedTime);

        // Save the event
        saveEvent(title, description, formattedDateTime);
    }

    /**
     * Saves the event details to the database or memory.
     *
     * @param title       The title of the event.
     * @param description The description of the event.
     * @param startTime   The starting date and time of the event.
     */
    public void saveEvent(String title, String description, String startTime) {
        eventDAO.saveEventToDatabase(title, description, startTime, startTime);
        updateCalendar(); // Refresh UI to highlight event date
    }

    /**
     * Converts 12-hour time format to 24-hour time format.
     */
    private String convertTo24Hour(String time12h) {
        try {
            SimpleDateFormat sdf12 = new SimpleDateFormat("hh:mm a", Locale.US);
            SimpleDateFormat sdf24 = new SimpleDateFormat("HH:mm:ss", Locale.US);
            return sdf24.format(sdf12.parse(time12h));
        } catch (Exception e) {
            return "00:00:00"; // Fallback in case of error
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CalendarPanel::new);
    }
}
