/**
 * Handles timer functionality including notifications
 * This class manages timer state and notifications separately from the UI
 *
 * @author Matthew Tomme
 * @date 04/12/2025
 */
package com.example.pickitup.services.models;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class TimerHandler {

    // Singleton instance
    private static TimerHandler instance;

    // Timer state
    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> timerFuture;
    private int remainingSeconds;
    private boolean isRunning;

    // Listeners for timer events
    private List<TimerListener> listeners;

    /**
     * Interface for timer event listeners
     */
    public interface TimerListener {
        void onTimerTick(int remainingSeconds);
        void onTimerComplete();
    }

    /**
     * Private constructor for singleton pattern
     */
    private TimerHandler() {
        scheduler = Executors.newScheduledThreadPool(1);
        listeners = new ArrayList<>();
        isRunning = false;
    }

    /**
     * Gets the singleton instance
     *
     * @return TimerHandler instance
     */
    public static synchronized TimerHandler getInstance() {
        if (instance == null) {
            instance = new TimerHandler();
        }
        return instance;
    }

    /**
     * Adds a listener for timer events
     *
     * @param listener The listener to add
     */
    public void addListener(TimerListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Removes a listener
     *
     * @param listener The listener to remove
     */
    public void removeListener(TimerListener listener) {
        listeners.remove(listener);
    }

    /**
     * Starts a timer with the specified duration
     *
     * @param seconds Timer duration in seconds
     * @return true if timer started successfully, false otherwise
     */
    public boolean startTimer(int seconds) {
        if (isRunning) {
            return false; // Timer already running
        }

        if (seconds <= 0) {
            return false; // Invalid duration
        }

        remainingSeconds = seconds;
        isRunning = true;

        // Schedule timer task
        timerFuture = scheduler.scheduleAtFixedRate(() -> {
            remainingSeconds--;

            // Notify listeners of tick
            for (TimerListener listener : listeners) {
                listener.onTimerTick(remainingSeconds);
            }

            // Check if timer complete
            if (remainingSeconds <= 0) {
                stopTimer();

                // Notify listeners of completion
                for (TimerListener listener : listeners) {
                    listener.onTimerComplete();
                }
            }
        }, 1, 1, TimeUnit.SECONDS);

        return true;
    }

    /**
     * Stops the current timer
     */
    public void stopTimer() {
        if (timerFuture != null) {
            timerFuture.cancel(false);
        }
        isRunning = false;
    }

    /**
     * Pauses the current timer
     * Not implemented in this version
     */
    public void pauseTimer() {
        // This would be a more advanced feature
        // Could be implemented by saving remaining time and stopping timer
    }

    /**
     * Checks if a timer is currently running
     *
     * @return true if a timer is running, false otherwise
     */
    public boolean isTimerRunning() {
        return isRunning;
    }

    /**
     * Gets the remaining seconds on the current timer
     *
     * @return Remaining seconds, or 0 if no timer is running
     */
    public int getRemainingSeconds() {
        return isRunning ? remainingSeconds : 0;
    }

    /**
     * Formats the remaining time as a string (HH:MM:SS)
     *
     * @return Formatted time string
     */
    public String getFormattedRemainingTime() {
        int hours = remainingSeconds / 3600;
        int minutes = (remainingSeconds % 3600) / 60;
        int seconds = remainingSeconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * Cleans up resources
     * Call this when shutting down the application
     */
    public void shutdown() {
        stopTimer();
        scheduler.shutdown();
    }
}