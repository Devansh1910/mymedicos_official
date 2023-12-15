package com.example.my_medicos.activities.pg.activites.insiders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.my_medicos.R;
import com.example.my_medicos.activities.pg.fragment.QuestionbankFragment;
import com.example.my_medicos.activities.pg.fragment.VideoBankFragment;
import com.example.my_medicos.activities.pg.fragment.WeeklyQuizFragment;
import com.example.my_medicos.databinding.ActivitySpecialityPgBinding;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SpecialityPGInsiderActivity extends AppCompatActivity {

    ActivitySpecialityPgBinding binding;
//    ArrayList<SpecialitiesPG> specialitiesPostGraduate;
    BottomNavigationView bottomNavigationCategoryPublication;
    BottomAppBar bottomAppBarCategoryPublication;

    Toolbar toolbarpginsider;

//    SpecialitiesPGInsiderAdapter specialitiesPGInsiderAdapter;

    int catId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpecialityPgBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        toolbarpginsider = binding.specialitytoolbar;
        Intent intent = getIntent();
        String extraValue = intent.getStringExtra("specialityPgName");
        if (extraValue!=null) {
            Log.d("Specialitypdname", extraValue);
            Toolbar text=findViewById(R.id.specialitytoolbar);
            text.setTitle(extraValue);
        }

        else {
            Log.d("abcdef","ajcbaj");
        }




        setSupportActionBar(toolbarpginsider);



        bottomAppBarCategoryPublication = findViewById(R.id.bottomappabarslideshow);
        Log.d("abcdef","hscvh");
        Log.d("Debug", "BottomAppBar ID: " + bottomAppBarCategoryPublication);

//        bottomNavigationCategoryPublication = findViewById(R.id.bottomNavigationViewcategorypublication);

        if (bottomAppBarCategoryPublication != null) {
//            bottomAppBarCategoryPublication.replaceFragment(new QuestionbankFragment());
//            bottomNavigationCategoryPublication.setBackground(null);
            bottomNavigationCategoryPublication = bottomAppBarCategoryPublication.findViewById(R.id.bottomNavigationViewslideshow);

            bottomNavigationCategoryPublication.setBackground(null);
            Log.d("abcdef","hscvh");

            replaceFragment(QuestionbankFragment.newInstance(0));

//            replaceFragment(QuestionbankFragment.newInstance(catId));

            // Inside onCreate method
            bottomNavigationCategoryPublication.setOnItemSelectedListener(item -> {
                int frgId = item.getItemId();
                Log.d("abcdef","hscvh");
                Log.d("ItemSelected", "Fragment ID: " + frgId);
                if (frgId == R.id.qb) {
                    Log.d("ItemSelected", "Selected QuestionbankFragment");
                    replaceFragment(QuestionbankFragment.newInstance(catId));
                } else if (frgId == R.id.lc) {
                    Log.d("ItemSelected", "Selected VideoBankFragment");
                    replaceFragment(new VideoBankFragment());
                } else {
                    Log.d("ItemSelected", "Selected WeeklyQuizFragment");
                    replaceFragment(new WeeklyQuizFragment());
                }
                return true;
            });

        } else {
            Log.e("Error", "bottomNavigationCategoryPublication is null");
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_slideshow, fragment);
        fragmentTransaction.commit();
    }


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