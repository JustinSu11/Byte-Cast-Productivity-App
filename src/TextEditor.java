// ************************************************************
// TextEditor Class version 1.0:
// This class is for making the popup window.
//
// Please remember to update the version number if any changes
// are made to this file.
// ************************************************************

// imports
import javax.swing.*;   // to make the text area and GUI


// TextEditor inherits methods and fields from JFrame
// which is a part of Swing
// to make the popup window
public class TextEditor extends JFrame
{
    // fields
    private JTextArea textArea = null;                      // set to null for good practice

    // *****************************************************************
    // this method creates the window popup with a title, window size
    // and scroll feature
    // *****************************************************************
    public void setWindow()
    {
        textArea = new JTextArea();                         // make a text area object
        setTitle("Basic Text Editor");                      // title of the window popup in upper left corner
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     // closes program when exited
        setSize(800,600);                      // size of the window popup

        // centers the popup window when opened instead of
        // opening in the upper left corner of screen
        setLocationRelativeTo(null);

        // the "frame" is the window popup
        // this adds what is typed
        // from the keyboard into the frame
        add(textArea);

        JScrollPane scrollPane = new JScrollPane(textArea); // creates a scroll bar object
        add(scrollPane);                                    // adds the scroll bar to the window popup on the right
    }
}
