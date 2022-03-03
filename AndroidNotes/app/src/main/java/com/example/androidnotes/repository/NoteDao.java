package com.example.androidnotes.repository;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.androidnotes.entities.Notes;

import java.util.List;

@Dao
public interface NoteDao {
    @Query("select * from note")
    List<Notes> getAll();
}
