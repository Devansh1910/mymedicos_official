package com.example.my_medicos.activities.pg.activites;

import static androidx.media3.common.MediaLibraryInfo.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.media3.common.util.UnstableApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.my_medicos.R;
import com.example.my_medicos.activities.news.News;
import com.example.my_medicos.activities.news.NewsAdapter;
import com.example.my_medicos.activities.pg.adapters.QuestionBankPGAdapter;
import com.example.my_medicos.activities.pg.model.QuestionPG;
import com.example.my_medicos.activities.publications.model.Category;
import com.example.my_medicos.activities.utils.ConstantsDashboard;
import com.example.my_medicos.activities.pg.activites.insiders.SpecialityPGInsiderActivity;
import com.example.my_medicos.activities.pg.adapters.PerDayPGAdapter;
import com.example.my_medicos.activities.pg.adapters.SpecialitiesPGAdapter;
import com.example.my_medicos.activities.pg.adapters.VideoPGAdapter;
import com.example.my_medicos.activities.pg.model.PerDayPG;
import com.example.my_medicos.activities.pg.model.SpecialitiesPG;
import com.example.my_medicos.activities.pg.model.VideoPG;
import com.example.my_medicos.activities.publications.adapters.ProductAdapter;
import com.example.my_medicos.activities.publications.model.Product;
import com.example.my_medicos.activities.publications.utils.Constants;
import com.example.my_medicos.adapter.cme.items.cmeitem4;
import com.example.my_medicos.databinding.ActivityPgprepBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mancj.materialsearchbar.MaterialSearchBar;
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

public class  PgprepActivity extends AppCompatActivity {

    ActivityPgprepBinding binding;

    PerDayPGAdapter  PerDayPGAdapter;

    ArrayList<PerDayPG> dailyquestionspg;
    SpecialitiesPGAdapter specialitiesPGAdapter;
    ArrayList<SpecialitiesPG> specialitiespost;
    CardView nocardp;

    QuestionBankPGAdapter questionsAdapter;
    ArrayList<QuestionPG> questionsforpg;

    VideoPGAdapter videosAdapter;
    ArrayList<VideoPG> videoforpg;
    String quiztiddaya;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPgprepBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        LinearLayout toTheCart = findViewById(R.id.totheccart);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String user=currentUser.getPhoneNumber();
        nocardp=findViewById(R.id.nocardpg);
        nocardp.setVisibility(View.GONE);

//        // Initialize SwipeRefreshLayout
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this::refreshContent);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

