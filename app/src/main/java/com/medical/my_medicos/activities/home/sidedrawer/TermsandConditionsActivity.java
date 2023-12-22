package com.medical.my_medicos.activities.home.sidedrawer;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.medical.my_medicos.R;

public class TermsandConditionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termsand_conditions);

        WebView webView = findViewById(R.id.termsandconditions_page);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl("https://mymedicos.in/tc/");
    }
}
