package com.medical.my_medicos.activities.pg.activites;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.pg.model.QuizPGinsider;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultTextView = findViewById(R.id.resultTextView);

        Intent intent = getIntent();
        ArrayList<QuizPGinsider> questions = (ArrayList<QuizPGinsider>) intent.getSerializableExtra("questions");

        displayResults(questions);
    }

    private void displayResults(ArrayList<QuizPGinsider> quizList) {
        StringBuilder resultBuilder = new StringBuilder();

        for (QuizPGinsider quizQuestion : quizList) {
            String questionText = quizQuestion.getQuestion();
            String correctAnswer = quizQuestion.getCorrectAnswer();
            String selectedOption = quizQuestion.getSelectedOption();

            boolean isCorrect = correctAnswer.equals(selectedOption);

            resultBuilder.append("Question: ").append(questionText).append("\n");
            resultBuilder.append("Correct Answer: ").append(correctAnswer).append("\n");
            resultBuilder.append("Selected Option: ").append(selectedOption).append("\n");
            resultBuilder.append("Result: ").append(isCorrect ? "Correct" : "Wrong").append("\n\n");
        }

        resultTextView.setText(resultBuilder.toString());
    }
}