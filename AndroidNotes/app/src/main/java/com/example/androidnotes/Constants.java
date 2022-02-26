package com.example.androidnotes;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Constants {
    private static final String DateFormat = "MM/dd/yyyy HH:mm:ss";

    public static DateTime stringToDate(String dateStr)  {
        DateTimeFormatter sdf = DateTimeFormat.forPattern(DateFormat);
        return sdf.parseDateTime(dateStr);
    }
    public static String dateToString(DateTime date) {
        DateTimeFormatter sdf = DateTimeFormat.forPattern(DateFormat);
        return date.toString(sdf);
    }

    public static String dateToSimpleString(DateTime date) {

        String simpleDateFormat = "MM/dd/yyyy";
        DateTimeFormatter sdf = DateTimeFormat.forPattern(simpleDateFormat);
        return date.toString(sdf);
    }
    public static int GetLineCount(String str) {
        String[] lines = str.split("\r\n|\r|\n");
        return  lines.length;
    }
    public static String GetSubLines(String str, int lineCount) {
        String[] lines = str.split("\r\n|\r|\n");

        return String.join("\n",
                Arrays.stream(lines).limit(lineCount).collect(Collectors.toList()));
    }
}
