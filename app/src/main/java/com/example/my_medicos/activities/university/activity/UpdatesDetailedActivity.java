package com.example.my_medicos.activities.university.activity;

import android.os.Bundle;
import android.text.Html;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.my_medicos.activities.publications.utils.Constants;
import com.example.my_medicos.activities.university.model.Updates;
import com.example.my_medicos.databinding.ActivityUpdatesDetailedBinding;
import org.json.JSONException;
import org.json.JSONObject;

public class UpdatesDetailedActivity extends AppCompatActivity {
    ActivityUpdatesDetailedBinding binding;
    Updates currentUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityUpdatesDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String name = getIntent().getStringExtra("name");
        String image = getIntent().getStringExtra("image");
        int id = getIntent().getIntExtra("id",0);
        double price = getIntent().getDoubleExtra("price",0);

        Glide.with(this)
                .load(image)
                .into(binding.productImage);
        getProductDetails(id);
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    void getProductDetails(int id) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.GET_PRODUCT_DETAILS_URL + id;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if(object.getString("status").equals("success")) {
                        JSONObject update = object.getJSONObject("product");
                        String description = update.getString("description");
                        binding.productDescription.setText(
                                Html.fromHtml(description)
                        );

                        currentUpdate = new Updates(
                                update.getString("name"),
                                Constants.PRODUCTS_IMAGE_URL + update.getString("image"),
                                update.getString("status"),
                                update.getDouble("price"),
                                update.getDouble("price_discount"),
                                update.getInt("stock"),
                                update.getInt("id")
                        );

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
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}