/**
 * Manages storing notes
 *
 * @author Justin Nguyen
 * @date 04/23/2025
 */
package com.example.pickitup.services.models;

import com.example.pickitup.services.dao.NotesDAO;

import java.awt.*;

public class Note {
    //Create String variables to contain a note title, the contents of the note, and the id for storing and retrieving
    //A 'long' data type is used here because long can store a bigger number than 'int'.
    //'BigInteger' cannot be used here because in SQLite, a table row can hold up to a 64-bit for IDs
    private int note_id = 0;
    private int journal_id = 0;
    private String title = null;
    private String content = null;
    private String fontType = null;
    private int fontSize = 0;
    private Color textColor = null;
    private Color backgroundColor = null;
    private final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 16); // constant

    //Parameterized constructor
    public Note(String title, String content, int journal_id){
        this.title = title;
        this.content = content;
        this.journal_id = journal_id;
        this.fontType = DEFAULT_FONT.getFontName();
        this.fontSize = DEFAULT_FONT.getSize();
        this.textColor = new Color(0, 0, 0);
        this.backgroundColor = new Color(255, 255, 255);
        NotesDAO.insertNoteAtCreation(this);
    }

    //Parameterized constructor for retrieving note from the database
    public Note(int note_id, int journal_id, String title, String content, String fontType, int fontSize, int textColor, int backgroundColor) {
        this.note_id = note_id;
        this.journal_id = journal_id;
        this.title = title;
        this.content = content;
        this.fontType = fontType;
        this.fontSize = fontSize;
        this.textColor = new Color(textColor, true);
        this.backgroundColor = new Color(backgroundColor, true);
    }

    //Getters
    public int getNoteID() {
        return note_id;
    }
    public int getJournalID() {return journal_id;}
    public String getTitle() {
        return title;
    }
    public String getContent() {
        return content;
    }
    public String getFontType() {
        return fontType;
    }
    public int getFontSize() {
        return fontSize;
    }
    public Color getTextColor() {
        return textColor;
    }
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    //Setters
    public void setTitle(String title) {
        this.title = title;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setNoteID(int note_id) {this.note_id = note_id;}
    public void setFontType(String fontName) {
        this.fontType = fontName;
        NotesDAO.changeFontType(fontName);
    }
    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
        NotesDAO.changeFontSize(fontSize);
    }
    public void setTextColor(Color textColor) {
        this.textColor = textColor;
        NotesDAO.changeTextColor(textColor.getRGB());
    }
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        NotesDAO.changeBackgroundColor(backgroundColor.getRGB());
    }
}
