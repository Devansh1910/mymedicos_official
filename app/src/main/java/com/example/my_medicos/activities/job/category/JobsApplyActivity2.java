package com.example.my_medicos.activities.job.category;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_medicos.adapter.job.MyAdapter6;
import com.example.my_medicos.R;
import com.example.my_medicos.adapter.job.items.jobitem1;
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

public class JobsApplyActivity2 extends AppCompatActivity {
    RecyclerView  recyclerView1;
    String userEmail;
    String Jobid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_jobs2);


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            userEmail = currentUser.getEmail();
        }


        FirebaseFirestore dc = FirebaseFirestore.getInstance();
        //......
        dc.collection("JOBsApply")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task ) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {



                                Map<String, Object> dataMap = document.getData();
                                Jobid=(String) dataMap.get("Jobid");
                                String user = (String) dataMap.get("User");
                                int r=user.compareTo(userEmail);
                                Log.d("abcdefgh", String.valueOf(r));
                                if (r==0){
                                    fetch(Jobid);
                                }




//
//                                // Pass the joblist to the adapter
//                                Log.d("speciality2", speciality);




                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }
    List<jobitem1> joblist = new ArrayList<jobitem1>();
    void fetch(String Jobidn){
        recyclerView1 = findViewById(R.id.recyclerview7);

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
                                String Category = (String) dataMap.get("Job type");
                                String Title = (String) dataMap.get("JOB Title");
                                Log.d(user,"abc");
                                Log.d(Jobidn,"abc");
                                int r = Jobidn.compareTo(user);
                                Log.d("abcdefsfsd", String.valueOf(r));

                                if (r == 0) {
                                    jobitem1 c = new jobitem1(speciality, Organiser, Location, date, user, Title, Category);
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


}
