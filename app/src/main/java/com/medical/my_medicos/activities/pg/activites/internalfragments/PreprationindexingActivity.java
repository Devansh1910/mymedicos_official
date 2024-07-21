package com.medical.my_medicos.activities.pg.activites.internalfragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.medical.my_medicos.activities.pg.activites.internalfragments.Preprationindexing.PreprationNotesFragment;
import com.medical.my_medicos.activities.pg.activites.internalfragments.Preprationindexing.PreprationSWGT;
import com.medical.my_medicos.activities.pg.activites.internalfragments.Preprationindexing.PreprationTWGT;
import com.medical.my_medicos.databinding.ActivityPreprationindexingBinding;
import com.medical.my_medicos.R;

public class PreprationindexingActivity extends AppCompatActivity {

    private ActivityPreprationindexingBinding binding;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String speciality = getIntent().getStringExtra("specialityPgName");

        binding = ActivityPreprationindexingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitle(speciality);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationViewpginsiderindexing1);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment;

            String itemId = String.valueOf(item.getItemId());

            if (itemId.equals(String.valueOf(R.id.navigation_pgnotes))) {
                selectedFragment = PreprationNotesFragment.newInstance(speciality);
            } else if (itemId.equals(String.valueOf(R.id.navigation_pgtwgt))) {
                selectedFragment = PreprationTWGT.newInstance(speciality);
                Log.d("speciality is fragment",String.valueOf(R.id.navigation_pgtwgt));
                Log.d("speciality is fragment",itemId);
            } else if (itemId.equals(String.valueOf(R.id.navigation_pgswgt))) {
                Log.d("speciality is fragment1",String.valueOf(R.id.navigation_pgswgt));
                Log.d("speciality is fragment1",itemId);
                selectedFragment = PreprationSWGT.newInstance(speciality);
            } else {
                selectedFragment = PreprationNotesFragment.newInstance(speciality);
            }

            loadFragment(selectedFragment);
            return true;
        });





        // Load default fragment
        loadFragment(PreprationNotesFragment.newInstance(speciality));
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }
}
