package com.example.androidnotes.repository;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import com.example.androidnotes.entities.Note;
import com.example.androidnotes.entities.NoteType;
import com.example.androidnotes.extensions.FileWrapper;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public class NoteRepositoryImplTest {

    NoteRepositoryImpl noteRepository;
    private PipedInputStream inputStream;
    private PipedOutputStream outputStream;
    private PrintStream outputStreamWriter;
    private String json = "[\n" +
            "  {\n" +
            "    \"name\": \"molestie i\",\n" +
            "    \"description\": \"molestie in faucibus iaculis rhoncus ut nunc ipsum posuere vivamus in habitasse convallis non \",\n" +
            "    \"date\": \"11/12/2021 15:51:54\"\n" +
            "  }\n" +
            "]\n";
    private String jsonWithTypeAndDone = "[\n" +
            "  {\n" +
            "    \"name\": \"molestie i\",\n" +
            "    \"description\": \"molestie in faucibus iaculis rhoncus ut nunc ipsum posuere vivamus in habitasse convallis non \",\n" +
            "    \"date\": \"11/12/2021 15:51:54\",\n" +
            "    \"type\": \"Note\",\n" +
            "    \"taskComplete\": false\n" +
            "  }\n" +
            "]\n";

    private String jsonWithTypeAndDone2 = "[\n" +
            "  {\n" +
            "    \"name\": \"molestie i\",\n" +
            "    \"description\": \"molestie in faucibus iaculis rhoncus ut nunc ipsum posuere vivamus in habitasse convallis non \",\n" +
            "    \"date\": \"11/12/2021 15:51:54\",\n" +
            "    \"type\": \"Task\",\n" +
            "    \"taskComplete\": true\n" +
            "  }\n" +
            "]\n";

    private Note noteReference = new Note("molestie i",
            new DateTime(2021, 11, 12, 15, 51, 54),
            "molestie in faucibus iaculis rhoncus ut nunc ipsum posuere vivamus in habitasse convallis non ",
            NoteType.Note, false);

    private Note noteReference2 = new Note("molestie i",
            new DateTime(2021, 11, 12, 15, 51, 54),
            "molestie in faucibus iaculis rhoncus ut nunc ipsum posuere vivamus in habitasse convallis non ",
            NoteType.Task, true);

    @Before
    public void setUp() throws Exception {

        inputStream = new PipedInputStream();
        outputStream = new PipedOutputStream();
        inputStream.connect(outputStream);
        outputStreamWriter = new PrintStream(outputStream);


        noteRepository = new NoteRepositoryImpl(new FileWrapper() {
            @Override
            public InputStream FileInput(int file) throws FileNotFoundException {
                return inputStream;
            }

            @Override
            public PrintWriter FileOutput(int fileId) throws FileNotFoundException {
                return new PrintWriter(outputStreamWriter);
            }
        });
    }

    @Test
    public void convertStreamToString() throws Exception {
        String sourceString = "abc\n";

        outputStreamWriter.print(sourceString);
        outputStreamWriter.close();

        String result = NoteRepositoryImpl.convertStreamToString(inputStream);
        assertEquals(sourceString, result);
    }

    @Test
    public void loadNotes() {
        outputStreamWriter.print(json);
        outputStreamWriter.close();

        ArrayList<Note> notes = noteRepository.loadNotes(1);
        ArrayList<Note> expectedNotes = new ArrayList<>();
        expectedNotes.add(noteReference);
        assertArrayEquals(expectedNotes.toArray(), notes.toArray());
    }

    @Test
    public void loadNotesWithTask() {
        outputStreamWriter.print(jsonWithTypeAndDone2);
        outputStreamWriter.close();

        ArrayList<Note> notes = noteRepository.loadNotes(1);
        ArrayList<Note> expectedNotes = new ArrayList<>();
        expectedNotes.add(noteReference2);
        assertArrayEquals(expectedNotes.toArray(), notes.toArray());
    }

    @Test
    public void saveNotes() throws Exception {
        ArrayList<Note> expectedNotes = new ArrayList<>();
        expectedNotes.add(noteReference);
        noteRepository.saveNotes(1, expectedNotes);
        String result = NoteRepositoryImpl.convertStreamToString(inputStream);
        assertEquals(jsonWithTypeAndDone, result);
    }
}