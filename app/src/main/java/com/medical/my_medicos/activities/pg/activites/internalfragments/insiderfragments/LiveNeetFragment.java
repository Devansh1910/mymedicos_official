package com.medical.my_medicos.activities.pg.activites.internalfragments.insiderfragments;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.pg.activites.internalfragments.intwernaladapters.ExamQuizAdapter;
import com.medical.my_medicos.activities.pg.model.QuizPG;
import com.medical.my_medicos.activities.pg.model.QuizPGExam;
import com.medical.my_medicos.databinding.FragmentLiveNeetBinding;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class LiveNeetFragment extends Fragment {
    private FragmentLiveNeetBinding binding;
    private ArrayList<QuizPGExam> Livepg;
    private ExamQuizAdapter LiveAdapter;
    String quiztiddaya;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FirebaseUser currentUser;
    private String title1 = "Exam";

    public static LiveNeetFragment newInstance() {
        LiveNeetFragment fragment = new LiveNeetFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLiveNeetBinding.inflate(inflater, container, false);

        // Initialize quiz list and adapter early
        Livepg = new ArrayList<>();
        LiveAdapter = new ExamQuizAdapter(requireContext(), Livepg);

        RecyclerView recyclerViewVideos = binding.recyclerViewLiveExams;
        LinearLayoutManager layoutManagerVideos = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewVideos.setLayoutManager(layoutManagerVideos);
        recyclerViewVideos.setAdapter(LiveAdapter);

        binding.progressBar.setVisibility(View.VISIBLE);
        getPaidExam(title1); // Fetch data regardless of args

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getPhoneNumber();
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = currentUser.getPhoneNumber();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            binding.progressBar.setVisibility(View.GONE);
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(FragmentManager.TAG, document.getId() + " => " + document.getData());

                                Map<String, Object> dataMap = document.getData();
                                String field1 = (String) dataMap.get("Phone Number");

                                if (field1 != null && currentUser.getPhoneNumber() != null) {
                                    int a = field1.compareTo(userId);
                                    Log.d("Issue with the userID", String.valueOf(a));

                                    if (a == 0) {
                                        Log.d("Can't get it", String.valueOf(a));
                                        quiztiddaya = ((String) dataMap.get("QuizToday"));
                                        break;
                                    } else {
                                        quiztiddaya = null;
                                    }
                                }
                            }
                        } else {
                            Log.d(ContentValues.TAG, "Error getting documents: ", task.getException());
                            binding.progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    void getPaidExam(String title) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Timestamp now = Timestamp.now();  // Get the current timestamp directly in Firestore's format

        Log.d(TAG, "Current time: " + now.toString());  // Log current time for debugging

        if (user != null) {
            String userId = user.getPhoneNumber();
            CollectionReference quizzCollection = db.collection("PGupload").document("Weekley").collection("Quiz");

            Query query = quizzCollection.whereLessThanOrEqualTo("from", now)
                    .whereGreaterThanOrEqualTo("to", now);

            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String quizTitle = document.getString("title");
                            String speciality = document.getString("speciality");
                            Timestamp to = document.getTimestamp("to");
                            Timestamp from = document.getTimestamp("from");

                            Log.d(TAG, "Checking quiz: " + quizTitle + " from: " + from + " to: " + to);  // Log each quiz being checked

                            if ((title.isEmpty() || speciality.equals(title)) && now.compareTo(from) >= 0 && now.compareTo(to) <= 0) {
                                QuizPGExam quiz = new QuizPGExam(quizTitle, speciality, to, document.getId(), from);
                                Livepg.add(quiz);
                                Log.d(TAG, "Added live quiz: " + quizTitle);
                            }
                        }
                        LiveAdapter.notifyDataSetChanged();
                        if (Livepg.isEmpty()) {
                            Log.d(TAG, "No live quizzes found.");
                        }
                    } else {
                        Log.e(TAG, "Error getting live quizzes: ", task.getException());
                    }
                }
            });
        } else {
            Log.e(TAG, "User is not logged in.");
        }
    }


}