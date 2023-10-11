package com.example.my_medicos;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_medicos.ui.news.NewsActivity;
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
    MyAdapter adapter;
    RecyclerView recyclerView;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);





        recyclerView = rootView.findViewById(R.id.recyclerview_job1);

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


        List<jobitem> joblist = new ArrayList<jobitem>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false));
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

                                jobitem c = new jobitem(speciality, Organiser, Location,date);
                                joblist.add(c);
//                                Log.d("speciality2", speciality);
                                Log.d("speciality2", Organiser);
                                Log.d("speciality2", Location);
//
//                                // Pass the joblist to the adapter
//                                Log.d("speciality2", speciality);

                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                                adapter = new MyAdapter(getContext(),joblist); // Pass the joblist to the adapter
                                recyclerView.setAdapter(adapter);
//                                Log.d("speciality2", speciality);



                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

//        joblist.add(new jobitem("Dentist", "ESI hospital", "Hubli"));
//        joblist.add(new jobitem("Surgen", "Shushruta hospital", "Hubli"));
//        joblist.add(new jobitem("Gynacologist", "Tatvadarshi hospital", "Hubli"));
//        joblist.add(new jobitem("Pediatric", "KMC", "Hubli"));
//
//        adapter = new MyAdapter(getContext(),joblist); // Pass the joblist to the adapter
//        recyclerView.setAdapter(adapter);

        publication=rootView.findViewById(R.id.pub_image);
        publication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(),PublicationActivity.class);
                startActivity(i);
            }
        });


        return rootView;



    }


}
