package com.medical.my_medicos.activities.pg.activites;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.pg.adapters.ResultReportNeetAdapter;
import com.medical.my_medicos.activities.pg.model.Neetpg;
import com.medical.my_medicos.activities.pg.model.QuizPGinsider;

import java.util.ArrayList;

public class ResultActivityNeet extends AppCompatActivity {

    private RecyclerView resultRecyclerView;
    private ResultReportNeetAdapter resultAdapter;
    private TextView correctAnswersTextView;
    private TextView totalQuestionsTextView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultRecyclerView = findViewById(R.id.resultRecyclerView);
//        correctAnswersTextView = findViewById(R.id.correctAnswersTextView);
//        totalQuestionsTextView = findViewById(R.id.totalQuestionsTextView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        resultRecyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        ArrayList<Neetpg> questions = (ArrayList<Neetpg>) intent.getSerializableExtra("questions");

        resultAdapter = new ResultReportNeetAdapter(this, questions);
        resultRecyclerView.setAdapter(resultAdapter);

        // Calculate and display the number of correct answers and total questions
        int correctAnswers = calculateCorrectAnswers(questions);
        int totalQuestions = questions.size();
        Log.d("Correct Answer", String.valueOf(correctAnswers));
        Log.d("Correct Answer", String.valueOf(totalQuestions));
//
//        correctAnswersTextView.setText("Correct Answers: " + correctAnswers);
//        totalQuestionsTextView.setText("Total Questions: " + totalQuestions);
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
}
