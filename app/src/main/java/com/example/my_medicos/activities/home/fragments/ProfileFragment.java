package com.example.my_medicos.activities.home.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.my_medicos.activities.profile.Contactinfo;
import com.example.my_medicos.activities.profile.Personalinfo;
import com.example.my_medicos.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Map;
import java.util.prefs.Preferences;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private static final String DATA_LOADED_KEY = "data_loaded";

    CardView personalinfo, contactinfo;
    TextView user_name_dr, user_email_dr, user_phone_dr,user_location_dr,user_interest_dr;
    ImageView profileImageView;

    private boolean dataLoaded = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            dataLoaded = savedInstanceState.getBoolean(DATA_LOADED_KEY, false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        contactinfo = rootView.findViewById(R.id.contact_info);
        contactinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Personalinfo.class);
                startActivity(i);
            }
        });

        personalinfo = rootView.findViewById(R.id.personal_info);
        personalinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Contactinfo.class);
                startActivity(i);
            }
        });

        user_name_dr = rootView.findViewById(R.id.user_name_dr);
        user_email_dr = rootView.findViewById(R.id.user_email_dr);
        user_phone_dr = rootView.findViewById(R.id.user_phone_dr);
        user_location_dr = rootView.findViewById(R.id.user_location_dr);
        user_interest_dr = rootView.findViewById(R.id.user_interest_dr);
        profileImageView = rootView.findViewById(R.id.circularImageView);

        if (!dataLoaded) {
            fetchdata();
            fetchUserData();
        }
        return rootView;
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(DATA_LOADED_KEY, dataLoaded);
    }
    private void fetchdata() {
        Preferences preferences = Preferences.userRoot();
        if (preferences.get("username", null) != null) {
            System.out.println("Key '" + "username" + "' exists in preferences.");
            String username=preferences.get("username", null);
            Log.d("usernaem",username);
        }
        String username = preferences.get("username", "");
        String email = preferences.get("email", "");
        String location = preferences.get("location", "");
        String interest = preferences.get("interest", "");
        String phone=preferences.get("userphone","");
        user_name_dr.setText(username);
        user_email_dr.setText(email);
        user_location_dr.setText(location);
        user_interest_dr.setText(interest);
        user_phone_dr.setText(phone);

    }

    private void fetchUserData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getPhoneNumber();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    Map<String, Object> dataMap = document.getData();
                                    String field1 = (String) dataMap.get("Phone Number");
                                    int a = field1.compareTo(currentUser.getPhoneNumber());

                                    if (a == 0) {
                                        String userName = (String) dataMap.get("Name");
                                        String userEmail = (String) dataMap.get("Email ID");
                                        String userLocation = (String) dataMap.get("Location");
                                        String userInterest = (String) dataMap.get("Interest");
                                        String userPhone = (String) dataMap.get("Phone Number");
                                        Preferences preferences = Preferences.userRoot(); // User preferences
                                        // Store data
                                        preferences.put("username", userName);

                                        preferences.put("email", userEmail);

                                        preferences.put("location", userLocation);

                                        preferences.put("interest", userInterest);

                                        preferences.put("userphone",userPhone);
                                        user_name_dr.setText(userName);
                                        user_phone_dr.setText(userPhone);
                                        fetchdata();
                                        fetchUserProfileImage(userId);
                                    }
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }

    private void fetchUserProfileImage(String userId) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("users").child(userId).child("profile_image.jpg");
        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri).into(profileImageView);
        }).addOnFailureListener(exception -> {
            Log.e(TAG, "Error fetching profile image: " + exception.getMessage());
        });
    }
}