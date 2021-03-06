package com.example.androidnotes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidnotes.activities.NoteActivity;
import com.example.androidnotes.entities.Note;
import com.example.androidnotes.entities.NoteContainer;
import com.example.androidnotes.entities.NoteType;
import com.example.androidnotes.extensions.FileWrapper;
import com.example.androidnotes.extensions.StringExtensions;
import com.example.androidnotes.repository.NoteRepository;
import com.example.androidnotes.repository.NoteRepositoryImpl;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private final ArrayList<Note> noteList = new ArrayList<>();
    private final ArrayList<Note> deleteList = new ArrayList<>();
    private ActivityResultLauncher<Intent> resultLauncher;
    private NoteRepository notesRepository;
    private BottomNavigationView bottomNavigationView;
    private boolean completedTasks;
    private NoteType selectedNoteType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FileWrapper wrapper = new FileWrapper() {
            @Override
            public InputStream FileInput(int file) throws FileNotFoundException {
                return getApplicationContext().openFileInput(getString(file));
            }

            @Override
            public PrintWriter FileOutput(int fileId) throws FileNotFoundException {
                return new PrintWriter(getApplicationContext()
                        .openFileOutput(getString(fileId), Context.MODE_PRIVATE));
            }
        };
        notesRepository = new NoteRepositoryImpl(wrapper);

        noteList.addAll(
                loadFile()
                        .stream()
                        .sorted(
                                Comparator.comparing(Note::getRawTime)
                        )
                        .collect(Collectors.toList()));
        //noteList.addAll(loadFile());
        selectedNoteType = NoteType.Note;
        setTitle(StringExtensions.getTitleName(this.selectedNoteType, this.noteList.size(), completedTasks));

        recyclerView = findViewById(R.id.noteView);
        adapter = new NoteAdapter(noteList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleNewNote);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(this::onNavigationSelected);
        
    }

    private boolean onNavigationSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.notes) {
            this.selectedNoteType = NoteType.Note;
            Log.d(TAG, "notes selected");
        } else if (itemId == R.id.tasks) {
            this.selectedNoteType = NoteType.Task;
            completedTasks = true;
            Log.d(TAG, "tasks selected");
        } else if (itemId == R.id.openTasks) {
            this.selectedNoteType = NoteType.Task;
            completedTasks = false;
            Log.d(TAG, "open tasks selected");
            //case R.id.
        }
        setTitle(StringExtensions.getTitleName(this.selectedNoteType, this.noteList.size(), completedTasks));

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.add) {
            Intent intent = new Intent(this, NoteActivity.class);
            resultLauncher.launch(intent);
            return true;
        } else if (itemId == R.id.about) {
            Intent intent = new Intent(this, About.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.undo) {
            UndoDelete();
            return true;
        } else if (itemId == R.id.addTask) {
            Intent intent = new Intent(this, About.class);
            resultLauncher.launch(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void UndoDelete() {
        if(deleteList.size() == 0) {
            Toast.makeText(this, "No deleted note available", Toast.LENGTH_LONG).show();
            return;
        }
        Note undoNote = deleteList.get(0);
        int pos = undoNote.getPosition();
        noteList.add(pos, undoNote);
        deleteList.remove(undoNote);
        adapter.notifyItemInserted(pos);
        saveNote();
    }

    public void handleNewNote(ActivityResult result) {

        if (result == null || result.getData() == null) {
            Log.d(TAG, "handleNewNote: NULL ActivityResult received");
            return;
        }

        Intent data = result.getData();
        if (result.getResultCode() == Activity.RESULT_OK) {
            if (data.hasExtra(getString(R.string.new_or_edited_note))) {
                NoteContainer newNote = (NoteContainer) data.getSerializableExtra(getString(R.string.new_or_edited_note));
                boolean isNew = newNote.isNew();
                int targetPosition = 0;
                if (!isNew) {
                    targetPosition = newNote.getPosition();
                    noteList.remove(targetPosition);
                    adapter.notifyItemRemoved(targetPosition);
                }
                noteList.add(targetPosition, newNote.getNote());
                saveNote();
                adapter.notifyItemInserted(targetPosition);

            }  else
                Log.d(TAG, "handleNewNote: no new note");
        }
    }

    @Override
    protected void onResume() {
        noteList.clear();
        noteList.addAll(loadFile());
        deleteList.addAll(loadDelete());
        setTitle(StringExtensions.getTitleName(this.selectedNoteType, this.noteList.size(), completedTasks));
        super.onResume();

    }

    @Override
    protected void onPause() {
        saveNote();
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        Note temp = noteList.get(pos);
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra(getString(R.string.existing_note), temp);
        intent.putExtra(getString(R.string.note_position), pos);
        resultLauncher.launch(intent);
    }

    @Override
    public boolean onLongClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        Note m = noteList.get(pos);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete note " + m.getName() + "?");
        builder.setPositiveButton("Yes", (dialog, id) -> deleteNote(m, pos));
        builder.setNegativeButton("No", (dialog, id) -> Log.d(TAG, "Nothing Change"));
        AlertDialog dialog = builder.create();
        dialog.show();
        return false;
    }

    public void deleteNote(Note temp, int position) {
        temp.setPosition((noteList.indexOf(temp)));
        deleteList.add(temp);
        noteList.remove(temp);
        saveNote();
        adapter.notifyItemRemoved(position);
    }

    private ArrayList<Note> loadFile() {
        return notesRepository.loadNotes(R.string.file_name);
    }

    private ArrayList<Note> loadDelete() {
        return notesRepository.loadNotes(R.string.delete_file_name);
    }

    private void saveNote() {
        setTitle(StringExtensions.getTitleName(this.selectedNoteType, this.noteList.size(), completedTasks));
        try {
            this.notesRepository.saveNotes(R.string.file_name, noteList);
            this.notesRepository.saveNotes(R.string.delete_file_name, deleteList);
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    final ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            deleteNote(noteList.get(viewHolder.getAdapterPosition()), viewHolder.getAdapterPosition());
            adapter.notifyDataSetChanged();
            saveNote();
        }
    };

}
