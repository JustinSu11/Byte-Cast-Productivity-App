/*
    *******************************************************************************
    Launch Class
    Last Updated 04/12/2025
    Developers: Byte Cast

    This class contains the main method for running the app and
    setting up the database.

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
       App launchApp = new App();

       //creates tables for database
       DatabaseSetup.createTables();
       launchApp.runApp();

    } // end main

} // end class