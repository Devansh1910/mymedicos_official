package com.example.my_medicos.activities.slideshow.insider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.my_medicos.R;
import com.example.my_medicos.activities.pg.adapters.insiders.SpecialitiesPGInsiderAdapter;
import com.example.my_medicos.activities.pg.fragment.QuestionbankFragment;
import com.example.my_medicos.activities.pg.fragment.VideoBankFragment;
import com.example.my_medicos.activities.pg.fragment.WeeklyQuizFragment;
import com.example.my_medicos.activities.pg.model.SpecialitiesPG;
import com.example.my_medicos.activities.publications.activity.fragments.FreeFragment;
import com.example.my_medicos.activities.publications.activity.fragments.PaidFragment;
import com.example.my_medicos.activities.publications.activity.fragments.ResearchPaperFragment;
import com.example.my_medicos.activities.publications.activity.fragments.TextBooksFragment;
import com.example.my_medicos.activities.publications.utils.Constants;
import com.example.my_medicos.activities.slideshow.Slideshow;
import com.example.my_medicos.activities.slideshow.SlideshowAdapter;
import com.example.my_medicos.activities.utils.ConstantsDashboard;
import com.example.my_medicos.databinding.ActivitySlideshowInsidernBinding;
import com.example.my_medicos.databinding.ActivitySpecialityPgBinding;
import com.example.my_medicos.databinding.ActivitySpecialitySlideshowInsiderBinding;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SpecialitySlideshowInsiderActivity extends AppCompatActivity {

    ActivitySpecialitySlideshowInsiderBinding binding;
    //    ArrayList<SpecialitiesPG> specialitiesPostGraduate;
    BottomNavigationView bottomNavigationCategoryPublication;
    BottomAppBar bottomAppBarCategoryPublication;

    Toolbar toolbarpginsider;
    private SlideshowAdapter slideshowAdapter;
    private ArrayList<Slideshow> slideshows;

//    SpecialitiesPGInsiderAdapter specialitiesPGInsiderAdapter;

    int catId;
    RecyclerView recyclerslideshow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpecialitySlideshowInsiderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        toolbarpginsider = binding.specialitytoolbar;
        setSupportActionBar(toolbarpginsider);
        recyclerslideshow=findViewById(R.id.Recyclerviewslideshowinsider);
        Intent intent = getIntent();
        String extraValue = intent.getStringExtra("specialityPgName");
        if (extraValue!=null) {
            Log.d("specialityPgName", extraValue);
//            Toolbar text=findViewById(R.id.specialitytoolbar);
//            text.setTitle(extraValue);
        }
        toolbarpginsider.setTitle(extraValue);
//        bottomAppBarCategoryPublication = findViewById(R.id.bottomappabarslideshow);
//        bottomNavigationCategoryPublication = findViewById(R.id.bottomNavigationViewslideshow);
//        if (bottomNavigationCategoryPublication != null) {
//            bottomNavigationCategoryPublication.setBackground(null);
//            replaceFragment(QuestionbankFragment.newInstance(catId));
//            // Inside onCreate method
//            bottomNavigationCategoryPublication.setOnItemSelectedListener(item -> {
//                int frgId = item.getItemId();
//                Log.d("Something went wrong..", "Try again!");
//                if (frgId == R.id.qb) {
//                    replaceFragment(QuestionbankFragment.newInstance(catId));
//                } else if (frgId == R.id.lc) {
//                    replaceFragment(new VideoBankFragment());
//                } else {
//                    replaceFragment(new WeeklyQuizFragment());
//                }
//                return true;
//            });
//        } else {
//            Log.e("Error", "bottomNavigationCategoryPublication is null");
//        }
    }
//    private void replaceFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.frame_layout_pg, fragment);
//        fragmentTransaction.commit();
//    }
    //    void initSpecialityPG() {
//        specialitiesPostGraduate = new ArrayList<>();
//        specialitiesPGInsiderAdapter = new SpecialitiesPGInsiderAdapter(this, specialitiesPostGraduate);
//
//        getSpecialitiesPG();
//
//        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
//        binding.specialityinsidercontentList.setLayoutManager(layoutManager);
//        binding.specialityinsidercontentList.setAdapter(specialitiesPGInsiderAdapter);
//    }
//
//    void getSpecialitiesPG() {
//        RequestQueue queue = Volley.newRequestQueue(this);
//
//        StringRequest request = new StringRequest(Request.Method.GET, Constants.GET_CATEGORIES_URL, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    Log.e("err", response);
//                    JSONObject mainObj = new JSONObject(response);
//                    if (mainObj.getString("status").equals("success")) {
//                        JSONArray specialityArray = mainObj.getJSONArray("categories");
//                        for (int i = 0; i < specialityArray.length(); i++) {
//                            JSONObject object = specialityArray.getJSONObject(i);
//                            SpecialitiesPG specialitiesPGS = new SpecialitiesPG(
//                                    object.getString("id"),
//                                    object.getInt("priority")
//                            );
//                            specialitiesPostGraduate.add(specialitiesPGS);
//                        }
//                        specialitiesPGInsiderAdapter.notifyDataSetChanged();
//
//                    } else {
//                        // DO nothing
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//
//        queue.add(request);
//    }
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                finish();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
void getSlideshowRecent() {
    RequestQueue queue = Volley.newRequestQueue(SpecialitySlideshowInsiderActivity.this);

    String url = ConstantsDashboard.GET_SLIDESHOW;
    StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
        try {
            JSONObject object = new JSONObject(response);
            if ("success".equals(object.optString("status"))) {
                JSONArray dataArray = object.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject slideshowObj = dataArray.getJSONObject(i);

                    String fileUrl = slideshowObj.optString("file");
                    String title = slideshowObj.optString("title");

                    if (slideshowObj.has("images")) {
                        JSONArray imagesArray = slideshowObj.getJSONArray("images");
                        ArrayList<Slideshow.Image> images = new ArrayList<>();
                        for (int j = 0; j < imagesArray.length(); j++) {
                            JSONObject imageObj = imagesArray.getJSONObject(j);
                            String imageUrl = imageObj.optString("url");
                            String imageId = imageObj.optString("id");
                            images.add(new Slideshow.Image(imageId, imageUrl));
                        }
                        // Now you can create your Slideshow object with images
                        Slideshow slideshowItem = new Slideshow(title, images, fileUrl);
                        slideshows.add(slideshowItem);
                    } else {
                        // If "images" array does not exist, create Slideshow without images
                        Slideshow slideshowItem = new Slideshow(title, new ArrayList<>(), fileUrl);
                        slideshows.add(slideshowItem);
                    }
                }
                slideshowAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }, error -> {
        // Handle error
    });

    queue.add(request);
}
void initSliderContent() {
    slideshows = new ArrayList<>();
    slideshowAdapter = new SlideshowAdapter(SpecialitySlideshowInsiderActivity.this, slideshows);
    getSlideshowRecent();

    // Use requireContext() or getContext() to get a valid context
    GridLayoutManager layoutManager = new GridLayoutManager(SpecialitySlideshowInsiderActivity.this, 1);

    binding.Recyclerviewslideshowinsider.setLayoutManager(layoutManager);
    binding.Recyclerviewslideshowinsider.setAdapter(slideshowAdapter);
}
}