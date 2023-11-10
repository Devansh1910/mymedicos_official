package com.example.my_medicos.activities.job.fragments;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_medicos.adapter.job.MyAdapter6;
import com.example.my_medicos.R;
import com.example.my_medicos.adapter.job.items.jobitem1;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RegularFragment extends Fragment  {
    RecyclerView recyclerView;
    String title;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_regular, container, false);
        recyclerView = view.findViewById(R.id.jobs_recyclerview_regular);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Bundle args = getArguments();
        if (args != null) {
            title = args.getString("Title");
            Log.d("Title",title);
            // Use the 'title' as needed in RegularFragment
        }

        List<jobitem1> joblist = new ArrayList<jobitem1>();
        List<jobitem1> regularJobList = new ArrayList<jobitem1>();
        
        FirebaseFirestore dc = FirebaseFirestore.getInstance();
        //......
        dc.collection("JOB")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Map<String, Object> dataMap = document.getData();
                                String Category = (String) dataMap.get("Job type");

                                // Check if the job type is "regular"
                                if ("Regular".equalsIgnoreCase(Category)) {
                                    String Organiser = (String) dataMap.get("JOB Organiser");
                                    String Location = (String) dataMap.get("Location");
                                    String date = (String) dataMap.get("date");
                                    String speciality = (String) dataMap.get("Speciality");
                                    String user = (String) dataMap.get("User");
                                    String Title = (String) dataMap.get("JOB Title");
                                    Log.d("title",speciality);
                                    int r=title.compareTo(speciality);
                                    if (r==0) {
                                        jobitem1 c = new jobitem1(speciality, Organiser, Location, date, user, Title, Category);
                                        regularJobList.add(c);
                                    }
                                }
                            }

                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                            recyclerView.setAdapter(new MyAdapter6(getActivity(), regularJobList));
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        // Query and display Today's CME events in the RecyclerView
        // Customize your logic to query and display data for today's events.
        // Set the appropriate adapter for the RecyclerView.

        return view;
    }
}
