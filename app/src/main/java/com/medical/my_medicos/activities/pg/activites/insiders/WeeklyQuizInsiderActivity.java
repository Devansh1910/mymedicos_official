package com.medical.my_medicos.activities.pg.activites.insiders;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.pg.activites.PgprepActivity;
import com.medical.my_medicos.activities.pg.activites.ResultActivity;
import com.medical.my_medicos.activities.pg.adapters.WeeklyQuizAdapterinsider;
import com.medical.my_medicos.activities.pg.model.Neetpg;
import com.medical.my_medicos.activities.pg.model.QuizPGinsider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class WeeklyQuizInsiderActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private WeeklyQuizAdapterinsider adapter;
    private ArrayList<QuizPGinsider> quizList;
    private int currentQuestionIndex = 0;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_quiz_insider);

        recyclerView = findViewById(R.id.recycler_view);
        Intent intent = getIntent();
        String str = intent.getStringExtra("Title1"); // Specialty
        String str1 = intent.getStringExtra("Title"); // Title
        quizList = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference quizzCollection = db.collection("PGupload").document("Weekley").collection("Quiz");
        quizzCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                quizList.clear(); // Ensure this happens only once
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String speciality = document.getString("speciality");
                    String title = document.getString("title");

                    if (str.equals(speciality) && str1.equals(title)) {
                        ArrayList<Map<String, Object>> dataList = (ArrayList<Map<String, Object>>) document.get("Data");
                        for (Map<String, Object> entry : dataList) {
                            String question = (String) entry.get("Question");
                            String correctAnswer = (String) entry.get("Correct");
                            String optionA = (String) entry.get("A");
                            String optionB = (String) entry.get("B");
                            String optionC = (String) entry.get("C");
                            String optionD = (String) entry.get("D");
                            String description = (String) entry.get("Description");
                            String imageUrl = (String) entry.get("Image");

                            QuizPGinsider quizQuestion = new QuizPGinsider(question, optionA, optionB, optionC, optionD, correctAnswer, imageUrl, description);
                            quizList.add(quizQuestion);
                        }
                        break; // Assuming only one matching document, break after adding its questions
                    }
                }
                if (!quizList.isEmpty()) {
                    loadNextQuestion(); // Initialize the quiz with the first question
                } else {
                    Log.d("QuizLoading", "No questions loaded. Check filters or database.");
                }
            } else {
                Log.e("FirestoreError", "Error getting documents: ", task.getException());
            }
        });

        ImageView nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(v -> loadNextQuestion());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new WeeklyQuizAdapterinsider(this, quizList);
        recyclerView.setAdapter(adapter);

        TextView endButton = findViewById(R.id.endenabled);
        endButton.setOnClickListener(v -> handleEndButtonClick());

        LinearLayout toTheBackLayout = findViewById(R.id.totheback);
        toTheBackLayout.setOnClickListener(v -> showConfirmationDialog());

        adapter.setOnCountdownFinishedListener(this::onCountdownFinished);
    }

    private void loadNextQuestion() {
        if (currentQuestionIndex < quizList.size()) {
            QuizPGinsider currentQuestion = quizList.get(currentQuestionIndex);
            adapter.setQuizQuestions(Collections.singletonList(currentQuestion));
            recyclerView.smoothScrollToPosition(currentQuestionIndex);
            currentQuestionIndex++;
        } else {
            showEndQuizConfirmation();
        }
    }


    private void showEndQuizConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("End Quiz");
        builder.setMessage("Are you sure you want to end the quiz?");

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handleEndButtonClick();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("End Quiz");
        builder.setMessage("Are you sure you want to end the quiz?");

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handleEndButtonClick();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void navigateToPrepareActivity() {
        Intent intent = new Intent(WeeklyQuizInsiderActivity.this, PgprepActivity.class);
        startActivity(intent);
        finish();
    }

    private void handleEndButtonClick() {
        ArrayList<String> userSelectedOptions = new ArrayList<>();

        for (QuizPGinsider quizQuestion : quizList) {
            if (quizQuestion.getSelectedOption() == null || quizQuestion.getSelectedOption().isEmpty()) {
                userSelectedOptions.add("Skip");
            }
            userSelectedOptions.add(quizQuestion.getSelectedOption());
        }

        ArrayList<String> results = compareAnswers(userSelectedOptions);

        Intent intent = new Intent(WeeklyQuizInsiderActivity.this, ResultActivity.class);
        intent.putExtra("questions", quizList);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    private void showCustomToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, findViewById(R.id.customToastLayout));

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView text = layout.findViewById(R.id.customToastText);
        text.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    private ArrayList<String> compareAnswers(ArrayList<String> userSelectedOptions) {
        ArrayList<String> results = new ArrayList<>();

        for (int i = 0; i < quizList.size(); i++) {
            QuizPGinsider quizQuestion = quizList.get(i);
            String correctAnswer = quizQuestion.getCorrectAnswer();
            String userAnswer = userSelectedOptions.get(i);

            boolean isCorrect = correctAnswer.equals(userAnswer);

            results.add("Answer of Question " + (i + 1) + " is " + (isCorrect ? "Correct" : "Wrong"));
        }

        return results;
    }

    public void onCountdownFinished() {
        loadNextQuestion();
    }
}
