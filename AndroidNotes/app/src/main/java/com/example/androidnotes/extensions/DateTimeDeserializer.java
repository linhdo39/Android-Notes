package com.example.androidnotes.extensions;

import com.example.androidnotes.Constants;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.joda.time.DateTime;

import java.lang.reflect.Type;
import java.text.ParseException;

public class DateTimeDeserializer
        implements JsonDeserializer<DateTime>, JsonSerializer<DateTime> {
    @Override
    public DateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return Constants.stringToDate(json.getAsString());
        } catch (ParseException e) {
            e.printStackTrace();
            return new DateTime();
        }
    }

    @Override
    public JsonElement serialize(DateTime src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(Constants.dateToString(src));
    }
}
