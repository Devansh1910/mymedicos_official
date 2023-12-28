package com.medical.my_medicos.activities.pg.activites.extras;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.annotation.SuppressLint;
import android.os.Bundle;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.news.News;
import com.medical.my_medicos.activities.news.NewsAdapter;
import com.medical.my_medicos.activities.news.NewsToday;
import com.medical.my_medicos.activities.pg.activites.extras.adapter.RecentUpdatesAdapter;
import com.medical.my_medicos.databinding.ActivityRecetUpdatesNoticeBinding;
import java.util.ArrayList;

public class RecetUpdatesNoticeActivity extends AppCompatActivity {
    ActivityRecetUpdatesNoticeBinding binding;
    RecentUpdatesAdapter newsupdatespgAdapter;
    ArrayList<News> newspg;

    private SwipeRefreshLayout swipeRefreshLayoutUpdates;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecetUpdatesNoticeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        swipeRefreshLayoutUpdates = findViewById(R.id.swipeRefreshLayoutNews);
        swipeRefreshLayoutUpdates.setOnRefreshListener(this::refreshContent);
        binding.newsstoolbar.setNavigationOnClickListener(v -> onBackPressed());

        initImportantUpdates();
    }

    private void refreshContent() {
        clearData();
        fetchData();
        swipeRefreshLayoutUpdates.setRefreshing(false);
    }
    private void clearData() {
        newspg.clear();
    }
    private void fetchData() {
        getRecentNewsUpdates();
    }

    void initImportantUpdates() {
        newspg = new ArrayList<News>();
        newsupdatespgAdapter = new RecentUpdatesAdapter(this, newspg);
        getRecentNewsUpdates();
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        binding.newsListPg.setLayoutManager(layoutManager);

        binding.newsListPg.setAdapter(newsupdatespgAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    void getRecentNewsUpdates() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("MedicalNews")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String newsType = document.getString("type");

                            if ("Notice".equals(newsType)) {
                                News newsItem = new News(
                                        document.getString("Title"),
                                        document.getString("thumbnail"),
                                        document.getString("Description"),
                                        document.getString("Time"),
                                        document.getString("URL"),
                                        newsType
                                );
                                newspg.add(newsItem);
                            }
                        }
                        newsupdatespgAdapter.notifyDataSetChanged();
                    } else {
                        // Handle the case where data retrieval is not successful
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

}