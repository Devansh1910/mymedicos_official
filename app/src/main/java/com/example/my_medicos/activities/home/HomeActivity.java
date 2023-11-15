package com.example.my_medicos.activities.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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

import com.example.my_medicos.activities.drawer.drawercontent.notifications.NotificationsFragment;
import com.example.my_medicos.activities.drawer.drawercontent.settings.SettingsFragment;
import com.example.my_medicos.activities.home.fragments.ClubFragment;
import com.example.my_medicos.activities.home.fragments.HomeFragment;
import com.example.my_medicos.activities.home.fragments.ProfileFragment;
import com.example.my_medicos.R;
import com.example.my_medicos.activities.home.fragments.SlideshowFragment;
import com.example.my_medicos.activities.login.MainActivity;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;

    BottomAppBar bottomAppBar;

    DrawerLayout drawerLayout;

    NavigationView navigationView;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.OpenDrawer, R.string.CloseDrawer);

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.nav_settings) {
                    replaceFragmentDrawer(new SettingsFragment());
                } else if (id == R.id.nav_notifications) {
                    replaceFragmentDrawer(new NotificationsFragment());
                } else if (id == R.id.nav_logout) {
                    performLogout();
                } else {
                    Toast.makeText(HomeActivity.this, "Notification Clicked", Toast.LENGTH_SHORT).show();
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

    private void replaceFragmentDrawer(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment_content_drawer, fragment)
                .commit();
    }


    private void performLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked Confirm, perform logout
                        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Close the current activity
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked Cancel, do nothing or dismiss the dialog
                        dialog.dismiss();
                    }
                })
                .show();
    }


}