package com.medical.my_medicos.activities.home;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.Extras.NoInternetActivity;
import com.medical.my_medicos.activities.cme.CmeDetailsActivity;
import com.medical.my_medicos.activities.home.fragments.ClubFragment;
import com.medical.my_medicos.activities.home.fragments.HomeFragment;
import com.medical.my_medicos.activities.home.fragments.SlideshowFragment;
import com.medical.my_medicos.activities.home.sidedrawer.HomeSideActivity;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.medical.my_medicos.activities.job.JobDetailsActivity;
import com.medical.my_medicos.activities.news.NewsDetailedActivity;
import com.medical.my_medicos.activities.publications.activity.ProductDetailedActivity;
import com.medical.my_medicos.activities.utils.ConnectvityUtil;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
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
    private static final long DOUBLE_PRESS_EXIT_INTERVAL = 2000;
    private static final int MY_UPDATE_CODE = 100;
    private long lastPressTime;
    FrameLayout verifiedUser, circularImageView;
    ProgressBar loadingCircle;
    LinearLayout opensidehomedrawer;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (!ConnectvityUtil.isConnectedToInternet(this)) {
            // Start the NoInternetActivity if there is no internet connection
            Intent intent = new Intent(this, NoInternetActivity.class);
            startActivity(intent);
            finish(); // Close the current activity
            return; // Exit the method early
        }

        loadingCircle = findViewById(R.id.loadingCircle);
        opensidehomedrawer = findViewById(R.id.opensidehomedrawer);

        loadingCircle.setVisibility(View.VISIBLE);
        opensidehomedrawer.setVisibility(View.GONE);

        handleDeepLinkIntent();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.blue));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.blue));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }

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

