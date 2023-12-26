package com.medical.my_medicos.activities.publications.activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.publications.model.Product;
import com.medical.my_medicos.activities.utils.ConstantsDashboard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.medical.my_medicos.databinding.ActivityProductDetailedBinding;

import org.json.JSONException;

import java.util.prefs.Preferences;


public class ProductDetailedActivity extends AppCompatActivity {


    ActivityProductDetailedBinding binding;
    Product currentProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String name = getIntent().getStringExtra("Title");
        String image = getIntent().getStringExtra("thumbnail");
        String id = getIntent().getStringExtra("id");
        double price = getIntent().getDoubleExtra("Price",0);

        Glide.with(this)
                .load(image)
                .into(binding.productImage);

        getProductDetails(id);

        getSupportActionBar().setTitle(name);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        binding.addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDocId();

                String docId = Preferences.userRoot().get("docId", "");

                if (!docId.isEmpty()) {
                    String url = ConstantsDashboard.GET_CART + "/" + docId + "/add/" + "WrCBMohrJpfOUQAEo3yJ";
                    Log.e("function", url);

                    JSONObject requestBody = new JSONObject();
                    MyVolleyRequest.sendPostRequest(getApplicationContext(), url, requestBody, new MyVolleyRequest.VolleyCallback() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            Log.d("VolleyResponse", response.toString());
                        }

                        @Override
                        public void onError(String error) {
                            Log.e("VolleyError", error);
                        }
                    });
                } else {
                    Log.e("DocIdError", "Empty or not available");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.cart) {
            startActivity(new Intent(this, CartPublicationActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
    private void getDocId() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users")
                    .whereEqualTo("Phone Number", currentUser.getPhoneNumber())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String docID = document.getId();

                                    Preferences preferences = Preferences.userRoot();
                                    preferences.put("docId", docID);

                                    // Break out of the loop once the user is found
                                    break;
                                }
                            } else {
                                Log.e(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }

    void getProductDetails(String id) {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = ConstantsDashboard.GET_SPECIALITY_PRODUCT + id;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if(object.getString("status").equals("success")) {
                        JSONObject product = object.getJSONObject("data");
                        String description = product.getString("id");
                        binding.productDescription.setText(
                                Html.fromHtml(description)
                        );
                        Log.e("Something Went wrong..",product.getString("Title"));

                        currentProduct = new Product(
                                product.getString("Title"),
                                product.getString("thumbnail"),
                                product.getString("Author"),
                                product.getDouble("Price"),
                                product.getString("Type"),
                                product.getString("Category"),
                                product.getString("Subject"),
                                object.getString("id")

                        );


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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}