package com.example.my_medicos;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    TextView loginreg;
    Button register;

    private FirebaseAuth mauth;
    private ProgressDialog mdialog;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    TextInputEditText email, fullname, phone, pass, conpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register = findViewById(R.id.register);
        email = findViewById(R.id.emailedit);
        fullname = findViewById(R.id.fullnameedit);

        loginreg = findViewById(R.id.loginreg);

        loginreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        mauth = FirebaseAuth.getInstance();

        mdialog = new ProgressDialog(this);

        register();
    }

    private void register() {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = email.getText().toString().trim();
                String name = fullname.getText().toString().trim();
                String phoneno = phone.getText().toString().trim();
                String password = pass.getText().toString().trim();
                String conpassword = conpass.getText().toString().trim();

                if (TextUtils.isEmpty(mail)) {
                    email.setError("Email Required");
                    return;
                }
                if (TextUtils.isEmpty(name)) {
                    fullname.setError("Full Name Required");
                    return;
                }
                if (TextUtils.isEmpty(phoneno)) {
                    phone.setError("Phone Number Required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    pass.setError("password Required");
                    return;
                }
                if (!isPasswordValid(password)) {
                    pass.setError("Invalid password");
                    Toast.makeText(RegisterActivity.this, "Password is Invalid", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(conpassword)) {
                    conpass.setError("Confirm Password");
                    return;
                }

                mdialog.setMessage("Registering");
                mdialog.show();

                mauth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mauth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Create a new user document in Firestore
                                        createUserInFirestore(mail, name, phoneno);

                                        Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                                        startActivity(i);
                                        finish();

                                        Toast.makeText(getApplicationContext(), "Registration Successful, please verify your Email ID", Toast.LENGTH_SHORT).show();
                                        mdialog.dismiss();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
                                        mdialog.dismiss();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
                            mdialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    private void createUserInFirestore(String mail, String name, String phoneno) {
        // Create a new user document in Firestore without storing the password
        Map<String, Object> user = new HashMap<>();
        user.put("Email ID", mail);
        user.put("Name", name);
        user.put("Phone Number", phoneno);
        user.put("UID", mauth.getCurrentUser().getUid()); // Add UID as a separate field

        // Store the user data in Firestore
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "User document added with UID: " + mauth.getCurrentUser().getUid());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding user document", e);
                    }
                });
    }



    public static boolean isPasswordValid(String password) {
        // Minimum length of 6 characters
        if (password.length() < 6) {
            return false;
        }

        // At least one uppercase letter
        if (!Pattern.compile("[A-Z]").matcher(password).find()) {
            return false;
        }

        // At least one special character (any non-alphanumeric character)
        if (!Pattern.compile("[^a-zA-Z0-9]").matcher(password).find()) {
            return false;
        }

        // At least one digit
        if (!Pattern.compile("[0-9]").matcher(password).find()) {
            return false;
        }

        return true;
    }
}
