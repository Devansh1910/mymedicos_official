package com.medical.my_medicos.activities.news;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.publications.adapters.ProductAdapter;
import com.medical.my_medicos.activities.publications.model.Product;
import com.medical.my_medicos.activities.utils.ConstantsDashboard;
import com.medical.my_medicos.databinding.ActivitySearchNewsBinding;
import com.medical.my_medicos.databinding.ActivitySearchPublicationBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchNewsActivity extends AppCompatActivity {

    ActivitySearchNewsBinding binding;
    NewsAdapter newsAdapter;
    ArrayList<News> news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchNewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        news = new ArrayList<>();
        newsAdapter = new NewsAdapter(this, news);

        String query = getIntent().getStringExtra("query");

        // Set the query as the title of the search
        TextView titleTextView = findViewById(R.id.titleofthesearch);
        titleTextView.setText(query);

        getNews();

        ImageView backToPublicationActivity = findViewById(R.id.backtothenewsactivity);
        backToPublicationActivity.setOnClickListener(v -> {
            finish();
        });

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        binding.newsList.setLayoutManager(layoutManager);
        binding.newsList.setAdapter(newsAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    void getNews() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("MedicalNews")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String newsType = document.getString("type");

                            // Check if the news type is "News"
                            if ("News".equals(newsType)) {
                                News newsItem = new News(
                                        document.getString("Title"),
                                        document.getString("thumbnail"),
                                        document.getString("Description"),
                                        document.getString("Time"),
                                        document.getString("URL"),
                                        newsType
                                );
                                news.add(newsItem);
                            }
                        }
                        newsAdapter.notifyDataSetChanged();
                        if (news.isEmpty()) {
                            binding.noResults.setVisibility(View.VISIBLE);
                        } else {
                            binding.noResults.setVisibility(View.GONE);
                        }
                    } else {
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
