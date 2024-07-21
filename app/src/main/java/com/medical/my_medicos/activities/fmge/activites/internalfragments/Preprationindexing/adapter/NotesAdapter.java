package com.medical.my_medicos.activities.fmge.activites.internalfragments.Preprationindexing.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.fmge.activites.internalfragments.Preprationindexing.Model.Index.NotesData;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {
    private List<NotesData> notesList;

    public NotesAdapter(List<NotesData> notesList) {
        this.notesList = notesList;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NotesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        NotesData notesData = notesList.get(position);
        holder.titleTextView.setText(notesData.getTitle());
        holder.descriptionTextView.setText(notesData.getDescription());
//        holder.timeTextView.setText(notesData.getTime());
        // Handle the file field as needed (e.g., set up a click listener to open the PDF)
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public static class NotesViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView descriptionTextView;
//        public TextView timeTextView;

        public NotesViewHolder(View view) {
            super(view);
            titleTextView = view.findViewById(R.id.titleTextView);
            descriptionTextView = view.findViewById(R.id.descriptionTextView);
//            timeTextView = view.findViewById(R.id.timeTextView); // Uncomment this line
        }
    }
}
