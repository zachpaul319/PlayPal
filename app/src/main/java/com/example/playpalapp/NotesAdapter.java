package com.example.playpalapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.playpalapp.model.Note;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder>{
    public interface NotesAdapterDelegate {
        void didSelectNote(int position, View view);
        void didHoldNote(int position);
    }

    NotesAdapterDelegate delegate;

    List<Note> notesList;

    public NotesAdapter(List<Note> notesList) {
        this.notesList = notesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View noteView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, null);
        NotesAdapter.MyViewHolder myViewHolder = new NotesAdapter.MyViewHolder(noteView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.MyViewHolder holder, int position) {
        String title = notesList.get(position).title;
        String content = notesList.get(position).content;

        holder.setData(title, content);

        holder.itemView.setOnClickListener(v -> {
            if (delegate != null) {
                delegate.didSelectNote(position, holder.itemView);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (delegate != null) {
                delegate.didHoldNote(position);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titleOutput, descriptionOutput, timeOutput;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleOutput = itemView.findViewById(R.id.titleOutput);
            descriptionOutput = itemView.findViewById(R.id.descriptionOutput);
            timeOutput = itemView.findViewById(R.id.timeOutput);
        }

        private void setData(String title, String content) {
            titleOutput.setText(title);
            descriptionOutput.setText(content);
        }
    }
}
