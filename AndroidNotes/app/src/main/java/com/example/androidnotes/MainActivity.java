package com.example.androidnotes;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private final ArrayList<Note> noteList = new ArrayList<>();
    private ActivityResultLauncher<Intent> resultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteList.addAll(loadFile());
        if (!noteList.isEmpty())
            setTitle("Android Notes (" + noteList.size() + ")");
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
        } else {
            return super.onOptionsItemSelected(item);
        }
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
        noteList.remove(temp);
        saveNote();
        adapter.notifyItemRemoved(position);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private ArrayList<Note> loadFile() {
        ArrayList<Note> temp = new ArrayList<>();
        try {
            InputStream is = getApplicationContext().openFileInput(getString(R.string.file_name));
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONArray jsonArray = new JSONArray(sb.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                String time = jsonObject.getString("date");
                String desc = jsonObject.getString("description");
                Note note = new Note(name, time, desc);
                temp.add(note);
            }
        } catch (FileNotFoundException e) {
            Log.d(TAG, "loadJson: no Json file");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    private void saveNote() {
        if (!noteList.isEmpty())
            setTitle("Android Notes (" + noteList.size() + ")");
        else
            setTitle("Android Notes");
        try {
            FileOutputStream fos = getApplicationContext().
                    openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);

            PrintWriter printWriter = new PrintWriter(fos);
            printWriter.print(noteList);
            printWriter.close();
            fos.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            noteList.remove(viewHolder.getAdapterPosition());
            adapter.notifyDataSetChanged();
        }
    };

}
