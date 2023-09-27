package com.example.my_medicos;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import android.app.ProgressDialog;

public class PostJobActivity extends AppCompatActivity {
    EditText organisation,location,salary,contact,remaks;
    public FirebaseDatabase db = FirebaseDatabase.getInstance();
    FirebaseFirestore dc = FirebaseFirestore.getInstance();
    public DatabaseReference cmeref = db.getReference().child("CME's");
    String Speciality;
    Button post;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_job);
        organisation=findViewById(R.id.organization_name);
        location=findViewById(R.id.job_location);
        salary=findViewById(R.id.job_salary);
        contact=findViewById(R.id.contact_details);
        remaks=findViewById(R.id.remark);

        Spinner spinner2= (Spinner) findViewById(R.id.speciality);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> myadapter = ArrayAdapter.createFromResource(this,
                R.array.speciality, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        myadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner2.setAdapter(myadapter);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Speciality = spinner2.getSelectedItem().toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        post=findViewById(R.id.post1);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    post();
                }
            }
        });


    }
    public void post(){
        String Organisation = organisation.getText().toString().trim();
        String Location = location.getText().toString().trim();
        String Salary = salary.getText().toString().trim();
        String Contact = contact.getText().toString().trim();
        String Remark = remaks.getText().toString().trim();
        HashMap<String, Object> usermap = new HashMap<>();

        usermap.put("JOB Organiser", Organisation);
        usermap.put("JOB Location", Location);
        usermap.put("JOB Salary", Salary);
        usermap.put("Contact", Contact);
        usermap.put("Remark", Remark);


        cmeref.push().setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


                if (task.isSuccessful()) {
                    dc.collection("JOB").add(usermap);
                    Toast.makeText(PostJobActivity.this, "Posted Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PostJobActivity.this, "Task Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}