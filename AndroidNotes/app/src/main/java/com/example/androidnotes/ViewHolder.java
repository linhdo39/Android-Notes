package com.example.androidnotes;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    public TextView name;
    TextView description;
    TextView date;

    ViewHolder(View view) {
        super(view);
        name = view.findViewById(R.id.name);
        description = view.findViewById(R.id.description);
        date = view.findViewById(R.id.date);
    }

}

