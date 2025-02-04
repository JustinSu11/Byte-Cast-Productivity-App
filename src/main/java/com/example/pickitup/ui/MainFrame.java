package com.example.pickitup.ui;

import com.example.pickitup.service.BusinessService;
import javax.swing.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component //Marks this class as a Spring-managed component (bean)
public class MainFrame extends JFrame {

    private final BusinessService businessService; //Dependency injection for business Logic

    //Constructor with Spring's @Autowired to inject the BusinessService bean
    @Autowired
    public MainFrame(BusinessService businessService) {
        this.businessService = businessService;
        initialize(); //Call the method to set up UI components
    }

    private void initialize() {
        //Set the title for the window
        setTitle("Pick-It-Up Desktop Application");
        //Set the size of the window (width = 400, height = 300)
        setSize(400,300);
        //Specify the close operation to exit the application
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Use the business service to obtain a welcome message.
        String message = businessService.getWelcomeMessage();
        //Create a JLabel to display the message, centered horizontally
        JLabel label = new JLabel(message, SwingConstants.CENTER);
        //Add the label to the frame
        add(label);
    }
}
