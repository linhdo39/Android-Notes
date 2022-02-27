package com.example.androidnotes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidnotes.R;
import com.example.androidnotes.entities.Note;
import com.example.androidnotes.entities.NoteContainer;

import org.joda.time.DateTime;

public class NoteActivity extends AppCompatActivity {
    private EditText name;

    private EditText description;


    private Note existingNote;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note);

        name = findViewById(R.id.noteName);
        description = findViewById(R.id.detail);

        existingNote = this.getExistingOrNew();
        if(existingNote != null) {
            name.setText(existingNote.getName());
            description.setText(existingNote.getDescription());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selection = item.getItemId();
        switch(selection) {
            case R.id.save:
                doReturnData();
                break;
            case R.id.cancel:
                this.onBackPressed();
                break;
        }
        return false;
    }

    public boolean checkNull (){
        String text = name.getText().toString();
        if(text.trim().isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Cannot save without a title!");
            builder.setPositiveButton("Ok", (dialog, id) -> finish());
            builder.setNegativeButton("Cancel", (dialog, id) -> Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show());
            AlertDialog dialog = builder.create();
            dialog.show();
            return false;
        }
        return true;
    }
    public boolean checkChange(){
        if(existingNote == null) {
            return true;
        }
        if(name.getText().toString().equals(existingNote.getName())
                && description.getText().toString().equals(existingNote.getDescription())) {
            finish();
            return false;
        }
        return true;
    }

    public void doReturnData() {
        if(checkNull()) {
            String d1Text = name.getText().toString();
            String d2Text = description.getText().toString();
            int position = getIntent().getIntExtra(getString(R.string.note_position), 0);
            DateTime time = new DateTime();
            Note editNote = new Note(d1Text, time, d2Text);
            Intent data = new Intent();
            NoteContainer container =
                    new NoteContainer(editNote, position, existingNote == null);
            data.putExtra(getString(R.string.new_or_edited_note), container);
            setResult(RESULT_OK, data);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if(checkChange()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Save data");
            if (!name.getText().toString().trim().isEmpty())
                builder.setMessage("Do you want to save '" + name.getText().toString() + "'?");
            else
                builder.setMessage("Do you want to save this note?");
            builder.setPositiveButton("Yes", (dialog, id) -> doReturnData());
            builder.setNegativeButton("No", (dialog, id) -> finish());
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private Note getExistingOrNew() {
        if(!getIntent().hasExtra(getString(R.string.existing_note))) {
            return null;
        }
        Object obj = getIntent().getSerializableExtra(getString(R.string.existing_note));
        if(obj == null || obj.getClass() != Note.class) {
            return null;
        }
        return (Note)obj;
    }
}
