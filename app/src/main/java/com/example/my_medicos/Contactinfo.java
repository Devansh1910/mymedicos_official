package com.example.my_medicos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.my_medicos.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class Contactinfo extends AppCompatActivity {

    private Spinner location, speciality, subspeciality;
    private ArrayAdapter<CharSequence> locationAdapter, specialityAdapter, subspecialityAdapter;
    private CircleImageView avatarImageView;
    private TextView uploadAvatarCardView;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactinfo);

        location = findViewById(R.id.location);
        speciality = findViewById(R.id.speciality);
        subspeciality = findViewById(R.id.subspeciality);
        avatarImageView = findViewById(R.id.avatarImageView);
        uploadAvatarCardView = findViewById(R.id.upload_avatar);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
        mStorageRef = FirebaseStorage.getInstance().getReference().child("users").child(currentUser.getUid());

        locationAdapter = ArrayAdapter.createFromResource(this,
                R.array.indian_cities, android.R.layout.simple_spinner_item);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        location.setAdapter(locationAdapter);

        // Initially, hide the subspeciality spinner
        location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                // Check if the selected speciality has subspecialities
                int locationIndex = location.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });

        specialityAdapter = ArrayAdapter.createFromResource(this,
                R.array.speciality, android.R.layout.simple_spinner_item);
        specialityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        speciality.setAdapter(specialityAdapter);

        // Initially, hide the subspeciality spinner
        speciality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                // Check if the selected speciality has subspecialities
                int locationIndex = speciality.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });

        subspecialityAdapter = ArrayAdapter.createFromResource(this,
                R.array.subspeciality, android.R.layout.simple_spinner_item);
        subspecialityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subspeciality.setAdapter(subspecialityAdapter);

        // Initially, hide the subspeciality spinner
        subspeciality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                // Check if the selected speciality has subspecialities
                int locationIndex = subspeciality.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });

        // Set an OnClickListener for the "consubmit" button
        Button conSubmitButton = findViewById(R.id.upload_avatar);
        conSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            // Get the selected image's URI
            Uri imageUri = data.getData();

            // Show a progress dialog
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            // Upload the image to Firebase Storage
            StorageReference imageRef = mStorageRef.child("profile_image.jpg");
            imageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                // Image uploaded successfully, get the download URL
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();

                    // Save the image URL in the Firebase Realtime Database
                    mDatabaseRef.child("profileImage").setValue(imageUrl);

                    // Dismiss the progress dialog
                    progressDialog.dismiss();

                    // Show a "Uploaded successfully" toast message
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Contactinfo.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();
                        }
                    }, 1000); // Show for 1 second
                }).addOnFailureListener(e -> {
                    // Handle any errors
                    progressDialog.dismiss();
                });
            }).addOnFailureListener(e -> {
                // Handle any errors
                progressDialog.dismiss();
            });

            try {
                // Load the selected image into the ImageView
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                avatarImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
