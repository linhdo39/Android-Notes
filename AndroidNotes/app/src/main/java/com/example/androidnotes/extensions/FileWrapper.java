package com.example.androidnotes.extensions;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;

public interface FileWrapper {
    InputStream FileInput(int file) throws FileNotFoundException;

    PrintWriter FileOutput(int fileId) throws FileNotFoundException;
}
