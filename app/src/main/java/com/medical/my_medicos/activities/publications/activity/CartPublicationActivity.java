package com.medical.my_medicos.activities.publications.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

        String docId = Preferences.userRoot().get("docId", "");
        products = new ArrayList<>();

        String url = ConstantsDashboard.GET_CART + "/" + docId + "/get";
        Log.e("function", url);

        JSONObject requestBody = new JSONObject();
        MyVolleyRequest.sendPostRequest(getApplicationContext(), url, requestBody, new MyVolleyRequest.VolleyCallback() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray cartArray = response.getJSONArray("data");

                    for (int i = 0; i < cartArray.length(); i++) {
                        JSONObject cartItem = cartArray.getJSONObject(i);

                        Product product = new Product(
                                cartItem.getString("Title"),
                                cartItem.getString("thumbnail"),
                                cartItem.getString("Author"),
                                cartItem.getDouble("Price"),
                                cartItem.getString("Type"),
                                cartItem.getString("Category"),
                                cartItem.getString("id"),
                                cartItem.getString("Subject")
                        );

                        products.add(product);
                        Log.d("product loaded", cartItem.toString());
                    }

                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("VolleyResponse", response.toString());
            }

            @Override
            public void onError(String error) {
                Log.e("VolleyError", error);
            }
        });

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
