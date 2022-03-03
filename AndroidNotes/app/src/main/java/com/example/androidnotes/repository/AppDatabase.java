package com.example.androidnotes.repository;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.androidnotes.entities.Notes;

@Database(entities = {Notes.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NoteDao noteDao();
}

//Schema export directory is not provided to the annotation processor so we cannot export the schema. You can either provide `room.schemaLocation` annotation processor argument OR set exportSchema to false.
