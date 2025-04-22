/**
 * Class for managing journals in the database
 *
 * @author Justin Nguyen
 * @date 04/23/2025
 */
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
    private int selectedNoteIndex = 0;
    private int selectedJournalIndex = 0;

    //Constructor for journal for an ID and initial title
    public Journal(String title) {
        this.title = title;
        JournalDAO.insertJournal(this);
    }

    public Journal(int journalID, String title, int selectedNoteIndex) {
        this.journalID = journalID;
        this.title = title;
        this.selectedNoteIndex = selectedNoteIndex;
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
