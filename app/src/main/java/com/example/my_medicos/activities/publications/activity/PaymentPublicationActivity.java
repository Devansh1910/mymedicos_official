package com.example.my_medicos.activities.publications.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.my_medicos.activities.publications.utils.Constants;
import com.example.my_medicos.databinding.ActivityPaymentPublicationBinding;

public class PaymentPublicationActivity extends AppCompatActivity {

    ActivityPaymentPublicationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentPublicationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String orderCode = getIntent().getStringExtra("orderCode");

        binding.webview.setMixedContentAllowed(true);
        binding.webview.loadUrl(Constants.PAYMENT_URL + orderCode);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}