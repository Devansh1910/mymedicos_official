package com.example.my_medicos.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.my_medicos.R;
import com.example.my_medicos.activities.home.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;

public class GetstartedActivity extends AppCompatActivity {

    Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isLoggedIn()) {
            Intent intent = new Intent(GetstartedActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
            return;
}
        setContentView(R.layout.activity_getstarted);
        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GetstartedActivity.this, NavigationActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private boolean isLoggedIn() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        return mAuth.getCurrentUser() != null;

    }
}