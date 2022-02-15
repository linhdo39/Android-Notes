package com.example.androidnotes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Constants {
    private static String DateFormat = "MM/dd/yyyy HH:mm:ss";
    public static Date stringToDate(String dateStr) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);
        return sdf.parse(dateStr);
    }
    public static String dateToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
        return formatter.format(date);
    }

    public static String dateToSimpleString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        return formatter.format(date);
    }
    public static int GetLineCount(String str) {
        String[] lines = str.split("\r\n|\r|\n");
        return  lines.length;
    }
    public static String GetSubLines(String str, int lineCount) {
        String[] lines = str.split("\r\n|\r|\n");
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < Math.min(lineCount, lines.length); i++) {
            sb.append(lines[i] + "\n");
        }
        return sb.toString();
    }
}
