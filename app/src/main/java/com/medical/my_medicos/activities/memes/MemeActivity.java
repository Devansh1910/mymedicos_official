package com.medical.my_medicos.activities.memes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;

import com.airbnb.lottie.LottieAnimationView;
import com.medical.my_medicos.R;


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