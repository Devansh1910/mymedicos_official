package com.medical.my_medicos.activities.ug;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.medical.my_medicos.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.medical.my_medicos.activities.guide.PgGuideActivity;
import com.medical.my_medicos.activities.guide.UgGuideActivity;
import com.medical.my_medicos.activities.home.HomeActivity;
import com.medical.my_medicos.activities.news.NewsActivity;
import com.medical.my_medicos.activities.pg.activites.PgprepActivity;
import com.medical.my_medicos.activities.slideshow.SearchSlideshowActivity;
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

    private ImageView backtothehomefrompg;

    LinearLayout totheguide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUgExamBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar ugtoolbar = findViewById(R.id.ugtoolbar);

        setSupportActionBar(ugtoolbar);

        backtothehomefrompg = findViewById(R.id.backtothehomefrompg);
        backtothehomefrompg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UgExamActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });

        binding.searchBarUG.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for now
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for now
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not needed for now
            }
        });

        binding.searchBarUG.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                // Not needed for now
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                String query = text.toString();
                if (!TextUtils.isEmpty(query)) {
                    Intent intent = new Intent(UgExamActivity.this, SearchUGActivity.class);
                    intent.putExtra("query", query);
                    startActivity(intent);
                }
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                if (buttonCode == MaterialSearchBar.BUTTON_BACK) {
                    // Handle back button click
                }
            }
        });

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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}

