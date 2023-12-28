package com.medical.my_medicos.activities.pg.animations;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.pg.activites.PgprepActivity;

public class WrongAnswerActivity extends AppCompatActivity {

    LottieAnimationView wrong;
    TextView wrongOptionTextView;
    TextView descriptionTextView;

    LinearLayout completeend;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrong_answer);

        wrongOptionTextView = findViewById(R.id.wrongforperdayquestion);
        descriptionTextView = findViewById(R.id.descriptionforperdayquestion);

        Intent intent = getIntent();
        String correctOption = intent.getStringExtra("correctOption");
        String description = intent.getStringExtra("description");

        wrongOptionTextView.setText(correctOption + " is the Correct Option" );
        descriptionTextView.setText(description);

        completeend = findViewById(R.id.complete);
        completeend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(WrongAnswerActivity.this, PgprepActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });


    }
}