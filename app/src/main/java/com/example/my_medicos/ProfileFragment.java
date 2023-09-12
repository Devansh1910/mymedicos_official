package com.example.my_medicos;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class ProfileFragment extends Fragment {

    CardView personalinfo, contactinfo;
    TextView user_name_dr, user_email_dr, user_phone_dr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        contactinfo = rootView.findViewById(R.id.contact_info);
        contactinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Contactinfo.class);
                startActivity(i);
            }
        });

        personalinfo = rootView.findViewById(R.id.personal_info);
        personalinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Personalinfo.class);
                startActivity(i);
            }
        });

        user_name_dr = rootView.findViewById(R.id.user_name_dr);
        user_email_dr = rootView.findViewById(R.id.user_email_dr);
        user_phone_dr = rootView.findViewById(R.id.user_phone_dr);

        // Fetch user data from Firestore
        fetchUserData();

        return rootView;
    }
    String field1,field2;

    private void fetchUserData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task ) {


                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    Map<String, Object> dataMap = document.getData();
                                    field1 = (String) dataMap.get("Email ID");
                                    int a=field1.compareTo(currentUser.getEmail());
                                    if (a==0) {
                                        String userName = (String) dataMap.get("Name");
                                        String userEmail = (String) dataMap.get("Email ID");
                                        String userPhone = (String) dataMap.get("Phone Number");
                                        Log.d(TAG, (String) userEmail +"hell0");

                                        // Set the data to the TextViews
                                        user_name_dr.setText(userName);
                                        user_email_dr.setText(userEmail);
                                        user_phone_dr.setText(userPhone);
                                    }

                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });



        }
    }
}
