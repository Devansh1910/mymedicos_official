package com.example.my_medicos;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
public class PostCmeActivity extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    String current=user.getEmail();
    String post;





    TextView cmetitle,cmeorg,cmepresenter,cmevenu,virtuallink,cme_place;

    Button postcme;

    public FirebaseDatabase db= FirebaseDatabase.getInstance();
     FirebaseFirestore dc=FirebaseFirestore.getInstance();

    public DatabaseReference cmeref=db.getReference().child("CME's");

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_cme);

        Spinner  spinner2= (Spinner) findViewById(R.id.cmemode);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> myadapter = ArrayAdapter.createFromResource(this,
                R.array.mode, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        myadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Apply the adapter to the spinner
        spinner2.setAdapter(myadapter);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected item (value) from the Spinner
                post = parentView.getItemAtPosition(position).toString();

                // Now, 'selectedValue' holds the selected value in a variable
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle the case where nothing is selected (if needed)
            }
        });


        cmetitle=findViewById(R.id.cme_title);
        cmeorg=findViewById(R.id.cme_organiser);
        cmepresenter=findViewById(R.id.cme_presenter);
        cmevenu=findViewById(R.id.cme_venu);
        virtuallink=findViewById(R.id.cme_virtuallink);
        cme_place=findViewById(R.id.cme_place);


        postcme=findViewById(R.id.post_btn);

        postcme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postCme();
            }
        });
    }
    public void postCme() {
        String title = cmetitle.getText().toString().trim();
        String organiser = cmeorg.getText().toString().trim();
        String presenter = cmepresenter.getText().toString().trim();
        String venu = cmevenu.getText().toString().trim();
        String link = virtuallink.getText().toString().trim();
        String place = cme_place.getText().toString().trim();



        if (TextUtils.isEmpty(title)) {
            cmetitle.setError("Title Required");
            return;
        }
        if (TextUtils.isEmpty(post)) {
            cmetitle.setError("Mode Required");
            return;
        }
        if (TextUtils.isEmpty(organiser)) {
            cmeorg.setError("Organizer Required");
            return;
        }
        if (TextUtils.isEmpty(presenter)) {
            cmepresenter.setError("Email Required");
            return;
        }
        if (TextUtils.isEmpty(venu)) {
            cmevenu.setError("Email Required");
        }
        LocalDate currentDate = null;
        LocalTime currentTime=null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();
            currentTime = LocalTime.now();

        }
        String formattedDateTime = null;
                LocalDateTime currentDateTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
             formattedDateTime = currentDateTime.format(formatter);

        }


        HashMap<String, Object> usermap = new HashMap<>();


        usermap.put("CME Organiser", organiser);


        usermap.put("CME Place", place);
        usermap.put("CME Presenter", venu);
        usermap.put("CME Title", title);

        usermap.put("Virtual Link", link);

        usermap.put("User", current);
        usermap.put("date",formattedDateTime);
        usermap.put("MODE",post);

        System.out.println(usermap);






        cmeref.push().setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    dc.collection("CME").add(usermap);

                    Toast.makeText(PostCmeActivity.this, "Posted Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PostCmeActivity.this, "Task Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}