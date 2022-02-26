package com.example.androidnotes.extensions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

public class JsonExtensions {

    private static final Gson gson;

    static {
        DateTimeDeserializer dateTimeDeserializer = new DateTimeDeserializer();

        gson = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, dateTimeDeserializer)
                .setPrettyPrinting().create();
    }
    public static Gson getGson() {
        return gson;
    }
}
