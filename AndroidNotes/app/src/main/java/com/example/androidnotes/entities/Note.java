package com.example.androidnotes.entities;

import androidx.annotation.NonNull;

import com.example.androidnotes.extensions.DateExtensions;
import com.example.androidnotes.extensions.JsonExtensions;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import java.io.Serializable;

public class Note implements Serializable{
    @SerializedName("name")
    private final String name;
    @SerializedName("description")
    private final String description;
    @SerializedName("date")
    private final DateTime time;
    private int pos;
    public Note(String name, DateTime time, String description) {
        this.name = name;
        this.time = time;
        this.description = description;
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

    @NonNull
    public String toString() {
        Gson gson = JsonExtensions.getGson();
        return gson.toJson(this);
    }


}
