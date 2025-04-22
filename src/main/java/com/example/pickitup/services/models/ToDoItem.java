/**
 * Represents a single to do item with task details, due date, and status
 *
 * @author Matthew Tomme
 * @date 04/23/2025
 */
package com.example.pickitup.services.models;

import java.util.Date;


public class ToDoItem {
    private String task;
    private Date dueDate;
    private boolean completed;

    /**
     * Constructor for creating a new to-do item
     *
     * @param task The description of the task
     * @param dueDate The due date for the task
     */
    public ToDoItem(String task, Date dueDate) {
        this.task = task;
        this.dueDate = dueDate;
        this.completed = false;
    }

    /**
     * Gets the task description
     *
     * @return The task description
     */
    public String getTask() {
        return task;
    }

    /**
     * Sets the task description
     *
     * @param task The new task description
     */
    public void setTask(String task) {
        this.task = task;
    }

    /**
     * Gets the due date for the task
     *
     * @return The due date
     */
    public Date getDueDate() {
        return dueDate;
    }

    /**
     * Sets the due date for the task
     *
     * @param dueDate The new due date
     */
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Checks if the task is completed
     *
     * @return true if completed, false otherwise
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Sets the completion status of the task
     *
     * @param completed The new completion status
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    /**
     * Toggle the completion status of the task
     */
    public void toggleCompletion() {
        this.completed = !this.completed;
    }

    /**
     * Returns the status of the task as a string
     *
     * @return "Completed" if the task is completed, "Pending" otherwise
     */
    public String getStatusText() {
        return completed ? "Completed" : "Pending";
    }
}