package com.example.my_medicos.activities.home.fragments;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.media3.common.MediaItem;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.my_medicos.R;
import com.example.my_medicos.activities.cme.CmeActivity;
import com.example.my_medicos.activities.job.JobsActivity;
import com.example.my_medicos.activities.memes.MemeActivity;
import com.example.my_medicos.activities.news.NewsActivity;
import com.example.my_medicos.activities.pg.activites.PgprepActivity;
import com.example.my_medicos.activities.publications.activity.PublicationActivity;
import com.example.my_medicos.activities.ug.UgExamActivity;
import com.example.my_medicos.activities.university.activity.UniversityActivity;
import com.example.my_medicos.activities.utils.ConstantsDashboard;
import com.example.my_medicos.adapter.cme.MyAdapter2;
import com.example.my_medicos.adapter.cme.MyAdapter4;
import com.example.my_medicos.adapter.cme.items.cmeitem2;
import com.example.my_medicos.adapter.job.MyAdapter;
import com.example.my_medicos.adapter.job.items.jobitem;
import com.example.my_medicos.databinding.FragmentHomeBinding;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding; // Declare binding variable
    ImageView jobs,cme,news,publication,update,pg_prep,ugexams,meme;
    MyAdapter adapterjob;
    MyAdapter2 adaptercme;
    String Speciality;
    CardView cardjobs,cardcme;
    TextView home1,home2,home3,personname,personsuffix;
    RecyclerView recyclerViewjob;
    RecyclerView recyclerViewcme;
    private ExoPlayer player;
    String videoURL = "https://res.cloudinary.com/dmzp6notl/video/upload/v1701512080/videoforhome_gzfpen.mp4";
    TextView navigatetojobs, navigatetocme, navigatecmeinsider;

    private ViewFlipper viewFlipper;
    private boolean dataLoaded = false;
    private Handler handler;
    private LinearLayout dotsLayout;
    private final int AUTO_SCROLL_DELAY = 3000;
    @SuppressLint({"MissingInflatedId", "RestrictedApi"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
//
        StyledPlayerView playerView = rootView.findViewById(R.id.player_view_home);
        player = new ExoPlayer.Builder(requireContext()).build();
        playerView.setPlayer(player);

        recyclerViewjob = rootView.findViewById(R.id.recyclerview_job1);

        viewFlipper = rootView.findViewById(R.id.viewFlipper);
        dotsLayout = rootView.findViewById(R.id.dotsLayout);
        handler = new Handler();

        addDots();

        handler.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY);

        recyclerViewcme = rootView.findViewById(R.id.recyclerview_cme1);

        personname = rootView.findViewById(R.id.personnamewillbehere);

        personsuffix = rootView.findViewById(R.id.personsuffix);


        ugexams = rootView.findViewById(R.id.ugexams);
        ugexams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), UgExamActivity.class);
                startActivity(i);
            }
        });

        pg_prep = rootView.findViewById(R.id.pg_prep);
        pg_prep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), PgprepActivity.class);
                startActivity(i);
            }
        });
        home1=rootView.findViewById(R.id.home1);
        home2=rootView.findViewById(R.id.home2);
        home3=rootView.findViewById(R.id.home3);
        home1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), CmeActivity.class);
                startActivity(i);
            }
        });
        home2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), CmeActivity.class);
                startActivity(i);
            }
        });
        home3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), CmeActivity.class);
                startActivity(i);
            }
        });
        update = rootView.findViewById(R.id.university);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), UniversityActivity.class);
                startActivity(i);
            }
        });

        cme = rootView.findViewById(R.id.cme_img1);

        cme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), CmeActivity.class);
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

        news = rootView.findViewById(R.id.news_home);
        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), NewsActivity.class);
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
        cardcme=rootView.findViewById(R.id.cardcme);
        cardjobs=rootView.findViewById(R.id.cardjobs);
        cardcme.setVisibility(View.GONE);
        cardjobs.setVisibility(View.GONE);
        home1=rootView.findViewById(R.id.home1);
        home2=rootView.findViewById(R.id.home2);
        home3=rootView.findViewById(R.id.home3);
        home1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), CmeActivity.class);
                startActivity(i);
            }
        });
        home2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), CmeActivity.class);
                startActivity(i);
            }
        });
        home3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), CmeActivity.class);
                startActivity(i);
            }
        });

        navigatetocme = rootView.findViewById(R.id.navigatecme);

        navigatetocme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), CmeActivity.class);
                startActivity(i);
            }
        });

        navigatecmeinsider = rootView.findViewById(R.id.navigatecmeinsider);

        navigatecmeinsider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), CmeActivity.class);
                startActivity(i);
            }
        });

        meme = rootView.findViewById(R.id.meme);

        meme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MemeActivity.class);
                startActivity(i);
            }
        });

        FirebaseFirestore checking = FirebaseFirestore.getInstance();
