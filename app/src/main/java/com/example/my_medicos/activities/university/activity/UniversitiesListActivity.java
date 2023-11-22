package com.example.my_medicos.activities.university.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.my_medicos.activities.publications.adapters.ProductAdapter;
import com.example.my_medicos.activities.publications.model.Product;
import com.example.my_medicos.activities.publications.utils.Constants;
import com.example.my_medicos.activities.university.adapters.UniversitiesAdapter;
import com.example.my_medicos.activities.university.adapters.UpdatesAdapter;
import com.example.my_medicos.activities.university.model.Universities;
import com.example.my_medicos.activities.university.model.Updates;
import com.example.my_medicos.databinding.ActivityCategoryPublicationBinding;
import com.example.my_medicos.databinding.ActivityUniversityListBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UniversitiesListActivity extends AppCompatActivity {

    ActivityUniversityListBinding binding;
    UpdatesAdapter updateAdapter;
    ArrayList<Updates> updates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUniversityListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        updates = new ArrayList<>();
        updateAdapter = new UpdatesAdapter(this, updates);


        int universityId = getIntent().getIntExtra("nuniversityId", 0);
        String universityName = getIntent().getStringExtra("universityName");

        getSupportActionBar().setTitle(universityName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getUpdates(universityId);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        binding.universityList.setLayoutManager(layoutManager);
        binding.universityList.setAdapter(updateAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    void getUpdates(int universityId) {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Constants.GET_PRODUCTS_URL + "?category_id=" + universityId;
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
}