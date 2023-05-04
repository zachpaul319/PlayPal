package com.example.playpalapp.model.types;

public class User {
    public int userId;
    public String username, password, currentProduction, pastProductions;

    public User(int userId, String username, String password, String currentProduction, String pastProductions) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.currentProduction = currentProduction;
        this.pastProductions = pastProductions;
    }
}
