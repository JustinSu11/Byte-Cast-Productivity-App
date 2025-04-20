/*
 * @author Justin Nguyen
 * @version 1.0
 * @updated 03/24/2025
 * */

package com.example.pickitup.services.models;

import java.util.ArrayList;
import java.util.List;
import com.example.pickitup.services.dao.NotesDAO;

public class Journal {
    private int id;
    private String title;
    private List<Note> notes;

    //Constructor for journal for an ID and initial title
    public Journal(int id, String title) {
        this.id = id;
        this.title = title;
        this.notes = new ArrayList<>();
    }

    //Getters
    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public List<Note> getNotes() {
        return notes;
    }

    //Add a note to journal
    public void addNote(Note note) {
        notes.add(note);
    }

    //Remove a note from journal
    public void removeNote(Note note) {
        notes.remove(note);
    }

    //Load notes for this journal using NoteDAO
    public void loadNotes() {
        this.notes = NotesDAO.getNotesByJournalId(this.id);
    }
}
