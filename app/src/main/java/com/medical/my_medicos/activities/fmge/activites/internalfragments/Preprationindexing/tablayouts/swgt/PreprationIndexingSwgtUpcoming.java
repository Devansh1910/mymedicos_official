package com.medical.my_medicos.activities.fmge.activites.internalfragments.Preprationindexing.tablayouts.swgt;

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
import com.medical.my_medicos.activities.fmge.model.QuizFmge;
import com.medical.my_medicos.activities.pg.adapters.WeeklyQuizAdapter;
import com.medical.my_medicos.activities.pg.adapters.WeeklyQuizAdapterSwgt;
import com.medical.my_medicos.activities.pg.model.QuizPG;
import com.medical.my_medicos.activities.pg.model.Swgtmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PreprationIndexingSwgtUpcoming#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreprationIndexingSwgtUpcoming extends Fragment {
    private WeeklyQuizAdapterSwgt quizAdapter;
    private ArrayList<Swgtmodel> quizpg = new ArrayList<>();
    private String speciality;

    // Parameter key
    private static final String ARG_SPECIALITY = "speciality";

    public PreprationIndexingSwgtUpcoming() {
        // Required empty public constructor
    }

    public static PreprationIndexingSwgtUpcoming newInstance(String speciality) {
        PreprationIndexingSwgtUpcoming fragment = new PreprationIndexingSwgtUpcoming();
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
        View view = inflater.inflate(R.layout.fragment_prepration_indexing_swgt_upcoming, container, false);

        // Initialize RecyclerView and Adapter
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        quizAdapter = new WeeklyQuizAdapterSwgt(getContext(), quizpg);
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
            CollectionReference quizResultsCollection = db.collection("QuizResultsPGPrep").document(userId).collection("Weekley");

            quizResultsCollection.get()
                    .addOnCompleteListener(subcollectionTask -> {
                        if (subcollectionTask.isSuccessful()) {
                            for (QueryDocumentSnapshot subdocument : subcollectionTask.getResult()) {
                                String subcollectionId = subdocument.getId();
                                subcollectionIds.add(subcollectionId);
                                Log.d("Subcollection ID", subcollectionId);
                            }
                        } else {
                            Log.e("Subcollection ID", "Error fetching subcollections", subcollectionTask.getException());
                        }
                    });
        }

        CollectionReference quizzCollection = db.collection("PGupload").document("Weekley").collection("Quiz");
        Query query = quizzCollection.orderBy("from", Query.Direction.ASCENDING);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String id = document.getId();

                    if (!subcollectionIds.contains(id)) {
                        String title = document.getString("title");
                        String speciality = document.getString("speciality");
                        Timestamp to = document.getTimestamp("to");

                        Log.d("Speciality coming", speciality);
                        Log.d("Title to compare", title1);

                        int r = speciality.compareTo(title1);
                        if (r == 0) {
//                            Swgtmodel quizday = new Swgtmodel(title, title1, to, id);
//                            quizpg.add(quizday);
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
