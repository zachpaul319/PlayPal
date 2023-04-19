package com.example.playpalapp.model;

public class NewUserRequest {
    public String username, password, currentProduction, pastProductions;

    public NewUserRequest(String username, String password, String currentProduction, String pastProductions) {
        this.username = username;
        this.password = password;
        this.currentProduction = currentProduction;
        this.pastProductions = pastProductions;
    }
}
