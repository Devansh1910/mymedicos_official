package com.example.my_medicos;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    ImageView personalinfo, contactinfo;
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

    private void fetchUserData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("users").document(userId);

            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Get the data from the document
                            String userName = document.getString("Name");
                            String userEmail = document.getString("Email ID");
                            String userPhone = document.getString("Phone Number");

                            // Set the data to the TextViews
                            user_name_dr.setText(userName);
                            user_email_dr.setText(userEmail);
                            user_phone_dr.setText(userPhone);
                        } else {
                            // Handle document not found
                            Toast.makeText(getContext(), "User data not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Handle errors
                        Toast.makeText(getContext(), "Error fetching data from Firebase", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
