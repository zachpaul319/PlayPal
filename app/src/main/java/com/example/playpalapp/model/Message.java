package com.example.playpalapp.model;

public class Message {
    public int messageId, senderId, recipientId;
    public String text, timestamp;

    public Message(int messageId, int senderId, int recipientId, String text, String timestamp) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.text = text;
        this.timestamp = timestamp;
    }


}
