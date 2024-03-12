package com.medical.my_medicos.activities.news;

import static android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;

import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
//import com.medical.my_medicos.activities.news.tags.Tags;
//import com.medical.my_medicos.activities.news.tags.TagsAdapter;
//import com.medical.my_medicos.activities.news.tags.TagsInsiderActivity;
import com.medical.my_medicos.activities.pg.activites.PgprepActivity;
import com.medical.my_medicos.activities.publications.activity.PublicationActivity;
import com.medical.my_medicos.activities.publications.activity.SearchPublicationActivity;
import com.medical.my_medicos.activities.slideshow.SlideshareCategoryAdapter;
import com.medical.my_medicos.activities.slideshow.insider.SpecialitySlideshowInsiderActivity;
import com.medical.my_medicos.activities.slideshow.model.SlideshareCategory;
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

//    TagsAdapter tagsAdapter;
//    ArrayList<Tags> tags;

    //....News Activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS );
        binding = ActivityNewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController controller = getWindow().getInsetsController();
            if (controller != null) {
                controller.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
                controller.hide(WindowInsets.Type.statusBars());
            }
        } else {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
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

//        swipeRefreshLayoutNews = findViewById(R.id.swipeRefreshLayoutNews);
//        swipeRefreshLayoutNews.setOnRefreshListener(this::refreshContent);
        binding.newstoolbar.setNavigationOnClickListener(vv -> onBackPressed());

        initNews();
//        initTags();
        initNewsSlider();
        initTodaysSlider(); // Make sure to call this after initNewsSlider
    }

    private void refreshContent() {
        clearData();
        fetchData();
//        swipeRefreshLayoutNews.setRefreshing(false);
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

    //......
//    void initTags() {
//        tags = new ArrayList<>();
//        tagsAdapter = new TagsAdapter(this, tags);
//
//        getTags();
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        binding.newstags.setLayoutManager(layoutManager);
//        binding.newstags.setAdapter(tagsAdapter);
//    }
//    void getTags() {
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        StringRequest request = new StringRequest(Request.Method.GET, ConstantsDashboard.GET_SPECIALITY, new Response.Listener<String>() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onResponse(String response) {
//                try {
//                    Log.e("err", response);
//                    JSONObject mainObj = new JSONObject(response);
//                    if (mainObj.getString("status").equals("success")) {
//                        JSONArray tagsArray = mainObj.getJSONArray("data");
//                        int tagsCount = Math.min(tagsArray.length(), 40);
//                        for (int i = 0; i < tagsCount; i++) {
//                            JSONObject object = tagsArray.getJSONObject(i);
//
//                            int priority = object.getInt("priority");
//                            if (priority >= 1 && priority <= 3) {
//                                Tags tagscategories = new Tags(
//                                        object.getString("id"),
//                                        priority // Set the priority
//                                );
//                                tags.add(tagscategories);
//                                Log.e("Priority", String.valueOf(priority));
//                            }
//                        }
//
//
//                        tagsAdapter.notifyDataSetChanged();
//                        binding.newstags.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//                            @Override
//                            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
//                                View child = rv.findChildViewUnder(e.getX(), e.getY());
//                                int position = rv.getChildAdapterPosition(child);
//
//                                if (position != RecyclerView.NO_POSITION) {
//                                    if (position == tags.size() - 1 && tags.get(position).getPriority() == -1) {
//                                        Intent intent = new Intent(NewsActivity.this, TagsInsiderActivity.class);
//                                        startActivity(intent);
//                                    } else {
//
//                                    }
//                                }
//                                return false;
//                            }
//                            @Override
//                            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {}
//
//                            @Override
//                            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
//                        });
//                        tagsAdapter.notifyDataSetChanged();
//                    } else {
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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

}
