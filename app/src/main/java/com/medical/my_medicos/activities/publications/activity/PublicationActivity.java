package com.medical.my_medicos.activities.publications.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.publications.activity.insiders.CategoryPublicationInsiderActivity;
import com.medical.my_medicos.activities.publications.activity.insiders.CategoryPublicationInsiderForRealActivity;
import com.medical.my_medicos.activities.publications.adapters.CategoryAdapter;
import com.medical.my_medicos.activities.publications.adapters.ProductAdapter;
import com.medical.my_medicos.activities.publications.adapters.RecentHomeProductsAdapter;
import com.medical.my_medicos.activities.publications.adapters.SponsoredProductAdapter;
import com.medical.my_medicos.activities.publications.model.Category;
import com.medical.my_medicos.activities.publications.model.Product;
import com.medical.my_medicos.activities.publications.utils.Constants;
import com.medical.my_medicos.activities.utils.ConstantsDashboard;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.medical.my_medicos.databinding.ActivityPublicationBinding;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
public class PublicationActivity extends AppCompatActivity {

    //.. Binding Initiate...
    ActivityPublicationBinding binding;

    //... For Categories.....
    CategoryAdapter categoryAdapter;
    ArrayList<Category> categories;

    //... For ShowCase Products.....
    ProductAdapter productAdapter;
    ArrayList<Product> products;

    //.... For Recent Products ......
    RecentHomeProductsAdapter recentHomeProductsAdapter;
    ArrayList<Product> recenthomeproducts;

    //.... For Sponsored Products....
    SponsoredProductAdapter sponsoredProductsAdapter;
    ArrayList<Product> sponsoredProduct;

    //... Loader....

    LottieAnimationView loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //..... Security (Restricted for Screenshot or Recording).....

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        //... Binding Statement.....

        binding = ActivityPublicationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //..........Search Bar......


