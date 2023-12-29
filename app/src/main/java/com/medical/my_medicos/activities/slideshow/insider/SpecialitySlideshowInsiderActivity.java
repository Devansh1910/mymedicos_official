package com.medical.my_medicos.activities.slideshow.insider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.slideshow.Slideshow;
import com.medical.my_medicos.activities.slideshow.SlideshowAdapter;
import com.medical.my_medicos.activities.utils.ConstantsDashboard;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.medical.my_medicos.databinding.ActivitySpecialitySlideshowInsiderBinding;

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
        recyclerslideshow = findViewById(R.id.Recyclerviewslideshowinsider);
        Intent intent = getIntent();

        String extraValue = intent.getStringExtra("specialityPgName");
        if (extraValue != null) {
            Log.d("specialityPgName", extraValue);
        }
        toolbarpginsider.setTitle(extraValue);

        initSliderContent();
    }
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