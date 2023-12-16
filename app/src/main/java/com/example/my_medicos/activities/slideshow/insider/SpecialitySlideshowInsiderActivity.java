package com.example.my_medicos.activities.slideshow.insider;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.my_medicos.R;
import com.example.my_medicos.activities.pg.adapters.insiders.SpecialitiesPGInsiderAdapter;
import com.example.my_medicos.activities.pg.fragment.QuestionbankFragment;
import com.example.my_medicos.activities.pg.fragment.VideoBankFragment;
import com.example.my_medicos.activities.pg.fragment.WeeklyQuizFragment;
import com.example.my_medicos.activities.pg.model.SpecialitiesPG;
import com.example.my_medicos.activities.publications.activity.fragments.FreeFragment;
import com.example.my_medicos.activities.publications.activity.fragments.PaidFragment;
import com.example.my_medicos.activities.publications.activity.fragments.ResearchPaperFragment;
import com.example.my_medicos.activities.publications.activity.fragments.TextBooksFragment;
import com.example.my_medicos.activities.publications.utils.Constants;
import com.example.my_medicos.databinding.ActivitySlideshowInsidernBinding;
import com.example.my_medicos.databinding.ActivitySpecialityPgBinding;
import com.example.my_medicos.databinding.ActivitySpecialitySlideshowInsiderBinding;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SpecialitySlideshowInsiderActivity extends AppCompatActivity {

    ActivitySpecialitySlideshowInsiderBinding binding;
    Toolbar toolbarpginsider;

//    SpecialitiesPGInsiderAdapter specialitiesPGInsiderAdapter;

    int catId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpecialitySlideshowInsiderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(toolbarpginsider);


        //    void initSpecialityPG() {
//        specialitiesPostGraduate = new ArrayList<>();
//        specialitiesPGInsiderAdapter = new SpecialitiesPGInsiderAdapter(this, specialitiesPostGraduate);
//
//        getSpecialitiesPG();
//
//        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
//        binding.specialityinsidercontentList.setLayoutManager(layoutManager);
//        binding.specialityinsidercontentList.setAdapter(specialitiesPGInsiderAdapter);
//    }
//
//    void getSpecialitiesPG() {
//        RequestQueue queue = Volley.newRequestQueue(this);
//
//        StringRequest request = new StringRequest(Request.Method.GET, Constants.GET_CATEGORIES_URL, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    Log.e("err", response);
//                    JSONObject mainObj = new JSONObject(response);
//                    if (mainObj.getString("status").equals("success")) {
//                        JSONArray specialityArray = mainObj.getJSONArray("categories");
//                        for (int i = 0; i < specialityArray.length(); i++) {
//                            JSONObject object = specialityArray.getJSONObject(i);
//                            SpecialitiesPG specialitiesPGS = new SpecialitiesPG(
//                                    object.getString("id"),
//                                    object.getInt("priority")
//                            );
//                            specialitiesPostGraduate.add(specialitiesPGS);
//                        }
//                        specialitiesPGInsiderAdapter.notifyDataSetChanged();
//
//                    } else {
//                        // DO nothing
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//
//        queue.add(request);
//    }
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}