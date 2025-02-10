/*
    *******************************************************************************
    Pick It Up
    Byte Cast
    Launch Version 2.0 updated 02/10/25

    This program is a basic text editor to give members of the team a base
    that everyone can work with to implement their portions of the project.

    This code creates a basic text editor frame with a few simple features

    Please remember to update the version number if any changes
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
        // create the notepad and open the window
        Notepad notepad = new Notepad();
        notepad.newWindow();
    }

}