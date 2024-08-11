package com.medical.my_medicos.activities.home.fragments;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.media3.common.MediaItem;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.cme.CmeActivity;
import com.medical.my_medicos.activities.fmge.activites.FmgeprepActivity;
import com.medical.my_medicos.activities.job.JobsActivity;
import com.medical.my_medicos.activities.memes.MemeActivity;
import com.medical.my_medicos.activities.neetss.activites.SsprepActivity;
import com.medical.my_medicos.activities.news.NewsActivity;
import com.medical.my_medicos.activities.news.NewsToday;
import com.medical.my_medicos.activities.news.TodayNewsAdapter;
import com.medical.my_medicos.activities.pg.activites.PgprepActivity;
import com.medical.my_medicos.activities.profile.Contactinfo;
import com.medical.my_medicos.activities.publications.activity.PublicationActivity;
import com.medical.my_medicos.activities.slideshow.PaidSlideshowAdapter;
import com.medical.my_medicos.activities.slideshow.Slideshow;
import com.medical.my_medicos.activities.ug.UgExamActivity;
import com.medical.my_medicos.activities.university.activity.UniversityActivity;
import com.medical.my_medicos.activities.utils.ConstantsDashboard;
import com.medical.my_medicos.adapter.cme.MyAdapter2;
import com.medical.my_medicos.adapter.cme.MyAdapter4;
import com.medical.my_medicos.adapter.cme.items.cmeitem2;
import com.medical.my_medicos.adapter.job.MyAdapter;
import com.medical.my_medicos.adapter.job.items.jobitem;
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
import com.medical.my_medicos.databinding.FragmentHomeBinding;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.prefs.Preferences;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.CirclePromptFocal;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private SwipeRefreshLayout swipeRefreshLayout;
    ImageView jobs, cme, news, publication, update, pg_prep, ugexams, vector_home;

    LinearLayout meme;
    MyAdapter adapterjob;
    MyAdapter2 adaptercme;
    String Speciality;
    private FirebaseStorage storage;
    CardView cardjobs, cardcme;
    LinearLayout progress ;
    TextView home1, home2, home3;
    RecyclerView recyclerViewjob;
    RecyclerView recyclerViewcme;
    private ExoPlayer player;
    TodayNewsAdapter todayNewsAdapter;
    ArrayList<NewsToday> newstoday;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    String videoURL = "https://res.cloudinary.com/dmzp6notl/video/upload/v1701512080/videoforhome_gzfpen.mp4";
    TextView navigatetojobs, navigatetocme, navigatecmeinsider, navigatenews;
    public static final String INTENT_KEY_SPECIALITY = "speciality";
    public static final String INTENT_KEY_USER_PHONE = "user_phone";

    private boolean dataLoaded = false;

    private PaidSlideshowAdapter paidslideshowAdapter;
    private ArrayList<Slideshow> paidslideshows;
    private Handler handler;
    private LinearLayout dotsLayout;
    private final int AUTO_SCROLL_DELAY = 3000;

    @SuppressLint({"MissingInflatedId", "RestrictedApi"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = requireActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(requireContext(), R.color.teal_700));
        }
        progress=rootView.findViewById(R.id.progress);
        db = FirebaseFirestore.getInstance();
        progress.setVisibility(View.GONE);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to PersonalInfoActivity
                Intent intent = new Intent(getActivity(), Contactinfo.class);
                startActivity(intent);
            }
        });
        checkUserProfile();
