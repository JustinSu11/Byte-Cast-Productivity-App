package com.example.pickitup.service;

import org.springframework.stereotype.Service;

@Service //Marks this class as a service component for business logic
public class BusinessService {
    //Returns a welcome message that can be used in the UI
    public String getTitle() {
        return "Pick It Up";
    }
}
