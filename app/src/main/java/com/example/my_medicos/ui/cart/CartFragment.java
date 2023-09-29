package com.example.my_medicos.ui.cart;

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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.my_medicos.databinding.FragmentCartBinding;

public class CartFragment extends Fragment {

    private FragmentCartBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CartViewModel dashboardViewModel =
                new ViewModelProvider(this).get(CartViewModel.class);

        binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final WebView paid = binding.cartweb;
        final ProgressBar loadingpublication = binding.publicationloading;
        paid.loadUrl("https://mymedicos.in/index.php/cart/");
        paid.setWebViewClient(new WebViewClient(){
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
                if (keyEvent.getAction()==KeyEvent.ACTION_DOWN){
                    switch (keycode){
                        case KeyEvent.KEYCODE_BACK:
                            if (paid.canGoBack()){
                                paid.goBack();
                            }
                    }
                }
                return false;
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}