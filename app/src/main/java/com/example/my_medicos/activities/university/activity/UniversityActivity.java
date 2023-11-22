package com.example.my_medicos.activities.university.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.my_medicos.R;
import com.example.my_medicos.activities.publications.activity.insiders.CategoryPublicationInsiderActivity;
import com.example.my_medicos.activities.publications.adapters.ProductAdapter;
import com.example.my_medicos.activities.publications.model.Product;
import com.example.my_medicos.activities.publications.utils.Constants;
import com.example.my_medicos.activities.university.activity.insiders.UniversitiesInsiderActivity;
import com.example.my_medicos.activities.university.adapters.UniversitiesAdapter;
import com.example.my_medicos.activities.university.adapters.UpdatesAdapter;
import com.example.my_medicos.activities.university.model.Universities;
import com.example.my_medicos.activities.university.model.Updates;
import com.example.my_medicos.activities.utils.ConstantsDashboard;
import com.example.my_medicos.databinding.ActivityUniversityupdatesBinding;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UniversityActivity extends AppCompatActivity {
    ActivityUniversityupdatesBinding binding;
    UniversitiesAdapter universitiesAdapter;
    ArrayList<Universities> universities;
    UpdatesAdapter updateAdapter;
    ArrayList<Updates> updates;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUniversityupdatesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = findViewById(R.id.universitytoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initUpdates();
        initUniversity();
        initSliderUpdates();
    }

    private void initSliderUpdates() {
        getUpdatesSlider();
    }
    void initUniversity() {
        universities = new ArrayList<>();
        universitiesAdapter = new UniversitiesAdapter(this, universities);

        getUniversities();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        binding.universitiesList.setLayoutManager(layoutManager);
        binding.universitiesList.setAdapter(universitiesAdapter);
    }
    void getUniversities() {
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
                            Universities university = new Universities(
                                    object.getString("name"),
                                    Constants.CATEGORIES_IMAGE_URL + object.getString("icon"),
                                    object.getString("color"),
                                    object.getString("brief"),
                                    object.getInt("id")
                            );
                            universities.add(university);
                        }

                        // If there are more than 5 categories, add a "More" category
                        // Inside the if statement
                        if (categoriesArray.length() > 5) {
                            Universities moreUniversities = new Universities(
                                    "More",
                                    "more_category_icon", // Replace with the actual icon for the "More" category
                                    "#CCCCCC", // Replace with the color for the "More" category
                                    "View More Categories",
                                    -1 // Replace with a unique ID for the "More" category
                            );
                            universities.add(moreUniversities);
                        }

                        universitiesAdapter.notifyDataSetChanged();

// Add click listener to RecyclerView items
                        binding.universitiesList.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                            @Override
                            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                                View child = rv.findChildViewUnder(e.getX(), e.getY());
                                int position = rv.getChildAdapterPosition(child);

                                if (position != RecyclerView.NO_POSITION) {
                                    // Check if the clicked item is the "More" category
                                    if (position == universities.size() - 1 && universities.get(position).getId() == -1) {
                                        // Redirect to CategoryPublicationInsiderActivity
                                        Intent intent = new Intent(UniversityActivity.this, UniversitiesInsiderActivity.class);
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

                        universitiesAdapter.notifyDataSetChanged();

                    } else {

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
    void initUpdates() {
        updates = new ArrayList<>();
        updateAdapter = new UpdatesAdapter(this, updates);

        getRecentUpdates();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        binding.updatesList.setLayoutManager(layoutManager);
        binding.updatesList.setAdapter(updateAdapter);
    }
    void getRecentUpdates() {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Constants.GET_PRODUCTS_URL + "?count=8";
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if(object.getString("status").equals("success")){
                    JSONArray productsArray = object.getJSONArray("products");
                    for(int i =0; i< productsArray.length(); i++) {
                        JSONObject childObj = productsArray.getJSONObject(i);
                        Updates update = new Updates(
                                childObj.getString("name"),
                                Constants.PRODUCTS_IMAGE_URL + childObj.getString("image"),
                                childObj.getString("status"),
                                childObj.getDouble("price"),
                                childObj.getDouble("price_discount"),
                                childObj.getInt("stock"),
                                childObj.getInt("id")

                        );
                        updates.add(update);
                    }
                    updateAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> { });

        queue.add(request);
    }
    void getUpdatesSlider() {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, ConstantsDashboard.GET_UPDATES_SLIDER_URL, response -> {
            try {
                JSONArray homesliderArray = new JSONArray(response);
                for (int i = 0; i < homesliderArray.length(); i++) {
                    JSONObject childObj = homesliderArray.getJSONObject(i);
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
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}