package com.example.androidnotes.extensions;

import android.content.ContextWrapper;

import com.example.androidnotes.MainActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileWrapper {
    private ContextWrapper contextWrapper;
    public FileWrapper(ContextWrapper contextWrapper) {
        this.contextWrapper = contextWrapper;
    }

    public InputStream FileInput(int file) throws FileNotFoundException {
        return contextWrapper.getApplicationContext().openFileInput(contextWrapper.getString(file));
    }
}
