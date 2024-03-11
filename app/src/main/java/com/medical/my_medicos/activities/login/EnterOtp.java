package com.medical.my_medicos.activities.login;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

        binding.showmobilenumber.setText(String.format(
                getIntent().getStringExtra("mobile")
        ));

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
                binding.otpprogressbar.setVisibility(View.VISIBLE);
                binding.submitOtp.setVisibility(View.GONE);
                if (binding.inputotp1.getText().toString().trim().isEmpty() ||
                        binding.inputotp2.getText().toString().trim().isEmpty() ||
                        binding.inputotp3.getText().toString().trim().isEmpty() ||
                        binding.inputotp4.getText().toString().trim().isEmpty() ||
                        binding.inputotp5.getText().toString().trim().isEmpty() ||
                        binding.inputotp6.getText().toString().trim().isEmpty()) {
                    Toast.makeText(EnterOtp.this, "OTP is not Valid!", Toast.LENGTH_SHORT).show();
                    binding.otpprogressbar.setVisibility(View.GONE);
                    binding.submitOtp.setVisibility(View.VISIBLE);
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
                                            binding.otpprogressbar.setVisibility(View.VISIBLE);
                                            binding.submitOtp.setVisibility(View.INVISIBLE);
                                            Toast.makeText(EnterOtp.this, "Welcome...", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(EnterOtp.this, HomeActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        } else {
                                            binding.submitOtp.setVisibility(View.VISIBLE);
                                            Toast.makeText(EnterOtp.this, "OTP is not Valid!", Toast.LENGTH_SHORT).show();
                                            binding.otpprogressbar.setVisibility(View.GONE);
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
            }

            @Override
            public void onOtpTimeout() {
                Toast.makeText(EnterOtp.this, "Auto Fetch Otp Disabled!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void againOtpSend() {
        binding.otpprogressbar.setVisibility(View.VISIBLE);
        binding.submitOtp.setVisibility(View.INVISIBLE);

        String phoneNumber = getIntent().getStringExtra("mobile");
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            Toast.makeText(EnterOtp.this, "Phone number is not valid!", Toast.LENGTH_SHORT).show();
            return;
        }

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                binding.otpprogressbar.setVisibility(View.GONE);
                binding.submitOtp.setVisibility(View.VISIBLE);
                Toast.makeText(EnterOtp.this, "There is an issue from the Server. Please try again later.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                binding.otpprogressbar.setVisibility(View.GONE);
                binding.submitOtp.setVisibility(View.VISIBLE);
                Toast.makeText(EnterOtp.this, "OTP is successfully send.", Toast.LENGTH_SHORT).show();
            }
        };

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91" + phoneNumber.trim())
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (otp_receiver != null) {
            unregisterReceiver(otp_receiver);
        }
    }
}