package com.example.my_medicos.activities.university.activity;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.my_medicos.R;
import com.example.my_medicos.activities.publications.activity.insiders.CategoryPublicationInsiderActivity;
import com.example.my_medicos.activities.publications.adapters.ProductAdapter;
import com.example.my_medicos.activities.publications.model.Product;
import com.example.my_medicos.activities.publications.utils.Constants;
import com.example.my_medicos.activities.university.activity.insiders.UniversitiesInsiderActivity;
import com.example.my_medicos.activities.university.adapters.StatesAdapter;
import com.example.my_medicos.activities.university.adapters.UniversitiesAdapter;
import com.example.my_medicos.activities.university.adapters.UpdatesAdapter;
import com.example.my_medicos.activities.university.model.States;
import com.example.my_medicos.activities.university.model.Universities;
import com.example.my_medicos.activities.university.model.Updates;
import com.example.my_medicos.activities.utils.ConstantsDashboard;
import com.example.my_medicos.databinding.ActivityUniversityupdatesBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.prefs.Preferences;

public class UniversityActivity extends AppCompatActivity {
    ActivityUniversityupdatesBinding binding;
    UniversitiesAdapter universitiesAdapter;
    ArrayList<Universities> universities;
    ArrayList<States> statesofindia;
    StatesAdapter statesAdapter;
    UpdatesAdapter updateAdapter;
    ArrayList<Updates> updates;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUniversityupdatesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = findViewById(R.id.universitytoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initUpdates();
        initStates();
        initSliderUpdates();
    }

    private void initSliderUpdates() {
        getUpdatesSlider();
    }
    void initStates() {
        statesofindia = new ArrayList<>();
        statesAdapter = new StatesAdapter(this, statesofindia);

        getStates();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        binding.statesList.setLayoutManager(layoutManager);
        binding.statesList.setAdapter(statesAdapter);
    }
    void getStates() {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, ConstantsDashboard.GET_STATES, new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("err", response);
                    JSONObject mainObj = new JSONObject(response);
                    if (mainObj.getString("status").equals("success")) {
                        JSONArray statesArray = mainObj.getJSONArray("data");
                        for (int i = 0; i < statesArray.length(); i++) {
                            String stateName = statesArray.getString(i);
                            States state = new States(stateName);
                            statesofindia.add(state);
                        }
                        statesAdapter.notifyDataSetChanged();
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

    void initUpdates() {
        updates = new ArrayList<>();
        updateAdapter = new UpdatesAdapter(this, updates);

        getRecentUpdates();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        binding.updatesList.setLayoutManager(layoutManager);
        binding.updatesList.setAdapter(updateAdapter);
    }
    void getRecentUpdates() {
        getInterest(new VolleyCallback() {
            @Override
            public void onSuccess(String userInterest) {
                RequestQueue queue = Volley.newRequestQueue(UniversityActivity.this);

                StringRequest request = new StringRequest(Request.Method.GET, ConstantsDashboard.GET_UNIVERSITIES, new Response.Listener<String>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("err", response);
                            JSONObject mainObj = new JSONObject(response);
                            if (mainObj.getString("status").equals("success")) {
                                JSONArray statesArray = mainObj.getJSONArray("data");
                                for (int i = 0; i < statesArray.length(); i++) {
                                    String stateName = statesArray.getString(i);
                                    States state = new States(stateName);
                                    statesofindia.add(state);
                                }
                                statesAdapter.notifyDataSetChanged();
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
        });
    }

    void getInterest(final VolleyCallback callback) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users")
                    .whereEqualTo("Phone Number", currentUser.getPhoneNumber())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String userInterest = (String) document.get("Interest");
                                    if (userInterest != null) {
                                        callback.onSuccess(userInterest);
                                    } else {
                                        callback.onSuccess(""); // Provide a default value or handle accordingly
                                    }
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }

    interface VolleyCallback {
        void onSuccess(String userInterest);
    }

    void getUpdatesSlider() {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, ConstantsDashboard.GET_UPDATES_SLIDER_URL, response -> {
            try {
                JSONArray homesliderArray = new JSONArray(response);
                for (int i = 0; i < homesliderArray.length(); i++) {
                    JSONObject childObj = homesliderArray.getJSONObject(i);
                    binding.carousel.addData(
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
        });
        queue.add(request);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
    private void fetchUserInterest() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getPhoneNumber();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users")
                    .whereEqualTo("Phone Number", currentUser.getPhoneNumber())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());

                                    String userInterest = (String) document.get("Interest");

                                    if (userInterest != null) {
                                        Log.d(TAG, "User Interest: " + userInterest);
                                    } else {
                                        Log.e(TAG, "User Interest is null");
                                    }
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }
}