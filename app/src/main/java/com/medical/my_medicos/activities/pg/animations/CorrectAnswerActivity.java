package com.medical.my_medicos.activities.pg.animations;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.home.HomeActivity;
import com.medical.my_medicos.activities.home.sidedrawer.NotificationActivity;
import com.medical.my_medicos.activities.pg.activites.PgprepActivity;

public class CorrectAnswerActivity extends AppCompatActivity {

    LottieAnimationView congratsanim, done,continuetopgbtn;

    TextView correctOptionTextView;

    LinearLayout complete;
    TextView descriptionTextView;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correct_answer);

        congratsanim = findViewById(R.id.correctanswer);
        done = findViewById(R.id.doneanime);
        continuetopgbtn = findViewById(R.id.continueBtn);

        correctOptionTextView = findViewById(R.id.correctforperdayquestion);
        descriptionTextView = findViewById(R.id.descriptionforperdayquestion);

        Intent intent = getIntent();
        String correctOption = intent.getStringExtra("correctOption");
        String description = intent.getStringExtra("description");

        correctOptionTextView.setText(correctOption + " is the Correct Option" );
        descriptionTextView.setText(description);

        complete = findViewById(R.id.complete);
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CorrectAnswerActivity.this, PgprepActivity.class);
                // Add the FLAG_ACTIVITY_CLEAR_TOP flag
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                // Finish the current activity
                finish();
            }
        });


    }
}
