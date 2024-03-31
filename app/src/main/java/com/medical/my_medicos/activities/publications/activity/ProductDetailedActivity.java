package com.medical.my_medicos.activities.publications.activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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

import java.util.ArrayList;
import java.util.prefs.Preferences;

import jp.wasabeef.glide.transformations.BlurTransformation;


public class ProductDetailedActivity extends AppCompatActivity {
    ActivityProductDetailedBinding binding;
    Product currentProduct;
    TextView  tocartgo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.backgroundcolor));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.backgroundcolor));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        showLoadForLib();

        String name = getIntent().getStringExtra("Title");
        String image = getIntent().getStringExtra("thumbnail");
        String id = getIntent().getStringExtra("id");
        double price = getIntent().getDoubleExtra("Price",0);

        Glide.with(this)
                .load(image)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 4))) // Adjust the radius and sampling for desired blur level
                .into(binding.blurimageofthebook);

        Glide.with(this)
                .load(image)
                .into(binding.image);

        getProductDetails(id);


        if (isProductInCart(id)) {
            binding.addToCartBtn.setVisibility(View.GONE);
        }

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
                    String url = ConstantsDashboard.GET_CART + "/" + docId + "/add/" + id;
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
                Intent i = new Intent(ProductDetailedActivity.this, CartFromDetailActivity.class);
                startActivity(i);
            }
        });

        getProductDetails(id);
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

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getString("status").equals("success")) {
                    JSONObject product = object.getJSONObject("data");

                    // Extract product details
                    String title = product.getString("Title");
                    String author = product.getString("Author");
                    String type = product.getString("Type");
                    String description = product.getString("Description");
                    double price = product.getDouble("Price");

                    // Set the UI elements with product details
                    binding.titleoftheproduct.setText(title);
                    binding.author.setText(author);
                    binding.type.setText(type);
                    binding.productDescription.setText(Html.fromHtml(description));
                    binding.productprice.setText("₹" + price);

                    currentProduct = new Product(
                            title,
                            product.getString("thumbnail"),
                            author,
                            price,
                            product.getString("Type"),
                            product.getString("Category"),
                            product.getString("Subject"),
                            product.getString("URL"),
                            object.getString("id")
                    );

                    // Adjust visibility based on the price
                    if (price == 0.0) {
                        binding.redeemBtn.setVisibility(View.VISIBLE);
                        binding.addToCartBtn.setVisibility(View.GONE);
                        binding.tocartgo.setVisibility(View.GONE);
                    } else {
                        binding.redeemBtn.setVisibility(View.GONE);
                        binding.addToCartBtn.setVisibility(View.VISIBLE);
                        binding.tocartgo.setVisibility(View.VISIBLE);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            // Handle error if needed
        });

        queue.add(request);
    }


    private boolean isProductInCart(String productId) {
        ArrayList<String> cartProductIds = getCartProductIds();
        return cartProductIds.contains(productId);
    }

    private ArrayList<String> getCartProductIds() {
        return new ArrayList<>();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}