package com.example.my_medicos;

import static android.content.ContentValues.TAG;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CmeDetailsActivity extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    RecyclerView recyclerViewcmeinsider;
    MyAdapter2 adaptercmeinsider;
    Toolbar toolbar;
    String field3;
    String field7;
    String email;
    String field4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cme_details);
        //Youtube

        // Initialize Firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        toolbar = findViewById(R.id.detailtoolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final TextView textView = findViewById(R.id.description);
        final TextView moreButton = findViewById(R.id.moreButton);
        TextView date=findViewById(R.id.date);
        TextView time=findViewById(R.id.time1);
        TextView Name=findViewById(R.id.speakername);
        TextView Speciality=findViewById(R.id.speciality);

        moreButton.setOnClickListener(new View.OnClickListener() {
            boolean isExpanded = false;

            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    textView.setMaxLines(3);
                    moreButton.setText("More");
                } else {
                    textView.setMaxLines(Integer.MAX_VALUE); // Expand to show all lines
                    moreButton.setText("Less");
                }
                isExpanded = !isExpanded;
            }
        });
        String field1=getIntent().getExtras().getString("name");

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

        WebView webview = findViewById(R.id.youtube);
        String video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/z-mJEZbHFLs?si=MRCAlDt499plfZwz\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        webview.loadData(video,"text/html","utf-8");
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebChromeClient(new WebChromeClient());

    }
}

