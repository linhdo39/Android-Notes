package com.example.androidnotes.extensions;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class DateExtensionsTest {

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
                             int secondOfMinute)  {
        DateTime expected = new DateTime(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute);

        DateTime actual = DateExtensions.stringToDate(value);
        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void stringToDateThrowsNullError()  {
        DateTime actual = DateExtensions.stringToDate(null);
    }

    @Test(expected = IllegalArgumentException.class)
    @Parameters({
            "",
            "abc",
            "09/29/bvc 15:51:54",
            "13/29/2015 15:51:54"
    })
    public void stringToDateThrowsFormatError(String str)  {
        DateTime actual = DateExtensions.stringToDate(str);
    }

    @Test
    public void dateToSimpleString() {
        DateTime date = new DateTime(2021, 9, 29, 15, 51, 54);
        String expected = "09/29/2021";
        String actual = DateExtensions.dateToSimpleString(date);
        assertEquals(expected, actual);
    }

    @Test (expected = NullPointerException.class)
    public void dateToSimpleStringThrowsError() {
        DateTime date = null;
        DateExtensions.dateToSimpleString(date);
    }

    @Test
    public void dateToString() {
        DateTime date = new DateTime(2021, 9, 29, 15, 51, 54);
        String expected = "09/29/2021 15:51:54";
        String actual = DateExtensions.dateToString(date);
        assertEquals(expected, actual);
    }

    @Test (expected = NullPointerException.class)
    public void dateToStringThrowsError() {
        DateTime date = null;
        DateExtensions.dateToString(date);
    }


}