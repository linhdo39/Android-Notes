package com.example.androidnotes.entities;

import java.io.Serializable;

public class NoteContainer implements Serializable {
    private final Note note;
    private final int position;
    private final boolean isNew;

    public int getPosition() {
        return position;
    }

    public boolean isNew() {
        return isNew;
    }

    public Note getNote() {
        return note;
    }

    public NoteContainer(Note note, int position, boolean isNew) {
        this.note = note;
        this.position = position;
        this.isNew = isNew;
    }


}
