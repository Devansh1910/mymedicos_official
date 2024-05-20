package com.medical.my_medicos.activities.pg.activites;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.pg.activites.Neetexaminsider;

public class NeetExamPayment extends AppCompatActivity {

    private DatabaseReference database;
    private String currentUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neet_exam_payment);

        database = FirebaseDatabase.getInstance().getReference();
        currentUid = FirebaseAuth.getInstance().getUid();

        Intent intent = getIntent();
        String title1 = intent.getStringExtra("Title1");
        String title = intent.getStringExtra("Title");
        String Due = intent.getStringExtra("Due");

        TextView quizNameTextView = findViewById(R.id.quizNameTextView);
        TextView dueDateTextView = findViewById(R.id.DueDate);
        dueDateTextView.setText(Due);
        quizNameTextView.setText(title);

        CheckBox agreeCheckbox = findViewById(R.id.agreeCheckbox);
        LinearLayout startExamLayout = findViewById(R.id.startexamination);

        startExamLayout.setEnabled(false); // Initially disable the layout

        // Set an OnClickListener on the CheckBox
        agreeCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the CheckBox is checked
                if (agreeCheckbox.isChecked()) {
                    startExamLayout.setEnabled(true); // Enable the layout
                } else {
                    Toast.makeText(NeetExamPayment.this, "Please agree to the terms to start the examination", Toast.LENGTH_SHORT).show();
                    startExamLayout.setEnabled(false); // Disable the layout
                }
            }
        });

        // Set OnClickListener on start examination layout
        startExamLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog(title, title1);
            }
        });
    }

    private void startExamination(String title, String title1) {
        database.child("profiles")
                .child(currentUid)
                .child("coins")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            Integer coinsValue = snapshot.getValue(Integer.class);

                            if (coinsValue != null) {

                                int newCoinsValue = coinsValue - 50;
                                if (newCoinsValue >= 0) {

                                    database.child("profiles")
                                            .child(currentUid)
                                            .child("coins")
                                            .setValue(newCoinsValue);

                                    showQuizInsiderActivity(title, title1);

                                    Toast.makeText(NeetExamPayment.this, "Welcome", Toast.LENGTH_SHORT).show();
                                } else {

                                    Toast.makeText(NeetExamPayment.this, "Insufficient Credits", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void showConfirmationDialog(String title, String title1) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Starting this quiz will deduct 50 med coins from your account. Do you want to proceed?");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startExamination(title, title1);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


    // Method to show the quiz instructions activity
    private void showQuizInsiderActivity(String title, String title1) {
        Intent intent = new Intent(NeetExamPayment.this, Neetexaminsider.class);
        intent.putExtra("Title1", title1);
        intent.putExtra("Title", title);

        startActivity(intent);
    }
}
