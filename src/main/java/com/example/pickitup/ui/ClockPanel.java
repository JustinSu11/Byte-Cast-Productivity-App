/*
    *******************************************************************************
    ClockPanel Class
    Last Updated 04/12/2025
    Developers: Matthew Tomme

    This class has time related features such as: Current time display
    * 12/24 hour toggle, countdown timer with notifications,
    * and stopwatch functionality.

    Please remember to update the version date if any changes
    are made to this file.
    *******************************************************************************
 */
package com.example.pickitup.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import com.example.pickitup.services.models.TimerHandler;


public class ClockPanel extends JPanel {

    // UI Components
    private JLabel timeLabel;
    private JButton timerButton;
    private JButton stopwatchButton;
    private JDialog timerDialog;
    private JDialog stopwatchDialog;
    private JTextField hoursField, minutesField, secondsField;
    private JLabel stopwatchLabel;
    private JButton startTimerButton, resetTimerButton;
    private JButton startStopwatchButton, resetStopwatchButton;

    // State variables
    private boolean is24HourFormat = false;
    private Timer clockTimer;
    private long stopwatchStartTime = 0;
    private long stopwatchElapsedTime = 0;
    private boolean stopwatchRunning = false;
    private Timer stopwatchTimer;
    private TimerHandler timerHandler;
    private ThemeManager themeManager;

    /**
     * Constructor initializes the Clock Panel
     */
    public ClockPanel() {
        // Get theme manager instance
        themeManager = ThemeManager.getInstance();

        // Get timer handler instance
        timerHandler = TimerHandler.getInstance();

        // Set up panel
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(5, 10, 5, 10));

        // Create the top bar with time and buttons
        createTopBar();

        // Create dialog boxes for timer and stopwatch
        createTimerDialog();
        createStopwatchDialog();

        // Start the clock
        startClock();

