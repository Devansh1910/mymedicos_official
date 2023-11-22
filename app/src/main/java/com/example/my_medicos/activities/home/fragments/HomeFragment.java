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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.my_medicos.activities.memes.MemeActivity;
import com.example.my_medicos.activities.news.News;
import com.example.my_medicos.activities.news.NewsActivity;
import com.example.my_medicos.activities.news.NewsAdapter;
import com.example.my_medicos.activities.utils.ConstantsDashboard;
import com.example.my_medicos.adapter.job.MyAdapter;
import com.example.my_medicos.adapter.cme.MyAdapter2;
import com.example.my_medicos.R;
import com.example.my_medicos.activities.cme.CmeActivity;
import com.example.my_medicos.activities.job.JobsActivity;
import com.example.my_medicos.activities.pg.activites.PgprepActivity;
import com.example.my_medicos.activities.publications.activity.PublicationActivity;
import com.example.my_medicos.activities.ug.UgExamActivity;
import com.example.my_medicos.activities.university.activity.UniversityActivity;
import com.example.my_medicos.adapter.cme.items.cmeitem1;
import com.example.my_medicos.adapter.job.items.jobitem;
import com.example.my_medicos.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding; // Declare binding variable
    ImageView jobs,cme,news,publication,update,pg_prep,ugexams,meme;
    MyAdapter adapterjob;
    MyAdapter2 adaptercme;

    RecyclerView recyclerViewjob;
    RecyclerView recyclerViewcme;
    TextView navigatetojobs, navigatetocme, navigatecmeinsider;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

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
                Intent i =new Intent(getActivity(), UniversityActivity.class);
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

        meme=rootView.findViewById(R.id.meme);

        meme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(), MemeActivity.class);
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
                                String documentid =((String) dataMap.get("documentId"));

                                jobitem c = new jobitem(speciality, Organiser, Location,date,Title,Category,documentid);
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
                                String documentid=((String) dataMap.get("documentid"));

                                cmeitem1 cme = new cmeitem1(usercme, specialitycme,Date, titlecme,presentercme,organizercme,5,time,documentid);
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
        initHomeSlider();

        return rootView;
    }
    void getsliderHome() {
        RequestQueue queue = Volley.newRequestQueue(requireContext()); // Use requireContext()

        StringRequest request = new StringRequest(Request.Method.GET, ConstantsDashboard.GET_HOME_SLIDER_URL, response -> {
            try {
                JSONArray newssliderArray = new JSONArray(response);
                for (int i = 0; i < newssliderArray.length(); i++) {
                    JSONObject childObj = newssliderArray.getJSONObject(i);
                    binding.homecarousel.addData(
                            new CarouselItem(
                                    childObj.getString("url"),
                                    childObj.getString("action")
                            )
                    );
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            // Handle error if needed
        });

        queue.add(request);
    }

    private void initHomeSlider() {
        getsliderHome();
    }


}
