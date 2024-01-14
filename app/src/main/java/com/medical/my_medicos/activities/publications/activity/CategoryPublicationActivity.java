package com.medical.my_medicos.activities.publications.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.publications.activity.fragments.FreeFragment;
import com.medical.my_medicos.activities.publications.activity.fragments.PaidFragment;
import com.medical.my_medicos.activities.publications.activity.fragments.ResearchPaperFragment;
import com.medical.my_medicos.activities.publications.activity.fragments.TextBooksFragment;
import com.medical.my_medicos.activities.publications.adapters.ProductAdapter;
import com.medical.my_medicos.activities.publications.model.Product;
import com.medical.my_medicos.activities.utils.ConstantsDashboard;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.medical.my_medicos.databinding.ActivityCategoryPublicationBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoryPublicationActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationCategoryPublication;
    BottomAppBar bottomAppBarCategoryPublication;
    ActivityCategoryPublicationBinding binding;
    ProductAdapter productAdapter;
    ArrayList<Product> products;
    int catId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryPublicationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        products = new ArrayList<>();
        productAdapter = new ProductAdapter(this, products);

        catId = getIntent().getIntExtra("catId", 0);
        String categoryName = getIntent().getStringExtra("categoryName");

        getProducts(catId);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        binding.productList.setLayoutManager(layoutManager);
        binding.productList.setAdapter(productAdapter);

        bottomAppBarCategoryPublication = findViewById(R.id.bottomappabar);

        replaceFragment(TextBooksFragment.newInstance(catId));
        bottomNavigationCategoryPublication = findViewById(R.id.bottomNavigationViewcategorypublication);
        bottomNavigationCategoryPublication.setBackground(null);

        bottomNavigationCategoryPublication.setOnItemSelectedListener(item -> {
            int frgId = item.getItemId();
            Log.d("Something went wrong..", "Try again!");
            if (frgId == R.id.textbooks) {
                replaceFragment(TextBooksFragment.newInstance(catId));
            } else if (frgId == R.id.research) {
                replaceFragment(new ResearchPaperFragment());
            } else if (frgId == R.id.paidbooks) {
                replaceFragment(new PaidFragment());
            } else {
                replaceFragment(new FreeFragment());
            }
            return true;
        });

        //.... Back to the Previous Activity....

        ImageView backToHomeImageView = findViewById(R.id.backtothemoreactivity);
        backToHomeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    void getProducts(int catId) {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = ConstantsDashboard.GET_SPECIALITY_ALL_PRODUCT;
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if(object.getString("status").equals("success")){
                    JSONArray productsArray = object.getJSONArray("data");
                    for(int i =0; i< productsArray.length(); i++) {
                        JSONObject childObj = productsArray.getJSONObject(i);
                        Product product = new Product(
                                childObj.getString("Title"),
                                childObj.getString("thumbnail"),
                                childObj.getString("Author"),
                                childObj.getDouble("Price"),
                                childObj.getString("Type"),
                                childObj.getString("Category"),
                                childObj.getString("Subject"),
                                object.getString("id")

                        );
                        products.add(product);
                    }
                    productAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> { });

        queue.add(request);
    }
}
