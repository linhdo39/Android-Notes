package com.example.androidnotes.repository;

import com.example.androidnotes.entities.Note;
import com.example.androidnotes.extensions.FileWrapper;
import com.example.androidnotes.extensions.JsonExtensions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class NoteRepositoryImpl implements NoteRepository {

    private final FileWrapper wrapper;
    private final Gson gson;

    public NoteRepositoryImpl(FileWrapper wrapper) {

        this.gson = JsonExtensions.getGson();

        this.wrapper = wrapper;
    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    @Override
    public ArrayList<Note> loadNotes(int fileId) {
        try {
            try(InputStream file = this.wrapper.FileInput(fileId)) {
                String json = convertStreamToString(file);
                Type myType = new TypeToken<ArrayList<Note>>(){}.getType();
                return gson.fromJson(json, myType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public void saveNotes(int file_name, ArrayList<Note> noteList) throws IOException {
        String json = this.gson.toJson(noteList);

        try(FileOutputStream fos = this.wrapper.FileOutput(file_name)) {
            PrintWriter printWriter = new PrintWriter(fos);
            printWriter.print(json);
            printWriter.close();
        }
    }
}
