package com.medical.my_medicos.activities.home;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.home.fragments.ClubFragment;
import com.medical.my_medicos.activities.home.fragments.HomeFragment;
import com.medical.my_medicos.activities.home.fragments.SlideshowFragment;
import com.medical.my_medicos.activities.home.sidedrawer.HomeSideActivity;
import com.medical.my_medicos.activities.home.sidedrawer.NotificationActivity;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Map;
import java.util.Objects;
import java.util.prefs.Preferences;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;
    BottomAppBar bottomAppBar;
    ImageView notificationbtn;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    ImageView profileImageView, verifiedprofilebehere;
    FrameLayout verifiedUser, circularImageView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);



        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getPhoneNumber();
            Log.d("Something wennt wrong...", Objects.requireNonNull(userId));
        }

        fetchUserData();

        Log.d("Something went wrong..", Objects.requireNonNull(currentUser.getPhoneNumber()));

        Preferences preferences = Preferences.userRoot();
        if (preferences.get("username", null) != null) {
            String username = preferences.get("username", null);
            Log.d("usernaem2", username);
        }
        LinearLayout openhomedrawerIcon = findViewById(R.id.opensidehomedrawer);
        openhomedrawerIcon.setOnClickListener(v -> openHomeSideActivity());

        notificationbtn = findViewById(R.id.notificationbtn);
        notificationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, NotificationActivity.class);
                startActivity(i);
            }
        });

        bottomAppBar = findViewById(R.id.bottomappabar);

        replaceFragment(new HomeFragment());
        bottomNavigation = findViewById(R.id.bottomNavigationView);
        bottomNavigation.setBackground(null);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int frgId = item.getItemId();
            Log.d("nsjnjsn","hcisb");
            if (frgId == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (frgId == R.id.work) {
                replaceFragment(new ClubFragment());
            } else {
                replaceFragment(new SlideshowFragment());
            }
            return true;
        });
    }

    public void openHomeSideActivity() {
        Intent settingsIntent = new Intent(HomeActivity.this, HomeSideActivity.class);
        startActivity(settingsIntent);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @SuppressLint("RestrictedApi")
    public void fetchUserData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getPhoneNumber();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> dataMap = document.getData();
                                String field1 = (String) dataMap.get("Phone Number");
                                int a = (field1 != null) ? field1.compareTo(currentUser.getPhoneNumber()) : -1;

                                if (a == 0) {
                                    String userName = (String) dataMap.get("Name");
                                    String userEmail = (String) dataMap.get("Email ID");
                                    Boolean mcnVerified = (Boolean) dataMap.get("MCN verified");
                                    Preferences preferences = Preferences.userRoot();

                                    // Initialize views
                                    verifiedUser = findViewById(R.id.verifieduser);
                                    profileImageView = findViewById(R.id.circularImageView);

                                    if (mcnVerified != null && mcnVerified) {
                                        preferences.putBoolean("mcn_verified", true);
                                        verifiedUser.setVisibility(View.VISIBLE);
                                        profileImageView.setVisibility(View.GONE);
                                        fetchUserProfileImageVerified(userId);
                                    } else {
                                        preferences.putBoolean("mcn_verified", false);
                                        verifiedUser.setVisibility(View.GONE);
                                        profileImageView.setVisibility(View.VISIBLE);
                                        fetchUserProfileImage(userId);
                                    }

                                    // Fetch images (you may need to decide which one to fetch)
                                    fetchUserProfileImage(userId);
                                    fetchUserProfileImageVerified(userId);

                                    // Update the UI based on user authentication status
                                    runOnUiThread(() -> {
                                        updateProfileUI(userName, userId, userEmail);
                                    });
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    });
        }
    }


    private void updateProfileUI(String userName, String userId, String userEmail) {
        ImageView profileImageView = findViewById(R.id.circularImageView);
        FrameLayout verifiedUser = findViewById(R.id.verifieduser);
        ImageView verifiedprofilebehere = findViewById(R.id.verifiedprofilebehere); // Add this line

    }

    @SuppressLint("RestrictedApi")
    private void fetchUserProfileImage(String userId) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("users").child(userId).child("profile_image.jpg");
        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri).into(profileImageView);
        }).addOnFailureListener(exception -> {
            Log.e(TAG, "Error fetching profile image: " + exception.getMessage());
        });
    }
    @SuppressLint("RestrictedApi")
    private void fetchUserProfileImageVerified(String userId) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("users").child(userId).child("profile_image.jpg");

        verifiedprofilebehere = findViewById(R.id.verifiedprofilebehere);

        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            if (verifiedprofilebehere != null) {
                Picasso.get().load(uri).into(verifiedprofilebehere);
            }
        }).addOnFailureListener(exception -> {
            Log.e(TAG, "Error fetching profile image: " + exception.getMessage());
        });
    }
}