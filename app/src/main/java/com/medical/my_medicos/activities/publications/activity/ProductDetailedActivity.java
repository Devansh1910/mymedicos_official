package com.medical.my_medicos.activities.publications.activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.Layout;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.home.fragments.HomeFragment;
import com.medical.my_medicos.activities.login.MainActivity;
import com.medical.my_medicos.activities.login.NavigationActivity;
import com.medical.my_medicos.activities.pg.activites.PgprepActivity;
import com.medical.my_medicos.activities.pg.animations.CorrectAnswerActivity;
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
    TextView  tocartgo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        showLoadForLib();

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
                    Intent intent = new Intent(ProductDetailedActivity.this, SearchPublicationActivity.class);
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

        String name = getIntent().getStringExtra("Title");
        String image = getIntent().getStringExtra("thumbnail");
        String id = getIntent().getStringExtra("id");
        double price = getIntent().getDoubleExtra("Price",0);

        Glide.with(this)
                .load(image)
                .into(binding.productImage);


        getProductDetails(id);

        String query = getIntent().getStringExtra("Title");

        TextView titleTextView = findViewById(R.id.titleoftheproduct);
        titleTextView.setText(query);

        ImageView backToPublicationActivity = findViewById(R.id.backtothepublicationactivity);
        backToPublicationActivity.setOnClickListener(v -> {
            finish();
        });

        binding.addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDocId();

                String docId = Preferences.userRoot().get("docId", "");

                if (!docId.isEmpty()) {
                    String url = ConstantsDashboard.GET_CART + "/" + docId + "/add/" + "0ACmQESDsQQUZ8CduPYf";
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
        tocartgo = findViewById(R.id.tocartgo);
        tocartgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProductDetailedActivity.this, CartPublicationActivity.class);
                startActivity(i);
            }
        });
    }

    private void showLoadForLib() {
        binding.loaderforlib.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.loaderforlib.setVisibility(View.GONE);
            }
        }, 2000);
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

                        // Extract product details
                        String title = product.getString("Title");
                        String author = product.getString("Author");
                        String type = product.getString("Type");
                        String description = product.getString("Description");
                        double price = product.getDouble("Price");

                        // Set the UI elements with product details
                        binding.titleoftheproduct.setText(title);
                        binding.productauthor.setText(author);
                        binding.producttype.setText(type);
                        binding.productDescription.setText(Html.fromHtml(description));
                        binding.productprice.setText("₹" + String.valueOf(price));

                        currentProduct = new Product(
                                title,
                                product.getString("thumbnail"),
                                author,
                                price,
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
                // Handle error if needed
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