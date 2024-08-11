package com.medical.my_medicos.activities.neetss.activites.internalfragments.insiderfragments;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.medical.my_medicos.activities.neetss.activites.internalfragments.intwernaladapters.ExamQuizAdapter;
import com.medical.my_medicos.activities.neetss.model.QuizSSExam;
import com.medical.my_medicos.databinding.FragmentLiveNeetBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class LiveSSFragment extends Fragment {
    private FragmentLiveNeetBinding binding;
    private ArrayList<QuizSSExam> liveQuizzes;
    private ExamQuizAdapter adapter;
    private FirebaseUser currentUser;

    public static LiveSSFragment newInstance() {
        return new LiveSSFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLiveNeetBinding.inflate(inflater, container, false);
        setupRecyclerView();
        authenticateAndInitialize();
        return binding.getRoot();
    }

    private void setupRecyclerView() {
        liveQuizzes = new ArrayList<>();
        adapter = new ExamQuizAdapter(requireContext(), liveQuizzes);
        RecyclerView recyclerView = binding.recyclerViewLiveExams;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    private void authenticateAndInitialize() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            getPaidExam("Exam");
        } else {
            Log.e(TAG, "User is not logged in.");
            showToast("Please log in to continue");
            binding.progressBar.setVisibility(View.GONE);
        }
    }

    private void getPaidExam(String title) {
        fetchAttemptedQuizzes(currentUser.getPhoneNumber(), attemptedQuizIds ->
                fetchLiveQuizzes(FirebaseFirestore.getInstance(), title, Timestamp.now(), attemptedQuizIds));
    }

    private void fetchAttemptedQuizzes(String phoneNumber, Consumer<Set<String>> onCompleted) {
        FirebaseFirestore.getInstance().collection("QuizResults").document(phoneNumber).collection("Exam")
                .get()
                .addOnCompleteListener(task -> {
                    Set<String> attemptedQuizIds = new HashSet<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            attemptedQuizIds.add(document.getId());
                        }
                        onCompleted.accept(attemptedQuizIds);
                    } else {
                        Log.e(TAG, "Error fetching attempted quizzes: ", task.getException());
                        showToast("Failed to fetch quiz data.");
                        binding.progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void fetchLiveQuizzes(FirebaseFirestore db, String title, Timestamp now, Set<String> attemptedQuizIds) {

        String currentUid = currentUser.getPhoneNumber();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference userRef = database.getReference().child("profiles").child(currentUid);

        // Fetch coins
        userRef.child("Neetss").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String specialization = snapshot.getValue(String.class);
                Log.d("specialization",specialization);
                queryLiveQuizzes(db, specialization, title, now, attemptedQuizIds);

            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(FragmentManager.TAG, "Error loading coins from database: " + error.getMessage());
            }
        });

//        neetssRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
//                    String specialization = dataSnapshot.getChildren().iterator().next().getValue(String.class);
//                    if (specialization != null) {
//                        queryLiveQuizzes(db, specialization, title, now, attemptedQuizIds);
//                    } else {
//                        Log.d(TAG, "Specialization is null");
//                        updateUIForNoQuizzes();
//                    }
//                } else {
//                    Log.d(TAG, "No 'Neetss' data found for the user");
//                    updateUIForNoQuizzes();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.w(TAG, "Error fetching 'Neetss' data: ", databaseError.toException());
//                updateUIForNoQuizzes();
//            }
//        });
    }

    private void queryLiveQuizzes(FirebaseFirestore db, String specialization, String title, Timestamp now, Set<String> attemptedQuizIds) {
        CollectionReference quizCollection = db.collection("Neetss").document(specialization).collection("Weekley");
        Query query = quizCollection.whereLessThanOrEqualTo("from", now)
                .whereGreaterThanOrEqualTo("to", now);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                liveQuizzes.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    handleEachQuiz(document, title, now, attemptedQuizIds);
                }
                Collections.sort(liveQuizzes, Comparator.comparing(QuizSSExam::getTo));
                adapter.notifyDataSetChanged();
                updateUIForQuizzes();
            } else {
                Log.e(TAG, "Error getting live quizzes: ", task.getException());
                showToast("Failed to fetch live quizzes.");
                updateUIForNoQuizzes();
            }
        });
    }


    private void handleEachQuiz(QueryDocumentSnapshot document, String title, Timestamp now, Set<String> attemptedQuizIds) {
        String quizId = document.getId();
        Log.d("specialization","fewfwef");
        if (!attemptedQuizIds.contains(quizId)) {
            String quizTitle = document.getString("title");
            String speciality = document.getString("speciality");
            Timestamp to = document.getTimestamp("to");
            String id = document.getString("qid");
            boolean index=document.contains("index");
            if (index==true) {
                String index1 = document.getString("index");
                boolean paid1=document.contains("type");
                if (paid1==true) {


                    boolean paid = document.getBoolean("type");
                    Timestamp from = document.getTimestamp("from");
                    QuizSSExam quiz = new QuizSSExam(quizTitle, speciality, to, id, paid, index1);
                    liveQuizzes.add(quiz);
                }
            }
        }
    }

    private void updateUIForQuizzes() {
        if (liveQuizzes.isEmpty()) {
            Log.d(TAG, "No live quizzes found.");
            binding.nocardpg.setVisibility(View.VISIBLE); // Show the 'nocardpg' layout
        } else {
            binding.nocardpg.setVisibility(View.GONE); // Hide the 'nocardpg' layout
        }
        binding.progressBar.setVisibility(View.GONE);
    }

    private void updateUIForNoQuizzes() {
        binding.nocardpg.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.GONE);
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
