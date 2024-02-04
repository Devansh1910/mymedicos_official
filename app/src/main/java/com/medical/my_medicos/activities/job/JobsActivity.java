package com.medical.my_medicos.activities.job;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
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
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.CirclePromptFocal;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;


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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.backgroundcolor));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.backgroundcolor));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }

        toolbar = findViewById(R.id.jobstoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
//        boolean isFirstTimeUser = preferences.getBoolean("firstTimeUser", true);
//
//        if (isFirstTimeUser) {
//            showTapTargetOnFab();
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putBoolean("firstTimeUser", false);
//            editor.apply();
//        }

        floatingActionButton=findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(JobsActivity.this, PostJobActivity.class);
                startActivity(i);

            }
        });

//        showTapTargetOnFab();

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
//    private void showTapTargetOnFab() {
//        int blueColor = ContextCompat.getColor(JobsActivity.this, R.color.taptargetblue);
//        int whiteColor = ContextCompat.getColor(JobsActivity.this, R.color.white);
//        int texttapColor = ContextCompat.getColor(JobsActivity.this, R.color.unselected);
//
//        new MaterialTapTargetPrompt.Builder(JobsActivity.this)
//                .setTarget(floatingActionButton)
//                .setPrimaryText("Ready to Post a Job?")
//                .setSecondaryText("Tap here to create a new job opening, fill in the details, and share exciting opportunities with potential candidates.")
//                .setBackButtonDismissEnabled(true)
//                .setBackgroundColour(blueColor)
//                .setFocalColour(whiteColor)
//                .setFocalRadius(150f)
//                .setPrimaryTextSize(com.intuit.sdp.R.dimen._16sdp)
//                .setPrimaryTextColour(texttapColor)
//                .setSecondaryTextColour(texttapColor)
//                .setSecondaryTextSize(com.intuit.sdp.R.dimen._12sdp)
//                .setPromptFocal(new CirclePromptFocal())
//                .show();
//    }

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