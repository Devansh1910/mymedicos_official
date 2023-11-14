package com.example.my_medicos.activities.job;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.my_medicos.adapter.job.MyAdapter6;
import com.example.my_medicos.R;
import com.example.my_medicos.adapter.job.items.jobitem1;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JobDetailsActivity extends AppCompatActivity {

    Button shareButton;
    RecyclerView recyclerView1;
    MyAdapter6 adapter1;
    FloatingActionButton floatingActionButton;
    private ViewPager2 pager;
    private TabLayout tabLayout;
    RecyclerView recyclerView2;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String speciality,Organiser,Location;
    String receivedData;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_jobs);
        // Find views by their IDs
        TextView jobTitleTextView = findViewById(R.id.jobTitleTextView);
        TextView companyNameTextView = findViewById(R.id.companyNameTextView);
        TextView locationTextView = findViewById(R.id.locationTextView);
        TextView salaryEditText = findViewById(R.id.salaryEditText);
        TextView organizername = findViewById(R.id.organizername);
        TextView dateofpost = findViewById(R.id.jobposteddate);
        TextView openingsEditText = findViewById(R.id.openingsEditText);
        TextView experienceEditText = findViewById(R.id.experienceEditText);
        TextView authorSpecialityTextView = findViewById(R.id.authorSpecialityTextView);
        TextView authorSubSpecialityTextView = findViewById(R.id.authorSubSpecialityTextView);
        TextView jobDescriptionContentTextView = findViewById(R.id.jobDescriptionContentTextView);
        TextView jobtype = findViewById(R.id.Job_type);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("user")) {
            // Retrieve the extra data
            receivedData = intent.getStringExtra("user");

        }


        Button apply=findViewById(R.id.applyButton);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent j=new Intent(context, JobsApplyActivity.class);
                j.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                j.putExtra("user",receivedData);
                context.startActivity(j);
            }
        });

// Check if the Intent has extra data

        FirebaseFirestore dc = FirebaseFirestore.getInstance();
        //......
        dc.collection("JOB")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task ) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {


                                Map<String, Object> dataMap = document.getData();
                                String user = ((String) dataMap.get("User"));
                                int r=user.compareTo(receivedData);
                                Log.d("vivekpalnew", String.valueOf(r));
                                if (r==0) {

//
                                    jobTitleTextView.setText((String) dataMap.get("JOB Title"));
                                    organizername.setText((String) dataMap.get("JOB Organiser"));
                                    salaryEditText.setText((String) dataMap.get("Job Salary"));
                                    jobDescriptionContentTextView.setText((String) dataMap.get("JOB Description"));
                                    openingsEditText.setText((String) dataMap.get("JOB Opening"));
                                    dateofpost.setText((String) dataMap.get("date"));
                                    locationTextView.setText((String) dataMap.get("Location"));
                                    authorSpecialityTextView.setText((String) dataMap.get("Speciality"));
                                    authorSubSpecialityTextView.setText((String) dataMap.get("SubSpeciality"));
                                    companyNameTextView.setText((String) dataMap.get("Hospital"));
                                    jobtype.setText((String) dataMap.get("Job type"));


                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        recyclerView1 = findViewById(R.id.recyclerviewjobsinsider);
        List<jobitem1> joblist = new ArrayList<jobitem1>();


        FirebaseFirestore jobsinsider = FirebaseFirestore.getInstance();
        //......
        jobsinsider.collection("JOB")
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
                                jobitem1 c = new jobitem1(speciality, Organiser, Location, date, user,Title , Category);
                                joblist.add(c);

//                                // Pass the joblist to the adapter
//                                Log.d("speciality2", speciality);
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
}
