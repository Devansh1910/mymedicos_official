package com.example.my_medicos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class CmeDetailsActivity extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    Toolbar toolbar;
    String field3;
    String field7;
    String email;
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
        LinearLayout livebtn = findViewById(R.id.livebtn);
        LinearLayout pastbtn = findViewById(R.id.pastbtn);
        WebView youtube = findViewById(R.id.youtube);
        ImageView defaultimageofcme = findViewById(R.id.defaultimage);
        LinearLayout textpartforlive = findViewById(R.id.textpartforlive);
        LinearLayout textpartforupcoming = findViewById(R.id.textpartforupcoming);
        RelativeLayout relativeboxnotforpast = findViewById(R.id.relativeboxnotforpast);



        if ("UPCOMING".equals(field5)) {
            // Show the reservebtn and hide the livebtn for Upcoming events
            reservebtn.setVisibility(View.VISIBLE);
            livebtn.setVisibility(View.GONE);
            pastbtn.setVisibility(View.GONE);
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
            textpartforupcoming.setVisibility(View.INVISIBLE);
            relativeboxnotforpast.setVisibility(View.VISIBLE);


        } else if ("PAST".equals(field5)) {
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
                        int r=field1.compareTo(field3);
                        if (r==0){
                            field4 = ((String) dataMap.get("Name"));
                            Name.setText(field4);

                            break;
                        }
                        // Handle the retrieved data here
                        // You can access data using document.getData() and perform necessary actions
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
                        int r=field1.compareTo(field3);
                        if (r==0){
                            String speciality=((String) dataMap.get("Speciality"));
                            String venue=((String) dataMap.get("CME Venue"));
                            String date1=((String) dataMap.get("Selected Date"));
                            String time1=((String) dataMap.get("Selected Time"));
                            String title=((String) dataMap.get("CME Title"));
                            setSupportActionBar(toolbar);
                            getSupportActionBar().setTitle(title);
                            textView.setText(venue);
                            date.setText(date1);
                            time.setText(time1);
                            Speciality.setText(speciality);
                        }
                        // Handle the retrieved data here
                        // You can access data using document.getData() and perform necessary actions
                    }
                } else {
                    // Handle the error
                    Toast.makeText(CmeDetailsActivity.this, "Error fetching data from Firebase Firestore", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Replace "your_collection_name" and "your_field_name" with actual values
    }
}

