/*
    Pick It Up
    Byte Cast 2/5/2025
    Base Text Editor Program version 1.0

    This program is a basic text editor to give members of the team a base
    that everyone can work with to implement their portions of the project.

    This code creates a text editor area in a popup window with a title and
    scroll bar.
 */

// main class that contains the main method
public class Main
{
    // fields


    // main method - runs the entire program
    public static void main(String[] args)
    {
        TextEditor textEditor = new TextEditor();    // create a textEditor object
        textEditor.setWindow();                      // calls the method to make the popup window
        textEditor.setVisible(true);                 // means you can actually see the window when it gets made


    }


}