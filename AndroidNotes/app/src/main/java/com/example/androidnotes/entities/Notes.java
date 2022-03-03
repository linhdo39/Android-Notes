package com.example.androidnotes.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.androidnotes.extensions.DateConverter;

import org.joda.time.DateTime;

@Entity(tableName = "note")
@TypeConverters(DateConverter.class)
public class Notes {
    @PrimaryKey(autoGenerate = true)
    public int uid;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "description")
    public String description;
    @ColumnInfo(name = "date")
    public DateTime time;
    @ColumnInfo(name = "type")
    public NoteType noteType;
    @ColumnInfo(name = "task_complete")
    public boolean taskComplete;
}
