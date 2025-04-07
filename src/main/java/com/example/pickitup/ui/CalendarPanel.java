package com.example.pickitup.ui;

import com.example.pickitup.services.dao.CalendarEventDAO;
import com.example.pickitup.services.database.DatabaseSetup;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 *  Author: Anney & Aron
 *  Date: 3/21/2025
 *  Version: 1.0
 *  Purpose: CalendarApp is a simple Swing-based calendar application.
 *  It allows users to navigate through months, view existing events, and add new events.
 * The current day is highlighted for better visibility.
 */
public class CalendarPanel extends Component {
    private final JFrame frame;            // Main application window
    private final JPanel calendarPanel;    // Panel to display the calendar
    private final JLabel monthLabel;       // Label to display current month and year
    private final Calendar calendar;       // Calendar instance to manage date operations
    private final Calendar today;          // Tracks the current date

    /**
     * Constructor initializes the calendar UI components and sets up the frame.
     */
    public CalendarPanel() {

        frame = new JFrame("Calendar");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
        revalidate();
        repaint();
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
        calendarPanel.removeAll();
        monthLabel.setText(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) +
                " " + calendar.get(Calendar.YEAR));

        // Add day labels
        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : days) {
            JLabel dayLabel = new JLabel(day, SwingConstants.CENTER);
            dayLabel.setFont(new Font("Arial", Font.BOLD, 12));
            dayLabel.setPreferredSize(new Dimension(30, 30)); // Consistent size
            calendarPanel.add(dayLabel);
        }

        // Get first day and max days
        Calendar temp = (Calendar) calendar.clone();
        temp.set(Calendar.DAY_OF_MONTH, 1);
        int firstDay = temp.get(Calendar.DAY_OF_WEEK) - 1;
        int maxDays = temp.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Add padding before first day
        for (int i = 0; i < firstDay; i++) {
            JLabel emptyLabel = new JLabel("");
            emptyLabel.setPreferredSize(new Dimension(30, 30)); // Consistent size
            calendarPanel.add(emptyLabel);
        }

        // Add day buttons
        for (int day = 1; day <= maxDays; day++) {
            JButton dayButton = new JButton(String.valueOf(day));
            dayButton.setFont(new Font("Arial", Font.PLAIN, 10));
            dayButton.setPreferredSize(new Dimension(30, 30));

            String formattedDate = String.format("%d-%02d-%02d 00:00:00",
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, day);
            List<String> events = CalendarEventDAO.getEvents(formattedDate);

            if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                    calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                    day == today.get(Calendar.DAY_OF_MONTH)) {
                dayButton.setBackground(Color.CYAN);
                dayButton.setForeground(Color.BLACK);
            }
            if (!events.isEmpty()) {
                dayButton.setBackground(Color.PINK);
                dayButton.setToolTipText("<html>" + String.join("<br>", events) + "</html>");
            }

            int selectedDay = day;
            dayButton.addActionListener(e -> {
                if (!events.isEmpty()) {
                    String eventDetails = String.join("\n", events);
                    int choice = JOptionPane.showOptionDialog(frame,
                            "Events for " + formattedDate.split(" ")[0] + ":\n" + eventDetails,
                            "Event Details",
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            null,
                            new String[]{"Add New Event", "Delete", "Close"},
                            "Close");
                    if (choice == 0) {
                        openEventDialog(selectedDay);
                    } else if (choice == 1) {
                        deleteEvent(events);
                    }
                } else {
                    openEventDialog(selectedDay);
                }
            });
            calendarPanel.add(dayButton);
        }

        // Fill remaining slots
        int totalComponents = 7 + firstDay + maxDays;
        int remaining = 49 - totalComponents;
        for (int i = 0; i < remaining; i++) {
            JLabel emptyLabel = new JLabel("");
            emptyLabel.setPreferredSize(new Dimension(30, 30));
            calendarPanel.add(emptyLabel);
        }

        calendarPanel.revalidate();
        calendarPanel.repaint();
    }

    /**
     * Opens a dialog to enter an event for the selected date.
     *
     * @param day The selected day of the month.
     */
    private void openEventDialog(int day) {
        Calendar selectedDate = (Calendar) calendar.clone();
        selectedDate.set(Calendar.DAY_OF_MONTH, day);
        selectedDate.set(Calendar.HOUR_OF_DAY, 0);
        selectedDate.set(Calendar.MINUTE, 0);
        selectedDate.set(Calendar.SECOND, 0);
        selectedDate.set(Calendar.MILLISECOND, 0);

        Calendar todayDate = (Calendar) today.clone();
        todayDate.set(Calendar.HOUR_OF_DAY, 0);
        todayDate.set(Calendar.MINUTE, 0);
        todayDate.set(Calendar.SECOND, 0);
        todayDate.set(Calendar.MILLISECOND, 0);

        if (selectedDate.before(todayDate))
        {
            JOptionPane.showMessageDialog(frame,
                    "You can't add events to past dates.",
                    "Invalid Date",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        String title = JOptionPane.showInputDialog(frame, "Enter event title:", "New Event", JOptionPane.PLAIN_MESSAGE);
        if (title == null || title.trim().isEmpty())
        {
            JOptionPane.showMessageDialog(frame, "Description cannot be empty.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String description = JOptionPane.showInputDialog(frame, "Enter event description:", "Event Details", JOptionPane.PLAIN_MESSAGE);
        if (description == null || description.trim().isEmpty()) return;

        // Time selection using dropdown
        String[] times = {"08:00 AM", "09:00 AM", "10:00 AM", "11:00 AM", "12:00 PM",
                "01:00 PM", "02:00 PM", "03:00 PM", "04:00 PM", "05:00 PM",
                "06:00 PM", "07:00 PM", "08:00 PM", "09:00 PM","10:00 PM", "11:00 PM", "12:00 AM",
                "01:00 AM", "02:00 AM", "03:00 AM", "04:00 AM", "05:00 AM",
                "06:00 AM", "07:00 AM"};
        String selectedTime = (String) JOptionPane.showInputDialog(frame, "Select time:", "Event Time", JOptionPane.QUESTION_MESSAGE, null, times, times[0]);

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
     * Deletes an event based on user selection.
     *
     * @param events List of event strings including their event_ids.
     */
    private void deleteEvent (List<String> events)
    {
        if (events == null || events.isEmpty()) return;

        String[] eventOptions = new String[events.size()];
        for (int i=0; i<events.size(); i++)
        {
            eventOptions[i] = events.get(i);
        }
        String selectedEvent = (String) JOptionPane.showInputDialog(
                frame,
                "Select event to delete:",
                "Delete event",
                JOptionPane.QUESTION_MESSAGE,
                null,
                eventOptions,
                eventOptions[0]);
                if (selectedEvent != null)
                {
                    try
                    {
                        // Extract the event_id from the selected event string (e.g., "1: Meeting: Team sync at 14:00:00")
                        int eventId = Integer.parseInt(selectedEvent.split(":")[0].trim());
                        boolean success = CalendarEventDAO.deleteEvent(eventId);
                        if (success)
                        {
                            JOptionPane.showMessageDialog(frame, "Event deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                            updateCalendar(); // Refresh the calendar to reflect the deletion
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(frame, "Failed to delete event.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    catch (NumberFormatException | ArrayIndexOutOfBoundsException e)
                    {
                        JOptionPane.showMessageDialog(frame, "Invalid event selection.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
    }

    /**
     * Saves the event details to the database.
     *
     * @param title       The title of the event.
     * @param description The description of the event.
     * @param startTime   The starting date and time of the event (used for both start and end for simplicity).
     */
    public void saveEvent(String title, String description, String startTime) {
        CalendarEventDAO.saveEventToDatabase(title, description, startTime, startTime); // Use existing eventDAO instance
        updateCalendar(); // Refresh UI to highlight event date
        System.out.println("Event saved: " + title + ", " + description + ", " + startTime);
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
}