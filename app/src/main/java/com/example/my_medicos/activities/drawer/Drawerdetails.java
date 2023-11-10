//package com.example.my_medicos;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.cardview.widget.CardView;
//import androidx.fragment.app.Fragment;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.squareup.picasso.Picasso;
//
//import java.util.Map;
//import java.util.prefs.Preferences;
//
//public class Drawerdetails extends AppCompatActivity {
//
//    private static final String TAG = "ProfileFragment";
//    private static final String DATA_LOADED_KEY = "data_loaded";
//
//    CardView personalinfo, contactinfo;
//    TextView doctor_name_drawer, doctor_email_drawer;
//    ImageView doctor_profile_photo;
//
//    private boolean dataLoaded = false;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        if (savedInstanceState != null) {
//            dataLoaded = savedInstanceState.getBoolean(DATA_LOADED_KEY, false);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.header_layout, container, false);
//
//        doctor_name_drawer = rootView.findViewById(R.id.doctor_name_drawer);
//        doctor_email_drawer = rootView.findViewById(R.id.doctor_email_drawer);
//        doctor_profile_photo = rootView.findViewById(R.id.doctor_profile_photo);
//
//        if (!dataLoaded) {
//            fetchdata();
//            fetchUserData();
//
//        }
//
//        return rootView;
//    }
//
//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putBoolean(DATA_LOADED_KEY, dataLoaded);
//    }
//    private void fetchdata() {
//        Preferences preferences = Preferences.userRoot();
//        if (preferences.get("username", null) != null) {
//            System.out.println("Key '" + "username" + "' exists in preferences.");
//            String username=preferences.get("username", null);
//            Log.d("usernaem",username);
//        }
//        String username = preferences.get("username", "");
//        String email = preferences.get("email", "");
//        String phone=preferences.get("userphone","");
//        doctor_name_drawer.setText(username);
//        doctor_email_drawer.setText(email);
//
//    }
//
//    private void fetchUserData() {
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (currentUser != null) {
//            String userId = currentUser.getUid();
//            FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//            db.collection("users")
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if (task.isSuccessful()) {
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                    Log.d(TAG, document.getId() + " => " + document.getData());
//                                    Map<String, Object> dataMap = document.getData();
//                                    String field1 = (String) dataMap.get("Email ID");
//                                    String field2 = (String) dataMap.get("Phone Number");
//                                    int a = field1.compareTo(currentUser.getEmail());
//                                    int b = field2.compareTo(currentUser.getPhoneNumber());
//
//                                    if (a == 0 || b == 0) {
//                                        String userName = (String) dataMap.get("Name");
//                                        String userEmail = (String) dataMap.get("Email ID");
//                                        String userPhone = (String) dataMap.get("Phone Number");
//                                        Preferences preferences = Preferences.userRoot(); // User preferences
//                                        // Store data
//                                        preferences.put("username", userName);
//                                        preferences.put("email", userEmail);
//                                        preferences.put("userphone",userPhone);
//                                        doctor_name_drawer.setText(userName);
//                                        doctor_email_drawer.setText(userEmail);
//                                        fetchdata();
//
//                                        // Retrieve data
//                                        // Set the data to the TextViews
//
//                                        fetchUserProfileImage(userId);
//                                    }
//                                }
//                            } else {
//                                Log.d(TAG, "Error getting documents: ", task.getException());
//                            }
//                        }
//                    });
//        }
//    }
//
//    private void fetchUserProfileImage(String userId) {
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReference().child("users").child(userId).child("profile_image.jpg");
//
//        // Load the profile image using Picasso
//        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
//            Picasso.get().load(uri).into(doctor_profile_photo);
//        }).addOnFailureListener(exception -> {
//            // Handle any errors while fetching the image
//            Log.e(TAG, "Error fetching profile image: " + exception.getMessage());
//        });
//    }
//}
