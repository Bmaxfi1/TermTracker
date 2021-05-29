package com.example.termtracker.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.termtracker.Listeners.OnNoteClickListener;
import com.example.termtracker.Model.Note;
import com.example.termtracker.R;

import java.util.List;

public class NotesRecyclerViewAdapter extends RecyclerView.Adapter<NotesRecyclerViewAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView contentTextView;


        public ViewHolder(View itemView) {
            super(itemView);

            titleTextView = (TextView) itemView.findViewById(R.id.note_recyclerview_item_title);
            contentTextView = (TextView) itemView.findViewById(R.id.note_recyclerview_item_content);
        }
        public void bind(final Note note, final OnNoteClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(note);
                }
            });

        }
    }

    private List<Note> noteList;
    private OnNoteClickListener listener;

    public NotesRecyclerViewAdapter(List<Note> notes, OnNoteClickListener listener) {
        this.listener = listener;
        noteList = notes;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.note_recyclerview_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = noteList.get(position);

        TextView titleView = holder.titleTextView;
        TextView contentView = holder.contentTextView;

        titleView.setText(note.getTitle());
        contentView.setText(note.getContent());


        holder.bind(noteList.get(position), listener);

    }

    @Override
    public int getItemCount() {
        if (noteList != null) {
            return noteList.size();
        }
        else return -1;
    }

}
