package com.example.androidnotes.extensions;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StringExtensions {

    public static int GetLineCount(String str) {
        String[] lines = str.split("\r\n|\r|\n");
        return  lines.length;
    }
    public static String GetSubLines(String str, int lineCount) {
        String[] lines = str.split("\r\n|\r|\n");

        return Arrays.stream(lines).limit(lineCount).collect(Collectors.joining("\n"));
    }
}
