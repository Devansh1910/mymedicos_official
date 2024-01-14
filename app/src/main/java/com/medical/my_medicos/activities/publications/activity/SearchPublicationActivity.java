package com.medical.my_medicos.activities.publications.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.publications.adapters.ProductAdapter;
import com.medical.my_medicos.activities.publications.model.Product;
import com.medical.my_medicos.activities.utils.ConstantsDashboard;
import com.medical.my_medicos.databinding.ActivitySearchPublicationBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchPublicationActivity extends AppCompatActivity {

    ActivitySearchPublicationBinding binding;
    ProductAdapter productAdapter;
    ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchPublicationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        products = new ArrayList<>();
        productAdapter = new ProductAdapter(this, products);

        String query = getIntent().getStringExtra("query");

        // Set the query as the title of the search
        TextView titleTextView = findViewById(R.id.titleofthesearch);
        titleTextView.setText(query);

        getProducts(query);

        ImageView backToPublicationActivity = findViewById(R.id.backtothepublicationactivity);
        backToPublicationActivity.setOnClickListener(v -> {
            finish();
        });

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        binding.productList.setLayoutManager(layoutManager);
        binding.productList.setAdapter(productAdapter);
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    void getProducts(String query) {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = ConstantsDashboard.GET_SPECIALITY_ALL_PRODUCT;
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getString("status").equals("success")) {
                    JSONArray productsArray = object.getJSONArray("data");
                    for (int i = 0; i < productsArray.length(); i++) {
                        JSONObject childObj = productsArray.getJSONObject(i);
                        String title = childObj.getJSONObject("data").getString("Title");
                        if (title.toLowerCase().contains(query.toLowerCase())) {
                            Product product = new Product(
                                    title,
                                    childObj.getJSONObject("data").getString("thumbnail"),
                                    childObj.getJSONObject("data").getString("Author"),
                                    childObj.getJSONObject("data").getDouble("Price"),
                                    childObj.getJSONObject("data").getString("Type"),
                                    childObj.getJSONObject("data").getString("Category"),
                                    childObj.getJSONObject("data").getString("Subject"),
                                    childObj.getString("id")
                            );
                            products.add(product);
                        }
                    }
                    productAdapter.notifyDataSetChanged();
                    if (products.isEmpty()) {
                        binding.noResults.setVisibility(View.VISIBLE);
                    } else {
                        binding.noResults.setVisibility(View.GONE);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            // Handle error
        });

        queue.add(request);
    }
}
