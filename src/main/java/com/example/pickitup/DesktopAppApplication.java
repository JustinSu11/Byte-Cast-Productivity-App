package com.example.pickitup;

import com.example.pickitup.ui.MainFrame;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import javax.swing.SwingUtilities;

@SpringBootApplication // Enables Spring Boot autoconfiguration and component scanning
public class DesktopAppApplication {

    public static void main(String[] args){
        //Disable headless mode to allow Swing to create windows (Remove once a GUI is implemented)
        System.setProperty("java.awt.headless", "false");

        //Start the Spring Boot application, which initializes the application context.
        ConfigurableApplicationContext context = SpringApplication.run(DesktopAppApplication.class, args);

        //Launch the swing UI on the event Dispatch Thread to ensure thread safety.
        SwingUtilities.invokeLater(()->{
            //Retrieve the MainFrame bean from the Spring context.
            MainFrame frame = context.getBean(MainFrame.class);
            //Make the main application window visible.
            frame.setVisible(true);
        });
    }

}