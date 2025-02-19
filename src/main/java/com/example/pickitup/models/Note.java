package com.example.pickitup.models;

import com.google.gson.Gson;
import java.io.*;
import javax.swing.*;

public class Note {
    private String title = "";
    private String content = "";

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Note(){

    }

    public Note(String title, String content) {
        setTitle(title);
        setContent(content);
    }

    // Save notes to a file
    public static void saveNote(File file, Note note){
        try (FileWriter writer = new FileWriter(file)) {
            Gson gson = new Gson();
            gson.toJson(note, writer);
            JOptionPane.showMessageDialog(null, "Note saved successfully");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error while saving note" + e.getMessage());
            e.printStackTrace();
        }
    }

    // Save as (let user choose folder and name)
    public void saveNoteWithFileChooser(){
        // Open the file chooser dialog
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save As");
        fileChooser.setSelectedFile(new File(getTitle() + ".json")); // Default file name

        int userSelection = fileChooser.showSaveDialog(null);

        //If the user selects a file and clicks "save"
        if(userSelection == JFileChooser.APPROVE_OPTION){
            File fileToSave = fileChooser.getSelectedFile();

            // Ensure file has a .json extension
            if(!fileToSave.getName().toLowerCase().endsWith(".json")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".json");
            }
            saveNote(fileToSave, this);
        }
    }

    // Load notes from a file
    public static Note loadNoteFromFile(File file){
        if(!file.exists()){
            JOptionPane.showMessageDialog(null, "File not found");
            return null;
        }

        try(Reader reader = new FileReader(file)){
            Gson gson = new Gson();
            return gson.fromJson(reader, Note.class);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error while loading note: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Delete notes
    public static void deleteNote(File file) {
        if (file.exists()) {
            if (file.delete()) {
                JOptionPane.showMessageDialog(null, "🗑️ Note deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "❌ Error deleting note.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "⚠️ File does not exist.");
        }
    }
}
