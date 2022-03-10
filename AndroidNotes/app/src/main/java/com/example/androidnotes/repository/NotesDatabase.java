package com.example.androidnotes.repository;

import android.content.Context;

import androidx.room.Room;

public class NotesDatabase {
    private final Context mCtx;
    private static NotesDatabase mInstance;

    //our app database object
    private final AppDatabase appDatabase;

    private NotesDatabase(Context mCtx) {
        this.mCtx = mCtx;

        //creating the app database with Room database builder
        //MyToDos is the name of the database
        appDatabase = Room.databaseBuilder(mCtx, AppDatabase.class, "MyToDos").allowMainThreadQueries().build();
    }

    public static synchronized NotesDatabase getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new NotesDatabase(mCtx);
        }
        return mInstance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
