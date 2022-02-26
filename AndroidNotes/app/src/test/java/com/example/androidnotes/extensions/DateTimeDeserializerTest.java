package com.example.androidnotes.extensions;

import static org.junit.Assert.*;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

public class DateTimeDeserializerTest {

    class TestClass {
        @SerializedName("time")
        DateTime time;

        public TestClass(DateTime time) {
            this.time = time;
        }
    }

    Gson gson;
    private final String jsonString = "{\n" +
            "  \"time\": \"09/29/2021 15:51:54\"\n" +
            "}";
    private final DateTime dateTime =
            new DateTime(2021,
                    9,
                    29,
                    15,
                    51,
                    54);
    @Before
    public void setUp() throws Exception {
        gson = JsonExtensions.getGson();
    }

    @Test
    public void deserialize() {
        TestClass testCase = this.gson.fromJson(jsonString, TestClass.class);
        assertEquals(dateTime, testCase.time);
    }

    @Test
    public void serialize() {
        TestClass testCase = new TestClass(dateTime);
        String actual = this.gson.toJson(testCase);
        assertEquals(jsonString, actual);
    }
}