package com.example.my_medicos.activities.news;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.my_medicos.R;
import com.example.my_medicos.activities.utils.ConstantsDashboard;
import com.example.my_medicos.databinding.ActivityNewsBinding;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class  NewsActivity extends AppCompatActivity {
    ActivityNewsBinding binding;
    NewsAdapter newsAdapter;
    ArrayList<News> news;

    private SwipeRefreshLayout swipeRefreshLayoutNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        swipeRefreshLayoutNews = findViewById(R.id.swipeRefreshLayoutNews);
        swipeRefreshLayoutNews.setOnRefreshListener(this::refreshContent);

        initNews();
        initNewsSlider();
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
    void getRecentNews() {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = ConstantsDashboard.GET_NEWS;
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if(object.getString("status").equals("success")){
                    JSONArray array = object.getJSONArray("news");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject childObj = array.getJSONObject(i);
                        News newsItem = new News(
                                childObj.getString("Title"),
                                childObj.getString("thumbnail"),
                                childObj.getString("Description"),
                                childObj.getString("Time"),
                                childObj.getString("URL")
                        );
                        news.add(newsItem);
                    }
                    newsAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> { });

        queue.add(request);
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

    void initNewsSlider() {
        news = new ArrayList<>();
        newsAdapter = new NewsAdapter(this, news);
        getRecentNews();
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);  // Specify the number of columns
        binding.newsList.setLayoutManager(layoutManager);

        binding.newsList.setAdapter(newsAdapter);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

}