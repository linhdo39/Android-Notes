package com.example.androidnotes.extensions;

import android.content.Context;
import android.content.ContextWrapper;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FileWrapper {
    private final ContextWrapper contextWrapper;
    public FileWrapper(ContextWrapper contextWrapper) {
        this.contextWrapper = contextWrapper;
    }

    public InputStream FileInput(int file) throws FileNotFoundException {
        return contextWrapper.getApplicationContext().openFileInput(contextWrapper.getString(file));
    }

    public FileOutputStream FileOutput(int fileId) throws FileNotFoundException {

        return this.contextWrapper.getApplicationContext()
                .openFileOutput(this.contextWrapper.getString(fileId), Context.MODE_PRIVATE);
    }
}