        // Register with theme manager
        registerWithThemeManager();
    }

    /**
     * Creates the top bar with time display and buttons
     */
    private void createTopBar() {
        JPanel topBar = new JPanel();
        topBar.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        // Create timer button (left of time)
        timerButton = new JButton("\u23F2"); // Unicode for timer symbol
        timerButton.setFont(new Font("Dialog", Font.PLAIN, 18));
        timerButton.setToolTipText("Timer");
        timerButton.setFocusPainted(false);
        timerButton.setBorderPainted(true);
        timerButton.setContentAreaFilled(true);
        timerButton.setPreferredSize(new Dimension(40, 30));
        timerButton.setMargin(new Insets(2, 2, 2, 2));

        // Create time label (center)
        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Monospaced", Font.BOLD, 22));
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timeLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        timeLabel.setToolTipText("Click to toggle 12/24 hour format");
        timeLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        // Create stopwatch button (right of time)
        stopwatchButton = new JButton("\u23F1"); // Unicode for stopwatch symbol
        stopwatchButton.setFont(new Font("Dialog", Font.PLAIN, 18));
        stopwatchButton.setToolTipText("Stopwatch");
        stopwatchButton.setFocusPainted(false);
        stopwatchButton.setBorderPainted(true);
        stopwatchButton.setContentAreaFilled(true);
        stopwatchButton.setPreferredSize(new Dimension(40, 30));
        stopwatchButton.setMargin(new Insets(2, 2, 2, 2));

        // Add components directly next to each other in the center
        topBar.add(timerButton);
        topBar.add(timeLabel);
        topBar.add(stopwatchButton);

        // Add event listeners
        timerButton.addActionListener(e -> {
            // Visual press effect handled by Swing's ButtonUI
            timerDialog.setVisible(true);
        });

        stopwatchButton.addActionListener(e -> {
            // Visual press effect handled by Swing's ButtonUI
            stopwatchDialog.setVisible(true);
        });

        timeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                is24HourFormat = !is24HourFormat;
                updateTimeDisplay();
            }
        });

        // Add to main panel
        add(topBar, BorderLayout.CENTER);
    }

    /**
     * Creates the timer dialog
     */
    private void createTimerDialog() {
        // Create dialog
        timerDialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Timer", false);
        timerDialog.setSize(350, 250);
        timerDialog.setLocationRelativeTo(null);
        timerDialog.setResizable(false);

        // Create panel for timer content
        JPanel timerPanel = new JPanel();
        timerPanel.setLayout(new BorderLayout(0, 10));
        timerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Create input panel for hours, minutes, seconds
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        // Hours input
        JPanel hoursPanel = new JPanel(new BorderLayout());
        JLabel hoursLabel = new JLabel("Hours");
        hoursField = new JTextField("0", 3);
        hoursField.setHorizontalAlignment(JTextField.CENTER);
        hoursField.setFont(new Font("Arial", Font.PLAIN, 16));
        hoursPanel.add(hoursLabel, BorderLayout.NORTH);
        hoursPanel.add(hoursField, BorderLayout.CENTER);

        // Minutes input
        JPanel minutesPanel = new JPanel(new BorderLayout());
        JLabel minutesLabel = new JLabel("Minutes");
        minutesField = new JTextField("0", 3);
        minutesField.setHorizontalAlignment(JTextField.CENTER);
        minutesField.setFont(new Font("Arial", Font.PLAIN, 16));
        minutesPanel.add(minutesLabel, BorderLayout.NORTH);
        minutesPanel.add(minutesField, BorderLayout.CENTER);

        // Seconds input
        JPanel secondsPanel = new JPanel(new BorderLayout());
        JLabel secondsLabel = new JLabel("Seconds");
        secondsField = new JTextField("0", 3);
        secondsField.setHorizontalAlignment(JTextField.CENTER);
        secondsField.setFont(new Font("Arial", Font.PLAIN, 16));
        secondsPanel.add(secondsLabel, BorderLayout.NORTH);
        secondsPanel.add(secondsField, BorderLayout.CENTER);

        // Add input fields to panel
        inputPanel.add(hoursPanel);
        inputPanel.add(minutesPanel);
        inputPanel.add(secondsPanel);

        // Create countdown display
        JLabel countdownLabel = new JLabel("00:00:00", SwingConstants.CENTER);
        countdownLabel.setFont(new Font("Monospaced", Font.BOLD, 36));
        countdownLabel.setBorder(new EmptyBorder(20, 0, 20, 0));

        // Create control buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        startTimerButton = new JButton("Start Timer");
        resetTimerButton = new JButton("Reset");

        // Style the buttons
        startTimerButton.setBackground(new Color(46, 125, 50));
        startTimerButton.setForeground(Color.WHITE);
        startTimerButton.setFont(new Font("Arial", Font.BOLD, 14));
        startTimerButton.setFocusPainted(false);

        resetTimerButton.setBackground(new Color(211, 47, 47));
        resetTimerButton.setForeground(Color.WHITE);
        resetTimerButton.setFont(new Font("Arial", Font.BOLD, 14));
        resetTimerButton.setFocusPainted(false);

        buttonPanel.add(startTimerButton);
        buttonPanel.add(resetTimerButton);

        // Add components to timer panel
        timerPanel.add(inputPanel, BorderLayout.NORTH);
        timerPanel.add(countdownLabel, BorderLayout.CENTER);
        timerPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Register with TimerHandler
        timerHandler.addListener(new TimerHandler.TimerListener() {
            @Override
            public void onTimerTick(int remainingSeconds) {
                // Update the countdown display
                int hours = remainingSeconds / 3600;
                int minutes = (remainingSeconds % 3600) / 60;
                int seconds = remainingSeconds % 60;

                countdownLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
            }

            @Override
            public void onTimerComplete() {
                // Enable inputs
                enableTimerInputs(true);
                startTimerButton.setText("Start Timer");

                // Show notification
                showTimerNotification();
            }
        });

        // Set up event listeners
        startTimerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timerHandler.isTimerRunning()) {
                    // Stop the timer
                    timerHandler.stopTimer();
                    startTimerButton.setText("Start Timer");
                    enableTimerInputs(true);
                } else {
                    // Start the timer
                    try {
                        int hours = Integer.parseInt(hoursField.getText().trim());
                        int minutes = Integer.parseInt(minutesField.getText().trim());
                        int seconds = Integer.parseInt(secondsField.getText().trim());

                        // Validate inputs
                        if (hours < 0 || minutes < 0 || seconds < 0 || (hours == 0 && minutes == 0 && seconds == 0)) {
                            JOptionPane.showMessageDialog(timerDialog,
                                    "Please enter valid values for the timer.",
                                    "Invalid Input",
                                    JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        // Calculate total seconds
                        int totalSeconds = hours * 3600 + minutes * 60 + seconds;

                        // Disable inputs
                        enableTimerInputs(false);

                        // Start timer
                        if (timerHandler.startTimer(totalSeconds)) {
                            startTimerButton.setText("Stop Timer");
                        } else {
                            enableTimerInputs(true);
                            JOptionPane.showMessageDialog(timerDialog,
                                    "Failed to start timer. Please try again.",
                                    "Timer Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(timerDialog,
                                "Please enter valid numbers for hours, minutes, and seconds.",
                                "Invalid Input",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });

        resetTimerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timerHandler.isTimerRunning()) {
                    timerHandler.stopTimer();
                }

                // Reset inputs and display
                hoursField.setText("0");
                minutesField.setText("0");
                secondsField.setText("0");
                countdownLabel.setText("00:00:00");

                startTimerButton.setText("Start Timer");
                enableTimerInputs(true);
            }
        });

        // Add panel to dialog
        timerDialog.add(timerPanel);

        // Register with theme manager
        themeManager.registerComponent(timerPanel);
    }

    /**
     * Creates the stopwatch dialog
     */
    private void createStopwatchDialog() {
        // Create dialog
        stopwatchDialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Stopwatch", false);
        stopwatchDialog.setSize(350, 200);
        stopwatchDialog.setLocationRelativeTo(null);
        stopwatchDialog.setResizable(false);

        // Create panel for stopwatch content
        JPanel stopwatchPanel = new JPanel(new BorderLayout(0, 10));
        stopwatchPanel.setBorder(new EmptyBorder(20, 10, 20, 10));

        // Create stopwatch display
        stopwatchLabel = new JLabel("00:00:00.0", SwingConstants.CENTER);
        stopwatchLabel.setFont(new Font("Monospaced", Font.BOLD, 36));
        stopwatchLabel.setBorder(new EmptyBorder(30, 0, 30, 0));

        // Create control buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        startStopwatchButton = new JButton("Start");
        resetStopwatchButton = new JButton("Reset");

        // Style the buttons
        startStopwatchButton.setBackground(new Color(46, 125, 50));
        startStopwatchButton.setForeground(Color.WHITE);
        startStopwatchButton.setFont(new Font("Arial", Font.BOLD, 14));
        startStopwatchButton.setFocusPainted(false);

        resetStopwatchButton.setBackground(new Color(211, 47, 47));
        resetStopwatchButton.setForeground(Color.WHITE);
        resetStopwatchButton.setFont(new Font("Arial", Font.BOLD, 14));
        resetStopwatchButton.setFocusPainted(false);

        buttonPanel.add(startStopwatchButton);
        buttonPanel.add(resetStopwatchButton);

        // Add components to panel
        stopwatchPanel.add(stopwatchLabel, BorderLayout.CENTER);
        stopwatchPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Set up event listeners
        startStopwatchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (stopwatchRunning) {
                    // Stop the stopwatch
                    stopwatchRunning = false;
                    stopwatchTimer.stop();
                    stopwatchElapsedTime += System.currentTimeMillis() - stopwatchStartTime;
                    startStopwatchButton.setText("Start");
                } else {
                    // Start the stopwatch
                    stopwatchRunning = true;
                    stopwatchStartTime = System.currentTimeMillis();

                    if (stopwatchTimer == null) {
                        stopwatchTimer = new Timer(100, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                long elapsedMillis = stopwatchElapsedTime;
                                if (stopwatchRunning) {
                                    elapsedMillis += System.currentTimeMillis() - stopwatchStartTime;
                                }
                                updateStopwatchDisplay(elapsedMillis);
                            }
                        });
                    }

                    stopwatchTimer.start();
                    startStopwatchButton.setText("Stop");
                }
            }
        });

        resetStopwatchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopwatchRunning = false;
                if (stopwatchTimer != null) {
                    stopwatchTimer.stop();
                }
                stopwatchElapsedTime = 0;
                updateStopwatchDisplay(0);
                startStopwatchButton.setText("Start");
            }
        });

        // Add panel to dialog
        stopwatchDialog.add(stopwatchPanel);

        // Register with theme manager
        themeManager.registerComponent(stopwatchPanel);
    }

    /**
     * Enables or disables timer input fields
     *
     * @param enable True to enable inputs, false to disable
     */
    private void enableTimerInputs(boolean enable) {
        hoursField.setEnabled(enable);
        minutesField.setEnabled(enable);
        secondsField.setEnabled(enable);
    }

    /**
     * Updates the stopwatch display
     *
     * @param elapsedMillis Elapsed milliseconds
     */
    private void updateStopwatchDisplay(long elapsedMillis) {
        long hours = TimeUnit.MILLISECONDS.toHours(elapsedMillis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedMillis) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedMillis) % 60;
        long deciseconds = (elapsedMillis / 100) % 10;

        stopwatchLabel.setText(String.format("%02d:%02d:%02d.%01d", hours, minutes, seconds, deciseconds));
    }

    /**
     * Shows a notification when the timer completes
     */
    private void showTimerNotification() {
        // Play sound
        Toolkit.getDefaultToolkit().beep();

        // Show dialog
        JOptionPane.showMessageDialog(
                this,
                "Timer Complete!",
                "Time's Up",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Starts the clock timer
     */
    private void startClock() {
        clockTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTimeDisplay();
            }
        });
        clockTimer.start();
        updateTimeDisplay(); // Update immediately
    }

    /**
     * Updates the time display
     */
    private void updateTimeDisplay() {
        SimpleDateFormat sdf = new SimpleDateFormat(is24HourFormat ? "HH:mm:ss" : "hh:mm:ss a");
        timeLabel.setText(sdf.format(new Date()));
    }

    /**
     * Registers components with the theme manager
     */
    private void registerWithThemeManager() {
        themeManager.registerComponent(this);
        themeManager.registerComponent(timeLabel);
        themeManager.registerComponent(timerButton);
        themeManager.registerComponent(stopwatchButton);

        // Dialogs and their components will be registered separately
    }

    /**
     * Clean up resources when panel is removed
     */
    public void stopTimers() {
        if (clockTimer != null) {
            clockTimer.stop();
        }
        if (stopwatchTimer != null) {
            stopwatchTimer.stop();
        }
        if (timerHandler != null) {
            timerHandler.stopTimer();
        }
    }

    /**
     * Returns the time label for positioning
     *
     * @return The time label
     */
    public JLabel getTimeLabel() {
        return timeLabel;
    }
}