package com.example.my_medicos.activities.cme;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_medicos.adapter.cme.MyAdapter2;
import com.example.my_medicos.R;
import com.example.my_medicos.adapter.cme.items.cmeitem1;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CmeReservedActivity extends AppCompatActivity {
    RecyclerView recyclerView1cmereserved;
    String userEmail;
    String Cmeid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cme_reserved);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            userEmail = currentUser.getEmail();
        }

        FirebaseFirestore dc = FirebaseFirestore.getInstance();

        dc.collection("CMEsReserved")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> dataMap = document.getData();
                                Cmeid = (String) dataMap.get("Cmeid");
                                String user = (String) dataMap.get("User");
                                if (Cmeid != null && user != null) {
                                    int r = user.compareTo(userEmail);
                                    if (r == 0) {
                                        fetch(Cmeid);
                                    }
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    List<cmeitem1> cmelist = new ArrayList<cmeitem1>();

    void fetch(String Cmeidn) {
        recyclerView1cmereserved = findViewById(R.id.recyclerview7cmereserved);
        FirebaseFirestore dc = FirebaseFirestore.getInstance();

        dc.collection("CME")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> dataMap = document.getData();
                                String usercme = ((String) dataMap.get("User"));
                                String specialitycme = ((String) dataMap.get("Speciality"));
                                String presentercme = ((String) dataMap.get("CME Presenter"));
                                String titlecme = ((String) dataMap.get("CME Title"));
                                String organizercme = ((String) dataMap.get("CME Organiser"));
                                String Date = ((String) dataMap.get("Selected Date"));
                                String time = ((String) dataMap.get("Selected Time"));
                                if (Cmeidn != null && usercme != null) {
                                    int r = Cmeidn.compareTo(usercme);
                                    if (r == 0) {
                                        cmeitem1 cme = new cmeitem1(usercme, specialitycme, Date, titlecme, presentercme, organizercme, 5, time);
                                        cmelist.add(cme);
                                    }
                                }
                            }
                            recyclerView1cmereserved.setLayoutManager(new LinearLayoutManager(getApplication(), LinearLayoutManager.VERTICAL, false));
                            recyclerView1cmereserved.setAdapter(new MyAdapter2(getApplication(), cmelist));
                        } else {
                            Log.e(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
