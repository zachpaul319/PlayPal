package com.example.playpalapp.model;

public class Message {
    public int messageId, senderId, recipientId;
    public String text;

    public Message(int messageId, int senderId, int recipientId, String text) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.text = text;
    }


}
