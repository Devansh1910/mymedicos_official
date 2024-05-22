package com.medical.my_medicos.activities.pg.activites;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.pg.activites.PgprepActivity;
import com.medical.my_medicos.activities.pg.activites.extras.RecetUpdatesNoticeActivity;
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
    private TextView gotopghome;
    private FirebaseFirestore db;
    private FirebaseAuth auth; // Initialize FirebaseAuth

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_neet);

        NestedScrollView nestedScrollView = findViewById(R.id.nestedofresult);
        nestedScrollView.smoothScrollTo(0, nestedScrollView.getBottom());

        gotopghome = findViewById(R.id.gotopghome);
        gotopghome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ResultActivityNeet.this, PgprepActivity.class);
                startActivity(i);
            }
        });

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        resultRecyclerView = findViewById(R.id.resultRecyclerView);
        correctAnswersTextView = findViewById(R.id.correctanswercounter);
        totalQuestionsTextView = findViewById(R.id.totalanswwercounter);
//        remainingTimeTextView = findViewById(R.id.remainingTimeTextView); // Add this line

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        resultRecyclerView.setLayoutManager(layoutManager);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        ArrayList<Neetpg> questions = (ArrayList<Neetpg>) intent.getSerializableExtra("questions");
        long remainingTimeInMillis = intent.getLongExtra("remainingTime", 0);
        String id = intent.getStringExtra("id");
        int skip = intent.getIntExtra("skippedQuestions",0);

        Log.d("document id 1",id);


        resultAdapter = new ResultReportNeetAdapter(this, questions);
        resultRecyclerView.setAdapter(resultAdapter);

        int correctAnswers = calculateCorrectAnswers(questions);
        int totalQuestions = questions.size();
        Log.d("Correct Answer", String.valueOf(correctAnswers));
        Log.d("Correct Answer", String.valueOf(totalQuestions));

        correctAnswersTextView.setText(""+ correctAnswers);
        totalQuestionsTextView.setText("" + totalQuestions);

        String remainingTimeFormatted = formatTime(remainingTimeInMillis);
        Log.d("Formatted Remaining Time", remainingTimeFormatted);
        uploadResultsToFirestore(correctAnswers, totalQuestions, remainingTimeFormatted,id);

        int score = calculateScore(questions,skip);

//        correctAnswersTextView.setText("" + score);

        TextView resultScoreTextView = findViewById(R.id.result_score);
        resultScoreTextView.setText(String.valueOf(score));

        Log.d("Score", "Score: " + score);

//        correctAnswersTextView.setText("" + score);
        totalQuestionsTextView.setText("" + totalQuestions);

        double percentage = ((double) score / (totalQuestions * 4)) * 100;

        String greetingText;
        if (percentage < 50) {
            greetingText = "Don't worry, Keep Going";
        } else if (percentage <= 60) {
            greetingText = "Keep Practicing";
        } else if (percentage <= 75) {
            greetingText = "Keep it up";
        } else if (percentage <= 85) {
            greetingText = "Good";
        } else if (percentage <= 90) {
            greetingText = "Very Good";
        } else {
            greetingText = "Excellent";
        }

        TextView greetingTextView = findViewById(R.id.greeting);
        greetingTextView.setText(greetingText);

    }

    private int calculateScore(ArrayList<Neetpg> questions,int skip) {
        int score = 0;

        for (Neetpg question : questions) {
            if (question.isCorrect()) {
                score += 4;
            } else {
                score -= 1;
            }
        }
        return Math.max(0, score+skip);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Toast.makeText(ResultActivityNeet.this, "", Toast.LENGTH_SHORT).show();
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
        return String.format("%03d:%02d", minutes, seconds);
    }

    private void uploadResultsToFirestore(int correctAnswers, int totalQuestions, String remainingTime, String id) {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String userId = user.getPhoneNumber();
            DocumentReference userDocumentRef = db.collection("QuizResults").document(userId);
            CollectionReference idSubcollectionRef = userDocumentRef.collection("Exam");
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("correctAnswers", correctAnswers);
            resultData.put("totalQuestions", totalQuestions);
            resultData.put("remainingTime", remainingTime);
            Log.d("Result Upload",userId);
            idSubcollectionRef
                    .document(id)
                    .set(resultData)
                    .addOnSuccessListener(aVoid -> Log.d("Result Upload", "Results uploaded successfully"))
                    .addOnFailureListener(e -> Log.e("Result Upload", "Error uploading results", e));
        }
    }

}