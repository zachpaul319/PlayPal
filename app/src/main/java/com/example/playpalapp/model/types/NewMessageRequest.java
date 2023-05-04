package com.example.playpalapp.model.types;

public class NewMessageRequest {
    int senderId, recipientId;
    String text;

    public NewMessageRequest(int senderId, int recipientId, String text) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.text = text;
    }
}
