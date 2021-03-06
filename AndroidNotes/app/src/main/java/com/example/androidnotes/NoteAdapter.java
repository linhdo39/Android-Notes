package com.example.androidnotes;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidnotes.entities.Note;
import com.example.androidnotes.extensions.StringExtensions;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<ViewHolder> {

    private static final String TAG = "NoteAdapter";
    private final List<Note> noteList;
    private final MainActivity mainAct;

    NoteAdapter(List<Note> noteList, MainActivity ma) {
        this.noteList = noteList;
        mainAct = ma;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW MyViewHolder");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: FILLING VIEW HOLDER Note " + position);

        Note note = noteList.get(position);
        String des = note.getDescription();
        holder.name.setText(note.getName());

        if(des.length()>=80) {
            des = des.substring(0, 80) + "...";
        }
        int lineCount = StringExtensions.GetLineCount(des);
        if(lineCount > 3) {
            des = StringExtensions.GetSubLines(des, 3);
        }
        holder.description.setText(des);
        holder.date.setText(note.getShortTime());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }


}
