package com.example.my_medicos.activities.publications.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.my_medicos.activities.publications.adapters.CartAdapter;
import com.example.my_medicos.activities.publications.model.Product;
import com.example.my_medicos.activities.utils.ConstantsDashboard;
import com.example.my_medicos.databinding.ActivityCartPublicationBinding;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.model.Item;
import com.hishd.tinycart.util.TinyCartHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class CartPublicationActivity extends AppCompatActivity {

    ActivityCartPublicationBinding binding;
    CartAdapter adapter;
    ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartPublicationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        products = new ArrayList<>();



//        String url = ConstantsDashboard.GET_CART +"/rCDX20PCXC08Rnjl7nhk"+"/get/";
//        Log.e("function",url);
//
//        JSONObject requestBody = new JSONObject();
//        MyVolleyRequest.sendPostRequest(getApplicationContext(), url, requestBody, new MyVolleyRequest.VolleyCallback() {
//            @Override
//            public void onSuccess(JSONObject response) {
//                // Handle the successful response
//                Log.d("VolleyResponse", response.toString());
//            }
//            @Override
//            public void onError(String error) {
//                // Handle the error
//                Log.e("VolleyError", error);
//            }
//        });
        adapter = new CartAdapter(this, products, new CartAdapter.CartListener() {
            @Override
            public void onQuantityChanged() {
//                binding.subtotal.setText(String.format("INR %.2f",cart.getTotalPrice()));
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        binding.cartList.setLayoutManager(layoutManager);
        binding.cartList.addItemDecoration(itemDecoration);
        binding.cartList.setAdapter(adapter);


//        binding.subtotal.setText(String.format("INR %.2f",cart.getTotalPrice()));


        binding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartPublicationActivity.this, CheckoutPublicationActivity.class));
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}