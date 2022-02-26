package com.example.androidnotes.extensions;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

public interface FileWrapper {
    InputStream FileInput(int file) throws FileNotFoundException;

    FileOutputStream FileOutput(int fileId) throws FileNotFoundException;
}
