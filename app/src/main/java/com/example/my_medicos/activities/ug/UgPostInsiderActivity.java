package com.example.my_medicos.activities.ug;

import static com.example.my_medicos.list.subSpecialitiesData.subspecialities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
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
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.my_medicos.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
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
    String current = user.getPhoneNumber();
    static final int REQ = 1;
    private Uri pdfData;
    private String pdfName;
    String field3,field4;
    String downloadUrl;
    EditText ugtitle, ugorg, ugvenu;
    Button postug;
    public FirebaseDatabase db = FirebaseDatabase.getInstance();
    FirebaseFirestore dc = FirebaseFirestore.getInstance();
    private Spinner subspecialitySpinner;
    private Spinner specialitySpinner,ugtypeSpinner,ugcitySpinner;
    String subspecialities1;
    public DatabaseReference ugref = db.getReference().child("UG's");
    private ProgressDialog progressDialog;
    private static final int MAX_CHARACTERS = 1000;
    private Calendar calendar;
    private ProgressDialog pd;
    private SimpleDateFormat dateFormat, timeFormat;
    static final int REQUEST_STORAGE_PERMISSION = 1;
    static final int REQUEST_STORAGE_ACCESS = 2;
    private ArrayAdapter<CharSequence> specialityAdapter;
    private ArrayAdapter<CharSequence> ugAdapter;
    private ArrayAdapter<CharSequence> subspecialityAdapter;
    private CardView btnAccessStorage;
    private Button uploadpdfbtnjobs;
    private TextView addPdf,uploadPdfBtn;
    private DatabaseReference databasereference;
    private StorageReference storageReference;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ug_post_insider);
//        uploadpdfbtnjobs = findViewById(R.id.uploadpdfbtnug);
        addPdf = findViewById(R.id.addPdfjobs);
        databasereference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();



        addPdf = findViewById(R.id.addPdfug);

        uploadPdfBtn = findViewById(R.id.uploadpdfbtnug);

        addPdf.setOnClickListener(view -> {
            openGallery();
        });
        pd = new ProgressDialog(this);



        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        progressDialog = new ProgressDialog(this);

        specialitySpinner = findViewById(R.id.ugspeciality);
        subspecialitySpinner = findViewById(R.id.ugsubspeciality);
        ugtypeSpinner=findViewById(R.id.ugtype);
        ugAdapter = ArrayAdapter.createFromResource(this,
                R.array.ugtype, android.R.layout.simple_spinner_item);
        ugAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ugtypeSpinner.setAdapter(ugAdapter);
        ugcitySpinner=findViewById(R.id.ugcity);
        ArrayAdapter<CharSequence> myadapter = ArrayAdapter.createFromResource(this,
                R.array.indian_cities, android.R.layout.simple_spinner_item);
        myadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ugcitySpinner.setAdapter(myadapter);



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
                        field3 = ((String) dataMap.get("Phone Number"));
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
        uploadPdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pdfData == null){
                    Toast.makeText(UgPostInsiderActivity.this, "Select a Document", Toast.LENGTH_SHORT).show();
                }else{
                    uploadPdf();
                }
            }
        });


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
        String type = ugtypeSpinner.getSelectedItem().toString();
        String city = ugcitySpinner.getSelectedItem().toString();


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
        usermap.put("Type",type);
        usermap.put("City",city);
        usermap.put("pdf",downloadUrl);

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
    private void uploadData(String valueOf) {
        String uniqueKey = databasereference.child("pdf").push().getKey();
        HashMap data = new HashMap();
        data.put("pdfUrl",downloadUrl);

        databasereference.child("pdf").child(uniqueKey).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(UgPostInsiderActivity.this, "Pdf Uploaded Successfully", Toast.LENGTH_SHORT).show();
                ugtitle.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(UgPostInsiderActivity.this, "Failed to upload!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "Select File"), REQ);
    }
    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK) {
            pdfData = data.getData();

            if(pdfData.toString().startsWith("content://")){
                Cursor cursor = null;
                try {
                    cursor = UgPostInsiderActivity.this.getContentResolver().query(pdfData,null,null,null,null);
                    if(cursor != null && cursor.moveToFirst()){
                        pdfName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }else if (pdfData.toString().startsWith("file://")){
                pdfName = new File(pdfData.toString()).getName();
            }
            addPdf.setText(pdfName);
        }
    }
    private void uploadPdf() {
        pd.setTitle("Please wait..");
        pd.setMessage("Uploading Pdf..");
        pd.show();

        StorageReference reference = storageReference.child("pdf/" + pdfName + "-" + System.currentTimeMillis() + ".pdf");

        reference.putFile(pdfData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                uriTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri uri = task.getResult();
                        downloadUrl = String.valueOf(uri);
                        uploadData(String.valueOf(uri));
                        pd.dismiss();

                        // Call postUG() after successful upload and getting downloadUrl

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(UgPostInsiderActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }



}
