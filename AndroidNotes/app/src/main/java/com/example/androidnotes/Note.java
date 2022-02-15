package com.example.androidnotes;

import android.util.JsonWriter;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Date;

public class Note implements Serializable{
    private final String name;
    private final String description;
    private final Date time;
    public Note(String name, Date time, String description) {
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
        return Constants.dateToSimpleString(time);
    }

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
            jsonWriter.endObject();
            jsonWriter.close();
            return sw.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}
