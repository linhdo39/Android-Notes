package com.example.androidnotes.extensions;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateExtensions {
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
}
