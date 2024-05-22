package com.medical.my_medicos.activities.pg.activites.insiders.fragment;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.medical.my_medicos.activities.pg.activites.insiders.SpecialityPGInsiderActivity;
import com.medical.my_medicos.activities.pg.adapters.WeeklyQuizAdapter;
import com.medical.my_medicos.activities.pg.model.QuizPG;
import com.medical.my_medicos.databinding.FragmentWeeklyQuizBinding;

import java.util.ArrayList;
import java.util.List;

public class WeeklyQuizFragment extends Fragment {
    private FragmentWeeklyQuizBinding binding;
    private WeeklyQuizAdapter quizAdapter;
    private ArrayList<QuizPG> quizpg;
    String title1;
    private String quizTitle;
    public static WeeklyQuizFragment newInstance(int catId, String title) {
        WeeklyQuizFragment fragment = new WeeklyQuizFragment();
        Bundle args = new Bundle();
        args.putInt("catId", catId);
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWeeklyQuizBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Bundle args = getArguments();
        if (args != null) {
            title1= args.getString("title", "");

            if (getActivity() instanceof SpecialityPGInsiderActivity) {
                ((SpecialityPGInsiderActivity) getActivity()).setToolbarTitle(title1);
            }

            quizpg = new ArrayList<>();
            quizAdapter = new WeeklyQuizAdapter(requireContext(), quizpg);

            RecyclerView recyclerViewVideos = binding.quizListWeekly;
            LinearLayoutManager layoutManagerVideos = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
            recyclerViewVideos.setLayoutManager(layoutManagerVideos);
            recyclerViewVideos.setAdapter(quizAdapter);

            getQuestions(title1);
        } else {
            // Handle the case where arguments are null
            Log.e("ERROR", "Arguments are null in WeeklyQuizFragment");
        }

        return view;
    }

    void getQuestions(String title) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        List<String> subcollectionIds = new ArrayList<>();

        if (user != null) {
            String userId = user.getPhoneNumber();

            CollectionReference quizResultsCollection = db.collection("QuizResults").document(userId).collection("Weekley");

            // Array to store subcollection IDs


            // Fetch subcollections for the current user
            quizResultsCollection.get()
                    .addOnCompleteListener(subcollectionTask -> {
                        if (subcollectionTask.isSuccessful()) {
                            for (QueryDocumentSnapshot subdocument : subcollectionTask.getResult()) {
                                // Access each subcollection inside the document
                                String subcollectionId = subdocument.getId();
                                subcollectionIds.add(subcollectionId);
                                Log.d("Subcollection ID", subcollectionId);
                            }

                            // Now you can use the subcollectionIds array outside this block
                            for (String id : subcollectionIds) {
                                Log.d("All Subcollection IDs", id);
                            }
                        } else {
                            // Handle failure
                            Log.e("Subcollection ID", "Error fetching subcollections", subcollectionTask.getException());
                        }
                    });
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CollectionReference quizzCollection = db.collection("PGupload").document("Weekley").collection("Quiz");

            Query query = quizzCollection;

            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String id = document.getId();

                            // Check if the document ID is present in the subcollectionIds array
                            if (!subcollectionIds.contains(id)) {
                                String title = document.getString("title");
                                String speciality = document.getString("speciality");
                                Timestamp to = document.getTimestamp("to");
                                int r = speciality.compareTo(title1);
                                if (r == 0) {
                                    QuizPG quizday = new QuizPG(title, title1, to);
                                    quizpg.add(quizday);
                                }
                            }
                        }
                        quizAdapter.notifyDataSetChanged();

                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
        }

    }
}