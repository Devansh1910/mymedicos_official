package com.example.my_medicos;

import static com.example.my_medicos.PostCmeActivity.REQUEST_STORAGE_ACCESS;
import static com.example.my_medicos.PostCmeActivity.REQUEST_STORAGE_PERMISSION;
import static com.example.my_medicos.subSpecialitiesData.subspecialities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class JobsApplyActivity extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    String current = user.getEmail();
    String field3,field4;
    EditText jobname, jobage, jobgender, jobexperience, jobcover;
    Button postcme;
    public FirebaseDatabase db = FirebaseDatabase.getInstance();
    FirebaseFirestore dc = FirebaseFirestore.getInstance();
    private Spinner subspecialitySpinner;
    private Spinner specialitySpinner;
    String subspecialities1;
    public DatabaseReference cmeref = db.getReference().child("JOB Apply");
    private ProgressDialog progressDialog;

    static final int REQUEST_STORAGE_PERMISSION = 1;
    static final int REQUEST_STORAGE_ACCESS = 2;
    private ArrayAdapter<CharSequence> specialityAdapter;
    private ArrayAdapter<CharSequence> subspecialityAdapter;
    private CardView btnAccessStorage;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_jobs1);
        btnAccessStorage = findViewById(R.id.btnAccessStorage2);

        btnAccessStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestStoragePermission();
            }
        });
        dc.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> dataMap = document.getData();
                        field3 = ((String) dataMap.get("Email ID"));
                        boolean areEqualIgnoreCase = current.equalsIgnoreCase(field3);
                        Log.d("vivek", String.valueOf(areEqualIgnoreCase));
                        int r=current.compareTo(field3);
                        if (r==0){
                            field4 = ((String) dataMap.get("Name"));
                            Log.d("veefe",field4);
                            jobname.setText(field4);
                        }


                        // Handle the retrieved data here

                        // You can access data using document.getData() and perform necessary actions
                    }
                } else {
                    // Handle the error
                    Toast.makeText(JobsApplyActivity.this, "Error fetching data from Firebase Firestore", Toast.LENGTH_SHORT).show();
                }
            }
        });
        jobname = findViewById(R.id.name);
        jobage = findViewById(R.id.Age);

        jobname.setEnabled(false);
        jobname.setTextColor(Color.parseColor("#80000000"));
        jobname.setBackgroundResource(R.drawable.rounded_edittext_background);
        jobgender = findViewById(R.id.gender);
        jobcover = findViewById(R.id.cover);
        jobexperience = findViewById(R.id.cme_virtuallink);


        postcme = findViewById(R.id.post_btn1);
         Spinner modeSpinner = findViewById(R.id.experience);
        ArrayAdapter<CharSequence> modeAdapter = ArrayAdapter.createFromResource(this,
                R.array.experience, android.R.layout.simple_spinner_item);
        modeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modeSpinner.setAdapter(modeAdapter);
        Spinner cmemodeSpinner = findViewById(R.id.experience);
        cmemodeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedMode = cmemodeSpinner.getSelectedItem().toString();

                if (selectedMode.equals("Experienced")) {
                    // Show the virtual link EditText and hide the place EditText
                    jobexperience.setVisibility(View.VISIBLE);

                } else if (selectedMode.equals("Fresher")) {
                    // Show the place EditText and hide the virtual link EditText
                    jobexperience.setVisibility(View.GONE);

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
        } else {
            // Permission is already granted, open storage
            openStorageForAccess();
        }
    }

    private void openStorageForAccess() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(intent, REQUEST_STORAGE_ACCESS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open storage
                openStorageForAccess();
            } else {
                // Permission denied, show a message or disable functionality
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_STORAGE_ACCESS && resultCode == RESULT_OK) {
            if (data != null) {
                Uri treeUri = data.getData();
                if (treeUri != null) {
                    listFilesInDirectory(treeUri);
                }
            }
        }
    }

    private void listFilesInDirectory(Uri treeUri) {
        String treeDocumentId = DocumentsContract.getTreeDocumentId(treeUri);
        Uri docUri = DocumentsContract.buildDocumentUriUsingTree(treeUri, treeDocumentId);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void postCme() {
        String name = jobname.getText().toString().trim();
        String age = jobage.getText().toString().trim();
        String gender = jobgender.getText().toString().trim();
        String cover = jobcover.getText().toString().trim();
        String experience = jobexperience.getText().toString().trim();


//        if (!link.startsWith("https://")) {
//            virtuallink.setError("Not a Valid Link");
//            virtuallink.setTextColor(Color.RED);
//            return;
//        } else {
//            virtuallink.setTextColor(Color.DKGRAY);
//        }

        if (TextUtils.isEmpty(name)) {
            jobname.setError("Title Required");
            return;
        }
        if (TextUtils.isEmpty(age)) {
            jobage.setError("Organizer Required");
            return;
        }
        if (TextUtils.isEmpty(gender)) {
            jobgender.setError("Email Required");
            return;
        }
        if (TextUtils.isEmpty(cover)) {
            jobcover.setError("Email Required");
            return;
        }



        // Get the selected mode from the spinner
        Spinner cmemodeSpinner = findViewById(R.id.experience);
        String mode = cmemodeSpinner.getSelectedItem().toString();

        // Get the selected speciality from the spinner


        // Get current date and time



        HashMap<String, Object> usermap = new HashMap<>();
        usermap.put("Full name", name);
        usermap.put("Age", age);
        usermap.put("Gender", gender);
        usermap.put("cover", cover);
        usermap.put("experienced", experience);

        usermap.put("User", current);
        usermap.put("Experienced", mode);
        // Add selected time



//        cmeref.push().setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                progressDialog.dismiss();
//
//                if (task.isSuccessful()) {
//                    dc.collection("CME").add(usermap);
//                    Toast.makeText(JobsApplyActivity.this, "Posted Successfully", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(JobsApplyActivity.this, "Task Failed", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }


}
