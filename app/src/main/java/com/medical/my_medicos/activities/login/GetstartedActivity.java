package com.medical.my_medicos.activities.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.home.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;

public class GetstartedActivity extends AppCompatActivity {

    TextView startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.getstartedbottom));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.getstartedbottom));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }

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