        binding.searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for now
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for now
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not needed for now
            }
        });

        binding.searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                // Not needed for now
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                String query = text.toString();
                if (!TextUtils.isEmpty(query)) {
                    Intent intent = new Intent(PublicationActivity.this, SearchPublicationActivity.class);
                    intent.putExtra("query", query);
                    startActivity(intent);
                }
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                if (buttonCode == MaterialSearchBar.BUTTON_BACK) {
                    // Handle back button click
                }
            }
        });

        //..... Init Statements......

        initCategories();
        initProducts();
        initTopExploredReadables();
        initSlider();
        initSponsorSlider();
        initSponsorProduct();

        //......To the Content Page Purchased by the Person....

        binding.totheccart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PublicationActivity.this, UserContentActivity.class));
            }
        });

        //.... Back to the Previous Activity....

        ImageView backToHomeImageView = findViewById(R.id.backtothehomefrompublication);
        backToHomeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Loader.....
        loader = findViewById(R.id.loader);
        loader.setVisibility(View.VISIBLE);
        loader.playAnimation();
    }

    //... Normal Slider.....
    private void initSlider() {
        getRecentOffers();
    }

    void getRecentOffers() {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, Constants.GET_OFFERS_URL, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if(object.getString("status").equals("success")) {
                    JSONArray offerArray = object.getJSONArray("news_infos");
                    for(int i =0; i < offerArray.length(); i++) {
                        JSONObject childObj =  offerArray.getJSONObject(i);
                        binding.carousel.addData(
                                new CarouselItem(
                                        Constants.NEWS_IMAGE_URL + childObj.getString("image"),
                                        childObj.getString("title")
                                )
                        );
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {});
        queue.add(request);
    }

    //....... Sponsored Slider.....
    private void initSponsorSlider() {
        getRecentSlidersSponsored();
    }

    void getRecentSlidersSponsored() {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, ConstantsDashboard.GET_SPONSORS_SLIDER_URL, response -> {
            try {
                JSONArray newssliderArray = new JSONArray(response);
                for (int i = 0; i < newssliderArray.length(); i++) {
                    JSONObject childObj = newssliderArray.getJSONObject(i);
                    binding.carouselsponsor.addData(
                            new CarouselItem(
                                    childObj.getString("url"),
                                    childObj.getString("action")
                            )
                    );
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            // Handle error if needed
        });
        queue.add(request);
    }

    //....Categories.....
    void initCategories() {
        categories = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(this, categories);

        getCategories();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.categoriesList.setLayoutManager(layoutManager);
        binding.categoriesList.setAdapter(categoryAdapter);
    }

    void getCategories() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, ConstantsDashboard.GET_SPECIALITY, new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("err", response);
                    JSONObject mainObj = new JSONObject(response);
                    if (mainObj.getString("status").equals("success")) {
                        JSONArray categoriesArray = mainObj.getJSONArray("data");

                        for (int i = 0; i < categoriesArray.length(); i++) {
                            JSONObject object = categoriesArray.getJSONObject(i);
                            int priority = object.getInt("priority");
                            if (priority == 1 || priority == 2) {
                                Category category = new Category(
                                        object.getString("id"),
                                        priority
                                );
                                categories.add(category);
                                Log.e("Something went wrong..", String.valueOf(priority));
                            }
                        }

                        // Add the "More" item
                        Category moreItem = new Category("-1", -1);
                        categories.add(moreItem);

                        categoryAdapter.notifyDataSetChanged();

                        binding.categoriesList.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                            @Override
                            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                                View child = rv.findChildViewUnder(e.getX(), e.getY());
                                int position = rv.getChildAdapterPosition(child);

                                if (position != RecyclerView.NO_POSITION) {
                                    if (position == categories.size() - 1 && categories.get(position).getPriority() == -1) {
                                        Intent intent = new Intent(PublicationActivity.this, CategoryPublicationInsiderForRealActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Log.e("Error","Error Here");
                                    }
                                }
                                return false;
                            }

                            @Override
                            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {}

                            @Override
                            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
                        });

                        categoryAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("Error","Error Here");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle errors here
            }
        });

        queue.add(request);
    }

    // Top Explored Readables

    void initTopExploredReadables() {
        recenthomeproducts = new ArrayList<>();
        recentHomeProductsAdapter = new RecentHomeProductsAdapter(this, recenthomeproducts);

        getTopExploredReadables();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.recentaddedbooksList.setLayoutManager(layoutManager);
        binding.recentaddedbooksList.setAdapter(recentHomeProductsAdapter);
    }

    void getTopExploredReadables() {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = ConstantsDashboard.GET_SPECIALITY_ALL_PRODUCT_HOME;
        @SuppressLint("NotifyDataSetChanged") StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if(object.getString("status").equals("success")){
                    JSONArray recenthomeproductsArray = object.getJSONArray("data");
                    for(int i =0; i< recenthomeproductsArray.length(); i++) {
                        JSONObject childObj = recenthomeproductsArray.getJSONObject(i);
                        JSONObject recenthomeproductObj = childObj.getJSONObject("data");

                        String documentId = childObj.getString("id");

                        Product recenthomeproduct = new Product(
                                recenthomeproductObj.getString("Title"),
                                recenthomeproductObj.getString("thumbnail"),
                                recenthomeproductObj.getString("Author"),
                                recenthomeproductObj.getDouble("Price"),
                                recenthomeproductObj.getString("Type"),
                                recenthomeproductObj.getString("Category"),
                                documentId,
                                recenthomeproductObj.getString("Subject")
                        );

                        recenthomeproducts.add(recenthomeproduct);
                    }
                    recentHomeProductsAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> { });

        queue.add(request);
    }

    //.....For the Sponsor

    void initSponsorProduct() {
        sponsoredProduct = new ArrayList<>();
        sponsoredProductsAdapter = new SponsoredProductAdapter(this, sponsoredProduct);

        getSponsorProduct();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.specialproductList.setLayoutManager(layoutManager);
        binding.specialproductList.setAdapter(sponsoredProductsAdapter);
    }

    void getSponsorProduct() {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = ConstantsDashboard.GET_SPECIALITY_ALL_PRODUCT_SPONSORED;
        @SuppressLint("NotifyDataSetChanged") StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if(object.getString("status").equals("success")){
                    JSONArray sponsoredproductsArray = object.getJSONArray("data");
                    for(int i =0; i< sponsoredproductsArray.length(); i++) {
                        JSONObject childObj = sponsoredproductsArray.getJSONObject(i);
                        JSONObject sponsorproductObj = childObj.getJSONObject("data");

                        String documentId = childObj.getString("id");

                        Product sponsoredproduct = new Product(
                                sponsorproductObj.getString("Title"),
                                sponsorproductObj.getString("thumbnail"),
                                sponsorproductObj.getString("Author"),
                                sponsorproductObj.getDouble("Price"),
                                sponsorproductObj.getString("Type"),
                                sponsorproductObj.getString("Category"),
                                documentId,
                                sponsorproductObj.getString("Subject")
                        );

                        sponsoredProduct.add(sponsoredproduct);
                    }
                    sponsoredProductsAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> { });

        queue.add(request);
    }

    //....ShowCase Products.....

    void initProducts() {
        products = new ArrayList<>();
        productAdapter = new ProductAdapter(this, products);

        getRecentProducts();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        binding.productList.setLayoutManager(layoutManager);
        binding.productList.setAdapter(productAdapter);
    }

    void getRecentProducts() {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = ConstantsDashboard.GET_SPECIALITY_ALL_PRODUCT;
        @SuppressLint("NotifyDataSetChanged") StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if(object.getString("status").equals("success")){
                    JSONArray productsArray = object.getJSONArray("data");
                    for(int i =0; i< productsArray.length(); i++) {
                        JSONObject childObj = productsArray.getJSONObject(i);
                        JSONObject productObj = childObj.getJSONObject("data");

                        String documentId = childObj.getString("id");

                        Product product = new Product(
                                productObj.getString("Title"),
                                productObj.getString("thumbnail"),
                                productObj.getString("Author"),
                                productObj.getDouble("Price"),
                                productObj.getString("Type"),
                                productObj.getString("Category"),
                                documentId,
                                productObj.getString("Subject")
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

    //....Navigate Back Statement...

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    //...........................

}
