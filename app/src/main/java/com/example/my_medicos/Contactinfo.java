package com.example.my_medicos;

import static com.example.my_medicos.R.id.others;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.my_medicos.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Contactinfo extends AppCompatActivity {
    private EditText email,phone,Present,Permanent,Age;
    String permanent,present,age;

    private Spinner location, speciality, subspeciality;
    String selectedGender;
    Button Submit;
    private ArrayAdapter<CharSequence> locationAdapter, specialityAdapter, subspecialityAdapter;
    private CircleImageView avatarImageView;
    RadioButton rb;
    private TextView uploadAvatarCardView;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;
    FirebaseFirestore dc=FirebaseFirestore.getInstance();
    HashMap<String, Object> usermap = new HashMap<>();

    String userid;

    @SuppressLint({"NonConstantResourceId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contactinfo);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();





        location = findViewById(R.id.location);
        speciality = findViewById(R.id.speciality);
        subspeciality = findViewById(R.id.subspeciality);
        avatarImageView = findViewById(R.id.avatarImageView);
        uploadAvatarCardView = findViewById(R.id.upload_avatar);
        email=findViewById(R.id.email1);
        phone=findViewById(R.id.phonenumber);
        email.setEnabled(false);
        email.setTextColor(Color.parseColor("#80000000"));
        email.setBackgroundResource(R.drawable.rounded_edittext_background);
        phone.setEnabled(false);
        phone.setTextColor(Color.parseColor("#80000000"));
        phone.setBackgroundResource(R.drawable.rounded_edittext_background);





        userid=currentUser.getEmail();
        email.setText(userid);
        dc.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> dataMap = document.getData();
                        String field3 = ((String) dataMap.get("Email ID"));

                        int r=userid.compareTo(field3);
                        if (r==0){
                            String field4 = ((String) dataMap.get("Phone Number"));
                            Log.d("veefe",field4);
                            phone.setText(field4);
                        }


                        // Handle the retrieved data here

                        // You can access data using document.getData() and perform necessary actions
                    }
                } else {
                    // Handle the error
                    Toast.makeText(Contactinfo.this, "Error fetching data from Firebase Firestore", Toast.LENGTH_SHORT).show();
                }
            }
        });




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
                String selectedMode = location.getSelectedItem().toString();
                usermap.put("location",selectedMode);

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
                String Speciality=speciality.getSelectedItem().toString();
                usermap.put("speciality",Speciality);
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
                String Subspeciality=subspeciality.getSelectedItem().toString();
                usermap.put("subspeciality",Subspeciality);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });



        RadioGroup radioGroup = findViewById(R.id.radiogrp);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                RadioButton selectedRadioButton = findViewById(checkedId);
                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);

                if (selectedRadioButton != null) {
                    String selectedText = selectedRadioButton.getText().toString();

//                    String selectedText = selectedRadioButton.getText().toString();
                    // Log the selected text to the console or show it in a Toast message
                    Log.d("RadioButton"+selectedRadioButtonId, "Selected Text: " + selectedText);
                    selectedGender=selectedText;
                    // Or use Toast.makeText to display a message
                    // Toast.makeText(YourActivity.this, "Selected Text: " + selectedText, Toast.LENGTH_SHORT).show();
                }
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
        Submit=findViewById(R.id.consubmit);
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    postinfo();
                }
            }
        });
        Present=findViewById(R.id.present1);
        Permanent=findViewById(R.id.permanent1);
        Age=findViewById(R.id.age1);
        present=Present.getText().toString().trim();
        permanent=Permanent.getText().toString().trim();

        CheckBox sameAddressCheckBox = findViewById(R.id.checkbox1);
        sameAddressCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // If the checkbox is checked, copy the content from present address to permanent address
                    String presentAddress = Present.getText().toString();
                    Permanent.setText(presentAddress);
                } else {
                    // Clear the permanent address EditText if the checkbox is unchecked
                    Permanent.setText("");
                }
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
    private void postinfo(){
        age=Age.getText().toString().trim();
        present=Present.getText().toString().trim();
        permanent=Permanent.getText().toString().trim();


        usermap.put("Gender",selectedGender);

        Log.d("present",present);
        usermap.put("present",present);
        usermap.put("permanent",permanent);
        usermap.put("Age",age);
        if (TextUtils.isEmpty(present)) {
            Present.setError("Present Address Required");
            return;
        }
        if (TextUtils.isEmpty(permanent)) {
            Permanent.setError("Permanent Address Required");
            return;
        }
        if (TextUtils.isEmpty(age)) {
            Age.setError("Age Required");
            return;
        }

        Query query = dc.collection("users")
                .whereEqualTo("Email ID", userid);

        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                            // You found a document that matches your criteria
                            String documentId = documentSnapshot.getId();
                            // Now you can update or add a new field to this document

                            DocumentReference docRef = documentSnapshot.getReference();

                            // Update or add a new field to the document
                            docRef.update(usermap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Document updated successfully
                                            Toast.makeText(Contactinfo.this, "Posted Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Handle the failure to update the document
                                            Toast.makeText(Contactinfo.this, " Not Posted ", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors that occurred while querying
                        Toast.makeText(Contactinfo.this, " Not Posted ", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
