package com.medical.my_medicos.activities.ug;

import android.os.Bundle;

import com.medical.my_medicos.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.medical.my_medicos.databinding.ActivityUgExamBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Objects;

public class UgExamActivity extends AppCompatActivity {
    private ActivityUgExamBinding binding;

    Toolbar ugtoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUgExamBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar ugtoolbar = findViewById(R.id.ugtoolbar);

        setSupportActionBar(ugtoolbar);

        // Enable the back arrow in the Toolbar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        BottomNavigationView navView = findViewById(R.id.bottomNavigationViewug);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_ughome, R.id.navigation_ugform)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_ug);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.bottomNavigationViewug, navController);
    }
}

