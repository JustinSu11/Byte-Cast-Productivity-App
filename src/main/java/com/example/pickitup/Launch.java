/*
    *******************************************************************************
    Pick It Up
    Developed by Byte Cast

    Launch Class
    Last Updated 03/31/2025

    This class contains the main method
    Running this code creates the app

    Please remember to update the version date if any changes
    are made to this file.
    *******************************************************************************
 */
package com.example.pickitup;


import com.example.pickitup.ui.App;
import com.example.pickitup.services.database.DatabaseSetup;

public class Launch
{
    public static void main(String[] args)
    {
        //creates tables for database
        DatabaseSetup.createTables();
        App launchApp = new App();
        launchApp.runApp();
    } // end main

} // end Launch class