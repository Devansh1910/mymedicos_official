package com.medical.my_medicos.activities.university.activity;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.medical.my_medicos.activities.university.adapters.UpdatesAdapter;
import com.medical.my_medicos.activities.university.model.Updates;
import com.medical.my_medicos.databinding.ActivityUniversityListBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UniversitiesListActivity extends AppCompatActivity {
    private ActivityUniversityListBinding binding;
    private UpdatesAdapter updateAdapter;
    private ArrayList<Updates> updates;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUniversityListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        updates = new ArrayList<>();
        updateAdapter = new UpdatesAdapter(this, updates);

        String stateName = getIntent().getStringExtra("stateName");

        // Make sure getSupportActionBar() is not null before using it
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(stateName);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        getUniversities(stateName);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        binding.universityList.setLayoutManager(layoutManager);
        binding.universityList.setAdapter(updateAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    void getUniversities(String stateName) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Updates")
                .document(stateName)
                .collection("Institutions")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Assuming each document inside "Institutions" has a field named "data" (Array of college names)
                                ArrayList<String> universitiesArray = (ArrayList<String>) document.get("data");

                                if (universitiesArray != null) {
                                    Log.d("UniversitiesListActivity", "Received universities data for " + stateName + ": " + universitiesArray.toString());
                                    Log.d("UniversitiesListActivity", "Number of universities for " + stateName + ": " + universitiesArray.size());

                                    for (String universityName : universitiesArray) {
                                        Log.d("UniversitiesListActivity", "University Name: " + universityName);
                                        Updates update = new Updates(stateName, Collections.singletonList(universityName));
                                        updates.add(update);
                                    }
                                    updateAdapter.notifyDataSetChanged();
                                } else {
                                    Log.d("UniversitiesListActivity", "Universities data is null for " + stateName);
                                }
                            }
                        } else {
                            Log.d("UniversitiesListActivity", "Error getting documents for " + stateName + ": ", task.getException());
                        }
                    }
                });
    }
}
