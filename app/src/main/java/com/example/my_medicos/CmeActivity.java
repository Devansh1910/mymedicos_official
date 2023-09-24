package com.example.my_medicos;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
public class CmeActivity extends AppCompatActivity {

    String field1;
    String  field2,email,Date,Time1,venue;
    String field3;

    String field4;
    FloatingActionButton floatingActionButton;
    Button OK;
    RecyclerView recyclerView;
    RecyclerView recyclerView3;
    RecyclerView recyclerView2;
    RecyclerView recyclerView4;
    private ViewPager2 pager;
    private TabLayout tabLayout;
    private Spinner specialitySpinner;
    String selectedMode1,selectedMode2,selectedMode;
    private FirebaseFirestore db;
    private Spinner subspecialitySpinner;
    private ArrayAdapter<CharSequence> specialityAdapter;
    private ArrayAdapter<CharSequence> subspecialityAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    Toolbar toolbar;

    // Define subspecialities for each speciality
    private final String[][] subspecialities = subSpecialitiesData.subspecialities;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cme);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override

            public void onRefresh() {
                // Implement your refresh logic here, e.g., fetching new data
                fetchData();

                // After fetching data, update the UI

                // Complete the refresh animation
                swipeRefreshLayout.setRefreshing(false);
            }

        });




        toolbar = findViewById(R.id.cmetoolbar);

        pager = findViewById(R.id.view_pager);
//        tabLayout = findViewById(R.id.tabLayout);
        specialitySpinner = findViewById(R.id.speciality);
        subspecialitySpinner = findViewById(R.id.subspeciality);

        specialityAdapter = ArrayAdapter.createFromResource(this,
                R.array.speciality, android.R.layout.simple_spinner_item);
        specialityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specialitySpinner.setAdapter(specialityAdapter);


        subspecialityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        subspecialityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subspecialitySpinner.setAdapter(subspecialityAdapter);
        // Initially, hide the subspeciality spinner
        subspecialitySpinner.setVisibility(View.GONE);

        Spinner modeSpinner = findViewById(R.id.mode);
        ArrayAdapter<CharSequence> modeAdapter = ArrayAdapter.createFromResource(this,
                R.array.mode, android.R.layout.simple_spinner_item);
        modeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modeSpinner.setAdapter(modeAdapter);
        modeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedMode = modeSpinner.getSelectedItem().toString();



            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        specialitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedMode1 = specialitySpinner.getSelectedItem().toString();



            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
                            selectedMode2 = subspecialitySpinner.getSelectedItem().toString();




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
        selectedMode1 = specialitySpinner.getSelectedItem().toString();




