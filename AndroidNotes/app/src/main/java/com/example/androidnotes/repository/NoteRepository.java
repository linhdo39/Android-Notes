package com.example.androidnotes.repository;

import com.example.androidnotes.entities.Note;

import java.io.IOException;
import java.util.ArrayList;

public interface NoteRepository {

    ArrayList<Note> loadNotes(int fileId);
    void saveNotes(int file_name, ArrayList<Note> noteList) throws IOException;
}
