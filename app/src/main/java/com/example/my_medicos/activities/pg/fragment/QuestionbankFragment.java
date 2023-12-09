package com.example.my_medicos.activities.pg.fragment;

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
import com.example.my_medicos.activities.pg.adapters.insiders.SpecialitiesPGInsiderAdapter;
import com.example.my_medicos.activities.pg.model.SpecialitiesPG;
import com.example.my_medicos.activities.publications.adapters.ProductAdapter;
import com.example.my_medicos.activities.publications.model.Product;
import com.example.my_medicos.activities.publications.utils.Constants;
import com.example.my_medicos.activities.utils.ConstantsDashboard;
import com.example.my_medicos.databinding.FragmentQuestionbankBinding;
import com.example.my_medicos.databinding.FragmentTextBooksBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuestionbankFragment extends Fragment {

    private FragmentQuestionbankBinding binding;
    private ProductAdapter productAdapter;
    SpecialitiesPGInsiderAdapter specialitiesPGInsiderAdapter;
    ArrayList<SpecialitiesPG> specialitiesPostGraduate;
    private ArrayList<Product> products;
    private int catId;

    public static QuestionbankFragment newInstance(int catId) {
        QuestionbankFragment fragment = new QuestionbankFragment();
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
        binding = FragmentQuestionbankBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        products = new ArrayList<>();
        productAdapter = new ProductAdapter(requireContext(), products);

        RecyclerView recyclerView = binding.specialityinsidercontentListQuestion;
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(productAdapter);

        initSpecialityPG();

        return view;
    }

    void initSpecialityPG() {
        specialitiesPostGraduate = new ArrayList<>();
        specialitiesPGInsiderAdapter = new SpecialitiesPGInsiderAdapter(getContext(), specialitiesPostGraduate);

        getSpecialitiesPG();

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        binding.specialityinsidercontentListQuestion.setLayoutManager(layoutManager);
        binding.specialityinsidercontentListQuestion.setAdapter(specialitiesPGInsiderAdapter);
    }

    void getSpecialitiesPG() {
        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest request = new StringRequest(Request.Method.GET, Constants.GET_CATEGORIES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("err", response);
                    JSONObject mainObj = new JSONObject(response);
                    if (mainObj.getString("status").equals("success")) {
                        JSONArray specialityArray = mainObj.getJSONArray("categories");
                        for (int i = 0; i < specialityArray.length(); i++) {
                            JSONObject object = specialityArray.getJSONObject(i);
                            SpecialitiesPG specialitiesPGS = new SpecialitiesPG(
                                    object.getString("id"),
                                    object.getInt("priority")
                            );
                            specialitiesPostGraduate.add(specialitiesPGS);
                        }
                        specialitiesPGInsiderAdapter.notifyDataSetChanged();

                    } else {

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

}
