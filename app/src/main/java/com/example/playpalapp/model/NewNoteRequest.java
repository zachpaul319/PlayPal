package com.example.playpalapp.model;

import com.example.playpalapp.NewNoteFragment;

public class NewNoteRequest {
    int userId;
    String title, content;

    public NewNoteRequest(int userId, String title, String content) {
        this.userId = userId;
        this.title = title;
        this.content = content;
    }
}