// ......
        checking.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            FirebaseUser user = auth.getCurrentUser();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> dataMap = document.getData();
                                String phoneNumberFromFirestore = (String) dataMap.get("Phone Number");

                                // Check if the logged-in user's phone number matches the one in Firestore
//                                if (user != null && user.getPhoneNumber() != null
//                                        && user.getPhoneNumber().equals(phoneNumberFromFirestore)) {
//                                    // Skip this document as it belongs to the currently logged-in user
//                                    continue;
//                                }

                                // Check if the Interest field matches
                                String current = (String) dataMap.get("Phone Number");
                                int a = current.compareTo(user.getPhoneNumber());

                                if (a == 0) {
                                    Speciality = (String) dataMap.get("Interest");
                                    Log.d("Speciality", Speciality);
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        List<jobitem> joblist = new ArrayList<jobitem>();
        recyclerViewjob.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        FirebaseFirestore dc = FirebaseFirestore.getInstance();
        dc.collection("JOB")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            FirebaseUser user = auth.getCurrentUser();
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Map<String, Object> dataMap = document.getData();
                                String speciality = ((String) dataMap.get("Speciality"));
                                String Organiser = ((String) dataMap.get("JOB Organiser"));
                                String Location = ((String) dataMap.get("Location"));
                                String date = ((String) dataMap.get("date"));
                                String Title = ((String) dataMap.get("JOB Title"));
                                String Category = ((String) dataMap.get("Job type"));
                                String documentid = ((String) dataMap.get("documentId"));
                                Log.d("Speacility21",speciality);
                                String User=((String) dataMap.get("User"));
                                Log.d("user2",user.getPhoneNumber());


                                if ((speciality!=null)&&(Speciality!=null)) {
                                    int b=(user.getPhoneNumber()).compareTo(User);
                                    int a = Speciality.compareTo(speciality);
                                    Log.d("phonenumber", String.valueOf(b));
                                    if ((a == 0)&&(b != 0)) {
                                        Log.d("Speciality",String.valueOf(a));
                                        Log.d("phonenumber", String.valueOf(b));

                                        jobitem c = new jobitem(speciality, Organiser, Location, date, Title, Category, documentid);
                                        joblist.add(c);
                                    }
                                }
                                Log.d("speciality2", speciality);

                                recyclerViewjob.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                                adapterjob = new MyAdapter(getContext(), joblist); // Pass the joblist to the adapter
                                recyclerViewjob.setAdapter(adapterjob);
                            }
                            Log.d("abcdef", joblist.toString());
                            if (joblist.isEmpty()){
                                cardjobs.setVisibility(View.VISIBLE);
                                TextView nocontent=rootView.findViewById(R.id.descriptionTextView);


                            }
                        }
                        else {
                        }

                    }
                });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<cmeitem2> myitem = new ArrayList<cmeitem2>();
        //......
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Query query = db.collection("CME").orderBy("Time", Query.Direction.DESCENDING);

            query.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                FirebaseAuth auth = FirebaseAuth.getInstance();
                                FirebaseUser user = auth.getCurrentUser();
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    Map<String, Object> dataMap = document.getData();
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                                    DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                    String Time = document.getString("Selected Time");
                                    String date = document.getString("Selected Date");

                                    //
                                    LocalTime parsedTime = null;
                                    LocalDate parsedDate = null;
                                    try {
                                        // Parse the time string into a LocalTime object
                                        parsedTime = null;
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            parsedTime = LocalTime.parse(Time, formatter);
                                            parsedDate = LocalDate.parse(date, formatter1);

                                        }

                                        // Display the parsed time
                                        System.out.println("Parsed Time: " + parsedTime);
                                    } catch (DateTimeParseException e) {
                                        // Handle parsing error, e.g., if the input string is in the wrong format
                                        System.err.println("Error parsing time: " + e.getMessage());

                                    }
                                    LocalTime currentTime = null;
                                    LocalDate currentDate = null;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        currentTime = LocalTime.now();
                                        currentDate = LocalDate.now();
                                    }


                                    int r = parsedTime.compareTo(currentTime);

                                    Log.d("vivek", String.valueOf(r));
                                    int r1 = parsedDate.compareTo(currentDate);
                                    if ((r1 <= 0)) {
                                        String field3 = ((String) dataMap.get("CME Title"));

                                        List<String> presenters = (List<String>) dataMap.get("CME Presenter");
                                        String field4 = presenters.get(0);
                                        String field1 = (String) dataMap.get("CME Organiser");
                                        String field2;
                                        String field5 = ((String) dataMap.get("User"));
                                        field2 = ((String) dataMap.get("Speciality"));
                                        String Date = ((String) dataMap.get("Selected Date"));
                                        String time = ((String) dataMap.get("Selected Time"));
                                        String documentid = ((String) dataMap.get("documentId"));
                                        String end = ((String) dataMap.get("endtime"));
                                        Log.d("user2",user.getPhoneNumber());

                                        if ((Speciality!=null)&&(field2!=null)) {
                                            int a = Speciality.compareTo(field2);
                                            Log.d("user2",user.getPhoneNumber());
                                            int b=(user.getPhoneNumber()).compareTo(field5);
                                            Log.d("phonenumber", String.valueOf(b));
                                            Log.d("phonenumber", String.valueOf(b));
                                            cmeitem2 c = new cmeitem2(field1, field2, Date, field3, field4, 5, time, field5, "PAST", documentid);
                                            if ((end != null) && (a == 0)&&(b!=0)) {
                                                myitem.add(c);

                                            }
                                        }


                                    } else if ((r < 0) && (r1 == 0)) {
                                        String field3 = ((String) dataMap.get("CME Title"));
                                        String field4 = ((String) dataMap.get("CME Presenter"));
                                        String field1 = (String) dataMap.get("CME Organiser");
                                        String field2;
                                        String time = ((String) dataMap.get("Selected Time"));
                                        String field5 = ((String) dataMap.get("User"));
                                        field2 = ((String) dataMap.get("Speciality"));
                                        String Date = ((String) dataMap.get("Selected Date"));
                                        String documentid = ((String) dataMap.get("documentId"));
                                        String end = ((String) dataMap.get("endtime"));
                                        Log.d("user2",user.getPhoneNumber());


                                        if ((Speciality!=null)&&(field2!=null)) {
                                            int a = Speciality.compareTo(field2);
                                            int b=(user.getPhoneNumber()).compareTo(field5);
                                            Log.d("phonenumber", String.valueOf(b));
                                            Log.d("phonenumber", String.valueOf(b));
                                            cmeitem2 c = new cmeitem2(field1, field2, Date, field3, field4, 5, time, field5, "PAST", documentid);
                                            if ((end != null) && (a == 0)&&(b!=0)) {
                                                myitem.add(c);

                                            }
                                        }

                                    }

                                }
                                Log.d("myitem", myitem.toString());
                                if (myitem.isEmpty()){
                                    cardcme.setVisibility(View.VISIBLE);
                                }
                                recyclerViewcme.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                                recyclerViewcme.setAdapter(new MyAdapter4(getContext(), myitem));
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });

            publication = rootView.findViewById(R.id.pub_image);
            publication.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getActivity(), PublicationActivity.class);
                    startActivity(i);
                }
            });
            player = new ExoPlayer.Builder(requireContext()).build();
            playerView.setPlayer(player);

            MediaItem mediaItem = null;
            if (videoURL != null) {
                mediaItem = MediaItem.fromUri(videoURL);
            }
