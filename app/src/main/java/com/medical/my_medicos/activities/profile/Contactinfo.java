package com.medical.my_medicos.activities.profile;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.medical.my_medicos.R;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Contactinfo extends AppCompatActivity {
    private EditText email, phone;
    EditText presentaddress, permanentaddress, agedr;
    private Spinner location, speciality;
    String selectedGender;
    String selectedMode;
    Button Submit;
    private ArrayAdapter<CharSequence> locationAdapter, specialityAdapter;
    private CircleImageView avatarImageView;
    RadioButton rb;
    private TextView uploadAvatarCardView;
    String documentid;
    private FirebaseAuth mAuth;
    public FirebaseDatabase profiledb = FirebaseDatabase.getInstance();
    public DatabaseReference userref = profiledb.getReference().child("users");
    private StorageReference mStorageRef;
    FirebaseFirestore dc = FirebaseFirestore.getInstance();
    HashMap<String, Object> usermap = new HashMap<>();
    String userid;
    String currentaddress;
    String fulladdress ;
    String drage ;

    Toolbar toolbar;

    private ProgressDialog pdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactinfo);

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

        toolbar = findViewById(R.id.contactsstoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        location = findViewById(R.id.location);
//        speciality = findViewById(R.id.speciality);
        avatarImageView = findViewById(R.id.avatarImageView);
        uploadAvatarCardView = findViewById(R.id.upload_avatar);

        email = findViewById(R.id.email1);
        phone = findViewById(R.id.phonenumber);

        email.setEnabled(false);
        email.setTextColor(Color.parseColor("#80000000"));
        email.setBackgroundResource(R.drawable.rounded_edittext_background);

        phone.setEnabled(false);
        phone.setTextColor(Color.parseColor("#80000000"));
        phone.setBackgroundResource(R.drawable.rounded_edittext_background);

        userid = currentUser.getPhoneNumber();
        email.setText(userid);

        dc.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> dataMap = document.getData();
                        String field3 = ((String) dataMap.get("Phone Number"));


                        if (field3 != null && userid != null) {
                            int r = userid.compareTo(field3);
                            documentid=document.getId();
                            Log.d("documentid",documentid);

                            if (r == 0) {
                                String field4 = ((String) dataMap.get("Email ID"));
                                Log.d("Something went wrong", field4);
                                phone.setText(field4);
                                break;
                            }
                        } else {
                            Log.e(TAG, "Field3 or userid is null");
                        }
                    }
                } else {
                    Toast.makeText(Contactinfo.this, "Error fetching data from Firebase Firestore", Toast.LENGTH_SHORT).show();
                }
            }
        });

        userref = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getPhoneNumber());
        mStorageRef = FirebaseStorage.getInstance().getReference().child("users").child(currentUser.getPhoneNumber());

        presentaddress = findViewById(R.id.present1);
        permanentaddress = findViewById(R.id.permanent1);
        agedr = findViewById(R.id.age1);

        locationAdapter = ArrayAdapter.createFromResource(this,
                R.array.indian_cities, android.R.layout.simple_spinner_item);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        location.setAdapter(locationAdapter);

        location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                int locationIndex = location.getSelectedItemPosition();
              selectedMode = location.getSelectedItem().toString();
                usermap.put("location", selectedMode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        RadioGroup radioGroup = findViewById(R.id.radiogrp);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);

                if (selectedRadioButton != null) {
                    String selectedText = selectedRadioButton.getText().toString();

                    Log.d("RadioButton"+selectedRadioButtonId, "Selected Text: " + selectedText);
                    selectedGender=selectedText;
                }
            }
        });

        Button conSubmitButton = findViewById(R.id.upload_avatar);
        conSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        Submit=findViewById(R.id.consubmit);

         currentaddress = presentaddress.getText().toString().trim();
        fulladdress = permanentaddress.getText().toString().trim();
         drage = agedr.getText().toString().trim();
        CheckBox sameAddressCheckBox = findViewById(R.id.checkbox1);
        sameAddressCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    String presentAddress = presentaddress.getText().toString();
                    permanentaddress.setText(presentAddress);
                } else {
                    permanentaddress.setText("");
                }
            }
        });

        dc.collection("users")
                .whereEqualTo("Phone Number", userid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> dataMap = document.getData();

                                String presentAddress = (String) dataMap.get("present");
                                String permanentAddress = (String) dataMap.get("permanent");
                                String age = (String) dataMap.get("Age");

                                presentaddress.setText(presentAddress);
                                permanentaddress.setText(permanentAddress);
                                agedr.setText(age);

                            }
                        } else {
                            // Handle the error
                            Toast.makeText(Contactinfo.this, "Error fetching data from Firebase Firestore", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                postinfo();

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
            Uri imageUri = data.getData();
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            StorageReference imageRef = mStorageRef.child("profile_image.jpg");
            imageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    userref.child("profileImage").setValue(imageUrl);
                    progressDialog.dismiss();
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Contactinfo.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();
                        }
                    }, 1000);
                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                });
            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
            });
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                avatarImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void postinfo() {
        currentaddress = presentaddress.getText().toString().trim();
        fulladdress= permanentaddress.getText().toString().trim();

        drage = agedr.getText().toString().trim();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.d("Something went wrong ",currentaddress);
        Log.d("Something went wrong",documentid);

        DocumentReference docRef = db.collection("users").document(documentid);

        docRef.update("present", currentaddress,
                        "permanent", fulladdress,
                        "Age", drage,
                        "Location",selectedMode)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(ContentValues.TAG, "DocumentSnapshot successfully updated!");
                        Toast.makeText(Contactinfo.this, "Successfully Ended", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(ContentValues.TAG, "Error updating document", e);
                    }
                });
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}