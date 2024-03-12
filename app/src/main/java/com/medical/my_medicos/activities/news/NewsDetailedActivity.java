package com.medical.my_medicos.activities.news;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.medical.my_medicos.R;
import com.medical.my_medicos.databinding.ActivityNewsDetailedBinding;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class NewsDetailedActivity extends AppCompatActivity {

    ActivityNewsDetailedBinding binding;
    News currentNews;
    String documentid;
    String newstitle,newstype;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    String current=user.getPhoneNumber();
    TextView sharenews;
    String newsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsDetailedBinding.inflate(getLayoutInflater());
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

        String name = getIntent().getStringExtra("Title");
        String image = getIntent().getStringExtra("thumbnail");
        String status = getIntent().getStringExtra("Description");
        String time = getIntent().getStringExtra("Time");
        String url = getIntent().getStringExtra("URL");

        getNewsDetails(name);

        Glide.with(this)
                .load(image)
                .into(binding.newsthumbnail);

        binding.newsDescription.setText(status);
        binding.newsTitle.setText(name);
        binding.newsTime.setText(time);

        binding.readmoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the URL in a web browser
                openUrlInBrowser(url);
            }
        });

        // Enable the back button in the toolbar
        setSupportActionBar(binding.newsstoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        sharenews = findViewById(R.id.sharenews);
//
//        sharenews.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                createlink(current, documentid, newstitle,newstype);
//            }
//        });

        handleDeepLink();
        getNewsDetails(name);
    }

    void getNewsDetails(String name) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("MedicalNews")
                .document(name)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String description = documentSnapshot.getString("Description");
                        String time = documentSnapshot.getString("Time");
                        String url = documentSnapshot.getString("URL");
                        String thumbnail = documentSnapshot.getString("thumbnail");
                        String type = documentSnapshot.getString("type");

                        documentid = documentSnapshot.getId();

                        newstitle = documentSnapshot.getString("Title");

                        newstype = type;

                        newsId = documentSnapshot.getId();

                        binding.newsDescription.setText(Html.fromHtml(description));
                        binding.newsTime.setText(Html.fromHtml(time));

                        currentNews = new News(
                                documentSnapshot.getString("Title"),
                                thumbnail,
                                description,
                                time,
                                url,
                                type
                        );
                    } else {
                        // Handle the case where the document does not exist
                        Log.e(TAG, "No such document with name: " + name);
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                    Log.e(TAG, "Error fetching news details for name: " + name, e);
                });
    }


    private void openUrlInBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    public void createlink(String custid, String newsId,String newstitle, String newsdescription){
        Log.e("main","create link");

        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.mymedicos.in/newsdetails?custid=" + custid + "&newsId=" + newsId))
                .setDomainUriPrefix("https://app.mymedicos.in")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                .setIosParameters(new DynamicLink.IosParameters.Builder("com.example.ios").build())
                .buildDynamicLink();

        Uri dynamicLinkUri = dynamicLink.getUri();
        Log.e("main"," Long refer "+ dynamicLink.getUri());

        createreferlink(custid, newsId);
    }
    public void createreferlink(String custid, String newsId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("MedicalNews").document(newsId);

        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String newsTitle = documentSnapshot.getString("Title");
                String newsThumbnail = documentSnapshot.getString("thumbnail");

                String encodedNewsTitle = encode(newsTitle);

                String sharelinktext =
                        newsTitle + "\\n\\n\"" +
                                "https://app.mymedicos.in/?" +
                                "link=http://www.mymedicos.in/newsdetails?newsId=" + newsId +
                                "&st=" + encodedNewsTitle +
                                "&apn=" + getPackageName() +
                                "&si=" + newsThumbnail ;

                Log.e("Job Detailed Activity", "Sharelink - " + sharelinktext);

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, sharelinktext.toString());
                intent.setType("text/plain");
                startActivity(intent);
            } else {
                Log.e(TAG, "No such document with documentId: " + newsId);
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Error fetching job details for documentId: " + newsId, e);
        });
    }

    private String encode(String s) {
        try {
            return URLEncoder.encode(s, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return URLEncoder.encode(s);
        }
    }

    private void handleDeepLink() {
        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent())
                .addOnSuccessListener(this, pendingDynamicLinkData -> {
                    if (pendingDynamicLinkData != null) {
                        Uri deepLink = pendingDynamicLinkData.getLink();
                        if (deepLink != null) {
                            String newsId = deepLink.getQueryParameter("newsId");
                            Intent intent = getIntent();
                            intent.putExtra("newsId", newsId);
                            setIntent(intent);
                        }
                    }
                })
                .addOnFailureListener(this, e -> Log.w("DeepLink", "getDynamicLink:onFailure", e));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}