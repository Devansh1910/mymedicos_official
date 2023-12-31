package com.medical.my_medicos.activities.pg.activites.insiders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.pg.fragment.QuestionbankFragment;
import com.medical.my_medicos.activities.pg.fragment.ScoreboardFragment;
import com.medical.my_medicos.activities.pg.fragment.VideoBankFragment;
import com.medical.my_medicos.activities.pg.fragment.WeeklyQuizFragment;
import com.medical.my_medicos.databinding.ActivitySpecialityPgquizBinding;

public class SpecialityPGQuizActivity extends AppCompatActivity {

    private ActivitySpecialityPgquizBinding binding;

    private BottomNavigationView bottomNavigationCategoryQuizInsider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpecialityPgquizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar();
        setupBottomAppBar();

        if (savedInstanceState == null) {
            // Initial fragment setup
            String title = getIntent().getStringExtra("specialityPgName");
            replaceFragment(WeeklyQuizFragment.newInstance(0, title));
        }
    }

    private void setupToolbar() {
        binding.specialityquiztoolbar.setTitle(getIntent().getStringExtra("specialityPgName"));
        setSupportActionBar(binding.specialityquiztoolbar);
    }

    private void setupBottomAppBar() {
        BottomAppBar bottomAppBar = binding.bottomappabarpgquiz;
        bottomNavigationCategoryQuizInsider = bottomAppBar.findViewById(R.id.bottomNavigationViewpgquiz);
        bottomNavigationCategoryQuizInsider.setBackground(null);

        bottomNavigationCategoryQuizInsider.setOnItemSelectedListener(item -> {
            int frgId = item.getItemId();
            Log.d("ItemSelected", "Fragment ID: " + frgId);
            if (frgId == R.id.qui) {
                String title = getTitleFromIntent();
                WeeklyQuizFragment fragment = WeeklyQuizFragment.newInstance(0, title);
                replaceFragment(fragment);
            } else {
                String title = getTitleFromIntent();
                ScoreboardFragment fragment = ScoreboardFragment.newInstance(0, title);
                replaceFragment(fragment);
            }
            return true;
        });
    }


    private String getTitleFromIntent() {
        return getIntent().getStringExtra("specialityPgName");
    }


    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_pg_quiz, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setToolbarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

}