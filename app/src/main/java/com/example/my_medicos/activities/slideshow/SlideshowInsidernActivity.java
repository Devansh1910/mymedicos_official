package com.example.my_medicos.activities.slideshow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.my_medicos.R;
import com.example.my_medicos.activities.publications.adapters.CategoryAdapter;
import com.example.my_medicos.activities.publications.adapters.insiders.CategoryInsiderAdapter;
import com.example.my_medicos.activities.publications.model.Category;
import com.example.my_medicos.activities.publications.utils.Constants;
import com.example.my_medicos.activities.utils.ConstantsDashboard;
import com.example.my_medicos.databinding.ActivityCategoryPublicationInsiderBinding;
import com.example.my_medicos.databinding.ActivitySlideshowInsidernBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SlideshowInsidernActivity extends AppCompatActivity {

    ActivitySlideshowInsidernBinding binding;

    ArrayList<Category> categories;

    Toolbar toolbarpublications;

    CategoryInsiderAdapter categoryAdapterInsider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySlideshowInsidernBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbarpublications = findViewById(R.id.publicationstoolbar);
        setSupportActionBar(toolbarpublications);
//        tab=findViewById(R.id.tabLayout);
//        viewPager=findViewById(R.id.view_pager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initCategories();

    }

    void initCategories() {
        categories = new ArrayList<>();
        categoryAdapterInsider = new CategoryInsiderAdapter(this, categories);

        getCategories();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        binding.categoriesListInsider.setLayoutManager(layoutManager);
        binding.categoriesListInsider.setAdapter(categoryAdapterInsider);
    }

    void getCategories() {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, ConstantsDashboard.GET_SPECIALITY, new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("err", response);
                    JSONObject mainObj = new JSONObject(response);
                    if (mainObj.getString("status").equals("success")) {
                        JSONArray categoriesArray = mainObj.getJSONArray("categories");
                        for (int i = 0; i < categoriesArray.length(); i++) {
                            JSONObject object = categoriesArray.getJSONObject(i);
                            Category category = new Category(
                                    object.getString("id"),
                                    object.getInt("priority")
                            );
                            categories.add(category);
                        }
                        categoryAdapterInsider.notifyDataSetChanged();
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

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                // Handle the back arrow click, finish the current activity
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}