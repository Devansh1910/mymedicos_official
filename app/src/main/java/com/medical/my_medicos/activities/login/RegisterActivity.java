package com.medical.my_medicos.activities.login;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.medical.my_medicos.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.Contract;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    Button register;
    private Spinner prefixSpinner, locationSpinner, interestSpinner,interestSpinner2;
    private ArrayAdapter<CharSequence> locationAdapter;
    private ArrayAdapter<CharSequence> interestAdapter;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    HashMap<String, Object> userMap = new HashMap<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText email, fullName, phoneNumber, password;
    TextView loginreg;
    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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

        ImageView backArrow = findViewById(R.id.backbtn);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        TextView  loginreg = findViewById(R.id.loginreg);
        loginreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });


        register = findViewById(R.id.register);
        prefixSpinner = findViewById(R.id.prefixSpinner);
        locationSpinner = findViewById(R.id.city);
        interestSpinner = findViewById(R.id.interest1);
        interestSpinner2 = findViewById(R.id.interest2);
        email = findViewById(R.id.emailedit);
        fullName = findViewById(R.id.fullnameedit);
        phoneNumber = findViewById(R.id.phonenumberededit);
        password = findViewById(R.id.passwordedit);


        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        userMap.put("MedCoins", 50);
        userMap.put("Streak", 0);
        userMap.put("QuizToday", "default");

        setupSpinners();
        register();
    }

    private void setupSpinners() {
        // Prefix Spinner
        ArrayAdapter<CharSequence> prefixAdapter = ArrayAdapter.createFromResource(this,
                R.array.prefix_options, android.R.layout.simple_spinner_item);
        prefixAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prefixSpinner.setAdapter(prefixAdapter);

        prefixSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedPrefix = adapterView.getItemAtPosition(position).toString();
                userMap.put("prefix", selectedPrefix);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });

        // Location Spinner
        locationAdapter = ArrayAdapter.createFromResource(this,
                R.array.indian_cities, android.R.layout.simple_spinner_item);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationAdapter);

        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedLocation = adapterView.getItemAtPosition(position).toString();
                userMap.put("location", selectedLocation);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });

        // Interest Spinner
        interestAdapter = ArrayAdapter.createFromResource(this,
                R.array.speciality, android.R.layout.simple_spinner_item);
        interestAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        interestSpinner.setAdapter(interestAdapter);
        interestSpinner2.setAdapter(interestAdapter);

        interestSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedInterest = adapterView.getItemAtPosition(position).toString();
                userMap.put("interest", selectedInterest);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });
        interestSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedInterest = adapterView.getItemAtPosition(position).toString();
                userMap.put("interest2", selectedInterest);
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
                String mail = Objects.requireNonNull(email.getText()).toString().trim();


                String name = Objects.requireNonNull(fullName.getText()).toString().trim();
                String enteredPhone = Objects.requireNonNull(phoneNumber.getText()).toString().trim();
                String pass = Objects.requireNonNull(password.getText()).toString().trim();

                String phoneNo;
                if (prefixSpinner.getSelectedItemPosition() == 0){
                    Toast.makeText(RegisterActivity.this, "Please select a Designation", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (locationSpinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(RegisterActivity.this, "Please select a location", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if interest spinner 1 is empty
                if (interestSpinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(RegisterActivity.this, "Please select your first interest", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if interest spinner 2 is empty
                if (interestSpinner2.getSelectedItemPosition() == 0) {
                    Toast.makeText(RegisterActivity.this, "Please select your second interest", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (!enteredPhone.startsWith("+91")) {
                    phoneNo = "+91" + enteredPhone;
                } else {
                    phoneNo = enteredPhone;
                }


                if (TextUtils.isEmpty(mail)) {
                    email.setError("Email Required");
                    return;
                }
                if (TextUtils.isEmpty(name)) {
                    fullName.setError("Full Name Required");
                    return;
                }
                if (TextUtils.isEmpty(phoneNo)) {
                    phoneNumber.setError("Phone Number Required");
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    password.setError("Password Required");
                    return;

                }

                // Check if phone number is valid
                // Check if email is empty or invalid
                String emailValidity = isValidEmail(mail);
                if (emailValidity != null) {
                    email.setError(emailValidity);
                    Toast.makeText(RegisterActivity.this, emailValidity, Toast.LENGTH_SHORT).show();
                    return;
                }

// Check if phone number is empty or invalid
                String phoneValidity = isValidPhoneNumber(phoneNo);
                if (phoneValidity != null) {
                    phoneNumber.setError(phoneValidity);
                    Toast.makeText(RegisterActivity.this, phoneValidity, Toast.LENGTH_SHORT).show();
                    return;
                }

// Check if password is empty or invalid
                String passwordValidity = isPasswordValid(pass);
                if (passwordValidity != null) {
                    password.setError(passwordValidity);
                    Toast.makeText(RegisterActivity.this, passwordValidity, Toast.LENGTH_LONG).show();
                    return;
                }

                progressDialog.setMessage("Registering");
                progressDialog.show();

                mAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Use Number type for Streak in createUserInFirestore
                                        createUserInFirestore(mail, name, phoneNo,
                                                Objects.requireNonNull(userMap.get("location")).toString(),
                                                Objects.requireNonNull(userMap.get("interest")).toString(),
                                                Objects.requireNonNull(userMap.get("QuizToday")).toString(),
                                                Integer.parseInt(Objects.requireNonNull(userMap.get("MedCoins")).toString()),
                                                Integer.parseInt(Objects.requireNonNull(userMap.get("Streak")).toString()));

                                        Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                                        startActivity(i);
                                        finish();

                                        Toast.makeText(getApplicationContext(), "Registration Successful, please verify your Email ID", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                }

                            });
                        } else {
                            Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
            }
        });
    }



    private void createUserInFirestore(String mail, String name, String phoneNo, String location, String interest, String quiztoday,int medcoins,int streak) {
        Map<String, Object> user = new HashMap<>();
        user.put("Email ID", mail);
        user.put("Name", name);
        user.put("Phone Number", phoneNo);
        user.put("Location", location);
        user.put("QuizToday", quiztoday);
        user.put("MedCoins", medcoins);
        user.put("Streak", streak);
        user.put("Interest", interest);
        user.put("Interest2", interest);
        user.put("Prefix", userMap.get("prefix").toString());
        user.put("MCN verified", false);

        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String documentId = documentReference.getId();
                        documentReference.update("DocID", documentId)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Log success if needed
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Log failure if needed
                                    }
                                });
                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                                    startActivity(i);
                                    finish();
                                    Toast.makeText(getApplicationContext(), "Registration Successful, please verify your Email ID", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                } else {
//                                    Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static String isPasswordValid(String password) {
        if (password.length() < 6) {
            return "Password must be at least 6 characters long";
        } else if (!Pattern.compile("[A-Z]").matcher(password).find()) {
            return "Password must contain at least one uppercase letter";
        } else if (!Pattern.compile("[^a-zA-Z0-9]").matcher(password).find()) {
            return "Password must contain at least one special character";
        } else if (!Pattern.compile("[0-9]").matcher(password).find()) {
            return "Password must contain at least one digit";
        } else {
            return null; // Password is valid
        }
    }

    @Nullable
    @Contract("null -> !null")
    private String isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return "Email is required";
        } else if (!email.toLowerCase().endsWith("@gmail.com")) {
            return "Email must end with @gmail.com";
        } else {
            return null; // Email is valid
        }
    }

    @Nullable
    private String isValidPhoneNumber(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            return "Phone number is required";
        } else if (!phoneNumber.matches("\\+?[0-9]+")) {
            return "Phone number must contain only digits";
        } else {
            return null; // Phone number is valid
        }
    }
}