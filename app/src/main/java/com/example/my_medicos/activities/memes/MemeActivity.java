package com.example.my_medicos.activities.memes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.VideoView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.my_medicos.R;
import com.example.my_medicos.activities.job.category.JobsApplyActivity2;
import com.example.my_medicos.activities.job.category.JobsPostedYou;


public class MemeActivity extends AppCompatActivity {

    LottieAnimationView underconstructionanim;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meme);

        toolbar = findViewById(R.id.memestoolbar);
        setSupportActionBar(toolbar);
//        tab=findViewById(R.id.tabLayout);
//        viewPager=findViewById(R.id.view_pager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        underconstructionanim = findViewById(R.id.underconstructionanim);

    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}