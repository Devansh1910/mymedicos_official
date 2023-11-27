package com.example.my_medicos.activities.home;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.my_medicos.R;
import com.example.my_medicos.activities.drawer.DrawerActivity;
import com.example.my_medicos.activities.home.fragments.ClubFragment;
import com.example.my_medicos.activities.home.fragments.HomeFragment;
import com.example.my_medicos.activities.home.fragments.ProfileFragment;
import com.example.my_medicos.activities.home.fragments.SlideshowFragment;
import com.example.my_medicos.activities.login.MainActivity;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Map;
import java.util.prefs.Preferences;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;
    BottomAppBar bottomAppBar;
    private ImageView doctorProfileImageView;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView navUsername;
    TextView navUserMail;
    Toolbar toolbar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout1);
        navigationView = findViewById(R.id.nav_view2);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.OpenDrawer, R.string.CloseDrawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getPhoneNumber();
            Log.d("currentuser23", userId);
        }

        navUsername = navigationView.getHeaderView(0).findViewById(R.id.name2);
        navUserMail = navigationView.getHeaderView(0).findViewById(R.id.email2);

        navUserMail.setText(currentUser.getEmail());
        fetchUserData();

        Log.d("currentuser23", currentUser.getEmail());

        Preferences preferences = Preferences.userRoot();
        if (preferences.get("username", null) != null) {
            String username = preferences.get("username", null);
            Log.d("usernaem2", username);
            navUsername.setText(username);
        }
        Log.d("iditennnew", "djnvjvdssn");

        NavigationView navigationView = findViewById(R.id.nav_view2);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_settings) {
                    Intent settingsIntent = new Intent(HomeActivity.this, DrawerActivity.class);
                    startActivity(settingsIntent);
                } else if (id == R.id.nav_notifications) {
                    Intent notificationsIntent = new Intent(HomeActivity.this, DrawerActivity.class);
                    startActivity(notificationsIntent);
                } else if (id == R.id.nav_logout) {
                    performLogout();
                } else {
                    Toast.makeText(HomeActivity.this, "Something went Wrong ", Toast.LENGTH_SHORT).show();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
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
            } else if (frgId == R.id.slideshare) {
                replaceFragment(new SlideshowFragment());
            } else {
                replaceFragment(new ProfileFragment());
            }
            return true;
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.nav_settings1) {
//            Log.d("MenuItemSelected", "Settings item clicked!"); // Add this line
//            replaceFragmentDrawer(new SettingsFragment());
//            return true;
//        }
//        // Handle other menu items...
//        return super.onOptionsItemSelected(item);
//    }




    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

//    private boolean replaceFragmentDrawer(Fragment fragment) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.nav_host_fragment_content_drawer, fragment); // replace a Fragment with Frame Layout
//        transaction.commit(); // commit the changes
//        drawerLayout.closeDrawers(); // close the all open Drawer Views
//
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.nav_host_fragment_content_drawer, fragment)
//                .commit();
//        return true;
//    }

    private void performLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

        builder.setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @SuppressLint("RestrictedApi")
    private void fetchUserData() {
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
                                int a = field1.compareTo(currentUser.getPhoneNumber());

                                if (a == 0) {
                                    String userName = (String) dataMap.get("Name");
                                    String userEmail = (String) dataMap.get("Email ID");

                                    runOnUiThread(() -> {
                                        navUsername.setText(userName);
                                        navUserMail.setText(userEmail);
                                    });
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    });
        }
    }
}
