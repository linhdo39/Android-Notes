package com.example.androidnotes;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;


@RunWith(JUnitParamsRunner.class)
public class ConstantsTest {


    @Test
    @Parameters({
            "09/29/2021 15:51:54, 2021, 9, 29, 15, 51, 54",
            "09/29/2015 15:51:54, 2015, 9, 29, 15, 51, 54"
    })
    public void stringToDate(String value,
                             int year,
                             int monthOfYear,
                             int dayOfMonth,
                             int hourOfDay,
                             int minuteOfHour,
                             int secondOfMinute) throws ParseException {
        DateTime expected = new DateTime(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute);

        DateTime actual = Constants.stringToDate(value);
        assertEquals(expected, actual);
    }

    @Test
    @Parameters({
            "new DateTime(2021, 9, 29, 15, 51, 54)"
    })
    public void dateToString(DateTime a) {

    }

    @Test
    public void dateToSimpleString() {
    }

    @Test
    public void getLineCount() {
    }

    @Test
    @Parameters({
            "a\nb, 2, a\nb",
            "a, 2, a",
            "a\nb\nc, 2, a\nb"
    })
    public void getSubLines(String str, int lines, String expected) {
        String actual = Constants.GetSubLines(str, lines);
        assertEquals(expected, actual);
    }
}