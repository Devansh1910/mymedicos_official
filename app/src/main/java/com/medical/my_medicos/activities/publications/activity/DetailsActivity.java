package com.medical.my_medicos.activities.publications.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.medical.my_medicos.databinding.ActivityDetailsBinding;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailsActivity extends AppCompatActivity {
    ActivityDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String productId = getIntent().getStringExtra("productId");
        if (productId != null) {
            fetchProductDetailsAndShowPdf(productId);
        } else {
            Toast.makeText(this, "Product ID is missing", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchProductDetailsAndShowPdf(String productId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Publications").document(productId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Assuming the document contains a field named "URL" for the PDF
                    String url = document.getString("URL");
                    if (url != null) {
                        ShowPdf(url);
                    } else {
                        Toast.makeText(DetailsActivity.this, "PDF URL is missing", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DetailsActivity.this, "No such document", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(DetailsActivity.this, "Failed to load product details", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void ShowPdf(String URL) {
        if (URL == null || URL.isEmpty()) {
            Toast.makeText(this, "Invalid document URL", Toast.LENGTH_SHORT).show();
            return;
        }

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(URL).build();

        binding.progress.setVisibility(View.VISIBLE); // Show progress bar when loading starts

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> {
                    binding.progress.setVisibility(View.GONE);
                    Toast.makeText(DetailsActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    runOnUiThread(() -> {
                        binding.progress.setVisibility(View.GONE);
                        Toast.makeText(DetailsActivity.this, "Failed to load document", Toast.LENGTH_SHORT).show();
                    });
                    return;
                }
                InputStream inputStream = response.body().byteStream();
                runOnUiThread(() -> {
                    binding.pdfView.fromStream(inputStream)
                            .onLoad(nbPages -> binding.progress.setVisibility(View.GONE))
                            .load();
                });
            }
        });
    }
}