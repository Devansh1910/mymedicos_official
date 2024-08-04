package com.medical.my_medicos.activities.fmge.activites.internalfragments.Preprationindexing.tablayouts.twgt;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.pg.activites.internalfragments.Preprationindexing.Model.twgt.Quiz;
import com.medical.my_medicos.activities.pg.activites.internalfragments.Preprationindexing.adapter.QuizAdapter;

import java.util.ArrayList;
import java.util.List;

public class PreprationIndexTwgtBookmark extends Fragment {

    private DatabaseReference database;
    private String phoneNumber;
    private FirebaseFirestore firestore;
    private List<Quiz> bookmarkedQuizzes;
    private RecyclerView recyclerView;
    private QuizAdapter quizAdapter;
    private String speciality; // Add this field to store the speciality parameter
    private ProgressBar progressBar; // Add ProgressBar field

    public PreprationIndexTwgtBookmark() {
        // Required empty public constructor
    }

    public static PreprationIndexTwgtBookmark newInstance(String speciality) {
        PreprationIndexTwgtBookmark fragment = new PreprationIndexTwgtBookmark();
        Bundle args = new Bundle();
        args.putString("speciality", speciality); // Pass the speciality parameter
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            speciality = getArguments().getString("speciality"); // Retrieve the speciality parameter
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prepration_index_twgt_bookmark, container, false);

        // Initialize Firebase and RecyclerView
        database = FirebaseDatabase.getInstance().getReference();
        FirebaseUser current = FirebaseAuth.getInstance().getCurrentUser();
        phoneNumber = current.getPhoneNumber();

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        bookmarkedQuizzes = new ArrayList<>();
        quizAdapter = new QuizAdapter(getContext(),bookmarkedQuizzes);
        recyclerView.setAdapter(quizAdapter);

        progressBar = view.findViewById(R.id.progressBar); // Initialize ProgressBar

        fetchBookmarkedQuizzes();

        return view;
    }

    private void fetchBookmarkedQuizzes() {
        progressBar.setVisibility(View.VISIBLE); // Show progress bar

        DatabaseReference bookmarkRef = database.child("profiles").child(phoneNumber).child("bookmarks");
        bookmarkRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Boolean isBookmarked = snapshot.getValue(Boolean.class);
                    if (Boolean.TRUE.equals(isBookmarked)) { // Check if the bookmark is true
                        String quizId = snapshot.getKey();
                        fetchQuizDetailsFromFirestore(quizId);
                    }
                }

                progressBar.setVisibility(View.GONE); // Hide progress bar when data fetching is done
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE); // Hide progress bar in case of an error
                // Handle errors here
            }
        });
    }


    private void fetchQuizDetailsFromFirestore(String quizId) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Log.d("FetchQuiz", "User not logged in");
            return;
        }

        String userId = user.getPhoneNumber();

        // Check if the quizId is in QuizResultsPGPrep collection for the current user
        firestore.collection("QuizResults")
                .document(userId)
                .collection("Weekley")
                .document(quizId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            // Quiz ID is present in QuizResultsPGPrep
                            Log.d("FetchQuiz", "Quiz ID is already present in QuizResultsPGPrep");
                        } else {
                            // Quiz ID is not present, fetch the quiz details from PGupload
                            firestore.collection("PGupload")
                                    .document("Weekley")
                                    .collection("Quiz")
                                    .document(quizId)
                                    .get()
                                    .addOnCompleteListener(quizTask -> {
                                        if (quizTask.isSuccessful()) {
                                            DocumentSnapshot quizDocument = quizTask.getResult();
                                            if (quizDocument != null && quizDocument.exists()) {
                                                // Fetching required fields with presence check
                                                String title = quizDocument.contains("title") ? quizDocument.getString("title") : "Unknown Title";
                                                Timestamp dueDate = quizDocument.contains("dueDate") ? quizDocument.getTimestamp("dueDate") : null;
                                                boolean type = quizDocument.contains("type") && quizDocument.getBoolean("type");
                                                String index = quizDocument.contains("index") ? quizDocument.getString("index") : "Unknown Index";
                                                String title1 = quizDocument.contains("speciality") ? quizDocument.getString("speciality") : "Unknown Index";
                                                String Id = quizDocument.contains("qid") ? quizDocument.getString("qid") : "Unknown Index";
                                                String quizspeciality = quizDocument.contains("speciality") ? quizDocument.getString("speciality") : "Unknown Index";

                                                // Assuming you have a constructor or a method to set these fields in your Quiz object
                                                Quiz quiz = new Quiz();
                                                quiz.setTitle(title);
                                                quiz.setId(Id);
                                                quiz.setTitle1(title1);
                                                quiz.setDueDate(String.valueOf(dueDate));
                                                quiz.setType(type);
                                                quiz.setIndex(index);
                                                if(speciality.equals(quizspeciality)) {

                                                    bookmarkedQuizzes.add(quiz);
                                                    quizAdapter.notifyDataSetChanged();
                                                }
                                            } else {
                                                // Quiz document does not exist
                                                Log.d("FetchQuiz", "No such quiz document");
                                            }
                                        } else {
                                            // Handle errors here
                                            Log.d("FetchQuiz", "Get quiz failed with ", quizTask.getException());
                                        }
                                    });
                        }
                    } else {
                        // Handle errors here
                        Log.d("FetchQuiz", "Get failed with ", task.getException());
                    }
                });
    }
}
