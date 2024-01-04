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
import com.medical.my_medicos.activities.pg.adapters.ResultReportAdapter;
import com.medical.my_medicos.activities.pg.model.QuizPGinsider;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    private RecyclerView resultRecyclerView;
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

        Intent intent = getIntent();
        ArrayList<QuizPGinsider> questions = (ArrayList<QuizPGinsider>) intent.getSerializableExtra("questions");

        resultAdapter = new ResultReportAdapter(this, questions);
        resultRecyclerView.setAdapter(resultAdapter);

        int correctAnswers = calculateCorrectAnswers(questions);
        int totalQuestions = questions.size();
        Log.d("Correct Answer", String.valueOf(correctAnswers));
        Log.d("Correct Answer", String.valueOf(totalQuestions));


        correctAnswersTextView.setText("Correct Answers: " + correctAnswers);
        totalQuestionsTextView.setText("Total Questions: " + totalQuestions);
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
}
