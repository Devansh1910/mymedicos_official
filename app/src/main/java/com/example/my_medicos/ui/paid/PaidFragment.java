package com.example.my_medicos.ui.paid;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.my_medicos.databinding.FragmentPaidBinding;
import com.example.my_medicos.ui.paid.PaidViewModel;

public class PaidFragment extends Fragment {

    private FragmentPaidBinding binding;
    private boolean screenshotAttempted = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PaidViewModel notificationsViewModel =
                new ViewModelProvider(this).get(PaidViewModel.class);

        binding = FragmentPaidBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final WebView paid = binding.paidweb;
        final ProgressBar loadingpublication = binding.publicationloading;
        paid.loadUrl("https://mymedicos.in/index.php/product-category/paid/");
        paid.setWebViewClient(new WebViewClient() {
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

        paid.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keycode, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keycode) {
                        case KeyEvent.KEYCODE_BACK:
                            if (paid.canGoBack()) {
                                paid.goBack();
                            }
                    }
                }
                return false;
            }
        });

        // Prevent screenshot in this fragment
        getActivity().getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
        );

        // Add an OnTouchListener to detect screenshot attempts
        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && screenshotAttempted) {
                    // Show a toast message when the user tries to take a screenshot
                    Toast.makeText(getContext(), "Can't take screenshot", Toast.LENGTH_SHORT).show();
                }
                screenshotAttempted = true; // Mark that a screenshot attempt was made
                return false;
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Clear the FLAG_SECURE flag when leaving this fragment
        getActivity().getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_SECURE
        );
        binding = null;
    }
}
