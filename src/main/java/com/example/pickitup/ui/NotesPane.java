/*
    *******************************************************************************
    TabbedPane Class
    Last Updated 04/02/2025
    Developer CJ Quintero

    This class makes the tabbed pane and has methods to add or delete tabs.

    Please remember to update the version date if any changes
    are made to this file.
    *******************************************************************************
*/
package com.example.pickitup.ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import com.example.pickitup.services.dao.NotesDAO;
import com.example.pickitup.services.models.Note;

public class NotesPane extends JournalsPane
{
    // fields
    private JTabbedPane tabbedPane = null;
    private String title = null;
    private NoteEditor noteEditor = null;
    private int selectedIndex = 0;
    private final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 16); // constant
    private List<NoteEditor> noteEditors = new ArrayList<>();


    // constructor
    public NotesPane()
    {
        // initialize the variables
        tabbedPane = new JTabbedPane();
        noteEditor = new NoteEditor();

        // set the default font
        tabbedPane.setFont(DEFAULT_FONT);
        
//        // Add change listener to update AI Assistant when tab changes
//        tabbedPane.addChangeListener(e -> {
//            if (AppFrame.aiAssistantPanel != null) {
//                NoteEditor currentNoteEditor = getCurrentNoteEditor();
//                if (currentNoteEditor != null) {
//                    AppFrame.aiAssistantPanel.setNoteEditor(currentNoteEditor);
//                }
//            }
//        });

        title = "Page " + (tabbedPane.getTabCount() + 1);
        noteEditor.makeScrollPane();
        // adds the scroll pane to the new tab
        tabbedPane.addTab(title, noteEditor.getScrollPane());
    }

    // method to add a new tab to the tabbed pane
    public void addPageTab()
    {
        //New name setter for tabs
        String title = JOptionPane.showInputDialog("Enter title for new note");
        if (title == null || title.isEmpty() || title.equals("")){
            title = "Page " + (tabbedPane.getTabCount() + 1);
            JOptionPane.showMessageDialog(
                    null,
                    "No name was provided!\nDefault title is: " + title,
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        NoteEditor newNoteEditor = new NoteEditor();
        newNoteEditor.makeScrollPane();
        //inserts blank note into database (commented out due to missing journal tabs)
//        Note currentNote = new Note(title, newNoteEditor.getTextInTextEditor());
//        NotesDAO.insertNote(currentNote);
        // adds the scroll pane to the new tab
        tabbedPane.addTab(title, newNoteEditor.getScrollPane());
        
        // Store reference to the NoteEditor
        noteEditors.add(newNoteEditor);
    }


    // method to remove tabs
    public void deletePageTab()
    {
        // get the index of the selected tab
        selectedIndex = tabbedPane.getSelectedIndex();

        // delete the selected tab
        if(tabbedPane.getTabCount() > 0)
        {
            tabbedPane.removeTabAt(tabbedPane.getSelectedIndex());
            
            // Remove the NoteEditor reference
            if (selectedIndex >= 0 && selectedIndex < noteEditors.size()) {
                noteEditors.remove(selectedIndex);
            }
        }
    }

    // method to change the font size in the tabs
    public void setFontSize(int fontSize){
        /*
        Font currentFont = tabbedPane.getFont();
        Font newFont = new Font(currentFont.getName(), currentFont.getStyle(), fontSize);
        tabbedPane.setFont(newFont);
         */
        // Update the font of the NoteEditor's textArea
        int selectedTabIndex = tabbedPane.getSelectedIndex();
        if (selectedTabIndex != -1) {
            Component selectedComponent = tabbedPane.getComponentAt(selectedTabIndex);
            if (selectedComponent instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) selectedComponent;
                JViewport viewport = scrollPane.getViewport();
                Component view = viewport.getView();
                if (view instanceof JTextArea) {
                    JTextArea textArea = (JTextArea) view;
                    Font textAreaFont = textArea.getFont();
                    Font newTextAreaFont = new Font(textAreaFont.getName(), textAreaFont.getStyle(), fontSize);
                    textArea.setFont(newTextAreaFont);
                }
            }
        }
        //for testing out the code
        System.out.println("Font size changed to " + fontSize);
    }

    // method to change the font in the tabs
    public void setFontType(String fontStyle){
        /*
        Font currentFont = tabbedPane.getFont();
        Font newFont = new Font(fontStyle, currentFont.getStyle(), currentFont.getSize());
        tabbedPane.setFont(newFont);
         */
        // Update the font of the NoteEditor's textArea
        int selectedTabIndex = tabbedPane.getSelectedIndex();
        if (selectedTabIndex != -1) {
            Component selectedComponent = tabbedPane.getComponentAt(selectedTabIndex);
            if (selectedComponent instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) selectedComponent;
                JViewport viewport = scrollPane.getViewport();
                Component view = viewport.getView();
                if (view instanceof JTextArea) {
                    JTextArea textArea = (JTextArea) view;
                    Font textAreaFont = textArea.getFont();
                    Font newTextAreaFont = new Font(fontStyle, textAreaFont.getStyle(), textAreaFont.getSize());
                    textArea.setFont(newTextAreaFont);
                }
            }
        }
        System.out.println("Font style changed to " + fontStyle);
    }

    public void setNewPageName(String newName){
        tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(), newName);
    }

    public void setFontColor(Color color){
        int selectedTabIndex = tabbedPane.getSelectedIndex();
        if (selectedTabIndex != -1) {
            Component selectedComponent = tabbedPane.getComponentAt(selectedTabIndex);
            if (selectedComponent instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) selectedComponent;
                JViewport viewport = scrollPane.getViewport();
                Component view = viewport.getView();
                if (view instanceof JTextArea) {
                    JTextArea textArea = (JTextArea) view;
                    textArea.setForeground(color);
                }
            }
        }
    }

    public void setBackgroundColor(Color color){
        int selectedTabIndex = tabbedPane.getSelectedIndex();
        if (selectedTabIndex != -1) {
            Component selectedComponent = tabbedPane.getComponentAt(selectedTabIndex);
            if (selectedComponent instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) selectedComponent;
                JViewport viewport = scrollPane.getViewport();
                Component view = viewport.getView();
                if (view instanceof JTextArea) {
                    JTextArea textArea = (JTextArea) view;
                    textArea.setBackground(color);
                }
            }
        }
    }

    // returns the notes pane to the JournalsPane class
    public JTabbedPane getTabbedPane()
    {
        return tabbedPane;
    } // end getTabbedPane
    
    /**
     * Gets the current NoteEditor based on the selected tab
     * 
     * @return The current NoteEditor or null if no tab is selected
     */
    public NoteEditor getCurrentNoteEditor() {
        int selectedIndex = tabbedPane.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < noteEditors.size()) {
            return noteEditors.get(selectedIndex);
        }
        return null;
    }

} // end class
