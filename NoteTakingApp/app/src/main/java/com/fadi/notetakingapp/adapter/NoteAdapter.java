package com.fadi.notetakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fadi.notetakingapp.EditorActivity;
import com.fadi.notetakingapp.R;
import com.fadi.notetakingapp.model.Note;
import com.fadi.notetakingapp.utility.Constant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private final List<Note> notes;
    private final Context context;


    public NoteAdapter(List<Note> notes, Context context) {
        this.notes = notes;
        this.context = context;
    }

    @NonNull
    @Override
    //will be called each time a new view holder has to be created(the ViewHolder class)
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        //inflating the customized layout for my item list
        LayoutInflater infalter = LayoutInflater.from(parent.getContext());
        View view = infalter.inflate(R.layout.note_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    // when updating the display of the list item
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Note note = notes.get(i);
        viewHolder.mTextView.setText(note.getText());
        // opening the note by clicking the floating action button
        viewHolder.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditorActivity.class);
                intent.putExtra(Constant.NoteAdapter_NOTE_KEY, note.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    // returning number of items
    public int getItemCount() {
        return notes.size();
    }

    // the purpose of the view holder is to manage the view itself
    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.note_text)
        TextView mTextView;

        //get reference to the floating action button
        @BindView(R.id.fab)
        FloatingActionButton fab;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
