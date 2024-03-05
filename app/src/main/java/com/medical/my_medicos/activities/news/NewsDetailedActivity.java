package com.medical.my_medicos.activities.news;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.medical.my_medicos.R;
import com.medical.my_medicos.databinding.ActivityNewsDetailedBinding;

public class NewsDetailedActivity extends AppCompatActivity {

    ActivityNewsDetailedBinding binding;
    News currentNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.backgroundcolor));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.backgroundcolor));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }

        String name = getIntent().getStringExtra("Title");
        String image = getIntent().getStringExtra("thumbnail");
        String status = getIntent().getStringExtra("Description");
        String time = getIntent().getStringExtra("Time");
        String url = getIntent().getStringExtra("URL"); // Add this line to retrieve URL

        Glide.with(this)
                .load(image)
                .into(binding.newsthumbnail);

        binding.newsDescription.setText(status);
        binding.newsTitle.setText(name);
        binding.newsTime.setText(time);

        // Set up the "Read More" button click listener
        binding.readmoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the URL in a web browser
                openUrlInBrowser(url);
            }
        });


        // Enable the back button in the toolbar
        setSupportActionBar(binding.newsstoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getNewsDetails(name);
    }

    void getNewsDetails(String name) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("MedicalNews")
                .document(name)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String description = documentSnapshot.getString("Description");
                        String time = documentSnapshot.getString("Time");

                        binding.newsDescription.setText(Html.fromHtml(description));
                        binding.newsTime.setText(Html.fromHtml(time));

                        currentNews = new News(
                                documentSnapshot.getString("Title"),
                                documentSnapshot.getString("thumbnail"),
                                documentSnapshot.getString("Description"),
                                documentSnapshot.getString("Time"),
                                documentSnapshot.getString("URL"),
                                documentSnapshot.getString("type")
                        );
                    } else {
                        // Handle the case where the document does not exist
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                });
    }

    // Method to open a URL in a web browser
    private void openUrlInBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    // Handle back button click
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
