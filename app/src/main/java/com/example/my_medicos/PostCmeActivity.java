package com.example.my_medicos;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class PostCmeActivity extends AppCompatActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    String current = user.getEmail();

    EditText cmetitle, cmeorg, cmepresenter, cmevenu, virtuallink, cme_place;

    Button postcme;
    String selectedTime,selectedDate;

    public FirebaseDatabase db = FirebaseDatabase.getInstance();

    FirebaseFirestore dc = FirebaseFirestore.getInstance();

    public DatabaseReference cmeref = db.getReference().child("CME's");

    private ProgressDialog progressDialog;

    private static final int MAX_CHARACTERS = 1000;


    private Button btnDatePicker, btnTimePicker;
    private TextView tvDate, tvTime;
    private Calendar calendar;
    private SimpleDateFormat dateFormat, timeFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_cme);
        btnDatePicker = findViewById(R.id.btnDatePicker);
        btnTimePicker = findViewById(R.id.btnTimePicker);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());





        progressDialog = new ProgressDialog(this);

        Spinner spinner2 = findViewById(R.id.cmemode);
        ArrayAdapter<CharSequence> myadapter = ArrayAdapter.createFromResource(this,
                R.array.mode, android.R.layout.simple_spinner_item);
        myadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(myadapter);

        Spinner spinner = findViewById(R.id.cmespeciality);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.speciality, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        cmetitle = findViewById(R.id.cme_title);
        cmeorg = findViewById(R.id.cme_organiser);
        cmeorg.setText(current);
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
                    Toast.makeText(PostCmeActivity.this, "Character limit exceeded (1000 characters max)", Toast.LENGTH_SHORT).show();
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
    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        selectedDate = dateFormat.format(calendar.getTime());
                        tvDate.setText("Selected Date: " + selectedDate);
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
                        selectedTime = timeFormat.format(calendar.getTime());
                        tvTime.setText("Selected Time: " + selectedTime);

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

        if (!link.startsWith("https://")) {
            virtuallink.setError("Not a Valid Link");
            virtuallink.setTextColor(Color.RED);
            return;
        } else {
            virtuallink.setTextColor(Color.DKGRAY);
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
            cmepresenter.setError("Email Required");
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

        HashMap<String, Object> usermap = new HashMap<>();
        usermap.put("CME Title", title);
        usermap.put("CME Organiser", organiser);
        usermap.put("CME Presenter", presenter);
        usermap.put("CME Venue", venu);
        usermap.put("Virtual Link", link);
        usermap.put("CME Place", place);
        usermap.put("User", current);
        usermap.put("Date", formattedDate);
        usermap.put("Time", formattedTime); // Store EventTime as "Time"
        usermap.put("Mode", mode);
        usermap.put("Speciality", speciality);
        usermap.put("CME Date",selectedDate);
        usermap.put("CME Time",selectedTime);


        progressDialog.setMessage("Posting...");
        progressDialog.show();

        cmeref.push().setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();

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

