package com.medical.my_medicos.activities.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.home.HomeActivity;
import com.medical.my_medicos.databinding.ActivityEnterOtpBinding;

import java.util.concurrent.TimeUnit;

public class EnterOtp extends AppCompatActivity {

    ActivityEnterOtpBinding binding;
    EditText input1, input2, input3, input4, input5, input6;
    Button submit;
    ProgressBar progressBar;
    String getotpbackend;
    TextView showMobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEnterOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }

        // Initialize UI elements
        input1 = binding.inputotp1;
        input2 = binding.inputotp2;
        input3 = binding.inputotp3;
        input4 = binding.inputotp4;
        input5 = binding.inputotp5;
        input6 = binding.inputotp6;
        submit = binding.submitOtp;
        progressBar = binding.otpprogressbar;
        showMobileNumber = binding.showmobilenumber; // Initialize the TextView

        // Get the phone number from the intent
        String phoneNumber = getIntent().getStringExtra("mobile");
        showMobileNumber.setText(phoneNumber); // Display the phone number

        // Get the backend OTP from the intent
        getotpbackend = getIntent().getStringExtra("backendotp");

        Log.d("EnterOtp", "Backend OTP: " + getotpbackend);

        // Set up the submit button click listener
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyOtp();
            }
        });

        // Set up text watchers for OTP input fields
        setUpTextWatchers();
    }

    private void setUpTextWatchers() {
        input1.addTextChangedListener(new OtpTextWatcher(input1, input2));
        input2.addTextChangedListener(new OtpTextWatcher(input2, input3));
        input3.addTextChangedListener(new OtpTextWatcher(input3, input4));
        input4.addTextChangedListener(new OtpTextWatcher(input4, input5));
        input5.addTextChangedListener(new OtpTextWatcher(input5, input6));
    }

    private void verifyOtp() {
        if (input1.getText().toString().isEmpty() ||
                input2.getText().toString().isEmpty() ||
                input3.getText().toString().isEmpty() ||
                input4.getText().toString().isEmpty() ||
                input5.getText().toString().isEmpty() ||
                input6.getText().toString().isEmpty()) {
            Toast.makeText(EnterOtp.this, "Enter all OTP digits", Toast.LENGTH_SHORT).show();
        } else {
            if (getotpbackend != null) {
                String code = input1.getText().toString() +
                        input2.getText().toString() +
                        input3.getText().toString() +
                        input4.getText().toString() +
                        input5.getText().toString() +
                        input6.getText().toString();

                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(getotpbackend, code);
                FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(EnterOtp.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            Toast.makeText(EnterOtp.this, "Welcome", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        } else {
                            Toast.makeText(EnterOtp.this, "Enter Correct OTP", Toast.LENGTH_SHORT).show();
                            // Clear OTP fields
                            input1.setText("");
                            input2.setText("");
                            input3.setText("");
                            input4.setText("");
                            input5.setText("");
                            input6.setText("");
                            input1.requestFocus();
                        }
                    }
                });
            } else {
                Toast.makeText(EnterOtp.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(EnterOtp.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        }
    }

    private class OtpTextWatcher implements TextWatcher {
        private EditText currentEditText;
        private EditText nextEditText;

        OtpTextWatcher(EditText currentEditText, EditText nextEditText) {
            this.currentEditText = currentEditText;
            this.nextEditText = nextEditText;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (!charSequence.toString().trim().isEmpty()) {
                nextEditText.requestFocus();
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }
}
