package com.example.androidnotes;

import android.util.JsonWriter;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Date;

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

    String getDescription() {
        return description;
    }
    String getName() {
        return name;
    }
    String getTime(){
        return Constants.dateToString(time);
    }
    String getShortTime(){
        return Constants.dateToSimpleString(time);
    }
    void setPosition(int pos){this.pos = pos;}
    int getPosition(){return pos;}

    @NonNull
    public String toString() {

        try {
            StringWriter sw = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(sw);
            jsonWriter.setIndent("  ");
            jsonWriter.beginObject();
            jsonWriter.name("name").value(getName());
            jsonWriter.name("date").value(getTime());
            jsonWriter.name("description").value(getDescription());
            jsonWriter.name("pos").value(getPosition());
            jsonWriter.endObject();
            jsonWriter.close();
            return sw.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public DateTime getRawTime() {
        return time;
    }
}
