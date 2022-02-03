package com.example.androidnotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditNote extends AppCompatActivity {
    private EditText name;
    private String originalName;
    private EditText description;
    private String originalDes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note);

        name = findViewById(R.id.noteName);
        description = findViewById(R.id.detail);
        Note m = (Note) getIntent().getSerializableExtra("info");
        originalName = m.getName();
        originalDes = m.getDescription();
        name.setText(m.getName());
        description.setText(m.getDescription());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save) {
            if(checkChange()) {
                doReturnData(null);
                return true;
            }
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
       if(name.getText().toString().equals(originalName) && description.getText().toString().equals(originalDes)) {
           finish();
           return false;
       }
       return true;
    }

    public void doReturnData(View v) {
        if(checkNull()) {
            String d1Text = name.getText().toString();
            String d2Text = description.getText().toString();
            int position = getIntent().getIntExtra("position", 0);
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
            String time = formatter.format(new Date());
            Note editNote = new Note(d1Text, time, d2Text);
            Intent data = new Intent();
            data.putExtra("edit", editNote);
            data.putExtra("pos", position);
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
            builder.setPositiveButton("Yes", (dialog, id) -> doReturnData(null));
            builder.setNegativeButton("No", (dialog, id) -> finish());
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
