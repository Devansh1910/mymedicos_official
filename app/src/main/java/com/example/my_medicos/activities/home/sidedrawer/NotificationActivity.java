package com.example.my_medicos.activities.home.sidedrawer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.my_medicos.R;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}