package com.example.playpalapp.model;

public class Contact {
    public int userId;
    public String username, text;

    public Contact(int userId, String username, String text) {
        this.userId = userId;
        this.username = username;
        this.text = text;
    }
}
