package com.example.my_medicos.activities.news;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.my_medicos.R;
import com.example.my_medicos.activities.cme.utils.ConstantsDashboard;
import com.example.my_medicos.activities.publications.activity.CartPublicationActivity;
import com.example.my_medicos.databinding.ActivityNewsDetailedBinding;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;

import org.json.JSONException;
import org.json.JSONObject;


public class NewsDetailedActivity extends AppCompatActivity {


    ActivityNewsDetailedBinding binding;
    News currentNews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityNewsDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String name = getIntent().getStringExtra("Title");
        String image = getIntent().getStringExtra("thumbnail");
        String status = getIntent().getStringExtra("Description");
        String time = getIntent().getStringExtra("Time");


        Glide.with(this)
                .load(image)
                .into(binding.newsthumbnail);

        getNewsDetails(name);

        getSupportActionBar().setTitle(name);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Cart cart = TinyCartHelper.getCart();


        binding.readmoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.readmoreBtn.setEnabled(false);
                binding.readmoreBtn.setText("Added in cart");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.cart) {
            startActivity(new Intent(this, CartPublicationActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    void getNewsDetails(String name) {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = ConstantsDashboard.GET_NEWS + name;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject news = new JSONObject(response);
                        String description = news.getString("Description");
                        binding.newsDescription.setText(
                                Html.fromHtml(description)
                        );
                        currentNews = new News(
                                news.getString("name"),
                                news.getString("image"),
                                news.getString("status"),
                                news.getString("url"),
                                news.getString("date")
                        );

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