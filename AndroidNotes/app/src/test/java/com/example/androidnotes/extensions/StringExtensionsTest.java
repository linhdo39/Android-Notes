package com.example.androidnotes.extensions;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class StringExtensionsTest {

    @Test
    @Parameters({
            ", 1",
            "\n, 1",
            "\n\n, 1",
            "a\nb\n, 2",
            "a\nb\n\na\b, 4"
    })
    public void getLineCount(String str, int expectedLineCount) {
        int actualLineCount = StringExtensions.GetLineCount(str);
        assertEquals(expectedLineCount, actualLineCount);
    }

    @Test
    @Parameters({
            "a\nb, 2, a\nb",
            "a, 2, a",
            "a\nb\nc, 2, a\nb"
    })
    public void getSubLines(String str, int lines, String expected) {
        String actual = StringExtensions.GetSubLines(str, lines);
        assertEquals(expected, actual);
    }
}