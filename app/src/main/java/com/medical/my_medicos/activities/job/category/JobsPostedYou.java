package com.medical.my_medicos.activities.job.category;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.my_medicos.adapter.job.MyAdapter6;
import com.medical.my_medicos.R;
import com.medical.my_medicos.adapter.job.items.jobitem1;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JobsPostedYou extends AppCompatActivity {
    private static final String TAG = "JobsPostedYou"; // Logging tag
    String userPhoneNumber = null;
    RecyclerView recyclerView1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_you);

        Toolbar toolbar = findViewById(R.id.jobstoolbar);

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

        setSupportActionBar(toolbar);

        // Set the navigation icon and listener
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.arrow_bk);
        }

        // Access UI elements
        recyclerView1 = findViewById(R.id.recyclerview8);

        // Firebase Authentication
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            userPhoneNumber = currentUser.getPhoneNumber();
        }

        // Now, userEmail contains the current user's email address

        List<jobitem1> joblist = new ArrayList<>();

        FirebaseFirestore dc = FirebaseFirestore.getInstance();
        // ...

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
                                String Title = (String) dataMap.get("JOB Title");
                                String Category = (String) dataMap.get("Job type");
                                String documentid = (String) dataMap.get("documentId");
                                int r = user.compareTo(userPhoneNumber);
                                Log.d("abcdefsfsd", String.valueOf(r));

                                if (r == 0) {
                                    jobitem1 c = new jobitem1(speciality, Organiser, Location, date, user, Title, Category,documentid);
                                    joblist.add(c);
                                }

                                recyclerView1.setLayoutManager(new LinearLayoutManager(getApplication(), LinearLayoutManager.VERTICAL, false));
                                recyclerView1.setAdapter(new MyAdapter6(getApplication(), joblist));
                                Log.d("speciality2", speciality);
                            }
                        } else {
                            Log.e(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
