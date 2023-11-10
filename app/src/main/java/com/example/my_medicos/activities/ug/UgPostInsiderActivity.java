package com.example.my_medicos.activities.ug;

import static com.example.my_medicos.list.subSpecialitiesData.subspecialities;

import android.app.ProgressDialog;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.my_medicos.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UgPostInsiderActivity extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    String current = user.getEmail();
    String field3,field4;
    EditText ugtitle, ugorg, ugvenu;
    Button postug;
    public FirebaseDatabase db = FirebaseDatabase.getInstance();
    FirebaseFirestore dc = FirebaseFirestore.getInstance();
    private Spinner subspecialitySpinner;
    private Spinner specialitySpinner;
    String subspecialities1;
    public DatabaseReference ugref = db.getReference().child("UG's");
    private ProgressDialog progressDialog;
    private static final int MAX_CHARACTERS = 1000;
    private Calendar calendar;
    private SimpleDateFormat dateFormat, timeFormat;

    static final int REQUEST_STORAGE_PERMISSION = 1;
    static final int REQUEST_STORAGE_ACCESS = 2;
    private ArrayAdapter<CharSequence> specialityAdapter;
    private ArrayAdapter<CharSequence> subspecialityAdapter;
    private CardView btnAccessStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ug_post_insider);
        btnAccessStorage = findViewById(R.id.btnAccessStorage);

        btnAccessStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestStoragePermission();
            }
        });

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        progressDialog = new ProgressDialog(this);

        specialitySpinner = findViewById(R.id.ugspeciality);
        subspecialitySpinner = findViewById(R.id.ugsubspeciality);

        specialityAdapter = ArrayAdapter.createFromResource(this,
                R.array.speciality, android.R.layout.simple_spinner_item);
        specialityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specialitySpinner.setAdapter(specialityAdapter);

        subspecialityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        subspecialityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subspecialitySpinner.setAdapter(subspecialityAdapter);
        // Initially, hide the subspeciality spinner
        subspecialitySpinner.setVisibility(View.GONE);
        specialitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                // Check if the selected speciality has subspecialities
                int specialityIndex = specialitySpinner.getSelectedItemPosition();
                if (specialityIndex >= 0 && specialityIndex < subspecialities.length && subspecialities[specialityIndex].length > 0) {
                    String[] subspecialityArray = subspecialities[specialityIndex];
                    subspecialityAdapter.clear();
                    subspecialityAdapter.add("Select Subspeciality");
                    for (String subspeciality : subspecialityArray) {
                        subspecialityAdapter.add(subspeciality);
                    }
                    // Show the subspeciality spinner
                    subspecialitySpinner.setVisibility(View.VISIBLE);
                    subspecialitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            subspecialities1 = subspecialitySpinner.getSelectedItem().toString();

                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } else {
                    // Hide the subspeciality spinner
                    subspecialitySpinner.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
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
                            ugorg.setText(field4);
                        }

                    }
                } else {
                    // Handle the error
                    Toast.makeText(UgPostInsiderActivity.this, "Error fetching data from Firebase Firestore", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ugtitle = findViewById(R.id.ug_title);
        ugorg = findViewById(R.id.ug_organiser);

        ugorg.setEnabled(false);
        ugorg.setTextColor(Color.parseColor("#80000000"));
        ugorg.setBackgroundResource(R.drawable.rounded_edittext_background);
        ugvenu = findViewById(R.id.ug_venu);

        postug = findViewById(R.id.post_ug_btn);


        // Initialize the charCount TextView
        TextView charCount = findViewById(R.id.char_counter);

        // Add a TextWatcher to the cmevenu EditText for character counting and button enabling/disabling
        ugvenu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int currentCount = charSequence.length();
                charCount.setText(currentCount + "/" + MAX_CHARACTERS);

                // You can change the color of the charCount TextView based on the character count
                if (currentCount > MAX_CHARACTERS) {
                    charCount.setTextColor(Color.RED);
                    postug.setEnabled(false); // Disable the "Post" button
                    // Add a Toast message to notify the user
                    Toast.makeText(UgPostInsiderActivity.this, "Character limit exceeded (2000 characters max)", Toast.LENGTH_SHORT).show();
                } else {
                    charCount.setTextColor(Color.DKGRAY);
                    postug.setEnabled(true); // Enable the "Post" button
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // Disable the "Post" button initially
        postug.setEnabled(false);

        postug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    postUG();
                }
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
    public void postUG() {
        String title = ugtitle.getText().toString().trim();
        String organiser = ugorg.getText().toString().trim();
        String venu = ugvenu.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            ugtitle.setError("Title Required");
            return;
        }
        if (TextUtils.isEmpty(organiser)) {
            ugorg.setError("Organizer Required");
            return;
        }
        if (TextUtils.isEmpty(venu)) {
            ugvenu.setError("Email Required");
            return;
        }

        if (venu.length() > MAX_CHARACTERS) {
            // Display an error message if the content exceeds the limit
            ugvenu.setError("Character limit exceeded");
            return;
        } else {
            ugvenu.setError(null); // Clear any previous error
        }

        Spinner ugspecialitySpinner = findViewById(R.id.ugspeciality);
        String speciality = ugspecialitySpinner.getSelectedItem().toString();


        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedDate = currentDateTime.format(dateFormatter);
        String formattedTime = currentDateTime.format(timeFormatter);


        HashMap<String, Object> usermap = new HashMap<>();
        usermap.put("UG Title", title);
        usermap.put("UG Organiser", field4);
        usermap.put("UG Description", venu);
        usermap.put("User", current);
        usermap.put("Date", formattedDate);
        usermap.put("Time",formattedTime);
        usermap.put("Speciality", speciality);
        usermap.put("SubSpeciality",subspecialities1);

        progressDialog.setMessage("Posting...");
        progressDialog.show();

        ugref.push().setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();

                if (task.isSuccessful()) {
                    dc.collection("UG").add(usermap);
                    Toast.makeText(UgPostInsiderActivity.this, "Posted Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UgPostInsiderActivity.this, "Task Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
