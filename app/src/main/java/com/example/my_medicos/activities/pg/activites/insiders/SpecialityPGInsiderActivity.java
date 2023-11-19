package com.example.my_medicos.activities.pg.activites.insiders;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.my_medicos.R;
import com.example.my_medicos.activities.pg.adapters.insiders.SpecialitiesPGInsiderAdapter;
import com.example.my_medicos.activities.pg.model.SpecialitiesPG;
import com.example.my_medicos.activities.publications.utils.Constants;
import com.example.my_medicos.databinding.ActivitySpecialityPgBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SpecialityPGInsiderActivity extends AppCompatActivity {

    ActivitySpecialityPgBinding binding;

    ArrayList<SpecialitiesPG> specialitiesPostGraduate;

    Toolbar toolbarpginsider;

    SpecialitiesPGInsiderAdapter specialitiesPGInsiderAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpecialityPgBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbarpginsider = findViewById(R.id.pginsidertoolbar);
        setSupportActionBar(toolbarpginsider);
//        tab=findViewById(R.id.tabLayout);
//        viewPager=findViewById(R.id.view_pager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initSpecialityPG();

    }

    void initSpecialityPG() {
        specialitiesPostGraduate = new ArrayList<>();
        specialitiesPGInsiderAdapter = new SpecialitiesPGInsiderAdapter(this, specialitiesPostGraduate);

        getSpecialitiesPG();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        binding.categoriesListInsider.setLayoutManager(layoutManager);
        binding.categoriesListInsider.setAdapter(specialitiesPGInsiderAdapter);
    }

    void getSpecialitiesPG() {
        RequestQueue queue = Volley.newRequestQueue(this);

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
                                    object.getString("name"),
                                    object.getString("icon"),
                                    object.getString("color"),
                                    object.getString("brief"),
                                    object.getInt("id")
                            );
                            specialitiesPostGraduate.add(specialitiesPGS);
                        }
                        specialitiesPGInsiderAdapter.notifyDataSetChanged();

                    } else {
                        // DO nothing
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

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                // Handle the back arrow click, finish the current activity
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}