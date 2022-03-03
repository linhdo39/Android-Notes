package com.example.androidnotes.extensions;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.joda.time.DateTime;

import java.lang.reflect.Type;

public class DateConverter {
    private static Gson gson = new Gson();

    @TypeConverter
    public static DateTime stringToDate(String data) {

        Type type = new TypeToken<DateTime>() {
        }.getType();

        return gson.fromJson(data, type);
    }

    @TypeConverter
    public static String dateToString(DateTime dateTime) {
        return gson.toJson(dateTime);
    }
}
