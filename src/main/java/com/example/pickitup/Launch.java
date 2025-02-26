/*
    *******************************************************************************
    Pick It Up
    Developed by Byte Cast

    Launch Class
    Last Updated 02/19/2025

    This class contains the main method
    Running this code creates a window with a text area

    Please remember to update the version date if any changes
    are made to this file.
    *******************************************************************************
 */

package com.example.pickitup;

// imports
import com.example.pickitup.ui.Notepad;


public class Launch
{
    public static void main(String[] args)
    {
        Notepad notepad = new Notepad();
        notepad.newNotepadWindow();

    } // end main

} // end Launch class