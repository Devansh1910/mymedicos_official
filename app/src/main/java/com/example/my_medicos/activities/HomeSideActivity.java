package com.example.my_medicos.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.my_medicos.R;
import com.example.my_medicos.activities.profile.Contactinfo;
import com.example.my_medicos.activities.profile.Personalinfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Map;
import java.util.prefs.Preferences;

public class HomeSideActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private static final String DATA_LOADED_KEY = "data_loaded";

    CardView personalinfo, contactinfo;
    TextView user_name_dr, user_email_dr, user_phone_dr, user_location_dr, user_interest_dr, user_prefix;
    ImageView profileImageView, verifiedprofilebehere;
    FrameLayout verifiedUser, circularImageView;

    Toolbar toolbar;

    private boolean dataLoaded = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_side);

        toolbar = findViewById(R.id.profiletoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState != null) {
            dataLoaded = savedInstanceState.getBoolean(DATA_LOADED_KEY, false);
        }

        personalinfo = findViewById(R.id.personal_info);
        personalinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeSideActivity.this, Personalinfo.class);
                startActivity(i);
            }
        });

        contactinfo = findViewById(R.id.contact_info);
        contactinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeSideActivity.this, Contactinfo.class);
                startActivity(i);
            }
        });

        user_name_dr = findViewById(R.id.user_name_dr);
        user_email_dr = findViewById(R.id.user_email_dr);
        user_phone_dr = findViewById(R.id.user_phone_dr);
        user_location_dr = findViewById(R.id.user_location_dr);
        user_interest_dr = findViewById(R.id.user_interest_dr);
        profileImageView = findViewById(R.id.circularImageView);
        user_prefix = findViewById(R.id.prefixselecterfromuser);
        verifiedprofilebehere = findViewById(R.id.verifiedprofilebehere);
        verifiedUser = findViewById(R.id.verifieduser);

        if (!dataLoaded) {
            fetchdata();
            fetchUserData();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(DATA_LOADED_KEY, dataLoaded);
    }

    private void fetchdata() {
        Preferences preferences = Preferences.userRoot();
        if (preferences.get("username", null) != null) {
            System.out.println("Key '" + "username" + "' exists in preferences.");
            String username = preferences.get("username", null);
            Log.d("usernaem", username);
        }
        String username = preferences.get("username", "");
        String email = preferences.get("email", "");
        String location = preferences.get("location", "");
        String interest = preferences.get("interest", "");
        String phone = preferences.get("userphone", "");
        String prefix = preferences.get("prefix", "");
        user_name_dr.setText(username);
        user_email_dr.setText(email);
        user_location_dr.setText(location);
        user_interest_dr.setText(interest);
        user_phone_dr.setText(phone);
        user_prefix.setText(prefix);
    }

    private void fetchUserData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getPhoneNumber();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    String docID = document.getId();
                                    Map<String, Object> dataMap = document.getData();
                                    String field1 = (String) dataMap.get("Phone Number");

                                    if (field1 != null && currentUser.getPhoneNumber() != null) {
                                        int a = field1.compareTo(currentUser.getPhoneNumber());
                                        if (a == 0) {
                                            String userName = (String) dataMap.get("Name");
                                            String userEmail = (String) dataMap.get("Email ID");
                                            String userLocation = (String) dataMap.get("Location");
                                            String userInterest = (String) dataMap.get("Interest");
                                            String userPhone = (String) dataMap.get("Phone Number");
                                            String userPrefix = (String) dataMap.get("Prefix");
                                            String userAuthorized = (String) dataMap.get("authorized");

                                            Boolean mcnVerified = (Boolean) dataMap.get("MCN verified");

                                            Preferences preferences = Preferences.userRoot();
                                            preferences.put("username", userName);
                                            preferences.put("email", userEmail);
                                            preferences.put("location", userLocation);
                                            preferences.put("interest", userInterest);
                                            preferences.put("userphone", userPhone);
                                            preferences.put("docId", docID);
                                            preferences.put("prefix", userPrefix);
                                            user_name_dr.setText(userName);
                                            user_phone_dr.setText(userPhone);

                                            if (mcnVerified != null && mcnVerified) {
                                                // Boolean value is true
                                                preferences.putBoolean("mcn_verified", true);
                                                verifiedUser.setVisibility(View.VISIBLE);
                                                profileImageView.setVisibility(View.GONE);
                                                fetchUserProfileImageVerified(userId);
                                            } else {
                                                // Boolean value is false or null
                                                preferences.putBoolean("mcn_verified", false);
                                                verifiedUser.setVisibility(View.GONE);
                                                profileImageView.setVisibility(View.VISIBLE);
                                                fetchUserProfileImage(userId);
                                            }

                                            fetchdata();
                                            fetchUserProfileImage(userId);
                                            fetchUserProfileImageVerified(userId);
                                        }
                                    } else {
                                        Log.e(TAG, "Field1 or currentUser.getPhoneNumber() is null");
                                    }
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }

    private void fetchUserProfileImage(String userId) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("users").child(userId).child("profile_image.jpg");
        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri).into(profileImageView);
        }).addOnFailureListener(exception -> {
            Log.e(TAG, "Error fetching profile image: " + exception.getMessage());
        });
    }

    private void fetchUserProfileImageVerified(String userId) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("users").child(userId).child("profile_image.jpg");
        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri).into(verifiedprofilebehere);
        }).addOnFailureListener(exception -> {
            Log.e(TAG, "Error fetching profile image: " + exception.getMessage());
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
