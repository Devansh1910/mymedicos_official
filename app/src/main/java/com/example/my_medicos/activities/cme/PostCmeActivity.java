package com.example.my_medicos.activities.cme;

import static com.example.my_medicos.list.subSpecialitiesData.subspecialities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.OpenableColumns;
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
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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

public class PostCmeActivity extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    String current = user.getPhoneNumber();
    String field3,field4;
    EditText cmetitle, cmeorg, cmepresenter, cmevenu, virtuallink, cme_place;
    Button postcme;
    public FirebaseDatabase db = FirebaseDatabase.getInstance();
    FirebaseFirestore dc = FirebaseFirestore.getInstance();
    private Spinner subspecialitySpinner;
    private Spinner specialitySpinner;
    String subspecialities1;
    public DatabaseReference cmeref = db.getReference().child("CME's");
    private ProgressDialog progressDialog;
    private static final int MAX_CHARACTERS = 1000;
    private EditText etName, etClass, etPhoneNumber;
    private Button btnDatePicker, btnTimePicker;

    private TextView tvDate, tvTime;
    private Calendar calendar;
    private SimpleDateFormat dateFormat, timeFormat;
    static final int REQ = 1;
    private Uri pdfData;
    private DatabaseReference databasereference;
    private StorageReference storageReference;
    private TextView addPdf,uploadPdfBtn;
    String downloadUrl = "";
    private ProgressDialog pd;
    private String pdfName;
    private ArrayAdapter<CharSequence> specialityAdapter;
    private ArrayAdapter<CharSequence> subspecialityAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_cme);
        addPdf = findViewById(R.id.addPdf);
        //..............
        databasereference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        pd = new ProgressDialog(this);

        addPdf = findViewById(R.id.addPdf);

        uploadPdfBtn = findViewById(R.id.uploadpdfbtn);

        addPdf.setOnClickListener(view -> {
            openGallery();
        });

        uploadPdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pdfData == null){
                    Toast.makeText(PostCmeActivity.this, "Select a Document", Toast.LENGTH_SHORT).show();
                }else{
                    uploadPdf();
                }
            }
        });

        //..................

        btnDatePicker = findViewById(R.id.btnDatePicker);
        btnTimePicker = findViewById(R.id.btnTimePicker);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        progressDialog = new ProgressDialog(this);

        specialitySpinner = findViewById(R.id.cmespeciality);
        subspecialitySpinner = findViewById(R.id.cmesubspeciality);

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
                            cmeorg.setText(field4);
                        }


                        // Handle the retrieved data here

                        // You can access data using document.getData() and perform necessary actions
                    }
                } else {
                    // Handle the error
                    Toast.makeText(PostCmeActivity.this, "Error fetching data from Firebase Firestore", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Spinner modeSpinner = findViewById(R.id.cmemode);
        ArrayAdapter<CharSequence> modeAdapter = ArrayAdapter.createFromResource(this,
                R.array.mode, android.R.layout.simple_spinner_item);
        modeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modeSpinner.setAdapter(modeAdapter);

        cmetitle = findViewById(R.id.cme_title);
        cmeorg = findViewById(R.id.cme_organiser);

        cmeorg.setEnabled(false);
        cmeorg.setTextColor(Color.parseColor("#80000000"));
        cmeorg.setBackgroundResource(R.drawable.rounded_edittext_background);
        cmepresenter = findViewById(R.id.cme_presenter);
        cmevenu = findViewById(R.id.cme_venu);
        virtuallink = findViewById(R.id.cme_virtuallink);
        cme_place = findViewById(R.id.cme_place);

        postcme = findViewById(R.id.post_btn);


        // Initialize the charCount TextView
        TextView charCount = findViewById(R.id.char_counter);

        // Add a TextWatcher to the cmevenu EditText for character counting and button enabling/disabling
        cmevenu.addTextChangedListener(new TextWatcher() {
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
                    postcme.setEnabled(false); // Disable the "Post" button
                    // Add a Toast message to notify the user
                    Toast.makeText(PostCmeActivity.this, "Character limit exceeded (2000 characters max)", Toast.LENGTH_SHORT).show();
                } else {
                    charCount.setTextColor(Color.DKGRAY);
                    postcme.setEnabled(true); // Enable the "Post" button
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // Disable the "Post" button initially
        postcme.setEnabled(false);

        postcme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    postCme();
                }
            }
        });

        // Set up the OnItemSelectedListener for cmemode Spinner
        Spinner cmemodeSpinner = findViewById(R.id.cmemode);
        cmemodeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedMode = cmemodeSpinner.getSelectedItem().toString();

                if (selectedMode.equals("Online")) {
                    // Show the virtual link EditText and hide the place EditText
                    virtuallink.setVisibility(View.VISIBLE);
                    cme_place.setVisibility(View.GONE);
                } else if (selectedMode.equals("Offline")) {
                    // Show the place EditText and hide the virtual link EditText
                    virtuallink.setVisibility(View.GONE);
                    cme_place.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });
    }
    private void uploadPdf() {
        pd.setTitle("Please wait..");
        pd.setMessage("Uploading Pdf..");
        pd.show();
        StorageReference reference = storageReference.child("pdf/"+pdfName+"-"+System.currentTimeMillis()+".pdf");
        reference.putFile(pdfData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri uri = uriTask.getResult();
                uploadData(String.valueOf(uri));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(PostCmeActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
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
                pd.dismiss();
                Toast.makeText(PostCmeActivity.this, "Pdf Uploaded Successfully", Toast.LENGTH_SHORT).show();
                cmetitle.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(PostCmeActivity.this, "Failed to upload!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //.................................

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
                    cursor = PostCmeActivity.this.getContentResolver().query(pdfData,null,null,null,null);
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


    //................................

    private void listFilesInDirectory(Uri treeUri) {
        String treeDocumentId = DocumentsContract.getTreeDocumentId(treeUri);
        Uri docUri = DocumentsContract.buildDocumentUriUsingTree(treeUri, treeDocumentId);
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String selectedDate = dateFormat.format(calendar.getTime());
                        tvDate.setText(selectedDate);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        String selectedTime = timeFormat.format(calendar.getTime());
                        tvTime.setText(selectedTime);
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true // 24-hour format
        );
        timePickerDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void postCme() {
        String title = cmetitle.getText().toString().trim();
        String organiser = cmeorg.getText().toString().trim();
        String presenter = cmepresenter.getText().toString().trim();
        String venu = cmevenu.getText().toString().trim();
        String link = virtuallink.getText().toString().trim();
        String place = cme_place.getText().toString().trim();

        if (!link.matches("https://us04web\\.zoom\\.us/.*")) {
            virtuallink.setError("Invalid link format");
            return;
        } else {
            virtuallink.setError(null); // Clear any previous error
        }

        if (TextUtils.isEmpty(title)) {
            cmetitle.setError("Title Required");
            return;
        }
        if (TextUtils.isEmpty(organiser)) {
            cmeorg.setError("Organizer Required");
            return;
        }
        if (TextUtils.isEmpty(presenter)) {
            cmepresenter.setError("Phone Number Required");
            return;
        }
        if (TextUtils.isEmpty(venu)) {
            cmevenu.setError("Email Required");
            return;
        }

        if (venu.length() > MAX_CHARACTERS) {
            // Display an error message if the content exceeds the limit
            cmevenu.setError("Character limit exceeded");
            return;
        } else {
            cmevenu.setError(null); // Clear any previous error
        }

        // Get the selected mode from the spinner
        Spinner cmemodeSpinner = findViewById(R.id.cmemode);
        String mode = cmemodeSpinner.getSelectedItem().toString();

        // Get the selected speciality from the spinner
        Spinner cmespecialitySpinner = findViewById(R.id.cmespeciality);
        String speciality = cmespecialitySpinner.getSelectedItem().toString();

        // Get current date and time

        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedDate = currentDateTime.format(dateFormatter);
        String formattedTime = currentDateTime.format(timeFormatter);

        // Get the selected date and time
        String selectedDate = tvDate.getText().toString();
        String selectedTime = tvTime.getText().toString();

        HashMap<String, Object> usermap = new HashMap<>();
        usermap.put("CME Title", title);
        usermap.put("CME Organiser", field4);
        usermap.put("CME Presenter", presenter);
        usermap.put("CME Venue", venu);
        usermap.put("Virtual Link", link);
        usermap.put("CME Place", place);
        usermap.put("User", current);
        usermap.put("Date", formattedDate);
        usermap.put("Time",formattedTime);
        usermap.put("Mode", mode);
        usermap.put("Speciality", speciality);
        usermap.put("SubSpeciality",subspecialities1);
        usermap.put("Selected Date", selectedDate); // Add selected date
        usermap.put("Selected Time", selectedTime); // Add selected time
        usermap.put("endtime",null);

        progressDialog.setMessage("Posting...");
        progressDialog.show();

        cmeref.push().setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();

                if (task.isSuccessful()) {
                    // Get the generated document ID
                    String generatedDocId = cmeref.push().getKey();

                    // Add the document ID to the usermap
                    usermap.put("documentId", generatedDocId);

                    // Add the data to the "CME" collection along with the document ID
                    dc.collection("CME").document(generatedDocId).set(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(PostCmeActivity.this, "Posted Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PostCmeActivity.this, "Task Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(PostCmeActivity.this, "Task Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}