package com.example.my_medicos.activities.cme;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.my_medicos.activities.cme.fragment.OngoingFragment;
import com.example.my_medicos.activities.cme.fragment.PastFragment;
import com.example.my_medicos.R;
import com.example.my_medicos.activities.cme.fragment.UpcomingFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;

public class CmeSearchActivity extends AppCompatActivity {
    String field1;
    String  field2,email,Date,Time1,venue;
    String field3;

    String field4;
    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    RecyclerView recyclerView3;
    RecyclerView recyclerView2;
    RecyclerView recyclerView4;
    private ViewPager2 pager;
    private TabLayout tabLayout;
    private Spinner specialitySpinner;
    private FirebaseFirestore db;
    private Spinner subspecialitySpinner;
    private ArrayAdapter<CharSequence> specialityAdapter;
    private ArrayAdapter<CharSequence> subspecialityAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    String Mode,Speciality,SubSpeciality;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_cme_search);
        Log.d("123456","5646");
       Speciality=getIntent().getExtras().getString("Speciality");
        SubSpeciality=getIntent().getExtras().getString("SubSpeciality");
        Mode=getIntent().getExtras().getString("Mode");
        Log.d(Speciality,"SubSpeciality");
        Toolbar toolbar = findViewById(R.id.toolbar); // Replace with your Toolbar's ID
        setSupportActionBar(toolbar); // Set the Toolbar as the app bar

        getSupportActionBar().setTitle(Speciality); // Set the main title
        getSupportActionBar().setSubtitle(SubSpeciality);

        fetchData();


    }
    public void fetchData() {
        recyclerView = findViewById(R.id.cme_recyclerview1);

//        Log.d("abc",subspeciality);



        db = FirebaseFirestore.getInstance();




//        recyclerView = findViewById(R.id.cme_recyclerview1);
//        List<cmeitem1> items = new ArrayList<>();
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            Query query=db.collection("CME").orderBy("Time", Query.Direction.DESCENDING);
//
//            query.get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task ) {
//
//                            if (task.isSuccessful()) {
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                    Log.d(TAG, document.getId() + " => " + document.getData());
//                                    Map<String, Object> dataMap = document.getData();
//                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
//                                    String mode=document.getString("Mode");
//                                    String speciality=document.getString("Speciality");
//                                    String subspeciality=document.getString("SubSpeciality");
//                                    String Time = document.getString("Selected Time");
//
//                                    Log.d(mode, Mode);
//                                    Log.d(speciality, Speciality);
//                                    Log.d(subspeciality, SubSpeciality);
//                                    int d=mode.compareTo(Mode);
//                                    int f=speciality.compareTo(Speciality);
//                                    int b=subspeciality.compareTo(SubSpeciality);
//                                    Log.d(speciality, String.valueOf(f));
//                                    if (d==0) {
//                                        if(f==0){
//
//                                            if (b==0) {
//
//
//                                                //
//                                                LocalTime parsedTime = null;
//                                                try {
//                                                    // Parse the time string into a LocalTime object
//                                                    parsedTime = null;
//                                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                                                        parsedTime = LocalTime.parse(Time, formatter);
//                                                        Log.d("vivek", "30");
//                                                    }
//
//                                                    // Display the parsed time
//                                                    System.out.println("Parsed Time: " + parsedTime);
//                                                } catch (
//                                                        java.time.format.DateTimeParseException e) {
//                                                    // Handle parsing error, e.g., if the input string is in the wrong format
//                                                    System.err.println("Error parsing time: " + e.getMessage());
//                                                    Log.d("vivek", "Time");
//                                                }
//                                                LocalTime currentTime = null;
//                                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                                                    currentTime = LocalTime.now();
//                                                    LocalDate currentDate = LocalDate.now();
//                                                }
//
//
//                                                int r = parsedTime.compareTo(currentTime);
//
//                                                Log.d("vivek", String.valueOf(r));
//                                                if (r <= 0) {
//                                                    field3 = ((String) dataMap.get("CME Title"));
//                                                    field4 = ((String) dataMap.get("CME Presenter"));
//                                                    field1 = (String) dataMap.get("User");
//                                                    field2 = ((String) dataMap.get("Speciality"));
//                                                    String field5 = ((String) dataMap.get("CME Organiser"));
//                                                    String Date=((String) dataMap.get("Selected Date"));
//                                                    String time =((String) dataMap.get("Selected Time"));
//
//
//                                                    cmeitem1 c = new cmeitem1(field1, field2, Date, field3, field4, field5,5,time);
//                                                    items.add(c);
//                                                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplication(), LinearLayoutManager.HORIZONTAL, false));
//                                                    recyclerView.setAdapter(new MyAdapter2(getApplication(), items));
//
//                                                } else {
//
//                                                }
//                                            }
//
//                                        }
//                                    }
//                                }
//                            } else {
//                                Log.d(TAG, "Error getting documents: ", task.getException());
//                            }
//                                }
//
//                    });
//        }
//        recyclerView4 = findViewById(R.id.cme_recyclerview4);
//        List<cmeitem4> items1 = new ArrayList<>();
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            Query query=db.collection("CME").orderBy("Time", Query.Direction.DESCENDING);
//
//            query.get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task ) {
//
//                            if (task.isSuccessful()) {
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//
//                                    Map<String, Object> dataMap = document.getData();
//                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
//                                    String Time = document.getString("Selected Time");
//                                    Log.d("vivek",Time);
//                                    String mode=document.getString("Mode");
//                                    String speciality=document.getString("Speciality");
//                                    String subspeciality=document.getString("SubSpeciality");
//
//                                    int d=mode.compareTo(Mode);
//                                    int f=speciality.compareTo(Speciality);
//                                    int b=subspeciality.compareTo(SubSpeciality);
//                                    Log.d(speciality, String.valueOf(b));
//                                    if (d==0) {
//                                        if (f==0) {
//
//                                            if (b == 0) {
//                                                //
//                                                LocalTime parsedTime = null;
//                                                try {
//                                                    // Parse the time string into a LocalTime object
//                                                    parsedTime = null;
//                                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                                                        parsedTime = LocalTime.parse(Time, formatter);
//                                                        Log.d("vivek", "0");
//                                                    }
//
//                                                    // Display the parsed time
//                                                    System.out.println("Parsed Time: " + parsedTime);
//                                                } catch (
//                                                        java.time.format.DateTimeParseException e) {
//                                                    // Handle parsing error, e.g., if the input string is in the wrong format
//                                                    System.err.println("Error parsing time: " + e.getMessage());
//                                                    Log.d("vivek", "Time");
//                                                }
//                                                LocalTime currentTime = null;
//                                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                                                    currentTime = LocalTime.now();
//                                                    LocalDate currentDate = LocalDate.now();
//                                                }
//
//
//                                                int r = parsedTime.compareTo(currentTime);
//
//                                                Log.d("vivek", String.valueOf(r));
//                                                if (r <= 0) {
//                                                    field3 = ((String) dataMap.get("CME Title"));
//                                                    field4 = ((String) dataMap.get("CME Presenter"));
//                                                    field1 = (String) dataMap.get("CME Organiser");
//                                                    field2 = ((String) dataMap.get("Speciality"));
//                                                    String time =((String) dataMap.get("Selected Time"));
//                                                    cmeitem4 c = new cmeitem4(field1, field2, Date, field3, field4,5,time);
//                                                    items1.add(c);
//                                                    recyclerView4.setLayoutManager(new LinearLayoutManager(getApplication(), LinearLayoutManager.HORIZONTAL, false));
//                                                    recyclerView4.setAdapter(new MyAdapter1(getApplication(), items1));
//
//
//                                                } else {
//
//                                                }
//                                            }
//                                        }
//                                    }
//
//                                }
//                            } else {
//                                Log.d(TAG, "Error getting documents: ", task.getException());
//                            }
//                        }
//                    });
//        }
//        // Add more
//        // Add more items here
//
//        recyclerView3 = findViewById(R.id.recyclerview3);
//        List<cmeitem3> item = new ArrayList<>();
//
//        //.....
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            Query query=db.collection("CME").orderBy("Time", Query.Direction.DESCENDING);
//            query
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task ) {
//
//                            if (task.isSuccessful()) {
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//
//                                    Log.d(TAG, document.getId() + " => " + document.getData());
//
//                                    Map<String, Object> dataMap = document.getData();
//                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
//                                    DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//                                    String Time = document.getString("Selected Time");
//                                    String date =  document.getString("Selected Date");
//                                    Log.d("vivek", Time);
//                                    String mode=document.getString("Mode");
//                                    String speciality=document.getString("Speciality");
//                                    String subspeciality=document.getString("SubSpeciality");
//
//                                    int d=mode.compareTo(Mode);
//                                    int f=speciality.compareTo(Speciality);
//                                    int b=subspeciality.compareTo(SubSpeciality);
//                                    Log.d(speciality, String.valueOf(b));
//                                    if (d==0) {
//                                        if (f==0){
//
//                                        if (b == 0) {
////
//                                            LocalTime parsedTime = null;
//                                            LocalDate parsedDate = null;
//                                            try {
//                                                // Parse the time string into a LocalTime object
//                                                parsedTime = null;
//                                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                                                    parsedTime = LocalTime.parse(Time, formatter);
//
//
//                                                    Log.d("vivek", "0");
//                                                }
//
//                                                // Display the parsed time
//                                                System.out.println("Parsed Time: " + parsedTime);
//                                            } catch (java.time.format.DateTimeParseException e) {
//                                                // Handle parsing error, e.g., if the input string is in the wrong format
//                                                System.err.println("Error parsing time: " + e.getMessage());
//                                                Log.d("vivek", "Time");
//                                            }
//                                            parsedDate = LocalDate.parse(date, formatter1);
//                                            LocalTime currentTime = null;
//                                            LocalDate currentDate = null;
//                                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                                                currentTime = LocalTime.now();
//                                                currentDate = LocalDate.now();
//                                            }
//
//
//                                            int r = parsedTime.compareTo(currentTime);
//                                            int r1 = parsedDate.compareTo(currentDate);
//
//                                            Log.d("vivek1", String.valueOf(r1));
//                                            if ((r > 0) && (r1 <= 0)) {
//                                                field1 = (String) dataMap.get("CME Presenter");
//                                                field2 = ((String) dataMap.get("CME Title"));
//                                                Log.d(TAG, (String) dataMap.get("Speciality"));
//                                                String combinedDateTime = document.getString("Selected Date");
//
////                                        Log.d("vivek", combinedDateTime);
//
//                                                String cmetime = document.getString("Selected Time");
//
//                                                cmeitem3 c = new cmeitem3(combinedDateTime, cmetime, field2, field1);
//
//                                                item.add(c);
//
//                                                recyclerView3.setLayoutManager(new LinearLayoutManager(getApplication()));
//                                                recyclerView3.setAdapter(new MyAdapter3(getApplication(), item));
//                                            } else {
//
//                                            }
//                                        }
//                                        }
//                                    }
//                                }
//                            } else {
//                                Log.d(TAG, "Error getting documents: ", task.getException());
//                            }
//
//                        }
//                    });
//        }
//        //.....
//        recyclerView2 = findViewById(R.id.recyclerview2);
//        List<cmeitem2> myitem = new ArrayList<cmeitem2>();
//        //......
//        db.collection("CME")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task ) {
//
//
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//                                Map<String, Object> dataMap = document.getData();
//
//
//
//
//
//                                recyclerView2.setLayoutManager(new LinearLayoutManager(getApplication(), LinearLayoutManager.HORIZONTAL, false));
//                                recyclerView2.setAdapter(new MyAdapter4(getApplication(), myitem));
//
//                            }
//                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
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
                    return new OngoingFragment();
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

}
