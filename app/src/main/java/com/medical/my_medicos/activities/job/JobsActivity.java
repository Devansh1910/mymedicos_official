package com.medical.my_medicos.activities.job;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.medical.my_medicos.activities.cme.CmeActivity;
import com.medical.my_medicos.activities.guide.CmeGuideActivity;
import com.medical.my_medicos.activities.guide.JobGuideActivity;
import com.medical.my_medicos.activities.job.category.JobsApplyActivity2;
import com.medical.my_medicos.activities.job.category.JobsPostedYou;
import com.medical.my_medicos.adapter.job.MyAdapter6;
import com.medical.my_medicos.adapter.job.MyAdapter7;
import com.medical.my_medicos.R;
import com.medical.my_medicos.adapter.job.items.jobitem1;
import com.medical.my_medicos.adapter.job.items.jobitem2;
import com.medical.my_medicos.list.subSpecialitiesData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;


public class JobsActivity extends AppCompatActivity {

    RecyclerView recyclerView1;
    private final String[][] subspecialities = subSpecialitiesData.subspecialities;
    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView2;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Toolbar toolbar;
    ImageView cart_icon;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);



        toolbar = findViewById(R.id.jobstoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        cart_icon = findViewById(R.id.cart_icon);
        cart_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(JobsActivity.this, JobGuideActivity.class);
                startActivity(i);
            }
        });

        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        boolean isFirstTimeUser = preferences.getBoolean("firstTimeUser", true);

        if (isFirstTimeUser) {
            showTapTargetOnFab();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("firstTimeUser", false);
            editor.apply();
        }

        floatingActionButton=findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(JobsActivity.this, PostJobActivity.class);
                startActivity(i);

            }
        });

        String[] specialitiesArray = getResources().getStringArray(R.array.specialityjobs);
        List<String> specialitiesList = Arrays.asList(specialitiesArray);
        List<jobitem2> joblist2 = new ArrayList<jobitem2>();
        for (String subspeciality : specialitiesList) {
            joblist2.add(new jobitem2(subspeciality));
        }

        recyclerView2 = findViewById(R.id.recyclerview6);
        int spanCount = 2;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);
        recyclerView2.setLayoutManager(gridLayoutManager);
        recyclerView2.setAdapter(new MyAdapter7(this, joblist2));
        recyclerView1 = findViewById(R.id.recyclerview5);
        List<jobitem1> joblist = new ArrayList<jobitem1>();

        FirebaseFirestore dc = FirebaseFirestore.getInstance();
        dc.collection("JOB")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task ) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Map<String, Object> dataMap = document.getData();
                                String speciality = "";
                                String Organiser = (String) dataMap.get("JOB Organiser");
                                String Location = (String) dataMap.get("Location");
                                String date = (String) dataMap.get("date");
                                String user = (String) dataMap.get("User");
                                String Category=((String) dataMap.get("Job type"));
                                String Title=((String) dataMap.get("JOB Title"));
                                String documentid=((String) dataMap.get("documentId"));

                                jobitem1 c = new jobitem1(speciality, Organiser, Location, date, user,Title , Category,documentid);
                                joblist.add(c);
                                recyclerView1.setLayoutManager(new LinearLayoutManager(getApplication(),LinearLayoutManager.HORIZONTAL, false));
                                recyclerView1.setAdapter( new MyAdapter6(getApplication(),joblist));
                                Log.d("speciality2", speciality);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.jobs_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void showTapTargetOnFab() {
        new MaterialTapTargetPrompt.Builder(JobsActivity.this)
                .setTarget(floatingActionButton)
                .setPrimaryText("Welcome!")
                .setSecondaryText("Tap here to create and post a job.")
                .setBackButtonDismissEnabled(true) // Allow dismissing by tapping the back button
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.drawer_jobsposted) {
            Intent i = new Intent(this, JobsPostedYou.class);
            startActivity(i);
        } else if (itemId == R.id.drawer_jobsapplied) {
            Intent i = new Intent(this, JobsApplyActivity2.class);
            startActivity(i);
        }
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}