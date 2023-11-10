package com.example.my_medicos.activities.home.fragments;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_medicos.adapter.job.MyAdapter;
import com.example.my_medicos.adapter.cme.MyAdapter2;
import com.example.my_medicos.R;
import com.example.my_medicos.activities.cme.CmeActivity;
import com.example.my_medicos.activities.job.JobsActivity;
import com.example.my_medicos.activities.pg.PgprepActivity;
import com.example.my_medicos.activities.publications.PublicationActivity;
import com.example.my_medicos.activities.ug.UgExamActivity;
import com.example.my_medicos.activities.university.UniversityupdatesActivity;
import com.example.my_medicos.activities.publications.ui.news.NewsActivity;
import com.example.my_medicos.adapter.cme.items.cmeitem1;
import com.example.my_medicos.adapter.job.items.jobitem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    ImageView jobs,cme,news,publication,update,pg_prep,ugexams;
    MyAdapter adapterjob;
    MyAdapter2 adaptercme;

    RecyclerView recyclerViewjob;
    RecyclerView recyclerViewcme;
    TextView navigatetojobs, navigatetocme, navigatecmeinsider;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerViewjob = rootView.findViewById(R.id.recyclerview_job1);

        recyclerViewcme = rootView.findViewById(R.id.recyclerview_cme1);

        ugexams=rootView.findViewById(R.id.ugexams);
        ugexams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), UgExamActivity.class);
                startActivity(i);
            }
        });

        pg_prep=rootView.findViewById(R.id.pg_prep);
        pg_prep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(getActivity(), PgprepActivity.class);
                startActivity(i);
            }
        });

        update=rootView.findViewById(R.id.university);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(getActivity(), UniversityupdatesActivity.class);
                startActivity(i);
            }
        });

        cme=rootView.findViewById(R.id.cme_img1);

        cme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(), CmeActivity.class);
                startActivity(i);
            }
        });


        jobs = rootView.findViewById(R.id.jobs_img);

        jobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), JobsActivity.class);
                startActivity(i);
            }
        });

        news=rootView.findViewById(R.id.news_home);
        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(), NewsActivity.class);
                startActivity(i);
            }
        });

        navigatetojobs = rootView.findViewById(R.id.navigatejobs);

        navigatetojobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), JobsActivity.class);
                startActivity(i);
            }
        });

        navigatetocme=rootView.findViewById(R.id.navigatecme);

        navigatetocme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(), CmeActivity.class);
                startActivity(i);
            }
        });

        navigatecmeinsider=rootView.findViewById(R.id.navigatecmeinsider);

        navigatecmeinsider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(), CmeActivity.class);
                startActivity(i);
            }
        });


        List<jobitem> joblist = new ArrayList<jobitem>();
        recyclerViewjob.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false));
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
                                String speciality = ((String) dataMap.get("Speciality"));
                                String Organiser =((String) dataMap.get("JOB Organiser"));
                                String Location =((String) dataMap.get("Location"));
                                String date=((String) dataMap.get("date"));
                                String Title=((String) dataMap.get("JOB Title"));
                                String Category =((String) dataMap.get("Job type"));

                                jobitem c = new jobitem(speciality, Organiser, Location,date,Title,Category);
                                joblist.add(c);
                                Log.d("speciality2", Organiser);
                                Log.d("speciality2", Location);
//
//                                // Pass the joblist to the adapter
//                                Log.d("speciality2", speciality);

                                recyclerViewjob.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                                adapterjob = new MyAdapter(getContext(),joblist); // Pass the joblist to the adapter
                                recyclerViewjob.setAdapter(adapterjob);
//                                Log.d("speciality2", speciality);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        List<cmeitem1> cmelist = new ArrayList<cmeitem1>();
        recyclerViewcme.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false));
        FirebaseFirestore cmedc = FirebaseFirestore.getInstance();
        //......
        cmedc.collection("CME")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task ) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Map<String, Object> dataMap = document.getData();
                                String usercme = ((String) dataMap.get("User"));
                                String specialitycme =((String) dataMap.get("Speciality"));
                                String presentercme =((String) dataMap.get("CME Presenter"));
                                String titlecme=((String) dataMap.get("CME Title"));
                                String organizercme =((String) dataMap.get("CME Organiser"));
                                String Date=((String) dataMap.get("Selected Date"));
                                String time =((String) dataMap.get("Selected Time"));

                                cmeitem1 cme = new cmeitem1(usercme, specialitycme,Date, titlecme,presentercme,organizercme,5,time);
                                cmelist.add(cme);
                                recyclerViewcme.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                                adaptercme = new MyAdapter2(getContext(),cmelist); // Pass the cmelist to the adapter
                                recyclerViewcme.setAdapter(adaptercme);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        publication=rootView.findViewById(R.id.pub_image);
        publication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(), PublicationActivity.class);
                startActivity(i);
            }
        });


        return rootView;



    }


}
