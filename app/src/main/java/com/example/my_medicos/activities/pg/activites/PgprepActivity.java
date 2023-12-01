package com.example.my_medicos.activities.pg.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
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
import com.example.my_medicos.databinding.ActivityPgprepBinding;
import com.mancj.materialsearchbar.MaterialSearchBar;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class  PgprepActivity extends AppCompatActivity {

    ActivityPgprepBinding binding;

    PerDayPGAdapter  PerDayPGAdapter;
    ArrayList<PerDayPG> dailyquestionspg;
    SpecialitiesPGAdapter specialitiesPGAdapter;
    ArrayList<SpecialitiesPG> specialitiespost;

    QuestionBankPGAdapter questionsAdapter;
    ArrayList<QuestionPG> questionsforpg;

    VideoPGAdapter videosAdapter;
    ArrayList<VideoPG> videoforpg;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPgprepBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize SwipeRefreshLayout
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this::refreshContent);


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

        initPerDayQuestions();
        initSpecialities();
        initQuestionsBanks();
        initVideosBanks();
        initSliderPg();

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
        getPerDayQuestions();
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
    void initPerDayQuestions() {
        dailyquestionspg = new ArrayList<>();
        PerDayPGAdapter = new PerDayPGAdapter(this, dailyquestionspg);

        getPerDayQuestions();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        binding.perdayquestions.setLayoutManager(layoutManager);
        binding.perdayquestions.setAdapter(PerDayPGAdapter);
    }

    void getPerDayQuestions() {
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
                                childObj.getString("Correct")
                        );
                        dailyquestionspg.add(perday);
                    }
                    PerDayPGAdapter.notifyDataSetChanged();
                    Log.d("DEBUG", "getPerDayQuestions: Data added to the list");
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

        StringRequest request = new StringRequest(Request.Method.GET, Constants.GET_CATEGORIES_URL, new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("err", response);
                    JSONObject mainObj = new JSONObject(response);
                    if (mainObj.getString("status").equals("success")) {
                        JSONArray categoriesArray = mainObj.getJSONArray("categories");
                        int categoriesCount = Math.min(categoriesArray.length(), 5); // Limit to the first 5 categories or the actual count if less than 5
                        for (int i = 0; i < categoriesCount; i++) {
                            JSONObject object = categoriesArray.getJSONObject(i);
                            SpecialitiesPG specialitiespostgraduate = new SpecialitiesPG(
                                    object.getString("name"),
                                    Constants.CATEGORIES_IMAGE_URL + object.getString("icon"),
                                    object.getString("color"),
                                    object.getString("brief"),
                                    object.getInt("id")
                            );
                            specialitiespost.add(specialitiespostgraduate);
                        }

                        // If there are more than 5 categories, add a "More" category
                        // Inside the if statement
                        if (categoriesArray.length() > 5) {
                            SpecialitiesPG moreSpecialitiesPG = new SpecialitiesPG(
                                    "More",
                                    "more_category_icon", // Replace with the actual icon for the "More" category
                                    "#CCCCCC", // Replace with the color for the "More" category
                                    "View More Categories",
                                    -1 // Replace with a unique ID for the "More" category
                            );
                            specialitiespost.add(moreSpecialitiesPG);
                        }

                        specialitiesPGAdapter.notifyDataSetChanged();

                        binding.specialityList.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                            @Override
                            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                                View child = rv.findChildViewUnder(e.getX(), e.getY());
                                int position = rv.getChildAdapterPosition(child);

                                if (position != RecyclerView.NO_POSITION) {
                                    if (position == specialitiespost.size() - 1 && specialitiespost.get(position).getId() == -1) {
                                        Intent intent = new Intent(PgprepActivity.this, SpecialityPGInsiderActivity.class);
                                        startActivity(intent);
                                    } else {

                                    }
                                }

                                return false;
                            }

                            @Override
                            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {}

                            @Override
                            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
                        });



                        specialitiesPGAdapter.notifyDataSetChanged();
                    } else {
                        // DO nothing
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(request);
    }

    // this is for the questionbank
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

        String url = ConstantsDashboard.GET_PG_VIDEOS_URL;
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