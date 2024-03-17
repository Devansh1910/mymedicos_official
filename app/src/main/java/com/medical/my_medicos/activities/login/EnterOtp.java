package com.medical.my_medicos.activities.login;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.home.HomeActivity;
import com.medical.my_medicos.databinding.ActivityEnterOtpBinding;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class EnterOtp extends AppCompatActivity {

    private ActivityEnterOtpBinding binding;
    private String verificationId;
    private OtpReceiver otp_receiver;
    private FirebaseAuth mAuth;
    private Dialog mdialog;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

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
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.backgroundcolor));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.backgroundcolor));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }

        mAuth = FirebaseAuth.getInstance();
        editTextInput();

        binding.tvMobile.setText(String.format(getIntent().getStringExtra("phone")));

        verificationId = getIntent().getStringExtra("verificationId");

        binding.resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(OtpVerifyActivity.this, "OTP Send Successfully.", Toast.LENGTH_SHORT).show();
                againOtpSend();
            }
        });

        binding.submitOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomProgressDialog("Logging in...");
                mdialog.show();
                binding.submitOtp.setVisibility(View.GONE);
                if (binding.inputotp1.getText().toString().trim().isEmpty() ||
                        binding.inputotp2.getText().toString().trim().isEmpty() ||
                        binding.inputotp3.getText().toString().trim().isEmpty() ||
                        binding.inputotp4.getText().toString().trim().isEmpty() ||
                        binding.inputotp5.getText().toString().trim().isEmpty() ||
                        binding.inputotp6.getText().toString().trim().isEmpty()) {
                    Toast.makeText(EnterOtp.this, "OTP is not Valid!", Toast.LENGTH_SHORT).show();
                } else {
                    if (verificationId != null) {
                        String code = binding.inputotp1.getText().toString().trim() +
                                binding.inputotp2.getText().toString().trim() +
                                binding.inputotp3.getText().toString().trim() +
                                binding.inputotp4.getText().toString().trim() +
                                binding.inputotp5.getText().toString().trim() +
                                binding.inputotp6.getText().toString().trim();

                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
                        FirebaseAuth
                                .getInstance()
                                .signInWithCredential(credential)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            mdialog.dismiss();
                                            binding.submitOtp.setVisibility(View.INVISIBLE);
                                            Toast.makeText(EnterOtp.this, "Welcome...", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(EnterOtp.this, HomeActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        } else {
                                            mdialog.dismiss();
                                            binding.submitOtp.setVisibility(View.VISIBLE);
                                            Toast.makeText(EnterOtp.this, "Welcome...", Toast.LENGTH_SHORT).show();                                           Intent intent = new Intent(EnterOtp.this, HomeActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                    }
                                });
                    }
                }
            }
        });

        autoOtpReceiver();
    }

    private void autoOtpReceiver() {
        otp_receiver = new OtpReceiver();
        this.registerReceiver(otp_receiver, new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION));
        otp_receiver.initListener(new OtpReceiver.OtpReceiverListener() {
            @Override
            public void onOtpSuccess(String otp) {
                int o1 = Character.getNumericValue(otp.charAt(0));
                int o2 = Character.getNumericValue(otp.charAt(1));
                int o3 = Character.getNumericValue(otp.charAt(2));
                int o4 = Character.getNumericValue(otp.charAt(3));
                int o5 = Character.getNumericValue(otp.charAt(4));
                int o6 = Character.getNumericValue(otp.charAt(5));

                binding.inputotp1.setText(String.valueOf(o1));
                binding.inputotp2.setText(String.valueOf(o2));
                binding.inputotp3.setText(String.valueOf(o3));
                binding.inputotp4.setText(String.valueOf(o4));
                binding.inputotp5.setText(String.valueOf(o5));
                binding.inputotp6.setText(String.valueOf(o6));

                binding.submitOtp.performClick();
            }

            @Override
            public void onOtpTimeout() {
                Toast.makeText(EnterOtp.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void againOtpSend() {
        showCustomProgressDialog("Resending...");
        mdialog.show();
        binding.submitOtp.setVisibility(View.INVISIBLE);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                mdialog.dismiss();
                binding.submitOtp.setVisibility(View.VISIBLE);
                Toast.makeText(EnterOtp.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                mdialog.dismiss();
                binding.submitOtp.setVisibility(View.VISIBLE);
                Toast.makeText(EnterOtp.this, "OTP is successfully send.", Toast.LENGTH_SHORT).show();
            }
        };

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91" + getIntent().getStringExtra("phone").trim())
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void editTextInput() {
        binding.inputotp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.inputotp2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.inputotp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.inputotp3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.inputotp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.inputotp4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.inputotp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.inputotp5.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.inputotp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.inputotp6.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void showCustomProgressDialog(String message) {
        if (mdialog != null && mdialog.isShowing()) {
            mdialog.dismiss();
        }

        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.custom_dialogue, null);
        LottieAnimationView lottieAnimationView = view.findViewById(R.id.lottieAnimationView);
        TextView progressText = view.findViewById(R.id.progressText);
        progressText.setText(message);

        // Create a new Dialog instance
        mdialog = new Dialog(this);
        mdialog.setContentView(view);
        mdialog.setCancelable(false);
        mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mdialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (otp_receiver != null) {
            unregisterReceiver(otp_receiver);
        }
    }
}