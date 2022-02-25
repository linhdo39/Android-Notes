package com.example.androidnotes.repository;

import com.example.androidnotes.Note;

import java.util.ArrayList;

public interface NoteRepository {

    public ArrayList<Note> loadNotes(int fileId);
    public ArrayList<Note> loadDeletedNotes();

}