//        toTheCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showStreakDialog();
//            }
//        });

        Log.d("questionid","questionid");


        String userId = currentUser.getPhoneNumber();


        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @OptIn(markerClass = UnstableApi.class) @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                Map<String, Object> dataMap = document.getData();
                                String field1 = (String) dataMap.get("Phone Number");

                                if (field1 != null && currentUser.getPhoneNumber() != null) {
                                    int a = field1.compareTo(userId);
                                    Log.d("questionid2",String.valueOf(a));

                                    if (a == 0) {

                                        Log.d("questionid1", String.valueOf(a));


                                        quiztiddaya = ((String) dataMap.get("QuizToday"));

                                        break;
                                    }
                                    else {
                                        quiztiddaya = null;

                                    }


                                }
                            }

                        } else {
                            Log.d(ContentValues.TAG, "Error getting documents: ", task.getException());
                        }
                    }

                });


        binding.searchBarforpg.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                Intent intent = new Intent(PgprepActivity.this, SearchPgActivity.class);
                intent.putExtra("query", text.toString());
                startActivity(intent);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
        if (quiztiddaya!=null) {
            Log.d("questionid", quiztiddaya);


        }
        initPerDayQuestions(quiztiddaya);
        initSpecialities();
        initQuestionsBanks();
        initVideosBanks();
        initSliderPg();

    }
    private void fetchStreakData(String userId, TextView userStreak) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersCollection = db.collection("users");
        usersCollection.document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String streakValue = documentSnapshot.getString("Streak");

                            userStreak.setText(streakValue);
                        } else {
                            userStreak.setText("N/A");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        userStreak.setText("Error");
                    }
                });
    }

    private void refreshContent() {
        clearData();
        fetchData();
        swipeRefreshLayout.setRefreshing(false);
    }
    private void clearData() {
        dailyquestionspg.clear();
        specialitiespost.clear();
        questionsforpg.clear();
        videoforpg.clear();
    }
    private void fetchData() {
        getPerDayQuestions(quiztiddaya);
        getSpecialityPG();
        getRecentQuestions();
        getRecentVideos();
    }
    private void initSliderPg() {
        getRecentPgSlider();
    }

    void getRecentPgSlider() {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, ConstantsDashboard.GET_PG_SLIDER_URL, response -> {
            try {
                JSONArray pgsliderArray = new JSONArray(response);
                for (int i = 0; i < pgsliderArray.length(); i++) {
                    JSONObject childObj = pgsliderArray.getJSONObject(i);
                    binding.carousel.addData(
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

    // this is for the perdayquestions
    void initPerDayQuestions(String QuiaToday) {
        dailyquestionspg = new ArrayList<>();
        PerDayPGAdapter = new PerDayPGAdapter(this, dailyquestionspg);

        getPerDayQuestions(QuiaToday);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        binding.perdayquestions.setLayoutManager(layoutManager);
        binding.perdayquestions.setAdapter(PerDayPGAdapter);
    }

    void getPerDayQuestions( String quiz) {
        Log.d("DEBUG", "getPerDayQuestions: Making API request");
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = ConstantsDashboard.GET_DAILY_QUESTIONS_URL;
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                Log.d("DEBUG", "getPerDayQuestions: Response received");
                JSONObject object = new JSONObject(response);
                if(object.getString("status").equals("success")){
                    JSONArray perdayArray = object.getJSONArray("data");

                    for(int i = 0; i < perdayArray.length(); i++) {
                        JSONObject childObj = perdayArray.getJSONObject(i);
                        PerDayPG perday = new PerDayPG(
                                childObj.getString("Question"),
                                childObj.getString("A"),
                                childObj.getString("B"),
                                childObj.getString("C"),
                                childObj.getString("D"),
                                childObj.getString("Correct"),
                                childObj.getString("id")
                        );
                        String questionId = childObj.getString("id");
                        Log.d("questionid",questionId);

                        if ((questionId!=null)&&(quiztiddaya!=null)) {

                            int a = questionId.compareTo(quiztiddaya);
                            Log.d("questionid", String.valueOf(a));

                            if (a != 0) {
                                dailyquestionspg.add(perday);
                            }
                            else{
                                nocardp.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    if (dailyquestionspg != null) {

                        PerDayPGAdapter.notifyDataSetChanged();
                        Log.d("DEBUG", "getPerDayQuestions: Data added to the list");

                    }
                    else{
                        nocardp.setVisibility(View.VISIBLE);

                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.e("API_ERROR", "Error in API request: " + error.getMessage());
        });
        queue.add(request);
    }

    void initSpecialities() {
        specialitiespost = new ArrayList<>();
        specialitiesPGAdapter = new SpecialitiesPGAdapter(this, specialitiespost);

        getSpecialityPG();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.specialityList.setLayoutManager(layoutManager);
        binding.specialityList.setAdapter(specialitiesPGAdapter);
    }

    void getSpecialityPG() {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, ConstantsDashboard.GET_SPECIALITY, new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("err", response);
                    JSONObject mainObj = new JSONObject(response);
                    if (mainObj.getString("status").equals("success")) {
                        JSONArray specialityArray = mainObj.getJSONArray("data");

                        for (int i = 0; i < specialityArray.length(); i++) {
                            JSONObject object = specialityArray.getJSONObject(i);
                            SpecialitiesPG speciality = new SpecialitiesPG(
                                    object.getString("id"),
                                    object.getInt("priority")
                            );
                            specialitiespost.add(speciality);
                            Log.e("Something went wrong..", object.getString("priority"));
                        }

                        binding.specialityList.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                            @Override
                            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                                View child = rv.findChildViewUnder(e.getX(), e.getY());
                                int position = rv.getChildAdapterPosition(child);

                                if (position != RecyclerView.NO_POSITION) {
                                    if (position == specialitiespost.size() - 1 && specialitiespost.get(position).getPriority() == -1) {
                                        Intent intent = new Intent(PgprepActivity.this, SpecialityPGInsiderActivity.class);
                                        startActivity(intent);
                                    } else {
                                    }
                                }
                                return false;
                            }

                            @Override
                            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                            }

                            @Override
                            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                            }
                        });

                        specialitiesPGAdapter.notifyDataSetChanged();
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle the error response
            }
        });

        queue.add(request);
    }
    void initQuestionsBanks() {
        questionsforpg = new ArrayList<>();
        questionsAdapter = new QuestionBankPGAdapter(this, questionsforpg);

        getRecentQuestions();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.questionbankList.setLayoutManager(layoutManager);
        binding.questionbankList.setAdapter(questionsAdapter);
    }

    void getRecentQuestions() {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = ConstantsDashboard.GET_PG_QUESTIONBANK_URL_HOME;
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if(object.getString("status").equals("success")){
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject childObj = array.getJSONObject(i);
                        QuestionPG questionbankItem = new QuestionPG(
                                childObj.getString("Title"),
                                childObj.getString("Description"),
                                childObj.getString("Time"),
                                childObj.getString("file")
                        );
                        questionsforpg.add(questionbankItem);
                    }
                    questionsAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> { });

        queue.add(request);
    }

    // this is for the videobanks
    void initVideosBanks() {
        videoforpg = new ArrayList<>();
        videosAdapter = new VideoPGAdapter(this, videoforpg);

        getRecentVideos();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.videobankList.setLayoutManager(layoutManager);
        binding.videobankList.setAdapter(videosAdapter);
    }

    void getRecentVideos() {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = ConstantsDashboard.GET_PG_VIDEOS_URL_HOME;
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if(object.getString("status").equals("success")){
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject childObj = array.getJSONObject(i);
                        VideoPG videoItem = new VideoPG(
                                childObj.getString("Title"),
                                childObj.getString("Thumbnail"),
                                childObj.getString("Time"),
                                childObj.getString("URL")
                        );
                        videoforpg.add(videoItem);
                    }
                    videosAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> { });

        queue.add(request);
    }

    // the end of the content

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

}