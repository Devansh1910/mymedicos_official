package com.medical.my_medicos.activities.news;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.medical.my_medicos.databinding.ActivityNewsDetailedBinding;

public class NewsDetailedActivity extends AppCompatActivity {

    ActivityNewsDetailedBinding binding;
    News currentNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String name = getIntent().getStringExtra("Title");
        String image = getIntent().getStringExtra("thumbnail");
        String status = getIntent().getStringExtra("Description");
        String time = getIntent().getStringExtra("Time");
        String url = getIntent().getStringExtra("URL"); // Add this line to retrieve URL

        Glide.with(this)
                .load(image)
                .into(binding.newsthumbnail);

        binding.newsDescription.setText(status);
        binding.newsstoolbar.setTitle(name);

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

                        getSupportActionBar().setTitle(documentSnapshot.getString("Title"));

                        binding.newsstoolbar.setTitle(documentSnapshot.getString("Title"));
                        binding.newsDescription.setText(Html.fromHtml(description));

                        currentNews = new News(
                                documentSnapshot.getString("Title"),
                                documentSnapshot.getString("thumbnail"),
                                documentSnapshot.getString("Description"),
                                documentSnapshot.getString("Time"),
                                documentSnapshot.getString("URL"),
                                documentSnapshot.getString("type")

                        );
                    } else {
                    }
                })
                .addOnFailureListener(e -> {
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