//        notificationbtn = findViewById(R.id.notificationbtn);
//        notificationbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(HomeActivity.this, NotificationActivity.class);
//                startActivity(i);
//            }
//        });

        bottomAppBar = findViewById(R.id.bottomappabar);

        replaceFragment(new HomeFragment());
        bottomNavigation = findViewById(R.id.bottomNavigationView);
        bottomNavigation.setBackground(null);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int frgId = item.getItemId();
            Log.d("Something went wrong","Report to the Developer");
            Fragment selectedFragment = null;
            if (frgId == R.id.home) {
                selectedFragment = new HomeFragment();
            } else if (frgId == R.id.work) {
                selectedFragment = new ClubFragment();
            } else {
                selectedFragment = new SlideshowFragment();
            }

            replaceFragment(selectedFragment);
            updateToolbarColor(selectedFragment); // Update toolbar color based on the selected fragment

            return true;
        });

        checkforAppUpdate();
    }

    private void handleDeepLinkIntent() {
        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent())
                .addOnSuccessListener(this, pendingDynamicLinkData -> {
                    if (pendingDynamicLinkData != null) {
                        Uri deepLink = pendingDynamicLinkData.getLink();
                        if (deepLink != null) {
                            String link = deepLink.toString();
                            if (link.contains("jobdetails?")) {
                                handleDeepLinkIntentJOB(deepLink);
                            } else if (link.contains("cmedetails?")) {
                                handleDeepLinkIntentCME(deepLink);
                            } else if (link.contains("newsdetails?")) {
                                handleDeepLinkIntentNews(deepLink);
                            }else if (link.contains("bookdetails?")){
                                handleDeepLinkIntentPublication(deepLink);
                            }
                        }
                    }
                })
                .addOnFailureListener(this, e -> {
                    Log.e("DeepLink", "Error handling deep link: " + e.getMessage());
                });
    }
    private void handleDeepLinkIntentJOB(Uri deepLink) {
        String jobId = deepLink.getQueryParameter("jobId");
        Log.d("DeepLink", "Received deep link with jobId: " + jobId);
        openJobDetailsActivity(jobId);
    }
    private void handleDeepLinkIntentNews(Uri deepLink) {
        String newsId = deepLink.getQueryParameter("newsId");
        Log.d("DeepLink", "Received deep link with newsId: " + newsId);
        openNewsDetailsActivity(newsId);
    }
    private void handleDeepLinkIntentCME(Uri deepLink) {
        String cmeId = deepLink.getQueryParameter("cmeId");
        String typefordeep = deepLink.getQueryParameter("typefordeep");
        Log.d("DeepLink", "Received deep link with cmeId: " + cmeId + " and typefordeep: " + typefordeep);
        openCmeDetailsActivity(cmeId, typefordeep); // Pass typefordeep to openCmeDetailsActivity method
    }

    private void handleDeepLinkIntentPublication(Uri deepLink) {
        String bookId = deepLink.getQueryParameter("bookId");
        Log.d("DeepLink", "Received deep link with bookId: " + bookId);
        openPublicationsDetailsActivity(bookId);
    }

    private void openCmeDetailsActivity(String cmeId, String typefordeep) {
        Log.d("HomeActivity", "Opening CmeDetailsActivity with documentId: " + cmeId);
        Intent intent = new Intent(this, CmeDetailsActivity.class);
        intent.putExtra("cmeId", cmeId);
        intent.putExtra("typefordeep", typefordeep); // Pass typefordeep parameter to CmeDetailsActivity
        startActivity(intent);
        finish();
        Log.d("HomeActivity", "CmeDetailsActivity started");
    }
    private void openJobDetailsActivity(String jobId) {
        Log.d("HomeActivity", "Opening JobDetailsActivity with documentId: " + jobId);
        Intent intent = new Intent(this, JobDetailsActivity.class);
        intent.putExtra("jobId", jobId);
        startActivity(intent);
        finish();
        Log.d("HomeActivity", "JobDetailsActivity started");
    }
    private void openNewsDetailsActivity(String newsId) {
        Log.d("HomeActivity", "Opening NewsDetailsActivity with documentId: " + newsId);
        Intent intent = new Intent(this, NewsDetailedActivity.class);
        intent.putExtra("newsId", newsId);
        startActivity(intent);
        finish();
        Log.d("HomeActivity", "NewsDetailsActivity started");
    }

    private void openPublicationsDetailsActivity(String bookId) {
        Log.d("HomeActivity", "Opening PublicationsDetailsActivity with documentId: " + bookId);
        Intent intent = new Intent(this, ProductDetailedActivity.class);
        intent.putExtra("bookId", bookId);
        startActivity(intent);
        finish();
        Log.d("HomeActivity", "PublicationsDetailsActivity started");
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);

        if (currentFragment instanceof HomeFragment && getSupportFragmentManager().getBackStackEntryCount() == 0) {
            long currentTime = System.currentTimeMillis();

            if (currentTime - lastPressTime > DOUBLE_PRESS_EXIT_INTERVAL) {
                showToast("Press again to exit");
                lastPressTime = currentTime;
            } else {
                finishAffinity();
            }
        } else {
            super.onBackPressed();
        }
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private void updateToolbarColor(Fragment fragment) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (fragment instanceof SlideshowFragment || fragment instanceof ClubFragment) {
            toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundcolor));
        } else {
            toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.blue));
        }
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
            putUserData(userId);

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
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (!isFinishing()) {
                    loadingCircle.setVisibility(View.GONE);
                    opensidehomedrawer.setVisibility(View.VISIBLE);
                }
            }, 2000);
        }
    }
    private void updateProfileUI(String userName, String userId, String userEmail) {
        ImageView profileImageView = findViewById(R.id.circularImageView);
        FrameLayout verifiedUser = findViewById(R.id.verifieduser);
        ImageView verifiedprofilebehere = findViewById(R.id.verifiedprofilebehere); // Add this line

    }
    private void putUserData(String phoneNumber) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            Query query = db.collection("users").whereEqualTo("Phone Number", phoneNumber);

            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Update the found document with the new user data
                            db.collection("users").document(document.getId())
                                    .update("realtimeid", currentUser.getUid())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("Firestore", "DocumentSnapshot updated successfully!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("Firestore", "Error updating document", e);
                                        }
                                    });
                        }
                    } else {
                        Log.w("Firestore", "Error getting documents.", task.getException());
                    }
                }
            });
        }
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
    private void checkforAppUpdate(){
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {

                try {
                    appUpdateManager.startUpdateFlowForResult(
                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                            appUpdateInfo,
                            // an activity result launcher registered via registerForActivityResult
                            AppUpdateType.IMMEDIATE,
                            // Or pass 'AppUpdateType.FLEXIBLE' to newBuilder() for
                            // flexible updates.
                            this, MY_UPDATE_CODE);
                } catch (IntentSender.SendIntentException e) {
                    throw new RuntimeException(e);
                }

            }
        });
    }
    public void onActivityResult(int requestCode,int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_UPDATE_CODE) {
            if (resultCode != RESULT_OK) {
                Log.w("FirstActivity", "Update flow failed! Result code : " + resultCode);
            }
        }
    }

}
