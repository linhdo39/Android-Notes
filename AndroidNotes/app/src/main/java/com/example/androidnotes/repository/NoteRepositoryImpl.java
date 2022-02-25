package com.example.androidnotes.repository;

import com.example.androidnotes.Note;
import com.example.androidnotes.extensions.DateTimeDeserializer;
import com.example.androidnotes.extensions.FileWrapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.joda.time.DateTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class NoteRepositoryImpl implements NoteRepository {

    private FileWrapper wrapper;

    public NoteRepositoryImpl(FileWrapper wrapper) {
        this.wrapper = wrapper;
    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    @Override
    public ArrayList<Note> loadNotes(int fileId) {
        InputStream file = null;
        try {
            file = this.wrapper.FileInput(fileId);
            DateTimeDeserializer dateTimeDeserializer = new DateTimeDeserializer();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(DateTime.class, dateTimeDeserializer)
                    .setPrettyPrinting().create();
            String json = convertStreamToString(file);
            Type myType = new TypeToken<ArrayList<Note>>(){}.getType();
            ArrayList<Note>  notes = gson.fromJson(json, myType);
            return notes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (file != null) {
                try {
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Note> loadDeletedNotes() {
        return null;
    }
}
