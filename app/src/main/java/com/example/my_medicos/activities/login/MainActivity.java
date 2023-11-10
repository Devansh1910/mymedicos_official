package com.example.my_medicos.activities.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.my_medicos.R;
import com.example.my_medicos.activities.home.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    TextView createacc, forgotpassword;

    Button login;

    TextInputEditText email, password;

    private FirebaseAuth mauth;

    private ProgressDialog mdialog;

    ImageView mobile_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mobile_login=findViewById(R.id.mobile_login);

        mobile_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this, EnterMobile.class);
                startActivity(i);
                finish();
            }
        });
        login=findViewById(R.id.lgn_btn);
        email=findViewById(R.id.emailedit);
        password=findViewById(R.id.passedit);
        createacc=findViewById(R.id.createacc);

        // Check if the user is already logged in
        if (isLoggedIn()) {
            // User is already logged in, proceed to HomeActivity
            Intent i = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(i);
            finish(); // Close the current activity
            return; // Exit the onCreate method to prevent further execution
        }

        login = findViewById(R.id.lgn_btn);
        email = findViewById(R.id.emailedit);
        password = findViewById(R.id.passedit);
        createacc = findViewById(R.id.createacc);


        createacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });
        mauth = FirebaseAuth.getInstance();

        mdialog = new ProgressDialog(this);

        login();
    }

    private void login() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mail = email.getText().toString().trim();
                String pass = password.getText().toString().trim();

                if (TextUtils.isEmpty(mail)) {
                    email.setError("Email Required");
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    password.setError("Password Required");
                    return;
                }
                mdialog.setMessage("Logging in");
                mdialog.show();

                mauth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (mauth.getCurrentUser().isEmailVerified()) {
                                // Set the flag to indicate that the user is logged in
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
        });

        forgotpassword = findViewById(R.id.forgot_pass);
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ResetPasswordActivity.class);
                startActivity(i);
            }
        });
    }

    // Helper method to set the login status
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
}
