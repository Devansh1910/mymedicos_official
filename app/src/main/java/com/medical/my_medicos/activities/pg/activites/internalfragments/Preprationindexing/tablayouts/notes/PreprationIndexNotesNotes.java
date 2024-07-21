package com.medical.my_medicos.activities.pg.activites.internalfragments.Preprationindexing.tablayouts.notes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.pg.activites.internalfragments.Preprationindexing.Model.Index.NotesData;
import com.medical.my_medicos.activities.pg.activites.internalfragments.Preprationindexing.adapter.NotesAdapter;

import java.util.ArrayList;
import java.util.List;

public class PreprationIndexNotesNotes extends Fragment {

    private RecyclerView recyclerView;
    private NotesAdapter adapter;
    private List<NotesData> notesList;
    private FirebaseFirestore db;
    private String speciality;

    public PreprationIndexNotesNotes() {
        // Required empty public constructor
    }

    public static PreprationIndexNotesNotes newInstance(String speciality) {
        PreprationIndexNotesNotes fragment = new PreprationIndexNotesNotes();
        Bundle args = new Bundle();
        args.putString("speciality", speciality);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            speciality = getArguments().getString("speciality");
        }

        db = FirebaseFirestore.getInstance();
        notesList = new ArrayList<>();
        adapter = new NotesAdapter(notesList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_prepration_index_notes_notes, container, false);

        // Use member variable instead of creating a new local variable
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Fetch data
        fetchNotesData();

        return view;
    }

    private void fetchNotesData() {
        db.collection("PGupload").document("Notes").collection("Note")
                .whereEqualTo("speciality", speciality)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        notesList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String descriptionField = document.getString("Description");
                            String timeField = document.getString("Time");
                            String titleField = document.getString("Title");
                            String fileField = document.getString("file");

                            NotesData notesData = new NotesData();
                            notesData.setDescription(descriptionField);
                            notesData.setTime(timeField);
                            notesData.setTitle(titleField);
                            notesData.setFile(fileField);

                            notesList.add(notesData);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("FirestoreError", "Error getting documents: ", task.getException());
                    }
                });


    }
}
