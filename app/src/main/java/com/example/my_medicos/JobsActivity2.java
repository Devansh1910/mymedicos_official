package com.example.my_medicos;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JobsActivity2 extends AppCompatActivity {
    RecyclerView recyclerView1;
    String receivedData,Title1;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs2);

//        toolbar = findViewById(R.id.jobstoolbar);
//        setSupportActionBar(toolbar);
//        tab=findViewById(R.id.tabLayout);
//        viewPager=findViewById(R.id.view_pager);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if (intent != null) {
            receivedData = intent.getStringExtra("key");
            Title1=intent.getStringExtra("Title");


        }


        recyclerView1 = findViewById(R.id.recyclerview6);
        List<jobitem1> joblist = new ArrayList<jobitem1>();


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
                                String speciality = (String) dataMap.get("Speciality");
//                                Log.d("123456",speciality);

//
                                String Organiser = (String) dataMap.get("JOB Organiser");
                                String Location = (String) dataMap.get("Location");
                                String date = (String) dataMap.get("date");
                                String user = (String) dataMap.get("User");
                                String type=((String) dataMap.get("Job type"));
                                String Title=((String) dataMap.get("JOB Title"));
                                int r=speciality.compareTo(Title1);
                                Log.d("123456", String.valueOf(r));
                                if (r==0) {

//
                                    jobitem1 c = new jobitem1(speciality, Organiser, Location, date, user, Title);
                                    joblist.add(c);
                                }



//
//                                // Pass the joblist to the adapter
//                                Log.d("speciality2", speciality);
                                recyclerView1.setLayoutManager(new LinearLayoutManager(getApplication(),LinearLayoutManager.VERTICAL, false));
                                recyclerView1.setAdapter( new MyAdapter6(getApplication(),joblist));
                                Log.d("speciality2", speciality);



                            }
                            if (joblist.size()==0) {      

                                Toast.makeText(JobsActivity2.this, "No Content", Toast.LENGTH_SHORT).show();

                            }
                        }

                        else {

                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }


}
