package com.example.androidnotes.extensions;

import com.example.androidnotes.entities.NoteType;

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
    public static String getTitleName(NoteType noteType, int elementCount, boolean completedTasks) {
            String localType = "";
            switch (noteType) {
                case Note:
                    localType = "Note";
                    break;
                case Task:
                    if (completedTasks) {
                        localType = "All Tasks";
                    }
                    else {
                        localType = "Tasks";
                    }
                    break;
            }
            return "Android " + localType + " (" + elementCount + ")";
    }
}
