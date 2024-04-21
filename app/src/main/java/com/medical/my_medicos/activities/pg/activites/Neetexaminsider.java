package com.medical.my_medicos.activities.pg.activites;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import com.medical.my_medicos.activities.pg.adapters.neetexampadapter;
import com.medical.my_medicos.activities.pg.model.Neetpg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

public class Neetexaminsider extends AppCompatActivity implements neetexampadapter.OnOptionSelectedListener {

    private RecyclerView recyclerView;
    private neetexampadapter adapter;
    AlertDialog alertDialog;
    private boolean timerRunning = false;

    private TextView currentquestion;
    String id;
    private ArrayList<Neetpg> quizList1;
    private TextView timerTextView;

    private int currentQuestionIndex = 0;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 210 * 60 * 1000; // 210 minutes in milliseconds
    private long remainingTimeInMillis;
    private ArrayList<String> selectedOptionsList = new ArrayList<>();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.neetexaminsideractivity);

        currentquestion = findViewById(R.id.currentquestion);
        recyclerView = findViewById(R.id.recycler_view1);
        quizList1 = new ArrayList<>();
        timerTextView = findViewById(R.id.timerTextView);
        startTimer();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference quizzCollection = db.collection("PGupload").document("Weekley").collection("Quiz");

        quizzCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    quizList1.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        String speciality = document.getString("speciality");
                        String Title = document.getString("title");
                        Log.d("Error in Speciality", speciality);

                        Intent intent = getIntent();
                        String str1 = intent.getStringExtra("Title1");
                        String str = intent.getStringExtra("Title");
                        Log.d("Speciality", str1);
                        int r = str.compareTo(Title);

                        if (r == 0 && speciality.equals(str1)) {
                            ArrayList<Map<String, Object>> dataList = (ArrayList<Map<String, Object>>) document.get("Data");
                            if (dataList != null) {
                                for (Map<String, Object> entry : dataList) {
                                    String question = (String) entry.get("Question");
                                    String correctAnswer = (String) entry.get("Correct");
                                    String optionA = (String) entry.get("A");
                                    String optionB = (String) entry.get("B");
                                    String optionC = (String) entry.get("C");
                                    String optionD = (String) entry.get("D");
                                    String description = (String) entry.get("Description");
                                    String imageUrl = (String) entry.get("Image");
                                    id = document.getId();
                                    Log.d("document id ", id);

                                    Neetpg quizQuestion;

                                    if (imageUrl != null && !imageUrl.isEmpty()) {
                                        quizQuestion = new Neetpg(question, optionA, optionB, optionC, optionD, correctAnswer, imageUrl, description);
                                    } else {
                                        quizQuestion = new Neetpg(question, optionA, optionB, optionC, optionD, correctAnswer, imageUrl, description);
                                    }
                                    quizList1.add(quizQuestion);
                                    // Initialize selectedOptionsList with empty strings
                                    selectedOptionsList.add("");
                                }
                            }
                        }
                    }
                    loadNextQuestion();
                }
            }
        });

        TextView prevButton = findViewById(R.id.BackButtom);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPreviousQuestion();
            }
        });

        TextView nextButton = findViewById(R.id.nextButton1);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNextQuestion();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(Neetexaminsider.this));
        adapter = new neetexampadapter(Neetexaminsider.this, quizList1);
        adapter.setOnOptionSelectedListener(this); // Set the listener
        recyclerView.setAdapter(adapter);

        TextView instructionguide = findViewById(R.id.instructionguide);
        instructionguide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInstructionDialog();
            }
        });

        LinearLayout toTheBackLayout = findViewById(R.id.totheback1);
        toTheBackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });
    }



    private void loadNextQuestion() {
        if (currentQuestionIndex < quizList1.size()) {
            adapter.setQuizQuestions(Collections.singletonList(quizList1.get(currentQuestionIndex)));
            recyclerView.smoothScrollToPosition(currentQuestionIndex);
            updateQuestionNumber();
            currentQuestionIndex++;
        } else {
            showEndQuizConfirmation();
        }
    }

    private void showInstructionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Neetexaminsider.this, R.style.CustomAlertDialog);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_instruction_dialog, null);
        builder.setView(view);

        Button okButton = view.findViewById(R.id.instuction_understand);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        alertDialog = dialog;
    }

    private void loadPreviousQuestion() {
        if (!selectedOptionsList.isEmpty()) { // Check if the selectedOptionsList is not empty
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--;
                adapter.setQuizQuestions(Collections.singletonList(quizList1.get(currentQuestionIndex)));
                adapter.setSelectedOption(selectedOptionsList.get(currentQuestionIndex));
                recyclerView.smoothScrollToPosition(currentQuestionIndex);
                updateQuestionNumber();
            } else {
                Toast.makeText(this, "This is the first question", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No selected options available", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateQuestionNumber() {
        int totalQuestions = quizList1.size();
        String questionNumberText = (currentQuestionIndex + 1) + " / " + totalQuestions;
        currentquestion.setText(questionNumberText);
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

    private void handleEndButtonClick() {
        ArrayList<String> userSelectedOptions = new ArrayList<>();

        for (Neetpg quizQuestion : quizList1) {
            if (quizQuestion.getSelectedOption() == null || quizQuestion.getSelectedOption().isEmpty()) {
                userSelectedOptions.add("Skip");
            }
            userSelectedOptions.add(quizQuestion.getSelectedOption());
        }
        remainingTimeInMillis = timeLeftInMillis;

        ArrayList<String> results = compareAnswers(userSelectedOptions);
        countDownTimer.cancel();
        navigateToResultActivity();
    }

    private void navigateToResultActivity() {
        Intent intent = new Intent(Neetexaminsider.this, ResultActivityNeet.class);
        intent.putExtra("questions", quizList1);
        intent.putExtra("remainingTime", remainingTimeInMillis);
        intent.putExtra("id", id);
        startActivity(intent);
        finish();
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                handleQuizEnd();
            }
        }.start();
    }

    private void handleQuizEnd() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        remainingTimeInMillis = timeLeftInMillis;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Time's Up!");
        builder.setMessage("Sorry, you've run out of time. The quiz will be ended.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                navigateToResultActivity();
            }
        });

        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timerTextView.setText(timeFormatted);
    }

    private ArrayList<String> compareAnswers(ArrayList<String> userSelectedOptions) {
        ArrayList<String> results = new ArrayList<>();

        for (int i = 0; i < quizList1.size(); i++) {
            Neetpg quizQuestion = quizList1.get(i);
            String correctAnswer = quizQuestion.getCorrectAnswer();
            String userAnswer = userSelectedOptions.get(i);

            boolean isCorrect = correctAnswer.equals(userAnswer);

            results.add("Answer of Question " + (i + 1) + " is " + (isCorrect ? "Correct" : "Wrong"));
        }

        return results;
    }

//    @Override
//    public void onOptionSelected() {
//
//    }
    @Override
    public void onOptionSelected(String selectedOption) {
        // Update the selected option for the current question
        selectedOptionsList.set(currentQuestionIndex, selectedOption);
    }
}
