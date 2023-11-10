package com.example.my_medicos.activities.cme;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.my_medicos.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class CmeDetailsActivity extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    Toolbar toolbar;
    String field3;
    String field7;
    String email;
    FirebaseFirestore dc = FirebaseFirestore.getInstance();
    String receivedDataCme;
    String current = user.getEmail();
    String field4;
    VideoView videoView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cme_details);

        // Initialize Firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        toolbar = findViewById(R.id.detailtoolbar);

        final TextView textView = findViewById(R.id.description);
        final TextView moreButton = findViewById(R.id.moreButton);
        TextView date = findViewById(R.id.date);
        TextView time = findViewById(R.id.time1);
        TextView Name = findViewById(R.id.speakername);
        TextView Speciality = findViewById(R.id.speciality);
        TextView Type = findViewById(R.id.type);


        String field1 = getIntent().getExtras().getString("name");
        String field5 = getIntent().getExtras().getString("type");
        Type.setText(field5);

        LinearLayout reservebtn = findViewById(R.id.reservbtn);

        Button reservecmebtn = findViewById(R.id.reservecmebtn);
        Button reservedcmebtn = findViewById(R.id.reservedcmebtn);

        // Initialize SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("CmeDetails", Context.MODE_PRIVATE);

        // Check if the button is reserved in SharedPreferences
        boolean isReserved = sharedPref.getBoolean("isReserved", false);
        reservecmebtn.setVisibility(isReserved ? View.GONE : View.VISIBLE);
        reservedcmebtn.setVisibility(isReserved ? View.VISIBLE : View.GONE);

        reservecmebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reservedcmebtn.getVisibility() == View.VISIBLE) {
                    Toast.makeText(CmeDetailsActivity.this, "You'll be notified", Toast.LENGTH_SHORT).show();
                } else {
                    // Save the reservation state in SharedPreferences
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("isReserved", true);
                    editor.apply();

                    reservecmebtn.setVisibility(View.GONE);
                    reservedcmebtn.setVisibility(View.VISIBLE);
                }
            }
        });

        LinearLayout livebtn = findViewById(R.id.livebtn);
        LinearLayout pastbtn = findViewById(R.id.pastbtn);
        WebView youtube = findViewById(R.id.youtube);
        ImageView defaultimageofcme = findViewById(R.id.defaultimage);
        LinearLayout textpartforlive = findViewById(R.id.textpartforlive);
        Button attendcmebtn = findViewById(R.id.attendcmebtn);
        LinearLayout textpartforupcoming = findViewById(R.id.textpartforupcoming);
        RelativeLayout relativeboxnotforpast = findViewById(R.id.relativeboxnotforpast);

        if ("UPCOMING".equals(field5)) {
            // Show the reservebtn and hide the livebtn for Upcoming events
            reservebtn.setVisibility(View.VISIBLE);
            livebtn.setVisibility(View.GONE);
            pastbtn.setVisibility(View.GONE);
            reservecmebtn.setVisibility(View.VISIBLE);
            reservedcmebtn.setVisibility(View.GONE);
            reservecmebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Perform any actions needed when the button is clicked
                    // ...

                    // Hide the Reserve a Seat button and show the Reserved button
                    reservecmebtn.setVisibility(View.GONE);
                    reservedcmebtn.setVisibility(View.VISIBLE);
                    apply();
                }
            });
            reservedcmebtn.setVisibility(View.GONE);
            youtube.setVisibility(View.GONE);
            defaultimageofcme.setVisibility(View.VISIBLE);
            textpartforlive.setVisibility(View.INVISIBLE);
            textpartforupcoming.setVisibility(View.VISIBLE);
            relativeboxnotforpast.setVisibility(View.VISIBLE);

        } else if ("LIVE".equals(field5)) {
            // Show the livebtn and hide the reservebtn for Ongoing events
            reservebtn.setVisibility(View.GONE);
            livebtn.setVisibility(View.VISIBLE);
            pastbtn.setVisibility(View.GONE);
            youtube.setVisibility(View.GONE);
            defaultimageofcme.setVisibility(View.VISIBLE);
            textpartforlive.setVisibility(View.VISIBLE);
            textpartforlive.setVisibility(View.VISIBLE);
            attendcmebtn.setVisibility(View.VISIBLE);
            textpartforupcoming.setVisibility(View.INVISIBLE);
            relativeboxnotforpast.setVisibility(View.VISIBLE);

            attendcmebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Query query = db.collection("CME").whereEqualTo("User", current);
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String virtualLink = document.getString("Virtual Link");

                                    if (!TextUtils.isEmpty(virtualLink)) {
                                        // Open the virtual link in a web browser
                                        Uri uri = Uri.parse(virtualLink);
                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(intent);
                                    } else {
                                        // Show a message or handle the case when the virtual link is not available
                                        showReservationDialog();
                                    }
                                }
                            } else {
                                // Handle the error
                                Toast.makeText(CmeDetailsActivity.this, "Error fetching data from Firebase Firestore", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }
        else if ("PAST".equals(field5)) {
            // Show the livebtn and hide the reservebtn for Ongoing events
            reservebtn.setVisibility(View.GONE);
            livebtn.setVisibility(View.GONE);
            pastbtn.setVisibility(View.VISIBLE);
            youtube.setVisibility(View.VISIBLE);
            defaultimageofcme.setVisibility(View.GONE);
            textpartforlive.setVisibility(View.INVISIBLE);
            textpartforupcoming.setVisibility(View.INVISIBLE);
            relativeboxnotforpast.setVisibility(View.GONE);
        } else {
            // Handle other cases or set a default behavior if needed
            reservebtn.setVisibility(View.GONE);
            livebtn.setVisibility(View.GONE);
        }

        Query query1 = db.collection("users");
        query1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> dataMap = document.getData();
                        field3 = ((String) dataMap.get("Email ID"));
                        boolean areEqualIgnoreCase = field1.equalsIgnoreCase(field3);
                        Log.d("vivek", String.valueOf(areEqualIgnoreCase));
                        int r = field1.compareTo(field3);
                        if (r == 0) {
                            field4 = ((String) dataMap.get("Name"));
                            Name.setText(field4);
                            break;
                        }
                    }
                } else {
                    // Handle the error
                    Toast.makeText(CmeDetailsActivity.this, "Error fetching data from Firebase Firestore", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Query query = db.collection("CME");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> dataMap = document.getData();
                        field3 = ((String) dataMap.get("User"));
                        boolean areEqualIgnoreCase = field1.equalsIgnoreCase(field3);
                        Log.d("vivek", String.valueOf(areEqualIgnoreCase));
                        int r = field1.compareTo(field3);
                        if (r == 0) {
                            String speciality = ((String) dataMap.get("Speciality"));
                            String venue = ((String) dataMap.get("CME Venue"));
                            String date1 = ((String) dataMap.get("Selected Date"));
                            String time1 = ((String) dataMap.get("Selected Time"));
                            String title = ((String) dataMap.get("CME Title"));
                            String virtuallink = ((String) dataMap.get("Virtual Link"));

                            setSupportActionBar(toolbar);
                            getSupportActionBar().setTitle(title);
                            textView.setText(venue);
                            date.setText(date1);
                            time.setText(time1);
                            Speciality.setText(speciality);
                        }
                    }
                } else {
                    // Handle the error
                    Toast.makeText(CmeDetailsActivity.this, "Error fetching data from Firebase Firestore", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showReservationDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.loaderofmeeting);
        // Customize the dialog or handle button clicks as needed
        dialog.show();
    }

    public void apply() {
        HashMap<String, Object> usermap = new HashMap<>();
        usermap.put("User", current);
        usermap.put("Cmeidn",receivedDataCme);
        Log.d(receivedDataCme,"abcdef");
        dc.collection("CMEsReserved")
                .add(usermap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
}