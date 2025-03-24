package com.example.pickitup.services.dao;

import com.example.pickitup.services.models.ToDoItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Data Access Object for managing To-Do items
 * This implementation uses in-memory storage, but could be extended
 * to use database storage in the future
 *
 * @version 1.0
 */
public class ToDoItemDAO {
    private final List<ToDoItem> todoItems;

    /**
     * Constructor initializes the storage for to-do items
     */
    public ToDoItemDAO() {
        todoItems = new ArrayList<>();
    }

    /**
     * Adds a new to-do item to the list
     *
     * @param task The task description
     * @param dueDate The due date for the task
     * @return The created ToDoItem object
     */
    public ToDoItem addItem(String task, Date dueDate) {
        ToDoItem item = new ToDoItem(task, dueDate);
        todoItems.add(item);
        return item;
    }

    /**
     * Gets all to-do items
     *
     * @return List of all to-do items
     */
    public List<ToDoItem> getAllItems() {
        return new ArrayList<>(todoItems);
    }

    /**
     * Gets to-do items with the specified completion status
     *
     * @param completed true to get completed items, false to get pending items
     * @return List of filtered to-do items
     */
    public List<ToDoItem> getItemsByStatus(boolean completed) {
        List<ToDoItem> filteredItems = new ArrayList<>();

        for (ToDoItem item : todoItems) {
            if (item.isCompleted() == completed) {
                filteredItems.add(item);
            }
        }

        return filteredItems;
    }

    /**
     * Gets to-do items due on the specified date
     *
     * @param date The date to filter by
     * @return List of to-do items due on the specified date
     */
    public List<ToDoItem> getItemsByDueDate(Date date) {
        List<ToDoItem> filteredItems = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String targetDate = dateFormat.format(date);

        for (ToDoItem item : todoItems) {
            String itemDate = dateFormat.format(item.getDueDate());
            if (itemDate.equals(targetDate)) {
                filteredItems.add(item);
            }
        }

        return filteredItems;
    }

    /**
     * Updates the completion status of an item
     *
     * @param index The index of the item to update
     * @param completed The new completion status
     * @return true if update was successful, false otherwise
     */
    public boolean updateItemStatus(int index, boolean completed) {
        if (index >= 0 && index < todoItems.size()) {
            todoItems.get(index).setCompleted(completed);
            return true;
        }
        return false;
    }

    /**
     * Removes a to-do item from the list
     *
     * @param index The index of the item to remove
     * @return true if removal was successful, false otherwise
     */
    public boolean removeItem(int index) {
        if (index >= 0 && index < todoItems.size()) {
            todoItems.remove(index);
            return true;
        }
        return false;
    }

    /**
     * Returns the number of to-do items in the list
     *
     * @return The count of to-do items
     */
    public int getItemCount() {
        return todoItems.size();
    }
}