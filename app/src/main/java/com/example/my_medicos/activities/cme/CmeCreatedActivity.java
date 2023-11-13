package com.example.my_medicos.activities.cme;

import android.annotation.SuppressLint;
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

public class CmeCreatedActivity extends AppCompatActivity {
    private static final String TAG = "CmesPostedYou"; // Logging tag
    String userEmail = null;
    RecyclerView recyclerView1cme;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cme_created);

        // Access UI elements
        recyclerView1cme = findViewById(R.id.recyclerview8cme);

        // Firebase Authentication
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            userEmail = currentUser.getEmail();
        }

        // Now, userEmail contains the current user's email address

        List<cmeitem1> cmelist = new ArrayList<cmeitem1>();

        FirebaseFirestore dc = FirebaseFirestore.getInstance();
        // ...

        dc.collection("CME")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task ) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Map<String, Object> dataMap = document.getData();
                                String usercme = ((String) dataMap.get("User"));
                                String specialitycme =((String) dataMap.get("Speciality"));
                                String presentercme =((String) dataMap.get("CME Presenter"));
                                String titlecme=((String) dataMap.get("CME Title"));
                                String organizercme =((String) dataMap.get("CME Organiser"));
                                String Date=((String) dataMap.get("Selected Date"));
                                String time =((String) dataMap.get("Selected Time"));
                                String documentid=((String ) dataMap.get("documentId"));
                                int r = usercme.compareTo(userEmail);
                                Log.d("abcdefsfsd", String.valueOf(r));

                                if (r == 0) {
                                    cmeitem1 cme = new cmeitem1(usercme, specialitycme,Date, titlecme,presentercme,organizercme,5,time,documentid);
                                    cmelist.add(cme);
                                }

                                recyclerView1cme.setLayoutManager(new LinearLayoutManager(getApplication(), LinearLayoutManager.VERTICAL, false));
                                recyclerView1cme.setAdapter(new MyAdapter2(getApplication(), cmelist));
                            }
                        } else {
                            Log.e(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
