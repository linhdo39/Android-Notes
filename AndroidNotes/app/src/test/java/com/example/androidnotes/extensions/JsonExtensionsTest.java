package com.example.androidnotes.extensions;

import static org.junit.Assert.*;

import com.google.gson.Gson;

import org.junit.Test;

public class JsonExtensionsTest {

    @Test
    public void getGson() {
        Gson gson = JsonExtensions.getGson();
        assertNotNull(gson);
    }
}