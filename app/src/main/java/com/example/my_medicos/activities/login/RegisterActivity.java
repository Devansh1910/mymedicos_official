package com.example.my_medicos.activities.login;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.my_medicos.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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

    private Spinner locationspinner, interestspinner;
    private ArrayAdapter<CharSequence> interestAdapter, locationAdapter;
    private FirebaseAuth mauth;
    private ProgressDialog mdialog;

    HashMap<String, Object> usermap = new HashMap<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    TextInputEditText email, fullname, phone, pass;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register = findViewById(R.id.register);
        locationspinner = findViewById(R.id.city);
        interestspinner = findViewById(R.id.interest);
        email = findViewById(R.id.emailedit);
        fullname = findViewById(R.id.fullnameedit);
        phone = findViewById(R.id.phonenumberededit);
        pass = findViewById(R.id.passwordedit);

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

        interestAdapter = ArrayAdapter.createFromResource(this,
                R.array.speciality, android.R.layout.simple_spinner_item);
        interestAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        interestspinner.setAdapter(interestAdapter);

        // Initially, hide the subspeciality spinner
        interestspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                // Check if the selected speciality has subspecialities
                int interestIndex = interestspinner.getSelectedItemPosition();
                String selectInterest =interestspinner.getSelectedItem().toString();
                usermap.put("interest",selectInterest);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });


        locationAdapter = ArrayAdapter.createFromResource(this,
                R.array.indian_cities, android.R.layout.simple_spinner_item);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationspinner.setAdapter(locationAdapter);

        locationspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                int locationIndex = locationspinner.getSelectedItemPosition();
                String selectedLocation = locationspinner.getSelectedItem().toString();
                usermap.put("location", selectedLocation);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });
    }

    private void register() {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = email.getText().toString().trim();
                // Check if the email contains "@gmail.com"
                if (!isValidEmail(mail)) {
                    email.setError("Invalid Email Address");
                    return;
                }

                String name = fullname.getText().toString().trim();
                String enteredPhone = phone.getText().toString().trim();
                String password = pass.getText().toString().trim();

                // Add +91 as a default prefix to the phone number
                String phoneno;
                if (!enteredPhone.startsWith("+91")) {
                    phoneno = "+91" + enteredPhone;
                } else {
                    phoneno = enteredPhone;
                }

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
                    pass.setError("Password Required");
                    return;
                }
                if (!isPasswordValid(password)) {
                    pass.setError("Invalid password");
                    Toast.makeText(RegisterActivity.this, "Password is Invalid", Toast.LENGTH_SHORT).show();
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
                                        createUserInFirestore(mail, name, phoneno, usermap.get("location").toString(), usermap.get("interest").toString());

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

    private void createUserInFirestore(String mail, String name, String phoneno, String location,String interest) {
        Map<String, Object> user = new HashMap<>();
        user.put("Email ID", mail);
        user.put("Name", name);
        user.put("Phone Number", phoneno);
        user.put("Location", location);
        user.put("Interest", interest);
        user.put("UID", mauth.getCurrentUser().getUid());

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
        if (password.length() < 6) {
            return false;
        }

        if (!Pattern.compile("[A-Z]").matcher(password).find()) {
            return false;
        }

        if (!Pattern.compile("[^a-zA-Z0-9]").matcher(password).find()) {
            return false;
        }

        if (!Pattern.compile("[0-9]").matcher(password).find()) {
            return false;
        }

        return true;
    }

    // Method to check if the email contains "@gmail.com"
    private boolean isValidEmail(String email) {
        return email.toLowerCase().endsWith("@gmail.com");
    }

}