//        if (progress != null) {
//            progress.setVisibility(View.GONE);
//        }







        recyclerViewjob = rootView.findViewById(R.id.recyclerview_job1);
        handler = new Handler();
        ScrollView scrollView = rootView.findViewById(R.id.scrollerforthehome);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.beepsound);
        mediaPlayer.setVolume(0.1f, 0.1f);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            boolean isAtTop = scrollView.getScrollY() == 0;
            swipeRefreshLayout.setEnabled(isAtTop);
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            mediaPlayer.start();
            checkUserProfile();
            refreshData();
        });

        recyclerViewcme = rootView.findViewById(R.id.recyclerview_cme1);

        ugexams = rootView.findViewById(R.id.ugexams);
        ugexams.setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), UgExamActivity.class);
            startActivity(i);
        });

        pg_prep = rootView.findViewById(R.id.pg_prep);
        pg_prep.setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), PgprepActivity.class);
            startActivity(i);
        });

        home1 = rootView.findViewById(R.id.home1);
        home2 = rootView.findViewById(R.id.home2);
        home3 = rootView.findViewById(R.id.home3);

        home1.setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), CmeActivity.class);
            startActivity(i);
        });

        home2.setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), CmeActivity.class);
            startActivity(i);
        });

        home3.setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), CmeActivity.class);
            startActivity(i);
        });

        update = rootView.findViewById(R.id.university);
        update.setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), FmgeprepActivity.class);
            startActivity(i);
        });

        cme = rootView.findViewById(R.id.cme_img1);
        cme.setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), CmeActivity.class);
            startActivity(i);
        });

        jobs = rootView.findViewById(R.id.jobs_img);
        jobs.setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), JobsActivity.class);
            startActivity(i);
        });

        news = rootView.findViewById(R.id.news_home);
        news.setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), NewsActivity.class);
            startActivity(i);
        });

        navigatetojobs = rootView.findViewById(R.id.navigatejobs);
        navigatetojobs.setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), JobsActivity.class);
            startActivity(i);
        });

        cardjobs = rootView.findViewById(R.id.cardjobs);
        cardjobs.setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), JobsActivity.class);
            startActivity(i);
        });

        cardcme = rootView.findViewById(R.id.cardcme);
        cardcme.setVisibility(View.GONE);
        cardjobs.setVisibility(View.GONE);

        home1 = rootView.findViewById(R.id.home1);
        home2 = rootView.findViewById(R.id.home2);
        home3 = rootView.findViewById(R.id.home3);

        home1.setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), CmeActivity.class);
            startActivity(i);
        });

        home2.setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), CmeActivity.class);
            startActivity(i);
        });

        home3.setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), CmeActivity.class);
            startActivity(i);
        });

        navigatetocme = rootView.findViewById(R.id.navigatecme);
        navigatetocme.setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), CmeActivity.class);
            startActivity(i);
        });

        cardcme = rootView.findViewById(R.id.cardcme);
        cardcme.setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), CmeActivity.class);
            startActivity(i);
        });

        navigatecmeinsider = rootView.findViewById(R.id.navigatecmeinsider);
        navigatecmeinsider.setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), CmeActivity.class);
            startActivity(i);
        });

        meme = rootView.findViewById(R.id.meme);
        meme.setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), SsprepActivity.class);
            startActivity(i);
        });

        vector_home = rootView.findViewById(R.id.vector_home);
        vector_home.setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), PgprepActivity.class);
            startActivity(i);
        });

        FirebaseFirestore checking = FirebaseFirestore.getInstance();
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
                                String current = (String) dataMap.get("Phone Number");
                                if (current != null) {
                                    int a = current.compareTo(user.getPhoneNumber());
                                    if (a == 0) {
                                        Speciality = (String) dataMap.get("Interest");
                                        Log.d("Speciality", Speciality);
                                    }
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
                .orderBy("date", Query.Direction.ASCENDING)
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
                                Log.d("Error in Speciality", speciality);
                                String User = ((String) dataMap.get("User"));
                                Log.d("user2", user.getPhoneNumber());

                                if ((speciality != null) && (Speciality != null)) {
                                    int b = (user.getPhoneNumber()).compareTo(User);
                                    int a = Speciality.compareTo(speciality);
                                    Log.d("phonenumber", String.valueOf(b));
                                    if ((a == 0) && (b != 0)) {
                                        Log.d("Speciality", String.valueOf(a));
                                        Log.d("phonenumber", String.valueOf(b));

                                        jobitem c = new jobitem(speciality, Organiser, Location, date, Title, Category, documentid,User);
                                        joblist.add(c);
                                    }
                                }
                                Log.d("speciality2", speciality);

                                recyclerViewjob.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                                adapterjob = new MyAdapter(getContext(), joblist);
                                recyclerViewjob.setAdapter(adapterjob);
                            }
                            Log.d("Something went wrong!", joblist.toString());
                            if (joblist.isEmpty()) {
                                cardjobs.setVisibility(View.VISIBLE);
                                TextView nocontent = rootView.findViewById(R.id.descriptionTextView);
                            }
                        } else {
                        }

                    }
                });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<cmeitem2> myitem = new ArrayList<cmeitem2>();
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

                                    LocalTime parsedTime = null;
                                    LocalDate parsedDate = null;
                                    try {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            parsedTime = LocalTime.parse(Time, formatter);
                                            parsedDate = LocalDate.parse(date, formatter1);
                                        }
                                        System.out.println("Parsed Time: " + parsedTime);
                                    } catch (DateTimeParseException e) {
                                        System.err.println("Error parsing time: " + e.getMessage());
                                    }
                                    LocalTime currentTime = null;
                                    LocalDate currentDate = null;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        currentTime = LocalTime.now();
                                        currentDate = LocalDate.now();
                                    }

                                    if (parsedTime != null && currentTime != null) {
                                        int r = parsedTime.compareTo(currentTime);
                                        Log.d("vivek", String.valueOf(r));
                                        int r1 = parsedDate.compareTo(currentDate);
                                        if ((r1 <= 0)) {
                                            String field3 = ((String) dataMap.get("CME Title"));
                                            String field6 = ((String) dataMap.get("Mode"));
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
                                            Log.d("user2", user.getPhoneNumber());

                                            if ((Speciality != null) && (field2 != null)) {
                                                int a = Speciality.compareTo(field2);
                                                Log.d("user2", user.getPhoneNumber());
                                                int b = (user.getPhoneNumber()).compareTo(field5);
                                                Log.d("phonenumber", String.valueOf(b));
                                                Log.d("phonenumber", String.valueOf(b));
                                                cmeitem2 c = new cmeitem2(field1, field2, Date, field3, field4, 5, time, field5, "PAST", documentid, field6);
                                                if ((end != null) && (a == 0) && (b != 0)) {
                                                    myitem.add(c);

                                                }
                                            }

                                        } else if ((r < 0) && (r1 == 0)) {
                                            String field3 = ((String) dataMap.get("CME Title"));
                                            String field6 = ((String) dataMap.get("Mode"));
                                            String field4 = ((String) dataMap.get("CME Presenter"));
                                            String field1 = (String) dataMap.get("CME Organiser");
                                            String field2;
                                            String time = ((String) dataMap.get("Selected Time"));
                                            String field5 = ((String) dataMap.get("User"));
                                            field2 = ((String) dataMap.get("Speciality"));
                                            String Date = ((String) dataMap.get("Selected Date"));
                                            String documentid = ((String) dataMap.get("documentId"));
                                            String end = ((String) dataMap.get("endtime"));
                                            Log.d("user2", user.getPhoneNumber());

                                            if ((Speciality != null) && (field2 != null)) {
                                                int a = Speciality.compareTo(field2);
                                                int b = (user.getPhoneNumber()).compareTo(field5);
                                                Log.d("phonenumber", String.valueOf(b));
                                                Log.d("phonenumber", String.valueOf(b));
                                                cmeitem2 c = new cmeitem2(field1, field2, Date, field3, field4, 5, time, field5, "PAST", documentid, field6);
                                                if ((end != null) && (a == 0) && (b != 0)) {
                                                    myitem.add(c);

                                                }
                                            }

                                        }
                                    } else {
                                        Log.d(TAG, "Something went wrong", task.getException());
                                    }
                                }
                                Log.d("myitem", myitem.toString());
                                if (myitem.isEmpty()) {
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
            publication.setOnClickListener(view -> {
                Intent i = new Intent(getActivity(), PublicationActivity.class);
                startActivity(i);
            });

            navigatenews = rootView.findViewById(R.id.navigatenews);
            navigatenews.setOnClickListener(view -> {
                Intent i = new Intent(getActivity(), NewsActivity.class);
                startActivity(i);
            });

            initHomeSlider();
            if (!dataLoaded) {
                fetchdata();
                fetchUserData();
                checkUserProfile();
            }
            initSliderContent();
            initTodaysSlider();
        }
        return rootView;
    }

    private void checkUserProfile() {
        if (currentUser != null) {
            String phoneNumber = currentUser.getPhoneNumber();
            if (phoneNumber != null) {
                db.collection("users")
                        .whereEqualTo("Phone Number", phoneNumber)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();
                                if (!querySnapshot.isEmpty()) {
                                    DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                                    boolean isDetailsComplete = document.contains("Age")
                                            && document.contains("permanent")
                                            && document.contains("present");

                                    checkIfAvatarExists(isDetailsComplete);
                                } else {
                                    progress.setVisibility(View.VISIBLE);
                                }
                            } else {
                                progress.setVisibility(View.VISIBLE);
                            }
                        });
            } else {
                progress.setVisibility(View.VISIBLE);
            }
        } else {
            progress.setVisibility(View.VISIBLE);
        }
    }

        private void checkIfAvatarExists(boolean isDetailsComplete) {

            StorageReference avatarRef = storage.getReference().child("users/" + currentUser.getPhoneNumber() + "/profile_image.jpg");
            avatarRef.getDownloadUrl().addOnSuccessListener(uri -> {
                if (isDetailsComplete) {
                    Log.d("isdetailscomplete"+ isDetailsComplete,avatarRef.toString());
                    progress.setVisibility(View.GONE);
                } else {
                    progress.setVisibility(View.VISIBLE);
                }
            }).addOnFailureListener(exception -> {
                progress.setVisibility(View.VISIBLE);
            });
        }



    private void refreshData() {
        paidslideshows.clear();
        newstoday.clear();
        checkUserProfile();
        paidslideshowAdapter.notifyDataSetChanged();
        todayNewsAdapter.notifyDataSetChanged();

        getSlideshowRecent();
        getTodayNews();
        swipeRefreshLayout.setRefreshing(false);
    }

    void initSliderContent() {
        paidslideshows = new ArrayList<>();
        paidslideshowAdapter = new PaidSlideshowAdapter(getActivity(), paidslideshows);
        getSlideshowRecent();

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1, LinearLayoutManager.HORIZONTAL, false);

        binding.recyclerviewSlideshare1.setLayoutManager(layoutManager);
        binding.recyclerviewSlideshare1.setAdapter(paidslideshowAdapter);
    }

    void getSlideshowRecent() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        String url = ConstantsDashboard.GET_SLIDESHOW_HOME;
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if ("success".equals(object.optString("status"))) {
                    JSONArray dataArray = object.getJSONArray("data");
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject slideshowObj = dataArray.getJSONObject(i);

                        String fileUrl = slideshowObj.optString("file");
                        String title = slideshowObj.optString("title");
                        String type = slideshowObj.optString("type");

                        if (slideshowObj.has("images")) {
                            JSONArray imagesArray = slideshowObj.getJSONArray("images");
                            ArrayList<Slideshow.Image> images = new ArrayList<>();
                            for (int j = 0; j < imagesArray.length(); j++) {
                                JSONObject imageObj = imagesArray.getJSONObject(j);
                                String imageUrl = imageObj.optString("url");
                                String imageId = imageObj.optString("id");
                                images.add(new Slideshow.Image(imageId, imageUrl));
                            }
                            Slideshow slideshowItem = new Slideshow(title, images, fileUrl, type);
                            paidslideshows.add(slideshowItem);
                        } else {
                            Slideshow slideshowItem = new Slideshow(title, new ArrayList<>(), fileUrl, type);
                            paidslideshows.add(slideshowItem);
                        }
                    }
                    paidslideshowAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            // Handle error
        });

        queue.add(request);
    }

    void initTodaysSlider() {
        newstoday = new ArrayList<>();
        todayNewsAdapter = new TodayNewsAdapter(getContext(), newstoday);
        getTodayNews();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        binding.recyclerviewNews.setLayoutManager(layoutManager);
        binding.recyclerviewNews.setAdapter(todayNewsAdapter);
    }

    void getTodayNews() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        long twentyFourHoursAgo = calendar.getTimeInMillis();

        db.collection("MedicalNews")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            try {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.US);
                                Date newsDate = dateFormat.parse(document.getString("Time"));

                                long newsTime = newsDate.getTime();

                                if (newsTime >= twentyFourHoursAgo && "News".equals(document.getString("type"))) {
                                    NewsToday newsItemToday = new NewsToday(
                                            document.getId(),
                                            document.getString("Title"),
                                            document.getString("thumbnail"),
                                            document.getString("Description"),
                                            document.getString("subject"),
                                            document.getString("Time"),
                                            document.getString("URL")
                                    );
                                    newstoday.add(newsItemToday);
                                }
                            } catch (Exception e) {
                                Log.e("NewsActivity", "Error parsing timestamp", e);
                            }
                        }
                        todayNewsAdapter.notifyDataSetChanged();
                    } else {
                        // Handle the case where the data retrieval is not successful
                    }
                });
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
        RequestQueue queue = Volley.newRequestQueue(requireContext());

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

                                            Preferences preferences = Preferences.userRoot();
                                            preferences.put("username", userName);
                                            preferences.put("email", userEmail);
                                            preferences.put("location", userLocation);
                                            preferences.put("interest", userInterest);
                                            preferences.put("userphone", userPhone);
                                            preferences.put("docId", docID);
                                            preferences.put("prefix", userPrefix);

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
            String fullName = preferences.get("username", null);
            Log.d("Something went wrong", fullName);
        }
    }

    private void initHomeSlider() {
        getsliderHome();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        handler.removeCallbacksAndMessages(null);
    }

    private MyAsyncTask myAsyncTask;

    private void startAsyncTask() {
        if (isAdded()) {
            myAsyncTask = new MyAsyncTask();
            myAsyncTask.execute();
        }
    }

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
