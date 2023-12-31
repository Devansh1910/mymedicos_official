package com.medical.my_medicos.activities.pg.activites.extras;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.pg.activites.internalfragments.HomePgFragment;
import com.medical.my_medicos.activities.pg.activites.internalfragments.PreparationSubCategoryActivity;
import com.medical.my_medicos.activities.pg.activites.internalfragments.PreparationSubCategoryForMaterialActivity;
import com.medical.my_medicos.databinding.ActivityPreparationCategoryDisplayBinding;
import com.medical.my_medicos.databinding.ActivityPreparationCategoryMaterialDisplayBinding;

public class PreparationCategoryMaterialDisplayActivity extends AppCompatActivity {

    ActivityPreparationCategoryMaterialDisplayBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPreparationCategoryMaterialDisplayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ImageView backToHomeImageView = findViewById(R.id.backtothehomefromprepcategory);
        backToHomeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        findViewById(R.id.phase1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSubCategoryActivity(binding.titlephase1.getText().toString());
            }
        });

        findViewById(R.id.phase2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSubCategoryActivity(binding.titlephase2.getText().toString());
            }
        });

        findViewById(R.id.phase3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSubCategoryActivity(binding.titlephase3.getText().toString());
            }
        });
    }

    private void startSubCategoryActivity(String categoryTitle) {
        Intent intent = new Intent(this, PreparationSubCategoryForMaterialActivity.class);
        intent.putExtra("CATEGORY_TITLE", categoryTitle);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout_pg);
        if (currentFragment instanceof HomePgFragment) {
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
