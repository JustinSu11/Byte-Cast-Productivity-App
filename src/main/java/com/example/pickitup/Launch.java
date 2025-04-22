/**
 * Contains the main method
 *
 * @author Byte Cast
 * @date 04/23/2025
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