//        String subspeciality = subspecialitySpinner.getSelectedItem().toString();
//        String speciality = specialitySpinner.getSelectedItem().toString();
//
        OK = findViewById(R.id.ok);

        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View a) {
                Context context = a.getContext();

                Intent i = new Intent(context, CmeSearchActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("Speciality",selectedMode1 );
                i.putExtra("SubSpeciality",selectedMode2);
                i.putExtra("Mode",selectedMode);


                context.startActivity(i);
            }
        });


        pager.setAdapter(new ViewPagerAdapter(this));
        fetchData();


    }
    public void fetchData() {
        recyclerView = findViewById(R.id.cme_recyclerview1);
        setSupportActionBar(toolbar);
        db = FirebaseFirestore.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        floatingActionButton = findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CmeActivity.this, PostCmeActivity.class);
                startActivity(i);
            }
        });
        recyclerView = findViewById(R.id.cme_recyclerview1);
        List<cmeitem1> items = new ArrayList<>();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Query query=db.collection("CME").orderBy("Time", Query.Direction.DESCENDING);

            query.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task ) {

                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    Map<String, Object> dataMap = document.getData();
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                                    String Time = document.getString("Selected Time");

                                    //
                                    LocalTime parsedTime = null;
                                    try {
                                        // Parse the time string into a LocalTime object
                                        parsedTime = null;
                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                            parsedTime = LocalTime.parse(Time, formatter);
                                            Log.d("vivek","0");
                                        }

                                        // Display the parsed time
                                        System.out.println("Parsed Time: " + parsedTime);
                                    } catch (java.time.format.DateTimeParseException e) {
                                        // Handle parsing error, e.g., if the input string is in the wrong format
                                        System.err.println("Error parsing time: " + e.getMessage());
                                        Log.d("vivek","Time");
                                    }
                                    LocalTime currentTime = null;
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                        currentTime = LocalTime.now();
                                        LocalDate currentDate = LocalDate.now();
                                    }


                                    int r = parsedTime.compareTo(currentTime);

                                    Log.d("vivek", String.valueOf(r));
                                    if (r<=0) {
                                        field3 = ((String) dataMap.get("CME Title"));
                                        field4 = ((String) dataMap.get("CME Presenter"));
                                        field1 = (String) dataMap.get("User");
                                        field2 = ((String) dataMap.get("Speciality"));
                                        String field5=((String) dataMap.get("CME Organiser"));


                                        cmeitem1 c = new cmeitem1(field1, field2,  5,field3,field4,field5);
                                        items.add(c);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplication(), LinearLayoutManager.HORIZONTAL, false));
                                        recyclerView.setAdapter(new MyAdapter2(getApplication(), items));

                                    } else {

                                    }
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
        recyclerView4 = findViewById(R.id.cme_recyclerview4);
        List<cmeitem4> items1 = new ArrayList<>();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Query query=db.collection("CME").orderBy("Time", Query.Direction.DESCENDING);

            query.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task ) {

                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    Map<String, Object> dataMap = document.getData();
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                                    String Time = document.getString("Selected Time");
                                    Log.d("vivek",Time);
                                    //
                                    LocalTime parsedTime = null;
                                    try {
                                        // Parse the time string into a LocalTime object
                                        parsedTime = null;
                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                            parsedTime = LocalTime.parse(Time, formatter);
                                            Log.d("vivek","0");
                                        }

                                        // Display the parsed time
                                        System.out.println("Parsed Time: " + parsedTime);
                                    } catch (java.time.format.DateTimeParseException e) {
                                        // Handle parsing error, e.g., if the input string is in the wrong format
                                        System.err.println("Error parsing time: " + e.getMessage());
                                        Log.d("vivek","Time");
                                    }
                                    LocalTime currentTime = null;
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                        currentTime = LocalTime.now();
                                        LocalDate currentDate = LocalDate.now();
                                    }


                                    int r = parsedTime.compareTo(currentTime);

                                    Log.d("vivek", String.valueOf(r));
                                    if (r<=0) {
                                        field3 = ((String) dataMap.get("CME Title"));
                                        field4 = ((String) dataMap.get("CME Presenter"));
                                        field1 = (String) dataMap.get("CME Organiser");
                                        field2 = ((String) dataMap.get("Speciality"));
                                        cmeitem4 c = new cmeitem4(field1, field2,  5,field3,field4);
                                        items1.add(c);
                                        recyclerView4.setLayoutManager(new LinearLayoutManager(getApplication(), LinearLayoutManager.HORIZONTAL, false));
                                        recyclerView4.setAdapter(new MyAdapter1(getApplication(), items1));


                                    } else {

                                    }

                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
        // Add more
        // Add more items here

        recyclerView3 = findViewById(R.id.recyclerview3);
        List<cmeitem3> item = new ArrayList<>();

        //.....
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Query query=db.collection("CME").orderBy("Time", Query.Direction.DESCENDING);
            query
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task ) {

                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    Log.d(TAG, document.getId() + " => " + document.getData());

                                    Map<String, Object> dataMap = document.getData();
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                                    DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                    String Time = document.getString("Selected Time");
                                    String date =  document.getString("Selected Date");
                                    Log.d("vivek", Time);
//
                                    LocalTime parsedTime = null;
                                    LocalDate parsedDate = null;
                                    try {
                                        // Parse the time string into a LocalTime object
                                        parsedTime = null;
                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                            parsedTime = LocalTime.parse(Time, formatter);



                                            Log.d("vivek", "0");
                                        }

                                        // Display the parsed time
                                        System.out.println("Parsed Time: " + parsedTime);
                                    } catch (java.time.format.DateTimeParseException e) {
                                        // Handle parsing error, e.g., if the input string is in the wrong format
                                        System.err.println("Error parsing time: " + e.getMessage());
                                        Log.d("vivek", "Time");
                                    }
                                    parsedDate = LocalDate.parse(date, formatter1);
                                    LocalTime currentTime = null;
                                    LocalDate currentDate = null;
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                        currentTime = LocalTime.now();
                                        currentDate = LocalDate.now();
                                    }


                                    int r = parsedTime.compareTo(currentTime);
                                    int r1 = parsedDate.compareTo(currentDate);

                                    Log.d("vivek1", String.valueOf(r1));
                                    if ((r > 0)&&(r1<=0)) {
                                        field1 = (String) dataMap.get("CME Presenter");
                                        field2 = ((String) dataMap.get("CME Title"));
                                        Log.d(TAG, (String) dataMap.get("Speciality"));
                                        String combinedDateTime = document.getString("Selected Date");

//                                        Log.d("vivek", combinedDateTime);

                                        String cmetime = document.getString("Selected Time");

                                        cmeitem3 c = new cmeitem3(combinedDateTime, cmetime, field2, field1);

                                        item.add(c);

                                        recyclerView3.setLayoutManager(new LinearLayoutManager(getApplication()));
                                        recyclerView3.setAdapter(new MyAdapter3(getApplication(), item));
                                    } else {

                                    }
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
        //.....
        recyclerView2 = findViewById(R.id.recyclerview2);
        List<cmeitem2> myitem = new ArrayList<cmeitem2>();
        //......
        db.collection("CME")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task ) {


                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Map<String, Object> dataMap = document.getData();





                                recyclerView2.setLayoutManager(new LinearLayoutManager(getApplication(), LinearLayoutManager.HORIZONTAL, false));
                                recyclerView2.setAdapter(new MyAdapter4(getApplication(), myitem));

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        //......
    }

    class ViewPagerAdapter extends FragmentStateAdapter {

        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new TodaysFragment();
                case 1:
                    return new UpcomingFragment();
                case 2:
                    return new PastFragment();
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.home) {
            Toast.makeText(this, "Chat clicked", Toast.LENGTH_SHORT).show();
        }
        else{
            Intent i = new Intent(CmeActivity.this, HomeActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
