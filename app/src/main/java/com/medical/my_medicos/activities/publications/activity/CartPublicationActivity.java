package com.medical.my_medicos.activities.publications.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.medical.my_medicos.activities.publications.adapters.CartAdapter;
import com.medical.my_medicos.activities.publications.model.Product;
import com.medical.my_medicos.activities.utils.ConstantsDashboard;
import com.medical.my_medicos.databinding.ActivityCartPublicationBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.prefs.Preferences;

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

        adapter = new CartAdapter(this, products, new CartAdapter.CartListener() {
            @Override
            public void onQuantityChanged() {
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        binding.cartList.setLayoutManager(layoutManager);
        binding.cartList.addItemDecoration(itemDecoration);
        binding.cartList.setAdapter(adapter);

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
