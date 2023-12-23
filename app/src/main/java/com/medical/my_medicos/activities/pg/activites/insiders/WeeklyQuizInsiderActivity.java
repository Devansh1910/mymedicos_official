package com.medical.my_medicos.activities.pg.activites.insiders;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;
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
import com.medical.my_medicos.activities.pg.adapters.WeeklyQuizAdapterinsider;
import com.medical.my_medicos.activities.pg.model.QuizPGinsider;
import java.util.ArrayList;
import java.util.Map;

public class WeeklyQuizInsiderActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WeeklyQuizAdapterinsider adapter;
    private ArrayList<QuizPGinsider> quizList;
    private CountDownTimer countDownTimer;
    private TextView textViewTimer;

    String idQuestion;
    String titleOfSet ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_quiz_insider);

        recyclerView = findViewById(R.id.recycler_view);
        textViewTimer = findViewById(R.id.textViewTimer);

        quizList = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference quizzCollection = db.collection("PGupload").document("Weekley").collection("Quiz");

        quizzCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ArrayList<Map<String, Object>> dataList = (ArrayList<Map<String, Object>>) document.get("Data");

                        if (dataList != null) {
                            for (Map<String, Object> entry : dataList) {
                                String question = (String) entry.get("Question");
                                String correctAnswer = (String) entry.get("Correct");
                                String optionA = (String) entry.get("A");
                                String optionB = (String) entry.get("B");
                                String optionC = (String) entry.get("C");
                                String optionD = (String) entry.get("D");
                                String description = (String) entry.get("Desciption");
                                QuizPGinsider quizQuestion = new QuizPGinsider(question, optionA, optionB, optionC, optionD, correctAnswer, idQuestion, titleOfSet, description);
                                quizList.add(quizQuestion);
                            }
                        }
                    }

                    recyclerView.setLayoutManager(new LinearLayoutManager(WeeklyQuizInsiderActivity.this));
                    adapter = new WeeklyQuizAdapterinsider(WeeklyQuizInsiderActivity.this, quizList);
                    recyclerView.setAdapter(adapter);
                    startTimer();
                }
            }
        });
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(600000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Update the UI with the remaining time
                updateTimerUI(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                // Handle what should happen when the timer finishes
                // For example, you can finish the activity or show a message
                // when the timer reaches 0.
                // finish(); // Uncomment this line if you want to finish the activity
            }
        }.start();
    }

    private void updateTimerUI(long millisUntilFinished) {
        long seconds = millisUntilFinished / 1000;
        long minutes = seconds / 60;
        seconds %= 60;

        String timerText = String.format("%02d:%02d", minutes, seconds);
        textViewTimer.setText("Time remaining: " + timerText);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
