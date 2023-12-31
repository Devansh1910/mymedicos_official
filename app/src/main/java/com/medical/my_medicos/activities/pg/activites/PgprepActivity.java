package com.medical.my_medicos.activities.pg.activites;

import static androidx.fragment.app.FragmentManager.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.guide.NewsGuideActivity;
import com.medical.my_medicos.activities.guide.PgGuideActivity;
import com.medical.my_medicos.activities.news.NewsActivity;
import com.medical.my_medicos.activities.pg.activites.extras.CreditsActivity;
import com.medical.my_medicos.activities.pg.activites.internalfragments.HomePgFragment;
import com.medical.my_medicos.activities.pg.activites.internalfragments.NeetExamFragment;
import com.medical.my_medicos.activities.pg.activites.internalfragments.PreparationPgFragment;
import com.medical.my_medicos.databinding.ActivityPgprepBinding;

import java.util.Map;

public class  PgprepActivity extends AppCompatActivity {
    ActivityPgprepBinding binding;
    BottomNavigationView bottomNavigationPg;
    BottomAppBar bottomAppBarPg;
    private int lastSelectedItemId = 0;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPgprepBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUid = currentUser.getUid();
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            database.getReference().child("profiles")
                    .child(currentUid)
                    .child("coins")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Integer coinsValue = snapshot.getValue(Integer.class);
                            if (coinsValue != null) {
                                binding.currentcoinspg.setText(String.valueOf(coinsValue));
                            } else {
                                binding.currentcoinspg.setText("0");
                            }
                        }

                        @SuppressLint("RestrictedApi")
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e(TAG, "Error loading coins from database: " + error.getMessage());
                        }
                    });
        }
        setupBottomAppBar();

        HomePgFragment homeFragment = HomePgFragment.newInstance();
        replaceFragment(homeFragment);

        ImageView backToHomeImageView = findViewById(R.id.backtothehomefrompg);
        backToHomeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        LinearLayout openpgdrawerIcon = findViewById(R.id.creditscreen);
        openpgdrawerIcon.setOnClickListener(v -> openHomeSidePgActivity());


    }

    private void setupBottomAppBar() {
        BottomAppBar bottomAppBar = binding.bottomappabarpginsider;
        bottomNavigationPg = bottomAppBar.findViewById(R.id.bottomNavigationViewpginsider);
        bottomNavigationPg.setBackground(null);

        bottomNavigationPg.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_pghome) {
                if (lastSelectedItemId != R.id.navigation_pghome) {
                    replaceFragment(HomePgFragment.newInstance());
                    lastSelectedItemId = R.id.navigation_pghome;
                }
                return true;
            } else if (itemId == R.id.navigation_pgneet) {
                if (lastSelectedItemId != R.id.navigation_pgneet) {
                    replaceFragment(NeetExamFragment.newInstance());
                    lastSelectedItemId = R.id.navigation_pgneet;
                }
                return true;
            } else if (itemId == R.id.navigation_pgpreparation) {
                if (lastSelectedItemId != R.id.navigation_pgpreparation) {
                    replaceFragment(PreparationPgFragment.newInstance());
                    lastSelectedItemId = R.id.navigation_pgpreparation;
                }
                return true;
            }

            return false;
        });


    }

    public void openHomeSidePgActivity() {
        Intent settingsIntent = new Intent(PgprepActivity.this, CreditsActivity.class);
        startActivity(settingsIntent);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_pg, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

}