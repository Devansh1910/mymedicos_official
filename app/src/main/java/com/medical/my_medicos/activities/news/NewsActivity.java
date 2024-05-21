package com.medical.my_medicos.activities.news;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.load.resource.bitmap.BitmapDrawableDecoder;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.home.HomeActivity;
//import com.medical.my_medicos.activities.news.tags.Tags;
//import com.medical.my_medicos.activities.news.tags.TagsAdapter;
//import com.medical.my_medicos.activities.news.tags.TagsInsiderActivity;
import com.medical.my_medicos.activities.news.fragments.AllNewsFragment;
import com.medical.my_medicos.activities.news.fragments.DrugnDiseasesNewsFragment;
import com.medical.my_medicos.activities.news.fragments.EducationNewsFragment;
import com.medical.my_medicos.activities.news.fragments.JobsUpdatesNewsFragment;
import com.medical.my_medicos.activities.news.fragments.MedicalNewsFragment;
import com.medical.my_medicos.activities.utils.ConstantsDashboard;
import com.medical.my_medicos.databinding.ActivityNewsBinding;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class  NewsActivity extends AppCompatActivity {
    TodayNewsAdapter todayNewsAdapter;
    ArrayList<NewsToday>  newstoday;
    RelativeLayout importantnoticego;
    private ImageView backtothehomefrompg;
    ActivityResultLauncher<Intent> importantNoticesActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

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

        MaterialSearchBar searchBarNews = findViewById(R.id.searchBarNews);
        searchBarNews.addTextChangeListener(new TextWatcher() {
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

        searchBarNews.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
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

        importantNoticesActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {

                    }
                }
        );

        importantnoticego = findViewById(R.id.importantnoticego);
        importantnoticego.setOnClickListener(view -> {
            // Use the launcher to start ImportantNoticesNewsActivity
            Intent i = new Intent(NewsActivity.this, ImportantNoticesNewsActivity.class);
            importantNoticesActivityResultLauncher.launch(i);
        });
        TabLayout tabLayout = findViewById(R.id.tablayoutnews);
        ViewPager2 viewPager = findViewById(R.id.view_pager_news);

        FragmentStateAdapter adapter = new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position) {
                    case 0: // All
                        return new AllNewsFragment();
                    case 1: // Medical
                        return new MedicalNewsFragment();
                    case 2: // Education
                        return new EducationNewsFragment();
                    case 3: // Drugs & Diseases
                        return new DrugnDiseasesNewsFragment();
                    case 4: // Job Updates
                        return new JobsUpdatesNewsFragment();
                    default:
                        return new AllNewsFragment();
                }
            }

            @Override
            public int getItemCount() {
                return 5; // Number of tabs
            }
        };

        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Discovery");
                    break;
                case 1:
                    tab.setText("Law in Medicine");
                    break;
                case 2:
                    tab.setText("Education");
                    break;
                case 3:
                    tab.setText("Drugs & Devices");
                    break;
                case 4:
                    tab.setText("Job Alerts");
                    break;
            }
        }).attach();

        Toolbar newstoolbar = findViewById(R.id.newstoolbar);
        newstoolbar.setNavigationOnClickListener(vv -> onBackPressed());
        initTodaysSlider();
        setupRealtimeUpdatesListener();
    }
    @SuppressLint("NotifyDataSetChanged")
    void getTodayNews() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        long twentyFourHoursAgo = calendar.getTimeInMillis();

        db.collection("MedicalNews")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            try {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.US);
                                Date newsDate = dateFormat.parse(document.getString("Time"));
                                long newsTime = newsDate.getTime();
                                Log.d("Date of activity", String.valueOf(newsDate));
                                if (newsTime >= twentyFourHoursAgo && "News".equals(document.getString("type"))) {
                                    NewsToday newsItemToday = new NewsToday(
                                            document.getId(),
                                            document.getString("Title"),
                                            document.getString("thumbnail"),
                                            document.getString("Description"),
                                            document.getString("Time"),
                                            document.getString("URL"),
                                            document.getString("subject")
                                    );
                                    newstoday.add(newsItemToday);
                                }
                            } catch (Exception e) {
                                Log.e("NewsActivity", "Error parsing timestamp", e);
                            }
                        }
                        todayNewsAdapter.notifyDataSetChanged();
                    } else {

                    }
                });
    }

    void initTodaysSlider() {
        newstoday = new ArrayList<>();
        todayNewsAdapter = new TodayNewsAdapter(this, newstoday);
        getTodayNews();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        RecyclerView newsListToday = findViewById(R.id.newsListToday); // Use findViewById here
        newsListToday.setLayoutManager(layoutManager);
        newsListToday.setAdapter(todayNewsAdapter);
    }

    private void setupRealtimeUpdatesListener() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("MedicalNews")
                .whereEqualTo("type", "Notice")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            // Handle error
                            return;
                        }
                        try {
                            processUpdates(snapshots.getDocuments());
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                    }
                });
    }

    private void processUpdates(List<DocumentSnapshot> documents) throws JSONException {
        SharedPreferences sharedPreferences = getSharedPreferences("My_Medicos_Prefs", MODE_PRIVATE);
        String seenUpdatesJson = sharedPreferences.getString("SeenUpdates", "[]");
        JSONArray seenUpdatesArray = new JSONArray(seenUpdatesJson);
        HashSet<String> seenUpdates = new HashSet<>();
        for (int i = 0; i < seenUpdatesArray.length(); i++) {
            seenUpdates.add(seenUpdatesArray.getString(i));
        }

        int unseenCount = 0;
        for (DocumentSnapshot document : documents) {
            if (!seenUpdates.contains(document.getId())) {
                unseenCount++;
            }
        }

        updateImportantNoticesCounter(unseenCount);
    }

    private void updateImportantNoticesCounter(int unseenCount) {
        TextView counter = findViewById(R.id.counterforthenumberofimportantnotice);
        if (unseenCount > 0) {
            counter.setText(String.valueOf(unseenCount));
            counter.setVisibility(View.VISIBLE);
        } else {
            counter.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

}
