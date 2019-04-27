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
import com.fadi.notetakingapp.database.NoteEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private final List<NoteEntity> mNotes;
    private final Context mContext;
    public static final String NOTE_KEY = "Note_Key";

    public NoteAdapter(List<NoteEntity> mNotes, Context mContext) {
        this.mNotes = mNotes;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    //will be called each time a new view holder has to be created(the inner class)
    // each time the view refreshed with the java object note
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        // first I need to inflate the layout that I will be using
        LayoutInflater infalter = LayoutInflater.from(parent.getContext());
        View view = infalter.inflate(R.layout.note_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    // we be called each time I want to update the display of the list item
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final NoteEntity note = mNotes.get(i);
        viewHolder.mTextView.setText(note.getText());

        viewHolder.mfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditorActivity.class);
                intent.putExtra(NOTE_KEY, note.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    // just for returning the number of items in the data
    public int getItemCount() {
        return mNotes.size();
    }

    // the purpose of the view holder is to manage the view itself
    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.note_text)
        TextView mTextView;

        //get reference to the floating action button
        @BindView(R.id.fab)
        FloatingActionButton mfab;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
