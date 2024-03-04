package com.medical.my_medicos.activities.login;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.home.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    EditText phone;
    Button login;
    ProgressDialog mdialog;
    Toolbar toolbar;
    Spinner countryCodeSpinner;
    FirebaseAuth mauth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        // Toolbar

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ImageView backArrow = findViewById(R.id.toolbar_back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, NavigationActivity.class);
                startActivity(i);
                finish();
            }
        });

        // Ad Mob Content...

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        countryCodeSpinner = findViewById(R.id.countryCodeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.country_codes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countryCodeSpinner.setAdapter(adapter);

        phone = findViewById(R.id.phonenumberedit);
        login = findViewById(R.id.lgn_btn);
        mdialog = new ProgressDialog(this);
        mauth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedCountryCode = countryCodeSpinner.getSelectedItem().toString();
                String phoneNumber = phone.getText().toString().trim();

                if (TextUtils.isEmpty(phoneNumber)) {
                    phone.setError("Phone Number Required");
                    return;
                }
                if (phoneNumber.length() != 10) {
                    Toast.makeText(MainActivity.this, "Phone Number should be 10 digits", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Ensure the phone number includes the country code
                if (!phoneNumber.startsWith(selectedCountryCode)) {
                    phoneNumber = selectedCountryCode + phoneNumber;
                }

                checkIfUserExists(phoneNumber);
            }
        });
    }

    private void checkIfUserExists(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(MainActivity.this, "Phone Number Required", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("users")
                .whereEqualTo("Phone Number", phoneNumber)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null && !task.getResult().isEmpty()) {
                                mdialog.setMessage("Logging in");
                                mdialog.show();

                                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                        phoneNumber,
                                        60,
                                        TimeUnit.SECONDS,
                                        MainActivity.this,
                                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                            @Override
                                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                                signInWithPhoneAuthCredential(phoneAuthCredential);
                                            }

                                            @Override
                                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                mdialog.dismiss();
                                            }

                                            @Override
                                            public void onCodeSent(@NonNull String backendOtp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                                mdialog.dismiss();
                                                Intent intent = new Intent(MainActivity.this, EnterOtp.class);
                                                intent.putExtra("mobile", phoneNumber);
                                                intent.putExtra("backendotp", backendOtp);
                                                startActivity(intent);
                                            }
                                        }
                                );
                            } else {
                                Toast.makeText(MainActivity.this, "Not Registered", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Error checking user registration", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mauth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (mauth.getCurrentUser().isEmailVerified()) {
                                setLoggedIn(true);

                                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(MainActivity.this, HomeActivity.class);
                                startActivity(i);
                                finish();
                                mdialog.dismiss();
                            } else {
                                Toast.makeText(getApplicationContext(), "Please Verify your email", Toast.LENGTH_SHORT).show();
                                mdialog.dismiss();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                            mdialog.dismiss();
                        }
                    }
                });
    }

    private void setLoggedIn(boolean loggedIn) {
        SharedPreferences preferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("is_logged_in", loggedIn);
        editor.apply();
    }

    // Helper method to check if the user is already logged in
    private boolean isLoggedIn() {
        SharedPreferences preferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        return preferences.getBoolean("is_logged_in", false);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
