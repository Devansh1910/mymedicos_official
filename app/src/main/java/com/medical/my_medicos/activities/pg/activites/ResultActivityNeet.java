package com.medical.my_medicos.activities.pg.activites;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.pg.adapters.ResultReportNeetAdapter;
import com.medical.my_medicos.activities.pg.model.Neetpg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResultActivityNeet extends AppCompatActivity {

    private RecyclerView resultRecyclerView;
    private ResultReportNeetAdapter resultAdapter;
    private TextView correctAnswersTextView;
    private TextView totalQuestionsTextView;
    private TextView remainingTimeTextView;
    private FirebaseFirestore db;
    private FirebaseAuth auth; // Initialize FirebaseAuth

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultRecyclerView = findViewById(R.id.resultRecyclerView);
        correctAnswersTextView = findViewById(R.id.correctanswercounter);
        totalQuestionsTextView = findViewById(R.id.totalanswwercounter);
//        remainingTimeTextView = findViewById(R.id.remainingTimeTextView); // Add this line

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        resultRecyclerView.setLayoutManager(layoutManager);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance(); // Initialize FirebaseAuth

        Intent intent = getIntent();
        ArrayList<Neetpg> questions = (ArrayList<Neetpg>) intent.getSerializableExtra("questions");
        long remainingTimeInMillis = intent.getLongExtra("remainingTime", 0);
        String id = intent.getStringExtra("id");
        Log.d("document id 1",id);


        resultAdapter = new ResultReportNeetAdapter(this, questions);
        resultRecyclerView.setAdapter(resultAdapter);

        // Calculate and display the number of correct answers and total questions
        int correctAnswers = calculateCorrectAnswers(questions);
        int totalQuestions = questions.size();
        Log.d("Correct Answer", String.valueOf(correctAnswers));
        Log.d("Correct Answer", String.valueOf(totalQuestions));

        correctAnswersTextView.setText("Correct Answers: " + correctAnswers);
        totalQuestionsTextView.setText("Total Questions: " + totalQuestions);

        // Display remaining time
        String remainingTimeFormatted = formatTime(remainingTimeInMillis);
        Log.d("Formatted Remaining Time", remainingTimeFormatted);
        uploadResultsToFirestore(correctAnswers, totalQuestions, remainingTimeFormatted,id);
//      tted remaining time
    }

    private int calculateCorrectAnswers(ArrayList<Neetpg> questions) {
        int correctAnswers = 0;

        for (Neetpg question : questions) {
            if (question.isCorrect()) {
                correctAnswers++;
            }
        }

        return correctAnswers;
    }

    private String formatTime(long millis) {
        int seconds = (int) (millis / 1000) % 60;
        int minutes = (int) ((millis / (1000 * 60)) );
        return String.format("%03d:%02d", minutes, seconds); // Update the format
    }

    private void uploadResultsToFirestore(int correctAnswers, int totalQuestions, String remainingTime, String id) {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            // Create a reference to the user's document
            DocumentReference userDocumentRef = db.collection("QuizResults").document(userId);

            // Create a reference to the subcollection with the id parameter
            CollectionReference idSubcollectionRef = userDocumentRef.collection("Exam");

            // Create a map with the result data
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("correctAnswers", correctAnswers);
            resultData.put("totalQuestions", totalQuestions);
            resultData.put("remainingTime", remainingTime);

            // Add the data to the subcollection
            idSubcollectionRef
                    .document(id) // Auto-generated document ID
                    .set(resultData)
                    .addOnSuccessListener(aVoid -> Log.d("Result Upload", "Results uploaded successfully"))
                    .addOnFailureListener(e -> Log.e("Result Upload", "Error uploading results", e));
        }
    }

}
