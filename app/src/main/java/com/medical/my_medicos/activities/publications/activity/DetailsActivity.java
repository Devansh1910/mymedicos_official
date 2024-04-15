package com.medical.my_medicos.activities.publications.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.medical.my_medicos.R;
import com.medical.my_medicos.databinding.ActivityDetailsBinding;
import com.shockwave.pdfium.PdfDocument;
import java.io.File;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;

public class DetailsActivity extends AppCompatActivity {
    private ActivityDetailsBinding binding;
    private static final int STORAGE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
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

        if (checkStoragePermission()) {
            // Permissions are already granted, fetch the PDF
            fetchPdf();
        } else {
            // Request storage permissions
            requestStoragePermission();
        }
    }

    private void fetchPdf() {
        String productId = getIntent().getStringExtra("id");
        if (productId != null) {
            fetchProductDetailsAndShowPdf(productId);
        } else {
            Toast.makeText(this, "Product ID is missing", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchProductDetailsAndShowPdf(String productId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Publications").document(productId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String url = document.getString("URL");
                    if (url != null) {
                        downloadAndDisplayPdf(url);
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

    private void downloadAndDisplayPdf(final String pdfUrl) {
        final File pdfFile = new File(getExternalFilesDir(null), "downloadedPdf.pdf");

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(pdfUrl).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(DetailsActivity.this, "Failed to download file", Toast.LENGTH_SHORT).show();
                    binding.progress.setVisibility(View.GONE); // Hide progress bar on failure
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                // Assuming you have access to response headers, check Content-Length if available
                long contentLength = -1;
                String contentLengthHeader = response.header("Content-Length");
                if (contentLengthHeader != null) {
                    contentLength = Long.parseLong(contentLengthHeader);
                }

                File file = new File(getExternalFilesDir(null), "downloadedPdf.pdf");
                BufferedSink sink = Okio.buffer(Okio.sink(file));
                sink.writeAll(response.body().source());
                sink.close();

                if (file.length() != contentLength && contentLength != -1) {
                    // This means the file size does not match the expected size
                    Log.e("PDF Download", "File size does not match expected content length.");
                    // Handle error - might want to retry download or notify user
                } else {
                    // Proceed to display PDF
                    runOnUiThread(() -> {
                        binding.pdfView.fromFile(file).load();
                        binding.progress.setVisibility(View.GONE);
                    });
                }
            }

        });
    }


    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchPdf();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
