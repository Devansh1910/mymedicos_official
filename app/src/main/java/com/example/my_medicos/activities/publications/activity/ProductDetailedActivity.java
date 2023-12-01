package com.example.my_medicos.activities.publications.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.my_medicos.R;
import com.example.my_medicos.activities.publications.model.Product;
import com.example.my_medicos.activities.publications.utils.Constants;
import com.example.my_medicos.activities.utils.ConstantsDashboard;
import com.example.my_medicos.databinding.ActivityProductDetailedBinding;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;

import org.json.JSONException;
import org.json.JSONObject;


public class ProductDetailedActivity extends AppCompatActivity {


    ActivityProductDetailedBinding binding;
    Product currentProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String name = getIntent().getStringExtra("Title");
        String image = getIntent().getStringExtra("thumbnail");
        String id = getIntent().getStringExtra("id");
        double price = getIntent().getDoubleExtra("Price",0);

        Glide.with(this)
                .load(image)
                .into(binding.productImage);

        getProductDetails(id);

        getSupportActionBar().setTitle(name);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Cart cart = TinyCartHelper.getCart();


        binding.addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cart.addItem(currentProduct,0);
                binding.addToCartBtn.setEnabled(false);
                binding.addToCartBtn.setText("Added in cart");
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

    void getProductDetails(String id) {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = ConstantsDashboard.GET_SPECIALITY_PRODUCT + id;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if(object.getString("status").equals("success")) {
                        JSONObject product = object.getJSONObject("data");
                        String description = product.getString("id");
                        binding.productDescription.setText(
                                Html.fromHtml(description)
                        );
                        Log.e("Something Went wrong..",product.getString("Title"));

                        currentProduct = new Product(
                                product.getString("Title"),
                                product.getString("thumbnail"),
                                product.getString("Author"),
                                product.getDouble("Price"),
                                product.getString("Type"),
                                product.getString("Category"),
                                product.getString("Subject"),
                                object.getString("id")

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