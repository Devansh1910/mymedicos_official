package com.medical.my_medicos.activities.home.sidedrawer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.medical.my_medicos.R;

public class PrivacyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        WebView webView = findViewById(R.id.privacy_page);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl("https://mymedicos.in/tc/");
    }
}