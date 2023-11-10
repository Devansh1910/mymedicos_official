package com.example.my_medicos.activities.publications.ui.news;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.my_medicos.databinding.ActivityNewsBinding;

public class NewsActivity extends AppCompatActivity {


    Toolbar toolbar;
    private ActivityNewsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NewsViewModel homeViewModel =
                new ViewModelProvider(this).get(NewsViewModel.class);

        binding = ActivityNewsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final WebView newspreview = binding.newsweb;
        final ProgressBar loadingpublication = binding.publicationloading;
        newspreview.loadUrl("https://mymedicos.in/");
        newspreview.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                loadingpublication.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                loadingpublication.setVisibility(View.GONE);
            }
        });

        newspreview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keycode, KeyEvent keyEvent) {
                if (keyEvent.getAction()==KeyEvent.ACTION_DOWN){
                    switch (keycode){
                        case KeyEvent.KEYCODE_BACK:
                            if (newspreview.canGoBack()){
                                newspreview.goBack();
                            }
                    }
                }
                return false;
            }
        });

        return root;
    }
}