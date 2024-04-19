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
    private TextView currentquestion;
    String id;
    AlertDialog alertDialog;
    private ArrayList<Neetpg> quizList1;
    private TextView instructionguide;
    private TextView timerTextView;
    private int currentQuestionIndex = 0;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 210 * 60 * 1000; // 210 minutes in milliseconds
    private boolean timerRunning = true;
    private long remainingTimeInMillis;
    TextView title;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.neetexaminsideractivity);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        currentquestion = findViewById(R.id.currentquestion);
        recyclerView = findViewById(R.id.recycler_view1);
        title=findViewById(R.id.setnamewillbehere);
        quizList1 = new ArrayList<>();
        timerTextView = findViewById(R.id.timerTextView);


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
                        title.setText(Title);
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
                                    String question=(String) entry.get("Question");
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
                                        quizQuestion = new Neetpg(question, optionA, optionB, optionC, optionD, correctAnswer, null, description);
                                    }
                                    quizList1.add(quizQuestion);
                                }
                            }
                        }
                    }
                    adapter.setQuizQuestions(quizList1); // Set quiz questions to the adapter
                    loadNextQuestion();
                    startTimer();
                }
            }
        });


        TextView nextButton = findViewById(R.id.nextButton1);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNextQuestion();
                startTimer();
            }
        });
        TextView Back = findViewById(R.id.BackButtom);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPreviousQuestion();
                startTimer();
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(Neetexaminsider.this));
        adapter = new neetexampadapter(Neetexaminsider.this, quizList1);
        recyclerView.setAdapter(adapter);

        LinearLayout toTheBackLayout = findViewById(R.id.totheback1);
        toTheBackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });
        instructionguide = findViewById(R.id.instructionguide);
        instructionguide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInstructionDialog();
            }
        });
        adapter.setOnOptionSelectedListener(this);
    }

    private void loadPreviousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--; // Decrement the current question index
            Neetpg previousQuestion = quizList1.get(currentQuestionIndex);
            adapter.setQuizQuestions(Collections.singletonList(previousQuestion));

            if (previousQuestion.getRemainingTimeInMillis() != -1) {
                timeLeftInMillis = previousQuestion.getRemainingTimeInMillis();
                previousQuestion.setRemainingTimeInMillis(-1);
            }

            recyclerView.smoothScrollToPosition(currentQuestionIndex);
            updateQuestionNumber();
        } else {

            Toast.makeText(this, "This is the first question", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(Neetexaminsider.this, "Can't navigate back", Toast.LENGTH_SHORT).show();
    }

    private void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void loadNextQuestion() {
        if (currentQuestionIndex < quizList1.size()) {
            Neetpg currentQuestion = quizList1.get(currentQuestionIndex);
            adapter.setQuizQuestions(Collections.singletonList(currentQuestion));

            if (currentQuestion.getRemainingTimeInMillis() != -1) {
                timeLeftInMillis = currentQuestion.getRemainingTimeInMillis();
                currentQuestion.setRemainingTimeInMillis(-1);
            }

            recyclerView.smoothScrollToPosition(currentQuestionIndex);
            updateQuestionNumber();
            currentQuestionIndex++;
        } else {
            showEndQuizConfirmation();
        }
    }

    private void updateQuestionNumber() {
        int totalQuestions = quizList1.size();
        String questionNumberText =  (currentQuestionIndex + 1) + " / " + totalQuestions;
        Log.d("Currentquestion Number", String.valueOf(currentQuestionIndex+1));
        Log.d("Currentquestion Number",String.valueOf(totalQuestions));
        currentquestion.setText(questionNumberText);
    }


    private void showEndQuizConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Neetexaminsider.this, R.style.CustomAlertDialog);
        builder.setTitle("That's all");
        builder.setMessage("Would you like to proceed towards the result?");
        builder.setPositiveButton("Go to Result", new DialogInterface.OnClickListener() {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(Neetexaminsider.this, R.style.CustomAlertDialog);
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
        showConfirmationDialogForFinal();
    }

    private void proceedWithQuizEnd() {
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

        Intent intent = new Intent(Neetexaminsider.this, ResultActivityNeet.class);
        intent.putExtra("questions", quizList1);
        intent.putExtra("remainingTime", remainingTimeInMillis);
        intent.putExtra("id", id);
        Log.d("time left", String.valueOf(remainingTimeInMillis));
        startActivity(intent);
    }

    private void showConfirmationDialogForFinal() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Neetexaminsider.this, R.style.CustomAlertDialog);
        builder.setTitle("End Quiz");
        builder.setMessage("Are you sure you want to end the quiz?");

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                proceedWithQuizEnd();
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


    private void showCustomToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, findViewById(R.id.customToastLayout));

        TextView text = layout.findViewById(R.id.customToastText);
        text.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
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
            }
        }.start();
    }

    private void navigateToResultActivity() {
        Intent intent = new Intent(Neetexaminsider.this, ResultActivityNeet.class);
        intent.putExtra("questions", quizList1);
        intent.putExtra("remainingTime", remainingTimeInMillis);
        intent.putExtra("id", id);
        Log.d("time left", String.valueOf(remainingTimeInMillis));
        startActivity(intent);
        finish();
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

    @Override
    public void onOptionSelected(String selectedOption, int position) {
        if (selectedOption != null && !selectedOption.isEmpty()) {
            pauseTimer();
            boolean isOptionSelected = true;
        }
    }
}
