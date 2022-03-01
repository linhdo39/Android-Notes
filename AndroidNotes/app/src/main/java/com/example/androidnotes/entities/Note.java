package com.example.androidnotes.entities;

import androidx.annotation.NonNull;

import com.example.androidnotes.extensions.DateExtensions;
import com.example.androidnotes.extensions.JsonExtensions;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Objects;

public class Note implements Serializable{
    @SerializedName("name")
    private final String name;
    @SerializedName("description")
    private final String description;
    @SerializedName("date")
    private final DateTime time;
    @SerializedName("type")
    private final NoteType noteType;
    @SerializedName("taskComplete")
    private final boolean taskComplete;
    private transient int pos;

    public Note(String name, DateTime time, String description, NoteType noteType, boolean taskComplete) {
        this.name = name;
        this.time = time;
        this.description = description;
        this.noteType = noteType;
        this.taskComplete = taskComplete;
    }

    public String getDescription() {
        return description;
    }
    public String getName() {
        return name;
    }
    public DateTime getRawTime() {
        return time;
    }
    public String getShortTime(){
        return DateExtensions.dateToSimpleString(time);
    }
    public void setPosition(int pos){this.pos = pos;}
    public int getPosition(){return pos;}
    public NoteType getNoteType() { return noteType; }
    public boolean isTaskComplete() { return taskComplete; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return Objects.equals(name, note.name) && Objects.equals(description, note.description) && Objects.equals(time, note.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, time);
    }

    @NonNull
    public String toString() {
        Gson gson = JsonExtensions.getGson();
        return gson.toJson(this);
    }


}
