package com.example.my_medicos;

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
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
        TextView salaryLabel = findViewById(R.id.salaryLabel);
        TextView salaryEditText = findViewById(R.id.salaryEditText);
        TextView openingsLabel = findViewById(R.id.openingsLabel);
        TextView openingsEditText = findViewById(R.id.openingsEditText);
        TextView experienceLabel = findViewById(R.id.experienceLabel);
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
                                    locationTextView.setText((String) dataMap.get("JOB Organiser"));
                                    salaryEditText.setText((String) dataMap.get("Job Salary"));
                                    jobDescriptionContentTextView.setText((String) dataMap.get("JOB Description"));
                                    openingsEditText.setText((String) dataMap.get("JOB Opening"));
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

// Now you can access and manipulate these views as needed in your Java code




    }
}
