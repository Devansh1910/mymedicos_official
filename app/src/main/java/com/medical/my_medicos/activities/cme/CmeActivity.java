package com.medical.my_medicos.activities.cme;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.cme.fragment.OngoingFragment;
import com.medical.my_medicos.activities.cme.fragment.PastFragment;
import com.medical.my_medicos.activities.cme.fragment.UpcomingFragment;
import com.medical.my_medicos.list.subSpecialitiesData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

public class CmeActivity extends AppCompatActivity {
    FloatingActionButton floatingActionButton;
    Button OK;
    RecyclerView recyclerView;
    private ViewPager2 pager,viewpager;
    private TabLayout tabLayout;
    private Spinner specialitySpinner;
    String selectedMode1,selectedMode2,selectedMode;
    private FirebaseFirestore db;
    private Spinner subspecialitySpinner;
    private ArrayAdapter<CharSequence> specialityAdapter;
    private ArrayAdapter<CharSequence> subspecialityAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    Toolbar toolbar;
    private final String[][] subspecialities = subSpecialitiesData.subspecialities;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cme);
        OK = findViewById(R.id.ok);
        OK.setBackgroundColor(Color.GRAY);


        viewpager = findViewById(R.id.view_pager1);
        viewpager.setAdapter(new ViewPagerAdapter(this));
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
                refreshFragmentsData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        toolbar = findViewById(R.id.cmetoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tablayout);
        new TabLayoutMediator(tabLayout, viewpager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Ongoing");
                    break;
                case 1:
                    tab.setText("Upcoming");
                    break;
                case 2:
                    tab.setText("Past");
                    break;
            }
        }).attach();
        specialitySpinner = findViewById(R.id.speciality);
        subspecialitySpinner = findViewById(R.id.subspeciality);

        specialityAdapter = ArrayAdapter.createFromResource(this,
                R.array.speciality, android.R.layout.simple_spinner_item);
        specialityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specialitySpinner.setAdapter(specialityAdapter);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            String fcmToken = task.getResult();
                            // Store or use the FCM token as needed
                            // Example: Save the token to Firebase Firestore
                            saveFcmTokenToFirestore(fcmToken);
                            Log.d("fcm token ",fcmToken);
                        } else {
                            // Handle the error
                            Log.e("FCM Token", "Error getting FCM token", task.getException());
                        }
                    }
                });





        subspecialityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        subspecialityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subspecialitySpinner.setAdapter(subspecialityAdapter);
        subspecialitySpinner.setVisibility(View.GONE);

        Spinner modeSpinner = findViewById(R.id.mode);
        ArrayAdapter<CharSequence> modeAdapter = ArrayAdapter.createFromResource(this,
                R.array.mode, android.R.layout.simple_spinner_item);
        modeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modeSpinner.setAdapter(modeAdapter);
        modeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedMode = modeSpinner.getSelectedItem().toString();
                OK.setClickable(false);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        specialitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedMode1 = specialitySpinner.getSelectedItem().toString();
                OK.setClickable(false);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        specialitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                int specialityIndex = specialitySpinner.getSelectedItemPosition();
                if (specialityIndex >= 0 && specialityIndex < subspecialities.length && subspecialities[specialityIndex].length > 0) {
                    String[] subspecialityArray = subspecialities[specialityIndex];
                    subspecialityAdapter.clear();
                    subspecialityAdapter.add("Select Subspeciality");
                    for (String subspeciality : subspecialityArray) {
                        subspecialityAdapter.add(subspeciality);
                    }
                    subspecialitySpinner.setVisibility(View.VISIBLE);
                    subspecialitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            selectedMode2 = subspecialitySpinner.getSelectedItem().toString();
                            selectedMode1 = specialitySpinner.getSelectedItem().toString();
                            OK.setClickable(true);
                            OK.setBackgroundColor(Color.parseColor("#90EE90"));
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } else {
                    subspecialitySpinner.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View a) {
                Context context = a.getContext();

                Intent i = new Intent(context, CmeSearchActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("Speciality",selectedMode1);
                i.putExtra("SubSpeciality",selectedMode2);
                i.putExtra("Mode",selectedMode);
                context.startActivity(i);
            }
        });

        pager.setAdapter(new ViewPagerAdapter(this));
        fetchData();

    }
    private void saveFcmTokenToFirestore(String fcmToken) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String phoneNumber = user.getPhoneNumber();

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Query the "users" collection to find the document with the matching phone number
            db.collection("users")
                    .whereEqualTo("phoneNumber", phoneNumber)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    // Update the FCM token in the found user document
                                    db.collection("users").document(document.getId())
                                            .update("fcmToken", fcmToken)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("FCM Token", "FCM token stored successfully");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.e("FCM Token", "Error storing FCM token", e);
                                                }
                                            });
                                }
                            } else {
                                Log.e("FCM Token", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }


    public void fetchData() {

        setSupportActionBar(toolbar);
        db = FirebaseFirestore.getInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CmeActivity.this, PostCmeActivity.class);
                startActivity(i);
            }
        });
    }
    class ViewPagerAdapter extends FragmentStateAdapter {
        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }
        @Nullable
        public Fragment findFragmentByPosition(int position) {
            return getSupportFragmentManager().findFragmentByTag("f" + position);
        }
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new OngoingFragment();
                case 1:
                    return new UpcomingFragment();
                case 2:
                    return new PastFragment();
            }
            return null;
        }
        @Override
        public int getItemCount() {
            return 3;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.cme_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.drawer_cmereserved) {
            Intent i = new Intent(this, CmeReservedActivity.class);
            startActivity(i);
        } else if (itemId == R.id.drawer_cmecreated) {
            Intent i = new Intent(this, CmeCreatedActivity.class);
            startActivity(i);
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                // Handle the back arrow click, finish the current activity
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    private void refreshFragmentsData() {
        ViewPagerAdapter viewPagerAdapter = (ViewPagerAdapter) viewpager.getAdapter();

        OngoingFragment ongoingFragment = (OngoingFragment) viewPagerAdapter.findFragmentByPosition(0);
        if (ongoingFragment != null) {
            ongoingFragment.refreshData();
        }

        UpcomingFragment upcomingFragment = (UpcomingFragment) viewPagerAdapter.findFragmentByPosition(1);
        if (upcomingFragment != null) {
            upcomingFragment.refreshData();
        }
//
        PastFragment pastFragment = (PastFragment) viewPagerAdapter.findFragmentByPosition(2);
        if (pastFragment != null) {
            pastFragment.refreshData();
        }
    }

}