/*
    Pick It Up
    Byte Cast 2/5/2025
    Base Text Editor Program version 1.0

    This program is a basic text editor to give members of the team a base
    that everyone can work with to implement their portions of the project.

    This code creates a text editor area in a popup window with a title and
    scroll bar.

    Please remember to update the version number if any changes
    are made to this file.
 */


package com.example.pickitup;

// imports
import com.example.pickitup.ui.Notepad;

public class Launch
{
    public static void main(String[] args)
    {
        // create the notepad and open the window
        Notepad notepad = new Notepad();
        notepad.newWindow();
        notepad.setVisible(true);

    }

}