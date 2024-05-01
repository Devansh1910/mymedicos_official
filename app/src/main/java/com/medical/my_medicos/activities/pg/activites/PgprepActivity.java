package com.medical.my_medicos.activities.pg.activites;

import static androidx.fragment.app.FragmentManager.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
import com.medical.my_medicos.activities.home.HomeActivity;
import com.medical.my_medicos.activities.home.fragments.HomeFragment;
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

    private ImageView backtothehomefrompg;

    private int lastSelectedItemId = 0;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPgprepBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.green));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.backgroundcolor));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }

        backtothehomefrompg = findViewById(R.id.backtothehomefrompg);
        backtothehomefrompg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PgprepActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

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
    public void onBackPressed(){
//        Toast.makeText(ResultActivity.this, "", Toast.LENGTH_SHORT).show();
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