package com.medical.my_medicos.activities.publications.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.medical.my_medicos.R;
import com.medical.my_medicos.databinding.ActivityDetailsBinding;
import com.github.barteksc.pdfviewer.PDFView;
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
    private PDFView pdfView;
    private static final int STORAGE_PERMISSION_CODE = 1;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pdfView = findViewById(R.id.pdfView); // Assuming you have a PDFView in your layout with id pdfView
        sharedPreferences = getSharedPreferences("Permissions", MODE_PRIVATE);

        // Check and request storage permissions if not granted previously
        if (!hasStoragePermission() && !isStoragePermissionDeniedForever()) {
            requestStoragePermissions();
        } else {
            fetchPdf();
        }
    }

    private boolean hasStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isStoragePermissionDeniedForever() {
        return sharedPreferences.getBoolean("storage_permission_denied_forever", false);
    }

    private void requestStoragePermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with fetching PDF
                fetchPdf();
            } else {
                // Permission denied, show a message or take necessary action
                boolean shouldShowRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE);
                if (!shouldShowRationale) {
                    // User has permanently denied the permission
                    sharedPreferences.edit().putBoolean("storage_permission_denied_forever", true).apply();
                }
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
            }
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
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(pdfUrl).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(DetailsActivity.this, "Failed to download file", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(DetailsActivity.this, "Failed to download file: " + response.message(), Toast.LENGTH_SHORT).show();
                    });
                    return;
                }

                File file = new File(getExternalFilesDir(null), "downloadedPdf.pdf");
                BufferedSink sink = Okio.buffer(Okio.sink(file));
                try {
                    sink.writeAll(response.body().source());
                    sink.close();

                    runOnUiThread(() -> {
                        pdfView.fromFile(file).load();
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        Toast.makeText(DetailsActivity.this, "Failed to save file", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }
}
