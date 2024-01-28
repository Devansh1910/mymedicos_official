package com.medical.my_medicos.activities.notification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.home.HomeActivity;
import com.medical.my_medicos.activities.news.NewsActivity;
import com.medical.my_medicos.databinding.ActivityNewsBinding;
import com.medical.my_medicos.databinding.ActivityNotificationBinding;

public class NotificationActivity extends AppCompatActivity {
    private ImageView backtothehomefromnotification;
    ActivityNotificationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        backtothehomefromnotification = findViewById(R.id.backtothehomefromnotification);
        backtothehomefromnotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NotificationActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });
    }
}