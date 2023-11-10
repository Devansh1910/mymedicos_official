package com.example.my_medicos.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.my_medicos.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class EnterMobile extends AppCompatActivity {

    EditText mobileno;
    Button getotp;
    CountryCodePicker countryCodePicker;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_mobile);

        mobileno = findViewById(R.id.mobile_no);
        getotp = findViewById(R.id.get_otp);
        countryCodePicker = findViewById(R.id.countryCodePicker_otp);
        progressBar = findViewById(R.id.mobile_progressbar);

        countryCodePicker.setOnCountryChangeListener(() -> {
            // Update the selected country code when the user changes the country
            String selectedCountryCode = countryCodePicker.getSelectedCountryCode();
        });

        getotp.setOnClickListener(view -> {
            String selectedCountryCode = countryCodePicker.getSelectedCountryCode();
            String phoneNumber = mobileno.getText().toString().trim();

            if (TextUtils.isEmpty(phoneNumber)) {
                mobileno.setError("Mobile Number Can't be empty");
                Toast.makeText(EnterMobile.this, "Please enter Mobile Number", Toast.LENGTH_SHORT).show();
            } else if (phoneNumber.length() != 10) {
                mobileno.setError("Enter 10 digits");
                Toast.makeText(EnterMobile.this, "Please Enter 10 Digits", Toast.LENGTH_SHORT).show();
            } else {
                String phone ="+"+selectedCountryCode + phoneNumber;
                progressBar.setVisibility(View.VISIBLE);
                getotp.setVisibility(View.INVISIBLE);

                PhoneAuthProvider.getInstance().verifyPhoneNumber(phone, 60, TimeUnit.SECONDS, EnterMobile.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        progressBar.setVisibility(View.GONE);
                        getotp.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        progressBar.setVisibility(View.GONE);
                        getotp.setVisibility(View.VISIBLE);
                        Toast.makeText(EnterMobile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String backendotp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(backendotp, forceResendingToken);
                        progressBar.setVisibility(View.GONE);
                        getotp.setVisibility(View.VISIBLE);
                        Intent i = new Intent(EnterMobile.this, EnterOtp.class);
                        i.putExtra("mobile", phoneNumber);
                        i.putExtra("Countrycode", selectedCountryCode);
                        i.putExtra("backendotp", backendotp);
                        startActivity(i);
                    }
                });
            }
        });
    }
}
