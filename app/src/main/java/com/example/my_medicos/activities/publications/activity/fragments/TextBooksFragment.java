package com.example.my_medicos.activities.publications.activity.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.my_medicos.activities.publications.adapters.ProductAdapter;
import com.example.my_medicos.activities.publications.model.Product;
import com.example.my_medicos.activities.publications.utils.Constants;
import com.example.my_medicos.activities.utils.ConstantsDashboard;
import com.example.my_medicos.databinding.FragmentTextBooksBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TextBooksFragment extends Fragment {

    private FragmentTextBooksBinding binding;
    private ProductAdapter productAdapter;
    private ArrayList<Product> products;
    private int catId;
    public static TextBooksFragment newInstance(int catId) {
        TextBooksFragment fragment = new TextBooksFragment();
        Bundle args = new Bundle();
        args.putInt("catId", catId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            catId = getArguments().getInt("catId", 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTextBooksBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        products = new ArrayList<>();
        productAdapter = new ProductAdapter(requireContext(), products);

        RecyclerView recyclerView = binding.recyclerViewTextBooks;
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(productAdapter);

        getProducts(catId);

        return view;
    }

    private void getProducts(int catId) {
        RequestQueue queue = Volley.newRequestQueue(requireContext());

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

    //... (other methods)
}
