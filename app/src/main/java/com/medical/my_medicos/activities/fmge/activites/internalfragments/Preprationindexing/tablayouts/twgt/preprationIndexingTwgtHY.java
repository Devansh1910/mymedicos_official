package com.medical.my_medicos.activities.fmge.activites.internalfragments.Preprationindexing.tablayouts.twgt;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.pg.adapters.WeeklyQuizAdapter;
import com.medical.my_medicos.activities.pg.model.QuizPG;

import java.util.ArrayList;
import java.util.List;

public class preprationIndexingTwgtHY extends Fragment {
    private WeeklyQuizAdapter quizAdapter;
    private ArrayList<QuizPG> quizpg = new ArrayList<>();
    private String speciality;

    // Parameter key
    private static final String ARG_SPECIALITY = "speciality";

    public preprationIndexingTwgtHY() {
        // Required empty public constructor
    }

    public static preprationIndexingTwgtHY newInstance(String speciality) {
        preprationIndexingTwgtHY fragment = new preprationIndexingTwgtHY();
        Bundle args = new Bundle();
        args.putString(ARG_SPECIALITY, speciality);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            speciality = getArguments().getString(ARG_SPECIALITY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_prepration_indexing_twgt_h_y, container, false);

        // Initialize RecyclerView and Adapter
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        quizAdapter = new WeeklyQuizAdapter(getContext(), quizpg);
        recyclerView.setAdapter(quizAdapter);

        getQuestions(speciality);

        return view;
    }

    void getQuestions(String title1) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        List<String> subcollectionIds = new ArrayList<>();

        if (user != null) {
            String userId = user.getPhoneNumber();
            CollectionReference quizResultsCollection = db.collection("QuizResultsPGPrep")
                    .document(userId)
                    .collection("Weekley");

            // Fetch subcollections for the current user
            quizResultsCollection.get()
                    .addOnCompleteListener(subcollectionTask -> {
                        if (subcollectionTask.isSuccessful()) {
                            for (QueryDocumentSnapshot subdocument : subcollectionTask.getResult()) {
                                String subcollectionId = subdocument.getId();
                                subcollectionIds.add(subcollectionId);
                                Log.d("Subcollection ID", subcollectionId);
                            }

                            // Now fetch the quizzes
                            fetchQuizzes(title1, subcollectionIds);
                        } else {
                            Log.e("Subcollection ID", "Error fetching subcollections", subcollectionTask.getException());
                        }
                    });
        }
    }

    private void fetchQuizzes(String title1, List<String> subcollectionIds) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference quizzCollection = db.collection("PGupload")
                .document("Weekley")
                .collection("Quiz");

        Query query = quizzCollection.orderBy("from", Query.Direction.ASCENDING);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String id = document.getId();

                    // Check if the document ID is present in the subcollectionIds array
                    if (!subcollectionIds.contains(id)) {
                        String title = document.getString("title");
                        String speciality = document.getString("speciality");
                        Timestamp to = document.getTimestamp("to");

                        if (speciality != null && speciality.equals(title1)) {
                            QuizPG quizday = new QuizPG(title, title1, to, id);
                            quizpg.add(quizday);
                        }
                    }
                }
                quizAdapter.notifyDataSetChanged();
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
    }
}
