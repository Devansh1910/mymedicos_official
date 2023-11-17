package com.example.my_medicos.activities.login;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.my_medicos.R;
import com.example.my_medicos.activities.cme.CmeCreatedActivity;
import com.example.my_medicos.activities.cme.CmeReservedActivity;
import com.example.my_medicos.activities.home.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    TextInputEditText phone;
    Button login;
    ProgressDialog mdialog;

    Toolbar toolbar;

    FirebaseAuth mauth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phone = findViewById(R.id.phonenumberedit);
        login = findViewById(R.id.lgn_btn);
        mdialog = new ProgressDialog(this);
        mauth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.logintoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = phone.getText().toString().trim();

                if (TextUtils.isEmpty(phoneNumber)) {
                    phone.setError("Phone Number Required");
                    return;
                }

                // Add the country code if not present
                if (!phoneNumber.startsWith("+91")) {
                    phoneNumber = "+91" + phoneNumber; // Replace +91 with your country code
                }

                // Check if the user is already registered
                checkIfUserExists(phoneNumber);
            }
        });
    }

    private void checkIfUserExists(String phoneNumber) {
        db.collection("users")
                .whereEqualTo("Phone Number", phoneNumber)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null && !task.getResult().isEmpty()) {
                                // User exists, proceed with verification
                                mdialog.setMessage("Logging in");
                                mdialog.show();

                                String finalPhoneNumber = phoneNumber;
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
                                                intent.putExtra("mobile", finalPhoneNumber);
                                                intent.putExtra("Countrycode", "+91"); // Replace with your country code
                                                intent.putExtra("backendotp", backendOtp);
                                                startActivity(intent);
                                            }
                                        }
                                );
                            } else {
                                // User not registered, show toast and redirect to registration activity
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
                                // Set the flag to indicate that the user is logged in via phone
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
                // Handle the back arrow click, finish the current activity
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Other methods and overrides
}
