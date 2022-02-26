package com.example.androidnotes;

import android.app.Activity;
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

import com.example.androidnotes.entities.Note;
import com.example.androidnotes.extensions.FileWrapper;
import com.example.androidnotes.repository.NoteRepository;
import com.example.androidnotes.repository.NoteRepositoryImpl;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private final ArrayList<Note> noteList = new ArrayList<>();
    private final ArrayList<Note> deleteList = new ArrayList<>();
    private ActivityResultLauncher<Intent> resultLauncher;
    private NoteRepository notesRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FileWrapper wrapper = new FileWrapper(this);
        notesRepository = new NoteRepositoryImpl(wrapper);

        noteList.addAll(loadFile().stream().sorted((x, y) -> y.getRawTime().compareTo(x.getRawTime())).collect(Collectors.toList()));
        //noteList.addAll(loadFile());
        if(!noteList.isEmpty())
            setTitle("Android Notes (" + noteList.size()+")");

        recyclerView = findViewById(R.id.noteView);
        adapter = new NoteAdapter(noteList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleNewNote);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add) {
            Intent intent = new Intent(this, AddNote.class);
            resultLauncher.launch(intent);
            return true;
        } else if (item.getItemId() == R.id.about) {
            Intent intent = new Intent(this, About.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.undo) {
            UndoDelete();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
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
            if (data.hasExtra("new")) {
                Note newNote = (Note) data.getSerializableExtra("new");
                noteList.add(0, newNote);
                saveNote();
                adapter.notifyItemInserted(0);
            } else if (data.hasExtra("edit") && data.hasExtra("pos")) {
                Note editNote = (Note) data.getSerializableExtra("edit");
                int pos = data.getIntExtra("pos", 0);
                noteList.remove(pos);
                adapter.notifyItemRemoved(pos);
                noteList.add(0, editNote);
                adapter.notifyItemInserted(0);
                saveNote();
            } else
                Log.d(TAG, "handleNewNote: no new note");
        }
    }

    @Override
    protected void onResume() {
        noteList.clear();
        noteList.addAll(loadFile());
        deleteList.addAll(loadDelete());
        if (!noteList.isEmpty())
            setTitle("Android Notes (" + noteList.size() + ")");
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
        Intent intent = new Intent(this, EditNote.class);
        intent.putExtra("info", temp);
        intent.putExtra("position", pos);
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
        if (!noteList.isEmpty())
            setTitle("Android Notes (" + noteList.size() + ")");
        else
            setTitle("Android Notes");
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
