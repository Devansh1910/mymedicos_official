// EnterOtp.java

package com.medical.my_medicos.activities.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.home.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class EnterOtp extends AppCompatActivity {

    EditText input1, input2, input3, input4, input5, input6;
    Button submit;
    ProgressBar progressBar;
    String getotpbackend;

    public static final String AUTO_FILL_OTP_ACTION = "com.medical.my_medicos.AUTO_FILL_OTP";
    public static final String EXTRA_AUTO_FILL_OTP = "com.medical.my_medicos.AUTO_FILL_OTP_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.blue));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }

        input1 = findViewById(R.id.inputotp1);
        input2 = findViewById(R.id.inputotp2);
        input3 = findViewById(R.id.inputotp3);
        input4 = findViewById(R.id.inputotp4);
        input5 = findViewById(R.id.inputotp5);
        input6 = findViewById(R.id.inputotp6);
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

        numberOtpMove();

        // Check if OTP is received automatically
        autoFillOtpIfAvailable();
    }

    private void verifyOtp() {
        String otp1 = input1.getText().toString().trim();
        String otp2 = input2.getText().toString().trim();
        String otp3 = input3.getText().toString().trim();
        String otp4 = input4.getText().toString().trim();
        String otp5 = input5.getText().toString().trim();
        String otp6 = input6.getText().toString().trim();

        if (TextUtils.isEmpty(otp1) || TextUtils.isEmpty(otp2) || TextUtils.isEmpty(otp3) ||
                TextUtils.isEmpty(otp4) || TextUtils.isEmpty(otp5) || TextUtils.isEmpty(otp6)) {
            Toast.makeText(EnterOtp.this, "Enter all digits", Toast.LENGTH_SHORT).show();
            return;
        }

        String enteredOtp = otp1 + otp2 + otp3 + otp4 + otp5 + otp6;

        if (getotpbackend != null) {
            progressBar.setVisibility(View.VISIBLE);
            submit.setVisibility(View.INVISIBLE);

            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(getotpbackend, enteredOtp);

            FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.GONE);
                    submit.setVisibility(View.VISIBLE);

                    if (task.isSuccessful()) {
                        Intent i = new Intent(EnterOtp.this, HomeActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(EnterOtp.this, "Enter Correct OTP", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(EnterOtp.this, "Verification Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void numberOtpMove() {
        input1.addTextChangedListener(new OtpTextWatcher(input1, input2));
        input2.addTextChangedListener(new OtpTextWatcher(input2, input3));
        input3.addTextChangedListener(new OtpTextWatcher(input3, input4));
        input4.addTextChangedListener(new OtpTextWatcher(input4, input5));
        input5.addTextChangedListener(new OtpTextWatcher(input5, input6));

        // Handle the last input for smooth user experience
        input6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (!s.toString().trim().isEmpty()) {
                    // Do something if needed
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void autoFillOtpIfAvailable() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String autoFilledOtp = "123456";
                fillOtpFields(autoFilledOtp);
            }
        }, 2000);
    }

    private void fillOtpFields(String otp) {
        if (otp.length() == 6) {
            input1.setText(String.valueOf(otp.charAt(0)));
            input2.setText(String.valueOf(otp.charAt(1)));
            input3.setText(String.valueOf(otp.charAt(2)));
            input4.setText(String.valueOf(otp.charAt(3)));
            input5.setText(String.valueOf(otp.charAt(4)));
            input6.setText(String.valueOf(otp.charAt(5)));
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
        public void onTextChanged(CharSequence s, int i, int i1, int i2) {
            if (!s.toString().trim().isEmpty()) {
                nextEditText.requestFocus();
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }
}
