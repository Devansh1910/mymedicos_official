package com.medical.my_medicos.activities.pg.activites.internalfragments.preprationinternalfragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.EventListener;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.pg.adapters.SpecialitiesPGAdapter;
import com.medical.my_medicos.activities.pg.model.SpecialitiesPG;
import java.util.ArrayList;
import java.util.List;

public class AllPgPrep extends Fragment {

    private static final String TAG = "AllPgPrep";

    private DocumentReference documentReference;
    private ArrayList<SpecialitiesPG> categoryItems;
    private SpecialitiesPGAdapter specialitiesPGAdapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_pg_prep, container, false);

        // Initialize Firestore Document reference
        documentReference = FirebaseFirestore.getInstance().collection("Categories").document("39liVyLEjII6dtzolxSZ");
        Log.d(TAG, "Document Reference Path: " + documentReference.getPath());

        // Initialize ArrayList and Adapter
        categoryItems = new ArrayList<>();
        specialitiesPGAdapter = new SpecialitiesPGAdapter(getContext(), categoryItems);

        // Setup RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(specialitiesPGAdapter);

        // Initialize ProgressBar
        progressBar = view.findViewById(R.id.progressBar);

        // Fetch data from Firestore
        fetchData();

        return view;
    }

    private void fetchData() {
        // Show the loader
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@NonNull DocumentSnapshot documentSnapshot, @NonNull FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d(TAG, "Error fetching document: " + e.getMessage());
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    return;
                }

                categoryItems.clear();

                if (documentSnapshot.exists()) {
                    Log.d(TAG, "Document exists");

                    if (documentSnapshot.contains("All")) {
                        Log.d(TAG, "'All' field exists");

                        List<String> specialties = (List<String>) documentSnapshot.get("All");
                        for (String specialtyName : specialties) {
                            if (specialtyName != null) {
                                Log.d(TAG, "Specialty: " + specialtyName);
                                categoryItems.add(new SpecialitiesPG(specialtyName, 1)); // Assuming value 1 for each specialty
                            } else {
                                Log.d(TAG, "Specialty has null value");
                            }
                        }
                    } else {
                        Log.d(TAG, "'All' field not found in document");
                    }
                } else {
                    Log.d(TAG, "Document does not exist");
                }

                // Notify adapter
                specialitiesPGAdapter.notifyDataSetChanged();

                // Hide the loader and show RecyclerView
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
    }
}
