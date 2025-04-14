/*
    *******************************************************************************
    AppFrame Class
    Updated 04/02/2025


    This class creates the main frame for the app and
    sets some basic attributes for the frame.


    Please remember to update the version date if any changes
    are made to this file.
    *******************************************************************************
 */
package com.example.pickitup.ui;

import com.example.pickitup.services.database.DatabaseConnection;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class AppFrame extends JFrame
{
    private JPanel mainPanel = null;
    private final String TITLE = "Pick It Up"; // constant
    public static AIAssistantPanel aiAssistantPanel;


    // Constructor: Creates the objects and sets Look and Feel
    public AppFrame() {
        // fields
        JFrame mainFrame = new JFrame(TITLE);
        mainPanel = new JPanel();

        try {
            // Simple setup without checking for UIScale
            FlatLightLaf.setup();
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
            // Fall back to system look and feel if FlatLaf fails
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // This method sets basic attributes of the main app frame
    public void makeMainAppFrame()
    {
        // Force revalidation and repainting
        revalidate();
        repaint();

        // set some attributes of the frame
        setTitle(TITLE);
        // Save then close app when X is clicked
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                saveApplicationState();
                System.exit(0);
            }
        });
        setExtendedState(JFrame.MAXIMIZED_BOTH); // open in fullscreen
        setLocationRelativeTo(null); // open in the center of the screen

        Image icon = Toolkit.getDefaultToolkit().getImage("coconut.jpg");
        setIconImage(icon);

        // border layout is used for the main panel
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);
    } // end makeMainAppFrame()

    //This method saves the state of the application on exit for reload upon opening
    private void saveApplicationState() {
        try (Connection connection = DatabaseConnection.connect()) {
            //Disable auto commit
            connection.setAutoCommit(false);
            String saveJournalStatement = "REPLACE INTO journals (journal_id, title, selected_note_index) VALUES (?, ?, ?)";
            String saveNoteStatement = "REPLACE INTO notes (notes_id, journal_id, title, content, note_order) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement saveJournal = connection.prepareStatement(saveJournalStatement);
            PreparedStatement saveNote = connection.prepareStatement(saveNoteStatement);

            JTabbedPane journalsPane = JournalsPane.getJournalsPane();
            for (int i = 0; i < JournalsPane.getJournalsPane().getTabCount(); i++) {

                String journalTitle = journalsPane.getTitleAt(i);
                int journalID = JournalsPane.getNotesPanes().get(i).getJournalIDFromNotesPane();
                int selectedNoteIndex = JournalsPane.getNotesPanes().get(i).getSelectedNoteIndex();

                saveJournal.setInt(1, journalID);
                saveJournal.setString(2, journalTitle);
                saveJournal.setInt(3, selectedNoteIndex);
                saveJournal.executeUpdate();

                //For each note in this journal
                for (int j = 0; j < JournalsPane.getNotesPanes().size(); j++) {
                    JTextArea noteTextArea = JournalsPane.getNotesPanes().get(i).getNoteEditors().get(j).getTextArea();
                    String noteContent = noteTextArea.getText();
                    String noteTitle = JournalsPane.getNotesPanes().get(i).getNoteEditors().get(j).getNoteItem().getTitle();
                    int noteID = JournalsPane.getNotesPanes().get(i).getNoteEditors().get(j).getNoteItem().getNoteID();

                    saveNote.setInt(1, noteID);
                    saveNote.setInt(2, journalID);
                    saveNote.setString(3, noteTitle);
                    saveNote.setString(4, noteContent);
                    saveNote.setInt(5, j);
                    saveNote.executeUpdate();
                }
            }
            connection.commit();
        } catch (SQLException error) {
            System.out.println("Error with save before exit: " + error.getMessage());
        }
    }

    //load application from the last save
    public static void loadApplicationState() {
        try (Connection connection = DatabaseConnection.connect()) {
            String loadJournalStatement = "SELECT journal_id, title, selected_note_index FROM journals";
            Statement loadJournal = connection.createStatement();
            ResultSet journalResultSet = loadJournal.executeQuery(loadJournalStatement);

            while (journalResultSet.next()) {
                int journalID = journalResultSet.getInt("journal_id");
                String journalTitle = journalResultSet.getString("title");
                int selectedNoteIndex = journalResultSet.getInt("selected_note_index");

                NotesPane journalPane = new NotesPane(journalID);
                journalPane.deletePageTab();
                JournalsPane.getNotesPanes().add(journalPane);

                String loadNoteStatement = "SELECT notes_id, content, title, note_order FROM notes WHERE journal_id = " + journalID + " ORDER BY note_order";
                Statement loadNote = connection.createStatement();
                ResultSet noteResultSet = loadNote.executeQuery(loadNoteStatement);

                while (noteResultSet.next()) {
                    String noteContent = noteResultSet.getString("content");
                    String noteTitle = noteResultSet.getString("title");
                    NoteEditor newNoteEditor = new NoteEditor(noteTitle, journalID);

                    journalPane.addPageTabForLoad(newNoteEditor, noteContent);
                }

                //Add journal to journal pane
                JournalsPane.getJournalsPane().addTab(journalTitle, journalPane.getTabbedPane());

                //select the selected note before the last exit
                System.out.println(journalPane.getTabCount());
//                journalPane.setSelectedIndex(selectedNoteIndex);
            }
        } catch (SQLException error) {
            System.out.println("Error loading last save: " + error.getMessage());
        }
    }

} // end Frame class
