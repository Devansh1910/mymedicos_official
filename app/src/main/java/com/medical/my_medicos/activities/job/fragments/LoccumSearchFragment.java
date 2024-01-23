package com.medical.my_medicos.activities.job.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.my_medicos.adapter.job.MyAdapter6;
import com.medical.my_medicos.R;
import com.medical.my_medicos.adapter.job.items.jobitem1;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoccumSearchFragment extends Fragment {
    RecyclerView recyclerView;
    String title, speciality;

    // Static method to create a new instance of the fragment
    public static LoccumSearchFragment newInstance(String speciality, String title) {
        LoccumSearchFragment fragment = new LoccumSearchFragment();
        Bundle args = new Bundle();
        args.putString("speciality", speciality);
        args.putString("Title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_locum, container, false);
        recyclerView = view.findViewById(R.id.jobs_recyclerview_locum);
        Bundle args = getArguments();
        if (args != null) {
            speciality = args.getString("speciality");
            title = args.getString("Title");
            Log.d("Title", title);
        }

        List<jobitem1> locumJobList = new ArrayList<>();

        FirebaseFirestore dc = FirebaseFirestore.getInstance();

        dc.collection("JOB")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> dataMap = document.getData();
                            String category = (String) dataMap.get("Job type");

                            // Check if the job type is "Loccum" and speciality is "YourSpeciality"
                            if ("Loccum".equalsIgnoreCase(category)) {
                                int r = 0;
                                r = speciality.compareTo((String) dataMap.get("Location"));
                                if (r == 0) {
                                    String organiser = (String) dataMap.get("JOB Organiser");
                                    String location = (String) dataMap.get("Location");
                                    String date = (String) dataMap.get("date");
                                    String speciality = (String) dataMap.get("Speciality");
                                    String user = (String) dataMap.get("User");
                                    String jobTitle = (String) dataMap.get("JOB Title");
                                    String documentId = (String) dataMap.get("documentId");
                                    Log.d("title", speciality);

                                    // Use case-insensitive contains check
                                    if (title.toLowerCase().contains(speciality.toLowerCase())
                                            || speciality.toLowerCase().contains(title.toLowerCase())) {
                                        jobitem1 c = new jobitem1(speciality, organiser, location, date, user, jobTitle, category, documentId);
                                        locumJobList.add(c);
                                    }
                                }
                            }
                        }

                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                        recyclerView.setAdapter(new MyAdapter6(getActivity(), locumJobList));
                    } else {
                        Log.d("LocumFragment", "Error getting documents: ", task.getException());
                    }
                });

        return view;
    }
}
