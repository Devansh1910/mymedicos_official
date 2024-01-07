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
import com.medical.my_medicos.activities.pg.adapters.ResultReportAdapter;
import com.medical.my_medicos.activities.pg.model.QuizPGinsider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {

    private RecyclerView resultRecyclerView;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private ResultReportAdapter resultAdapter;
    private TextView correctAnswersTextView;
    private TextView totalQuestionsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultRecyclerView = findViewById(R.id.resultRecyclerView);
        correctAnswersTextView = findViewById(R.id.correctanswercounter);
        totalQuestionsTextView = findViewById(R.id.totalanswwercounter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        resultRecyclerView.setLayoutManager(layoutManager);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        ArrayList<QuizPGinsider> questions = (ArrayList<QuizPGinsider>) intent.getSerializableExtra("questions");
        String id = intent.getStringExtra("id");


        resultAdapter = new ResultReportAdapter(this, questions);
        resultRecyclerView.setAdapter(resultAdapter);

        int correctAnswers = calculateCorrectAnswers(questions);
        int totalQuestions = questions.size();
        Log.d("Correct Answer", String.valueOf(correctAnswers));
        Log.d("Correct Answer", String.valueOf(totalQuestions));


        correctAnswersTextView.setText("" + correctAnswers);
        totalQuestionsTextView.setText("" + totalQuestions);
        uploadResultsToFirestore(correctAnswers, totalQuestions, null,id);
    }

    private int calculateCorrectAnswers(ArrayList<QuizPGinsider> questions) {
        int correctAnswers = 0;

        for (QuizPGinsider question : questions) {
            if (question.isCorrect()) {
                correctAnswers++;
            }
        }
        return correctAnswers;
    }
    private void uploadResultsToFirestore(int correctAnswers, int totalQuestions, String remainingTime, String id) {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DocumentReference userDocumentRef = db.collection("QuizResults").document(userId);
            CollectionReference idSubcollectionRef = userDocumentRef.collection("Weekley");
            Log.d("collection id ",id);
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("correctAnswers", correctAnswers);
            resultData.put("totalQuestions", totalQuestions);
            resultData.put("remainingTime", remainingTime);
            idSubcollectionRef
                    .document(id)
                    .set(resultData)
                    .addOnSuccessListener(aVoid -> Log.d("Result Upload", "Results uploaded successfully"))
                    .addOnFailureListener(e -> Log.e("Result Upload", "Error uploading results", e));
        }
    }
}
