package com.medical.my_medicos.activities.publications.activity.mainfragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.publications.adapters.BookoftheDayAdapter;
import com.medical.my_medicos.activities.publications.adapters.ProductAdapter;
import com.medical.my_medicos.activities.publications.adapters.RecentHomeProductsAdapter;
import com.medical.my_medicos.activities.publications.adapters.SponsoredProductAdapter;
import com.medical.my_medicos.activities.publications.model.Product;
import com.medical.my_medicos.activities.publications.utils.Constants;
import com.medical.my_medicos.activities.utils.ConstantsDashboard;
import com.medical.my_medicos.databinding.FragmentHomePublicationBinding;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class HomePublicationFragment extends Fragment {

    private FragmentHomePublicationBinding binding;

    private BookoftheDayAdapter BookofthedayAdapter;
    private ArrayList<Product> bookoftheday;
    private ProductAdapter productAdapter;
    private ArrayList<Product> products;
    private RecentHomeProductsAdapter recentHomeProductsAdapter;
    private ArrayList<Product> recentHomeProducts;
    private SponsoredProductAdapter sponsoredProductsAdapter;
    private ArrayList<Product> sponsoredProduct;
    private LottieAnimationView loader;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomePublicationBinding.inflate(inflater, container, false);

        initProducts();
        initTopExploredReadables();
        initSlider();
        initSponsorSlider();
        initSponsorProduct();
        initBookReadables();


        return binding.getRoot();
    }

    //... Normal Slider.....
    private void initSlider() {
        getRecentOffers();
    }

    void getRecentOffers() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());

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
        getRecentSliderSponsored();
    }

    void getRecentSliderSponsored() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        StringRequest request = new StringRequest(Request.Method.GET, ConstantsDashboard.GET_SPONSORS_SLIDER_URL, response -> {
            try {
                JSONArray newssliderArray = new JSONArray(response);
                if (newssliderArray.length() > 0) {
                    JSONObject childObj = newssliderArray.getJSONObject(0);
                    String imageUrl = childObj.getString("url");
                    loadImageIntoView(imageUrl);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            // Handle error if needed
        });
        queue.add(request);
    }

    private void loadImageIntoView(String imageUrl) {
        Glide.with(requireContext())
                .load(imageUrl)
                .into(binding.imagesponsor);
    }

    void initTopExploredReadables() {
        recentHomeProducts = new ArrayList<>();
        recentHomeProductsAdapter = new RecentHomeProductsAdapter(requireContext(), recentHomeProducts);

        getTopExploredReadables();

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.recentaddedbooksList.setLayoutManager(layoutManager);
        binding.recentaddedbooksList.setAdapter(recentHomeProductsAdapter);
    }

    void getTopExploredReadables() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());

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
                                recenthomeproductObj.getString("id"),
                                recenthomeproductObj.getString("Title"),
                                recenthomeproductObj.getString("thumbnail"),
                                recenthomeproductObj.getString("Author"),
                                recenthomeproductObj.getDouble("Price"),
                                recenthomeproductObj.getString("Type"),
                                recenthomeproductObj.getString("Category"),
                                recenthomeproductObj.getString("Subject"),
                                recenthomeproductObj.getString("URL")
                        );

                        recentHomeProducts.add(recenthomeproduct);
                    }
                    recentHomeProductsAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> { });

        queue.add(request);
    }

    //......For Book Read...........

    void initBookReadables() {
        bookoftheday = new ArrayList<>();
        BookofthedayAdapter = new BookoftheDayAdapter(requireContext(), bookoftheday);

        getBookReadables();

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.bookoftheday.setLayoutManager(layoutManager);
        binding.bookoftheday.setAdapter(BookofthedayAdapter);
    }

    void getBookReadables() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = ConstantsDashboard.GET_SPECIALITY_ALL_PRODUCT;

        @SuppressLint("NotifyDataSetChanged") StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getString("status").equals("success")) {
                    JSONArray dataArray = object.getJSONArray("data");
                    List<Product> allProducts = new ArrayList<>();
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject childObj = dataArray.getJSONObject(i);
                        JSONObject productObj = childObj.getJSONObject("data");
                        String documentId = childObj.getString("id");
                        Product product = new Product(
                                productObj.getString("id"),
                                productObj.getString("Title"),
                                productObj.getString("thumbnail"),
                                productObj.getString("Author"),
                                productObj.getDouble("Price"),
                                productObj.getString("Type"),
                                productObj.getString("Category"),
                                productObj.getString("Subject"),
                                productObj.getString("URL")
                        );
                        allProducts.add(product);
                    }

                    Product selectedBook = selectRandomBook(allProducts, requireContext());
                    if (selectedBook != null) {
                        bookoftheday.clear();
                        bookoftheday.add(selectedBook);
                        BookofthedayAdapter.notifyDataSetChanged();
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

    Product selectRandomBook(List<Product> products, Context context) {
        Set<String> displayedBookIds = loadDisplayedBookIds(context);

        List<Product> filteredProducts = new ArrayList<>();
        for (Product product : products) {
            if (!displayedBookIds.contains(product.getId())) {
                filteredProducts.add(product);
            }
        }

        if (!filteredProducts.isEmpty()) {
            int randomIndex = new Random().nextInt(filteredProducts.size());
            Product selectedBook = filteredProducts.get(randomIndex);
            updateDisplayedBookIds(selectedBook.getId(), context);
            return selectedBook;
        }
        return null;
    }

    Set<String> loadDisplayedBookIds(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("BookDisplayHistory", Context.MODE_PRIVATE);
        return prefs.getStringSet("DisplayedBookIds", new HashSet<>());
    }

    void updateDisplayedBookIds(String newBookId, Context context) {
        SharedPreferences prefs = context.getSharedPreferences("BookDisplayHistory", Context.MODE_PRIVATE);
        Set<String> displayedBookIds = new HashSet<>(prefs.getStringSet("DisplayedBookIds", new HashSet<>()));
        if (displayedBookIds.size() >= 3) {
            List<String> tempList = new ArrayList<>(displayedBookIds);
            tempList.remove(0); // Remove the oldest entry
            displayedBookIds = new HashSet<>(tempList);
        }
        displayedBookIds.add(newBookId);
        prefs.edit().putStringSet("DisplayedBookIds", displayedBookIds).apply();
    }


    //.....For the Sponsor

    void initSponsorProduct() {
        sponsoredProduct = new ArrayList<>();
        sponsoredProductsAdapter = new SponsoredProductAdapter(requireContext(), sponsoredProduct);

        getSponsorProduct();

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.specialproductList.setLayoutManager(layoutManager);
        binding.specialproductList.setAdapter(sponsoredProductsAdapter);
    }

    void getSponsorProduct() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());

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
                                sponsorproductObj.getString("id"),
                                sponsorproductObj.getString("Title"),
                                sponsorproductObj.getString("thumbnail"),
                                sponsorproductObj.getString("Author"),
                                sponsorproductObj.getDouble("Price"),
                                sponsorproductObj.getString("Type"),
                                sponsorproductObj.getString("Category"),
                                sponsorproductObj.getString("Subject"),
                                sponsorproductObj.getString("URL")
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
        productAdapter = new ProductAdapter(requireContext(), products);

        getRecentProducts();

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.productList.setLayoutManager(layoutManager);
        binding.productList.setAdapter(productAdapter);

    }

    void getRecentProducts() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());

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
                                productObj.getString("id"),
                                productObj.getString("Title"),
                                productObj.getString("thumbnail"),
                                productObj.getString("Author"),
                                productObj.getDouble("Price"),
                                productObj.getString("Type"),
                                productObj.getString("Category"),
                                productObj.getString("Subject"),
                                productObj.getString("URL")
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Clean up the binding reference
    }
}
