package com.example.my_medicos.activities.pg.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.example.my_medicos.activities.cme.utils.ConstantsDashboard;
import com.example.my_medicos.activities.pg.activites.insiders.SpecialityPGInsiderActivity;
import com.example.my_medicos.activities.pg.adapters.PerDayPGAdapter;
import com.example.my_medicos.activities.pg.adapters.QuestionBanksPGAdapter;
import com.example.my_medicos.activities.pg.adapters.SpecialitiesPGAdapter;
import com.example.my_medicos.activities.pg.adapters.VideoPGAdapter;
import com.example.my_medicos.activities.pg.model.PerDayPG;
import com.example.my_medicos.activities.pg.model.QuestionsPG;
import com.example.my_medicos.activities.pg.model.SpecialitiesPG;
import com.example.my_medicos.activities.pg.model.VideoPG;
import com.example.my_medicos.activities.publications.activity.insiders.CategoryPublicationInsiderActivity;
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

    QuestionBanksPGAdapter questionsAdapter;
    ArrayList<QuestionsPG> questionsforpg;

    VideoPGAdapter videosAdapter;
    ArrayList<VideoPG> videoforpg;

    ProductAdapter productAdapter;
    ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPgprepBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


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

    // this is for the slider
    private void initSliderPg() {
        getRecentPgSlider();
    }

    void getRecentPgSlider() {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, Constants.GET_OFFERS_URL, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if(object.getString("status").equals("success")) {
                    JSONArray offerArray = object.getJSONArray("news_infos");
                    for(int i =0; i < offerArray.length(); i++) {
                        JSONObject childObj =  offerArray.getJSONObject(i);
                        binding.carousel.addData(
                                new CarouselItem(
                                        Constants.NEWS_IMAGE_URL + childObj.getString("image"),
                                        childObj.getString("title")
                                )
                        );
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {});
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
            // Log or print the error for debugging
            Log.e("API_ERROR", "Error in API request: " + error.getMessage());
        });

        queue.add(request);
    }


    // this is for the speciality
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

// Add click listener to RecyclerView items
                        binding.questionbankList.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                            @Override
                            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                                View child = rv.findChildViewUnder(e.getX(), e.getY());
                                int position = rv.getChildAdapterPosition(child);

                                if (position != RecyclerView.NO_POSITION) {
                                    // Check if the clicked item is the "More" category
                                    if (position == specialitiespost.size() - 1 && specialitiespost.get(position).getId() == -1) {
                                        // Redirect to CategoryPublicationInsiderActivity
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
        questionsAdapter = new QuestionBanksPGAdapter(this, questionsforpg);

        getRecentQuestions();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        binding.questionbankList.setLayoutManager(layoutManager);
        binding.questionbankList.setAdapter(questionsAdapter);
    }

    void getRecentQuestions() {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Constants.GET_PRODUCTS_URL + "?count=8";
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if(object.getString("status").equals("success")){
                    JSONArray questionbankArray = object.getJSONArray("products");
                    for(int i =0; i< questionbankArray.length(); i++) {
                        JSONObject childObj = questionbankArray.getJSONObject(i);
                        QuestionsPG questionbank = new QuestionsPG(
                                childObj.getString("name"),
                                Constants.PRODUCTS_IMAGE_URL + childObj.getString("image"),
                                childObj.getString("status"),
                                childObj.getDouble("price"),
                                childObj.getDouble("price_discount"),
                                childObj.getInt("stock"),
                                childObj.getInt("id")

                        );
                        questionsforpg.add(questionbank);
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

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        binding.videobankList.setLayoutManager(layoutManager);
        binding.videobankList.setAdapter(productAdapter);
    }

    void getRecentVideos() {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Constants.GET_PRODUCTS_URL + "?count=8";
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if(object.getString("status").equals("success")){
                    JSONArray videobankArray = object.getJSONArray("products");
                    for(int i =0; i< videobankArray.length(); i++) {
                        JSONObject childObj = videobankArray.getJSONObject(i);
                        VideoPG videobanks = new VideoPG(
                                childObj.getString("name"),
                                Constants.PRODUCTS_IMAGE_URL + childObj.getString("image"),
                                childObj.getString("status"),
                                childObj.getDouble("price"),
                                childObj.getDouble("price_discount"),
                                childObj.getInt("stock"),
                                childObj.getInt("id")

                        );
                        videoforpg.add(videobanks);
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