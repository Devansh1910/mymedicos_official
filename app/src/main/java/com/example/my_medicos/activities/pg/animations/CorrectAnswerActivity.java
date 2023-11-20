package com.example.my_medicos.activities.pg.animations;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.airbnb.lottie.LottieAnimationView;
import com.example.my_medicos.R;
import com.example.my_medicos.activities.home.HomeActivity;
import com.example.my_medicos.activities.pg.activites.PgprepActivity;

public class CorrectAnswerActivity extends AppCompatActivity {

    LottieAnimationView congratsanim, done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correct_answer);

        congratsanim = findViewById(R.id.correctanswer);
        done = findViewById(R.id.doneanime);


        congratsanim.animate().translationY(0).setDuration(2000).setStartDelay(0);
        done.animate().translationY(0).setDuration(2000).setStartDelay(0);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(), PgprepActivity.class);
                startActivity(i);
                finish(); // Close this activity to prevent it from being in the back stack
            }
        }, 1800);
    }
}