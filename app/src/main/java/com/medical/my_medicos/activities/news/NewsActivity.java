package com.medical.my_medicos.activities.news;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.guide.JobGuideActivity;
import com.medical.my_medicos.activities.guide.NewsGuideActivity;
import com.medical.my_medicos.activities.home.HomeActivity;
import com.medical.my_medicos.activities.job.JobsActivity;
import com.medical.my_medicos.activities.job.JobsActivity2;
import com.medical.my_medicos.activities.job.fragments.LocumFragment;
import com.medical.my_medicos.activities.job.fragments.RegularFragment;
import com.medical.my_medicos.activities.pg.activites.PgprepActivity;
import com.medical.my_medicos.activities.publications.activity.PublicationActivity;
import com.medical.my_medicos.activities.publications.activity.SearchPublicationActivity;
import com.medical.my_medicos.activities.utils.ConstantsDashboard;
import com.medical.my_medicos.databinding.ActivityNewsBinding;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class  NewsActivity extends AppCompatActivity {
    ActivityNewsBinding binding;
    NewsAdapter newsAdapter;
    ArrayList<News> news;
    LinearLayout totheguide;
    TodayNewsAdapter todayNewsAdapter;
    ArrayList<NewsToday>  newstoday;
    private ImageView backtothehomefrompg;

    private TabLayout tabLayoutnews;
    private ViewPager2 pagernews, viewpagernews;
    private SwipeRefreshLayout swipeRefreshLayoutNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsBinding.inflate(getLayoutInflater());
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

        binding.searchBarNews.addTextChangeListener(new TextWatcher() {
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

        binding.searchBarNews.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                // Not needed for now
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                String query = text.toString();
                if (!TextUtils.isEmpty(query)) {
                    Intent intent = new Intent(NewsActivity.this, SearchNewsActivity.class);
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

        backtothehomefrompg = findViewById(R.id.backtothehomefrompg);
        backtothehomefrompg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NewsActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });

        swipeRefreshLayoutNews = findViewById(R.id.swipeRefreshLayoutNews);
        swipeRefreshLayoutNews.setOnRefreshListener(this::refreshContent);
        binding.newstoolbar.setNavigationOnClickListener(vv -> onBackPressed());

        initNews();
        initNewsSlider();
        initTodaysSlider(); // Make sure to call this after initNewsSlider
    }

    private void refreshContent() {
        clearData();
        fetchData();
        swipeRefreshLayoutNews.setRefreshing(false);
    }
    private void clearData() {
        news.clear();
    }
    private void fetchData() {
        getRecentNews();
    }

    private void initNews() {
        getSliderNews();
    }
    @SuppressLint("NotifyDataSetChanged")
    void getRecentNews() {
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
                    } else {
                        // Handle the case where data retrieval is not successful
                    }
                });
    }

    void getSliderNews() {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, ConstantsDashboard.GET_NEWS_SLIDER_URL, response -> {
            try {
                JSONArray newssliderArray = new JSONArray(response);
                for (int i = 0; i < newssliderArray.length(); i++) {
                    JSONObject childObj = newssliderArray.getJSONObject(i);
                    binding.newscarousel.addData(
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
            // Handle error if needed
        });
        queue.add(request);
    }
    void getTodayNews() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Calculate the time 24 hours ago
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        long twentyFourHoursAgo = calendar.getTimeInMillis();

        db.collection("MedicalNews")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            try {
                                // Parse the timestamp string to a Date object
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.US);
                                Date newsDate = dateFormat.parse(document.getString("Time"));

                                // Get the timestamp as a long
                                long newsTime = newsDate.getTime();

                                // Check if the news was posted within the last 24 hours and has the type "News"
                                if (newsTime >= twentyFourHoursAgo && "News".equals(document.getString("type"))) {
                                    NewsToday newsItemToday = new NewsToday(
                                            document.getString("Title"),
                                            document.getString("thumbnail"),
                                            document.getString("Description"),
                                            document.getString("Time"),
                                            document.getString("URL")
                                    );
                                    newstoday.add(newsItemToday);
                                }
                            } catch (Exception e) {
                                Log.e("NewsActivity", "Error parsing timestamp", e);
                            }
                        }
                        todayNewsAdapter.notifyDataSetChanged();
                    } else {
                        // Handle the case where the data retrieval is not successful
                    }
                });
    }

    void initNewsSlider() {
        news = new ArrayList<News>();
        newsAdapter = new NewsAdapter(this, news);
        getRecentNews();
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);  // Specify the number of columns
        binding.newsList.setLayoutManager(layoutManager);

        binding.newsList.setAdapter(newsAdapter);
    }

    void initTodaysSlider() {
        newstoday = new ArrayList<>();
        todayNewsAdapter = new TodayNewsAdapter(this, newstoday);
        getTodayNews();

        // Use LinearLayoutManager with horizontal orientation
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        binding.newsListToday.setLayoutManager(layoutManager);
        binding.newsListToday.setAdapter(todayNewsAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

}
