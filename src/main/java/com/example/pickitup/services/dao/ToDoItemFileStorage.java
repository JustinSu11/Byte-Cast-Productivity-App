package com.example.pickitup.services.dao;

import com.example.pickitup.services.models.ToDoItem;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * File-based storage implementation for ToDoItems
 * Provides methods to save and load to-do items from a file
 *
 * @version 1.0
 */
public class ToDoItemFileStorage {
    // The file where to-do items will be stored
    private static final String STORAGE_FILE = "todoitems.dat";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Saves a list of ToDoItems to a file
     *
     * @param items The list of ToDoItems to save
     * @return true if the operation was successful, false otherwise
     */
    public boolean saveToFile(List<ToDoItem> items) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STORAGE_FILE))) {
            // Write each to-do item on a separate line
            for (ToDoItem item : items) {
                String line = String.format("%s,%s,%b",
                        item.getTask().replace(",", "\\,"), // Escape commas in task description
                        DATE_FORMAT.format(item.getDueDate()),
                        item.isCompleted());
                writer.write(line);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error saving to-do items: " + e.getMessage());
            return false;
        }
    }

    /**
     * Loads ToDoItems from a file
     *
     * @return A list of ToDoItems loaded from the file, or an empty list if the file doesn't exist
     */
    public List<ToDoItem> loadFromFile() {
        List<ToDoItem> items = new ArrayList<>();
        File file = new File(STORAGE_FILE);

        // If the file doesn't exist yet, return an empty list
        if (!file.exists()) {
            return items;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line into its components
                // We need to handle potential escaped commas in the task description
                List<String> parts = splitCSVLine(line);
                if (parts.size() >= 3) {
                    String task = parts.get(0).replace("\\,", ","); // Unescape commas
                    Date dueDate = DATE_FORMAT.parse(parts.get(1));
                    boolean completed = Boolean.parseBoolean(parts.get(2));

                    // Create and add the to-do item
                    ToDoItem item = new ToDoItem(task, dueDate);
                    item.setCompleted(completed);
                    items.add(item);
                }
            }
        } catch (IOException | ParseException e) {
            System.err.println("Error loading to-do items: " + e.getMessage());
        }

        return items;
    }

    /**
     * Helper method to split a CSV line properly handling escaped commas
     *
     * @param line The CSV line to split
     * @return A list of the separated values
     */
    private List<String> splitCSVLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder currentValue = new StringBuilder();
        boolean escapedComma = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '\\' && i + 1 < line.length() && line.charAt(i + 1) == ',') {
                // Found an escaped comma, keep it as is
                currentValue.append("\\,");
                i++; // Skip the next character (the comma)
                escapedComma = true;
            } else if (c == ',' && !escapedComma) {
                // Found a regular comma, add current value to result and reset
                result.add(currentValue.toString());
                currentValue = new StringBuilder();
            } else {
                // Regular character, add to current value
                currentValue.append(c);
                escapedComma = false;
            }
        }

        // Add the last value
        result.add(currentValue.toString());

        return result;
    }
}