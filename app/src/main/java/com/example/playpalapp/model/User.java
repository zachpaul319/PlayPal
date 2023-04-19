package com.example.playpalapp.model;

public class User {
    public int orderId;
    public String username, password, currentProduction, pastProductions;

    public User(int orderId, String username, String password, String currentProduction, String pastProductions) {
        this.orderId = orderId;
        this.username = username;
        this.password = password;
        this.currentProduction = currentProduction;
        this.pastProductions = pastProductions;
    }
}
