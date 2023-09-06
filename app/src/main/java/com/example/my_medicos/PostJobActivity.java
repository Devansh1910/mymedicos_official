package com.example.my_medicos;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
public class PostJobActivity extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    String current=user.getEmail();

    TextView joborg,jobloc,jobsal,jobcont,jobremark;
    Button postjob;
    FirebaseAuth.AuthStateListener authStateListener = firebaseAuth -> {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            System.out.println(currentUser);


            // User is signed in
        } else {
            // User is signed out
        }
    };





    FirebaseFirestore dc=FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_job);

        Spinner spinner2= (Spinner) findViewById(R.id.speciality);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> myadapter = ArrayAdapter.createFromResource(this,
                R.array.speciality, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        myadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner2.setAdapter(myadapter);

        joborg=findViewById(R.id.organization_name);
        jobloc=findViewById(R.id.job_location);
        jobsal=findViewById(R.id.job_salary);
        jobcont=findViewById(R.id.contact_details);
        jobremark=findViewById(R.id.remark);

        postjob=findViewById(R.id.post_job);
        postjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postJob();
            }
        });

    }
    public void postJob() {
        String organisation = joborg.getText().toString().trim();
        String location = jobloc.getText().toString().trim();
        String salary = jobsal.getText().toString().trim();
        String contact = jobcont.getText().toString().trim();
        String remark= jobremark.getText().toString().trim();




        if (TextUtils.isEmpty(organisation)) {
            joborg.setError("Organisation Required");
            return;
        }
        if (TextUtils.isEmpty(location)) {
            jobloc.setError("Location Required");
            return;
        }
        if (TextUtils.isEmpty(salary)) {
            jobsal.setError("Salary Required");
            return;
        }
        if (TextUtils.isEmpty(contact)) {
            jobcont.setError("Contact Required");
        }
        LocalDate currentDate = null;
        LocalTime currentTime=null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();
            currentTime = LocalTime.now();

        }


        HashMap<String, Object> usermap = new HashMap<>();


        usermap.put("Organisation", organisation);


        usermap.put("Location", location);
        usermap.put("Salary", salary);
        usermap.put("Contact", contact);

        usermap.put("Reamark", remark);
        usermap.put("User",current);
        System.out.println(usermap);
        dc.collection("JOB").add(usermap);










    }

}