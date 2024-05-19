package com.medical.my_medicos.activities.pg.activites;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.medical.my_medicos.R;
import static org.bouncycastle.asn1.x500.style.RFC4519Style.title;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.pg.activites.insiders.WeeklyQuizInsiderActivity;
import com.medical.my_medicos.activities.pg.model.QuizPG;
public class PgPrepPayement extends AppCompatActivity  {


    private DatabaseReference database;
    private String currentUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pg_prep_payment);


        database = FirebaseDatabase.getInstance().getReference();

        currentUid = FirebaseAuth.getInstance().getUid();
        Intent intent = getIntent();

        String title1 = intent.getStringExtra("Title1");
        String title = intent.getStringExtra("Title");
        String Due = intent.getStringExtra("Due");

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView quizNameTextView = findViewById(R.id.quizNameTextView1);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView dueDateTextView = findViewById(R.id.DueDate1);
        dueDateTextView.setText(Due);
        quizNameTextView.setText(title1);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) LinearLayout startExamLayout = findViewById(R.id.startexamination1);
        startExamLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startExamination(title,title1);
            }
        });
    }


    private void startExamination(String title,String title1) {

        database.child("profiles")
                .child(currentUid)
                .child("coins")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            Integer coinsValue = snapshot.getValue(Integer.class);

                            if (coinsValue != null) {

                                int newCoinsValue = coinsValue - 30;
                                if (newCoinsValue >= 0) {

                                    database.child("profiles")
                                            .child(currentUid)
                                            .child("coins")
                                            .setValue(newCoinsValue);

                                    showQuizInsiderActivity(title,title1);

                                    Toast.makeText(PgPrepPayement.this, "Welcome", Toast.LENGTH_SHORT).show();
                                } else {

                                    Toast.makeText(PgPrepPayement.this, "Insufficient Credits", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    // Method to show the quiz instructions activity
    private void showQuizInsiderActivity(String title ,String title1) {

        Intent intent = new Intent(PgPrepPayement.this, WeeklyQuizInsiderActivity.class);
        intent.putExtra("Title1", title1);
        intent.putExtra("Title", title);

        startActivity(intent);
    }
}
