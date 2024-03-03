package com.medical.my_medicos.activities.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.home.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.medical.my_medicos.databinding.ActivityEnterOtpBinding;

import android.os.Vibrator;

import java.util.concurrent.TimeUnit;

public class EnterOtp extends AppCompatActivity {

    ActivityEnterOtpBinding binding;
    EditText input1, input2, input3, input4, input5, input6;
    Button submit;
    ProgressBar progressBar;
    private Vibrator vibrator;
    private CountDownTimer resendOtpTimer;
    ImageView backtothephone;
    String getotpbackend;
    TextView countdownTimerTextView;

    private Animation shakeAnimation;
    TextView resendOtpTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEnterOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        countdownTimerTextView = binding.countdownTimer;
        resendOtpTextView = binding.resendOtp;

        themefetcher();

        backtothephone = binding.backbtnotp;
        backtothephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EnterOtp.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        // Initialization of the Otp

        progressBar = findViewById(R.id.otpprogressbar);

        TextView shownumber = findViewById(R.id.showmobilenumber);
        shownumber.setText(getIntent().getStringExtra("mobile"));

        submit = findViewById(R.id.submit_otp);
        getotpbackend = getIntent().getStringExtra("backendotp");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyOtp();
            }
        });

        editTextInput();
        resendOtpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resendOtpTimer != null) {
                    resendOtpTimer.cancel();
                }
                String phoneNumber = getIntent().getStringExtra("mobile");
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phoneNumber,
                        60,
                        TimeUnit.SECONDS,
                        EnterOtp.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(EnterOtp.this, "Failed to resend OTP: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String newBackendOtp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                getotpbackend = newBackendOtp;
                                Toast.makeText(EnterOtp.this, "OTP Resent", Toast.LENGTH_SHORT).show();
                                startResendOtpTimer();
                            }
                        }
                );
            }
        });

        startResendOtpTimer();

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake);
    }

    private void verifyOtp() {
        if (binding.inputotp1.getText().toString().trim().isEmpty() ||
                binding.inputotp2.getText().toString().trim().isEmpty() ||
                binding.inputotp3.getText().toString().trim().isEmpty() ||
                binding.inputotp4.getText().toString().trim().isEmpty() ||
                binding.inputotp5.getText().toString().trim().isEmpty() ||
                binding.inputotp6.getText().toString().trim().isEmpty()) {
            Toast.makeText(EnterOtp.this, "Enter all OTP digits", Toast.LENGTH_SHORT).show();
        } else {
            if (getotpbackend != null) {
                String code = binding.inputotp1.getText().toString().trim() +
                        binding.inputotp2.getText().toString().trim() +
                        binding.inputotp3.getText().toString().trim() +
                        binding.inputotp4.getText().toString().trim() +
                        binding.inputotp5.getText().toString().trim() +
                        binding.inputotp6.getText().toString().trim();


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
                            binding.inputotp1.setText("");
                            binding.inputotp2.setText("");
                            binding.inputotp3.setText("");
                            binding.inputotp4.setText("");
                            binding.inputotp5.setText("");
                            binding.inputotp6.setText("");
                            binding.inputotp1.requestFocus();
                            clearAndShakeFields();
                        }
                    }
                });
            } else {
                Toast.makeText(EnterOtp.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(EnterOtp.this, GetstartedActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        }
    }
    private void editTextInput() {
        binding.inputotp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.inputotp2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.inputotp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.inputotp3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.inputotp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.inputotp4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.inputotp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.inputotp5.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.inputotp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.inputotp6.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private void clearAndShakeFields() {
        // Clear OTP fields
        binding.inputotp1.setText("");
        binding.inputotp2.setText("");
        binding.inputotp3.setText("");
        binding.inputotp4.setText("");
        binding.inputotp5.setText("");
        binding.inputotp6.setText("");

        // Apply animation
        binding.inputotp1.startAnimation(shakeAnimation);
        binding.inputotp2.startAnimation(shakeAnimation);
        binding.inputotp3.startAnimation(shakeAnimation);
        binding.inputotp4.startAnimation(shakeAnimation);
        binding.inputotp5.startAnimation(shakeAnimation);
        binding.inputotp6.startAnimation(shakeAnimation);

        // Change border color to red temporarily
        changeBorderColor(binding.inputotp1, Color.RED);
        changeBorderColor(binding.inputotp2, Color.RED);
        changeBorderColor(binding.inputotp3, Color.RED);
        changeBorderColor(binding.inputotp4, Color.RED);
        changeBorderColor(binding.inputotp5, Color.RED);
        changeBorderColor(binding.inputotp6, Color.RED);

        // Vibrate the device
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(200); // Vibrate for 200 milliseconds
        }

        // Restore border color after 2 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                changeBorderColor(binding.inputotp1, Color.RED);
                changeBorderColor(binding.inputotp2, Color.RED);
                changeBorderColor(binding.inputotp3, Color.RED);
                changeBorderColor(binding.inputotp4, Color.RED);
                changeBorderColor(binding.inputotp5, Color.RED);
                changeBorderColor(binding.inputotp6, Color.RED);
            }
        }, 2000);
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
        public void onTextChanged(CharSequence s, int i, int i1, int i2) {
            if (!s.toString().trim().isEmpty()) {
                nextEditText.requestFocus();
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    private void themefetcher() {
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
    }

    private void startResendOtpTimer() {
        resendOtpTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdownTimerTextView.setText((millisUntilFinished / 1000) + "s");
                countdownTimerTextView.setTextSize(14);
                resendOtpTextView.setEnabled(false);
                resendOtpTextView.setTextColor(ContextCompat.getColor(EnterOtp.this, R.color.grey));
            }

            @Override
            public void onFinish() {
                resendOtpTextView.setEnabled(true);
                resendOtpTextView.setTextColor(ContextCompat.getColor(EnterOtp.this, R.color.blue));
                countdownTimerTextView.setVisibility(View.GONE);
            }
        }.start();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (resendOtpTimer != null) {
            resendOtpTimer.cancel();
        }
    }

    private void changeBorderColor(EditText editText, int color) {
        GradientDrawable drawable = (GradientDrawable) editText.getBackground();
        drawable.setStroke(2, color);
    }

}