package com.example.my_medicos.activities.pg.activites.detailed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.my_medicos.R;
import com.example.my_medicos.activities.pg.model.QuestionsPG;
import com.example.my_medicos.activities.publications.model.Product;
import com.example.my_medicos.activities.publications.utils.Constants;
import com.example.my_medicos.databinding.ActivityProductDetailedBinding;
import com.example.my_medicos.databinding.ActivityQuestionBankDetailedBinding;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;

import org.json.JSONException;
import org.json.JSONObject;


public class QuestionBankDetailedActivity extends AppCompatActivity {


    ActivityQuestionBankDetailedBinding binding;
    QuestionsPG currentQuestionBank;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityQuestionBankDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String name = getIntent().getStringExtra("name");
        String image = getIntent().getStringExtra("image");
        int id = getIntent().getIntExtra("id",0);
        double price = getIntent().getDoubleExtra("price",0);

        Glide.with(this)
                .load(image)
                .into(binding.questionbankthumbnailImage);

        getProductDetails(id);

        getSupportActionBar().setTitle(name);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Cart cart = TinyCartHelper.getCart();


        binding.DownloadQuestionBankBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cart.addItem(currentQuestionBank,1);
                binding.DownloadQuestionBankBtn.setEnabled(false);
                binding.DownloadQuestionBankBtn.setText("Added in cart");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart, menu);
        return super.onCreateOptionsMenu(menu);
    }

    void getProductDetails(int id) {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Constants.GET_PRODUCT_DETAILS_URL + id;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if(object.getString("status").equals("success")) {
                        JSONObject questionbank = object.getJSONObject("product");
                        String description = questionbank.getString("description");
                        binding.questionbankDescription.setText(
                                Html.fromHtml(description)
                        );

                        currentQuestionBank = new QuestionsPG(
                                questionbank.getString("name"),
                                Constants.PRODUCTS_IMAGE_URL + questionbank.getString("image"),
                                questionbank.getString("status"),
                                questionbank.getDouble("price"),
                                questionbank.getDouble("price_discount"),
                                questionbank.getInt("stock"),
                                questionbank.getInt("id")
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