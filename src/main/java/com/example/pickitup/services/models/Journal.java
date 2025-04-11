/*
 * @author Justin Nguyen
 * @version 1.0
 * @updated 03/24/2025
 * */

package com.example.pickitup.services.models;

import java.util.ArrayList;
import java.util.List;
import com.example.pickitup.services.dao.JournalDAO;
import com.example.pickitup.services.dao.NotesDAO;
import com.example.pickitup.ui.NoteEditor;

public class Journal {
    private int journalID = 0;
    private String title = null;
    List<Note> notes = null;

    //Constructor for journal for an ID and initial title
    public Journal(String title) {
        this.title = title;
        JournalDAO.insertJournal(this);
    }

    //Getters
    public int getJournalID() {
        return this.journalID;
    }
    public String getTitle() {
        return title;
    }
    public List<Note> getNotes() {
        return notes;
    }

//    public void addNote(Note note) {
//        notes.add(note);
//    }

    //Setters
    public void setJournalID(int journalID){
        this.journalID = journalID;
    }

    //Load notes for this journal using NoteDAO
//    public void loadNotes() {
//        this.notes = NotesDAO.getNotesByJournalId(this.journalID);
//    }
}
