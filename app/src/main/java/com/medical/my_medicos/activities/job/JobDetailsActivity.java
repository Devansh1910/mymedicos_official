package com.medical.my_medicos.activities.job;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.medical.my_medicos.adapter.job.Adapter8;
import com.medical.my_medicos.adapter.job.MyAdapter6;
import com.medical.my_medicos.R;
import com.medical.my_medicos.adapter.job.items.jobitem1;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.medical.my_medicos.adapter.ug.MyAdapter8;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JobDetailsActivity extends AppCompatActivity {

    Button shareButton;
    RecyclerView recyclerView1;
    MyAdapter8 adapter1;
    FloatingActionButton floatingActionButton;
    private ViewPager2 pager;
    private TabLayout tabLayout;
    RecyclerView recyclerView2;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String speciality,Organiser,Location;
    String receivedData,documentid;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    String current=user.getPhoneNumber();

    Toolbar toolbar;
    String documentid1;

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

        if (intent != null && intent.hasExtra("user")&&(intent.hasExtra("documentid"))) {
            // Retrieve the extra data
            receivedData = intent.getStringExtra("user");
            documentid=intent.getStringExtra("documentid");
            documentid1=intent.getStringExtra("documentid");

        }



        Toolbar toolbar = findViewById(R.id.jobsinsidertoolbar);

        // Set the support action bar
        setSupportActionBar(toolbar);

        // Set the navigation icon and listener
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.arrowbackforappbar);
        }

        // Set the click listener for the navigation icon
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button apply=findViewById(R.id.applyButton);
        Button alreadyapply=findViewById(R.id.Alreadyapplied);
        alreadyapply.setVisibility(View.GONE);
        LinearLayout applylinear=findViewById(R.id.applylinear);

        Button applycant=findViewById(R.id.applycant);
        apply.setVisibility(View.GONE);
        applycant.setVisibility(View.GONE);
        Log.d("received data",receivedData);
        int r=receivedData.compareTo(current);
        Log.d("received data1",receivedData);
        if (r==0){
            apply.setVisibility(View.GONE);
            applycant.setVisibility(View.VISIBLE);
            applycant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent j=new Intent(context, JobsApplicantActivty.class);
                    j.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    j.putExtra("user",receivedData);
                    j.putExtra("documentid",documentid);
                    context.startActivity(j);
                }
            });

        }

        else{
            apply.setVisibility(View.VISIBLE);
            applycant.setVisibility(View.GONE);
            apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent j=new Intent(context, JobsApplyActivity.class);
                    j.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    j.putExtra("user",receivedData);
                    j.putExtra("documentid",documentid);
                    context.startActivity(j);
                }
            });
        }

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
                                String user = ((String) dataMap.get("documentId"));

                                int r=0;
                                if (documentid1!=null) {
                                    r = documentid1.compareTo(user);
                                    Log.d("vivekpalnew", String.valueOf(r));
                                }
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
                                    String subSpeciality = (String) dataMap.get("SubSpeciality");
                                    if (subSpeciality != null && !subSpeciality.isEmpty()) {
                                        authorSubSpecialityTextView.setVisibility(View.VISIBLE);
                                        authorSubSpecialityTextView.setText(subSpeciality);
                                    } else {
                                        authorSubSpecialityTextView.setVisibility(View.GONE);
                                    }
                                    companyNameTextView.setText((String) dataMap.get("Hospital"));
                                    jobtype.setText((String) dataMap.get("Job type"));


                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}