//
//            if (mediaItem != null) {
//                player.setMediaItem(mediaItem);
//                player.prepare();
//                player.setPlayWhenReady(true);
//            }

            initHomeSlider();
            if (!dataLoaded) {
                fetchdata();
                fetchUserData();
            }
        }
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (player != null) {
            player.setPlayWhenReady(false);
            player.release();
            player = null;
        }
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

    private void fetchUserData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getPhoneNumber();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    String docID = document.getId();
                                    Map<String, Object> dataMap = document.getData();
                                    String field1 = (String) dataMap.get("Phone Number");

                                    if (field1 != null && currentUser.getPhoneNumber() != null) {
                                        int a = field1.compareTo(currentUser.getPhoneNumber());
                                        if (a == 0) {
                                            String userName = (String) dataMap.get("Name");
                                            String userEmail = (String) dataMap.get("Email ID");
                                            String userLocation = (String) dataMap.get("Location");
                                            String userInterest = (String) dataMap.get("Interest");
                                            String userPhone = (String) dataMap.get("Phone Number");
                                            String userPrefix = (String) dataMap.get("Prefix");
                                            String userAuthorized = (String) dataMap.get("authorized");

                                            Boolean mcnVerified = (Boolean) dataMap.get("MCN verified");

                                            Preferences preferences = Preferences.userRoot();
                                            preferences.put("username", userName);
                                            preferences.put("email", userEmail);
                                            preferences.put("location", userLocation);
                                            preferences.put("interest", userInterest);
                                            preferences.put("userphone", userPhone);
                                            preferences.put("docId", docID);
                                            preferences.put("prefix", userPrefix);
                                            personname.setText(userName);

                                            fetchdata();
                                        }
                                    } else {
                                        Log.e(TAG, "Field1 or currentUser.getPhoneNumber() is null");
                                    }
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }
    private void fetchdata() {
        Preferences preferences = Preferences.userRoot();
        if (preferences.get("username", null) != null) {
            System.out.println("Key '" + "username" + "' exists in preferences.");
            String username = preferences.get("username", null);
            Log.d("usernaem", username);
        }
        String username = preferences.get("username", "");
        String userPrefix = preferences.get("prefix", "");
        personname.setText(username);
        personsuffix.setText(userPrefix);

    }

    private void initHomeSlider() {
        getsliderHome();
    }{}

    private final Runnable autoScrollRunnable = new Runnable() {
        @Override
        public void run() {
            int currentChildIndex = viewFlipper.getDisplayedChild();
            int nextChildIndex = (currentChildIndex + 1) % viewFlipper.getChildCount();
            viewFlipper.setDisplayedChild(nextChildIndex);
            updateDots(nextChildIndex);
            handler.postDelayed(this, AUTO_SCROLL_DELAY);
        }
    };

    @SuppressLint("UseCompatLoadingForDrawables")
    private void addDots() {
        for (int i = 0; i < viewFlipper.getChildCount(); i++) {
            ImageView dot = new ImageView(requireContext());
            dot.setImageDrawable(getResources().getDrawable(R.drawable.inactive_thumb));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 0, 8, 0);
            dotsLayout.addView(dot, params);
        }
        updateDots(0);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        handler.removeCallbacksAndMessages(null);
    }

    private MyAsyncTask myAsyncTask;
    private void startAsyncTask() {
        if (isAdded()) {
            // Start the AsyncTask
            myAsyncTask = new MyAsyncTask();
            myAsyncTask.execute();
        }
    }

    // AsyncTask example
    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            if (isAdded()) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (isAdded()) {

            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (myAsyncTask != null && !myAsyncTask.isCancelled()) {
            myAsyncTask.cancel(true);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void updateDots(int currentDotIndex) {
        if (!isAdded()) {
            return;
        }

        for (int i = 0; i < dotsLayout.getChildCount(); i++) {
            ImageView dot = (ImageView) dotsLayout.getChildAt(i);
            dot.setImageDrawable(getResources().getDrawable(
                    i == currentDotIndex ? R.drawable.custom_thumb : R.drawable.inactive_thumb
            ));
        }
    }
}