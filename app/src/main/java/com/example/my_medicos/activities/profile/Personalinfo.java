package com.example.my_medicos.activities.profile;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.my_medicos.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Personalinfo extends AppCompatActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    String current = user.getPhoneNumber();

    Button verificationbtnformedical;
    FirebaseFirestore mcnumber = FirebaseFirestore.getInstance();
    EditText medicalcouncilnumber;

    public FirebaseDatabase db = FirebaseDatabase.getInstance();
    public DatabaseReference medicalref = db.getReference().child("Medical Council Number Request");
    private ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalinfo);

        progressDialog = new ProgressDialog(this);
        medicalcouncilnumber = findViewById(R.id.mcnumber);
        verificationbtnformedical = findViewById(R.id.verificationButton);

        checkPhoneNumberExists();

        if (isVerificationPending()) {
            showVerificationPopup();
        }

        verificationbtnformedical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    verificationbtnformedical();
                }
            }
        });
    }

    private boolean isVerificationPending() {
        return true;
    }

    private void showVerificationPopup() {
        final Dialog popupDialog = new Dialog(this);
        popupDialog.setContentView(R.layout.popupforverification);
        popupDialog.setTitle("Verification Pending");
        popupDialog.show();
    }

    private void checkPhoneNumberExists() {
        medicalref.orderByChild("User").equalTo(current).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    verificationbtnformedical.setVisibility(View.INVISIBLE);
                    medicalcouncilnumber.setEnabled(false);
                    verificationbtnformedical.setEnabled(false);
                    medicalcouncilnumber.setHint("Verification Pending");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error checking phone number: " + databaseError.getMessage());
            }
        });
    }

    public void verificationbtnformedical() {
        String organiser = medicalcouncilnumber.getText().toString().trim();

        if (TextUtils.isEmpty(organiser)) {
            medicalcouncilnumber.setError("Medical Council Number Required");
            return;
        }

        HashMap<String, Object> usermap = new HashMap<>();
        usermap.put("Medical Council Number", organiser);
        usermap.put("User", current);

        progressDialog.setMessage("Generating Request...");
        progressDialog.show();

        medicalref.push().setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();

                if (task.isSuccessful()) {
                    String generatedDocId = medicalref.push().getKey();
                    usermap.put("documentId", generatedDocId);
                    mcnumber.collection("Medical Council Number Request").document(generatedDocId).set(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Personalinfo.this, "Verification Requested", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Personalinfo.this, "Verification Request Failed", Toast.LENGTH_SHORT).show();
                                Log.e("Firebase", "Error writing to Firebase: " + task.getException());
                            }
                        }
                    });
                } else {
                    Toast.makeText(Personalinfo.this, "Failed to Proceed, Try again later", Toast.LENGTH_SHORT).show();
                    Log.e("Firebase", "Error writing to Firebase: " + task.getException());
                }
            }
        });
    }
}