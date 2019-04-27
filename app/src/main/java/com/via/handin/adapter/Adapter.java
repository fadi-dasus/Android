package com.via.handin.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.via.handin.Details;
import com.via.handin.R;
import com.via.handin.model.Note;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private final List<Note> notes;
    private final Context context;
    public static final String NOTE_KEY = "Note_Key";

    public Adapter(List<Note> notes, Context context) {
        this.notes = notes;
        this.context = context;
    }

    @NonNull
    @Override
    //will be called each time a new view holder has to be created(the inner class)
    // each time the view refreshed with the java object note
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        // first I need to inflate the layout that I will be using
        LayoutInflater infalter = LayoutInflater.from(parent.getContext());
        View view = infalter.inflate(R.layout.note_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    // we be called each time I want to update the display of the list item
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Note note = notes.get(i);
        viewHolder.textView.setText(note.getText());

       
    }

    @Override
    // just for returning the number of items in the data
    public int getItemCount() {
        return notes.size();
    }

    // the purpose of the view holder is to manage the view itself
    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.note_text)
        TextView textView;

        //get reference to the floating action button
        @BindView(R.id.fab)
        FloatingActionButton fab;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
