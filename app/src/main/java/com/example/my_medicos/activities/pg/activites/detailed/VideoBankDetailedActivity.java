package com.example.my_medicos.activities.pg.activites.detailed;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.my_medicos.R;
import com.example.my_medicos.activities.pg.model.VideoPG;
import com.example.my_medicos.activities.publications.utils.Constants;
import com.example.my_medicos.databinding.ActivityVideoBankDetailedBinding;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;

import org.json.JSONException;
import org.json.JSONObject;


public class VideoBankDetailedActivity extends AppCompatActivity {


    ActivityVideoBankDetailedBinding binding;
    VideoPG currentVideoBank;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityVideoBankDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String name = getIntent().getStringExtra("name");
        String image = getIntent().getStringExtra("image");
        int id = getIntent().getIntExtra("id",0);
        double price = getIntent().getDoubleExtra("price",0);

        Glide.with(this)
                .load(image)
                .into(binding.videobankthumbnailImage);

        getProductDetails(id);

        getSupportActionBar().setTitle(name);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Cart cart = TinyCartHelper.getCart();


        binding.VideovideoBankBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cart.addItem(currentVideoBank,1);
                binding.VideovideoBankBtn.setEnabled(false);
                binding.VideovideoBankBtn.setText("Opening Youtube..");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart, menu);
        return super.onCreateOptionsMenu(menu);
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
                        JSONObject videopg = object.getJSONObject("product");
                        String description = videopg.getString("description");
                        binding.videobankDescription.setText(
                                Html.fromHtml(description)
                        );

                        currentVideoBank = new VideoPG(
                                videopg.getString("name"),
                                Constants.PRODUCTS_IMAGE_URL + videopg.getString("image"),
                                videopg.getString("status"),
                                videopg.getDouble("price"),
                                videopg.getDouble("price_discount"),
                                videopg.getInt("stock"),
                                videopg.getInt("id")
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