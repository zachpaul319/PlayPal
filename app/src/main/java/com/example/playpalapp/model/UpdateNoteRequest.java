package com.example.playpalapp.model;

import com.example.playpalapp.UpdateNoteFragment;

public class UpdateNoteRequest {
    String title, content;

    public UpdateNoteRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
