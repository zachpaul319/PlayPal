package com.example.playpalapp.model.types;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Note implements Parcelable {
    public int noteId, userId;
    public String title, content;

    public Note(int noteId, int userId, String title, String content) {
        this.noteId = noteId;
        this.userId = userId;
        this.title = title;
        this.content = content;
    }

    protected Note(Parcel in) {
        noteId = in.readInt();
        userId = in.readInt();
        title = in.readString();
        content = in.readString();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {

        parcel.writeInt(noteId);
        parcel.writeInt(userId);
        parcel.writeString(title);
        parcel.writeString(content);
    }

}
