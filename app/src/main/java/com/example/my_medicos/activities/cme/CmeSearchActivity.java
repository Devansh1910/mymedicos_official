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

import com.example.my_medicos.R;
import com.example.my_medicos.activities.cme.fragment.OngoingFragmentSearch;
import com.example.my_medicos.activities.cme.fragment.PastFragmentSearch;
import com.example.my_medicos.activities.cme.fragment.UpcomingFragmentSearch;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.FirebaseFirestore;

public class CmeSearchActivity extends AppCompatActivity {
    String field1;
    String  field2,email,Date,Time1,venue;
    String field3;

    String field4;
    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    private ViewPager2 pager,viewpager;
    RecyclerView recyclerView3;
    RecyclerView recyclerView2;
    RecyclerView recyclerView4;

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
        viewpager = findViewById(R.id.view_pager1);

        Log.d("123456","5646");
       Speciality=getIntent().getExtras().getString("Speciality");
        SubSpeciality=getIntent().getExtras().getString("SubSpeciality");
        Mode=getIntent().getExtras().getString("Mode");
        Log.d(Speciality,"SubSpeciality");
        Toolbar toolbar = findViewById(R.id.toolbar); // Replace with your Toolbar's ID
        setSupportActionBar(toolbar); // Set the Toolbar as the app bar

        getSupportActionBar().setTitle(Speciality); // Set the main title
        getSupportActionBar().setSubtitle(SubSpeciality);
        viewpager.setAdapter(new ViewPagerAdapter(this, Speciality, SubSpeciality, Mode));
        pager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tablayout);
        new TabLayoutMediator(tabLayout, viewpager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Ongoing");
                    break;
                case 1:
                    tab.setText("Upcoming");
                    break;
                case 2:
                    tab.setText("Past");
                    break;
            }
        }).attach();




    }
//    public void fetchData() {
//        recyclerView = findViewById(R.id.cme_recyclerview1);
//
////        Log.d("abc",subspeciality);
//
//
//
//        db = FirebaseFirestore.getInstance();
//
//
//
//
//        recyclerView = findViewById(R.id.cme_recyclerview1);
//      FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            Query query=db.collection("CME").orderBy("Time", Query.Direction.DESCENDING);
//
//            query.get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @SuppressLint("RestrictedApi")
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task ) {
//                            List<cmeitem4> items1 = new ArrayList<>();
//
//                            if (task.isSuccessful()) {
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//
//                                    Map<String, Object> dataMap = document.getData();
//                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
//                                    DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//                                    String Time = document.getString("Selected Time");
//                                    String date =  document.getString("Selected Date");
//
//                                    //
//                                    LocalTime parsedTime = null;
//                                    LocalDate parsedDate=null;
//                                    try {
//                                        // Parse the time string into a LocalTime object
//                                        parsedTime = null;
//                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                            parsedTime = LocalTime.parse(Time, formatter);
//                                            parsedDate = LocalDate.parse(date, formatter1);
//
//                                        }
//
//                                        // Display the parsed time
//                                        System.out.println("Parsed Time: " + parsedTime);
//                                    } catch (DateTimeParseException e) {
//                                        // Handle parsing error, e.g., if the input string is in the wrong format
//                                        System.err.println("Error parsing time: " + e.getMessage());
//
//                                    }
//                                    LocalTime currentTime = null;
//                                    LocalDate currentDate = null;
//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                        currentTime = LocalTime.now();
//                                        currentDate = LocalDate.now();
//                                    }
//
//
//                                    int r = parsedTime.compareTo(currentTime);
//
//                                    Log.d("vivek", String.valueOf(r));
//                                    int r1 = parsedDate.compareTo(currentDate);
//                                    if ((r <= 0) && (r1 == 0)) {
//                                        String field3 = ((String) dataMap.get("CME Title"));
//                                        String field4 = ((String) dataMap.get("CME Presenter"));
//                                        String end=((String) dataMap.get("endtime"));
//                                        String field1 = (String) dataMap.get("CME Organiser");
//                                        String field2;
//                                        field2 = ((String) dataMap.get("Speciality"));
//                                        String subspeciality=((String) dataMap.get("SubSpeciality"));
//                                        String mode=((String) dataMap.get("Mode"));
//                                        String Date=((String) dataMap.get("Selected Date"));
//                                        String time =((String) dataMap.get("Selected Time"));
//                                        String field5=((String ) dataMap.get("User"));
//                                        String documentid=((String) dataMap.get("documentId"));
//                                        int r4=Speciality.compareTo(field2);
//                                        int r3=SubSpeciality.compareTo(subspeciality);
//                                        int r2=Mode.compareTo(mode);
//                                        if (end==null) {
//                                            if ((r2==0)&&(r3==0)&&(r4==0)) {
//                                                cmeitem4 c = new cmeitem4(field1, field2, Date, field3, field4, 5, time, field5, "LIVE", documentid);
//                                                items1.add(c);
//                                            }
//                                        }
//
//
//
//                                    } else {
//
//                                    }
//
//
//                                }
//                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplication(), LinearLayoutManager.HORIZONTAL, true));
//                                recyclerView.setAdapter(new MyAdapter1(getApplication(), items1));
//                            } else {
//                                Log.d(TAG, "Error getting documents: ", task.getException());
//                            }
//                        }
//                    });
//        }
//        recyclerView4 = findViewById(R.id.cme_recyclerview4);
//            List<cmeitem2> myitem = new ArrayList<cmeitem2>();
//
//            //......
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                Query query1=db.collection("CME").orderBy("Time", Query.Direction.DESCENDING);
//
//                query1.get()
//                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<QuerySnapshot> task ) {
//
//                                if (task.isSuccessful()) {
//                                    for (QueryDocumentSnapshot document : task.getResult()) {
//
//                                        Map<String, Object> dataMap = document.getData();
//                                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
//                                        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//                                        String Time = document.getString("Selected Time");
//                                        String date =  document.getString("Selected Date");
//
//                                        //
//                                        LocalTime parsedTime = null;
//                                        LocalDate parsedDate=null;
//                                        try {
//                                            // Parse the time string into a LocalTime object
//                                            parsedTime = null;
//                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                                parsedTime = LocalTime.parse(Time, formatter);
//                                                parsedDate = LocalDate.parse(date, formatter1);
//
//                                            }
//
//                                            // Display the parsed time
//                                            System.out.println("Parsed Time: " + parsedTime);
//                                        } catch (DateTimeParseException e) {
//                                            // Handle parsing error, e.g., if the input string is in the wrong format
//                                            System.err.println("Error parsing time: " + e.getMessage());
//
//                                        }
//                                        LocalTime currentTime = null;
//                                        LocalDate currentDate = null;
//                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                            currentTime = LocalTime.now();
//                                            currentDate = LocalDate.now();
//                                        }
//
//
//                                        int r = parsedTime.compareTo(currentTime);
//
//                                        Log.d("vivek", String.valueOf(r));
//                                        int r1 = parsedDate.compareTo(currentDate);
//                                        if ((r1 <= 0)) {
//                                            String field3 = ((String) dataMap.get("CME Title"));
//                                            String field4 = ((String) dataMap.get("CME Presenter"));
//                                            String field1 = (String) dataMap.get("CME Organiser");
//                                            String field2;
//                                            String field5=((String ) dataMap.get("User"));
//                                            field2 = ((String) dataMap.get("Speciality"));
//                                            String Date=((String) dataMap.get("Selected Date"));
//                                            String time =((String) dataMap.get("Selected Time"));
//                                            String documentid=((String) dataMap.get("documentId"));
//                                            String end=((String) dataMap.get("endtime"));
//                                            String subspeciality=((String) dataMap.get("SubSpeciality"));
//                                            String mode=((String) dataMap.get("Mode"));
//                                            cmeitem2 c = new cmeitem2(field1, field2, Date, field3, field4,5,time,field5,"PAST",documentid);
//                                            int r4=Speciality.compareTo(field2);
//                                            int r3=SubSpeciality.compareTo(subspeciality);
//                                            int r2=Mode.compareTo(mode);
//                                            if (end!=null) {
//                                                if ((r2==0)&&(r3==0)&&(r4==0)) {
//                                                    myitem.add(c);
//
//                                                }
//                                            }
//
//
//
//                                        } else if ((r < 0)&& (r1 == 0)){
//                                            String field3 = ((String) dataMap.get("CME Title"));
//                                            String field4 = ((String) dataMap.get("CME Presenter"));
//                                            String field1 = (String) dataMap.get("CME Organiser");
//                                            String field2;
//                                            String time =((String) dataMap.get("Selected Time"));
//                                            String field5=((String ) dataMap.get("User"));
//                                            field2 = ((String) dataMap.get("Speciality"));
//                                            String Date=((String) dataMap.get("Selected Date"));
//                                            String documentid=((String) dataMap.get("documentId"));
//                                            String end=((String) dataMap.get("endtime"));
//                                            String subspeciality=((String) dataMap.get("SubSpeciality"));
//                                            String mode=((String) dataMap.get("Mode"));
//                                            cmeitem2 c = new cmeitem2(field1, field2, Date, field3, field4,5,time,field5,"PAST",documentid);
//                                            int r4=Speciality.compareTo(field2);
//                                            int r3=SubSpeciality.compareTo(subspeciality);
//                                            int r2=Mode.compareTo(mode);
//                                            if (end!=null) {
//                                                if ((r2==0)&&(r3==0)&&(r4==0)) {
//                                                    myitem.add(c);
//
//                                                }
//                                            }
//
//
//                                        }
//
//                                    }
//                                    recyclerView4.setLayoutManager(new LinearLayoutManager(getApplication(), LinearLayoutManager.VERTICAL, false));
//                                    recyclerView4.setAdapter(new MyAdapter4(getApplication(), myitem));
//
//                                } else {
//                                    Log.d(ContentValues.TAG, "Error getting documents: ", task.getException());
//                                }
//                            }
//                        });
//        }
////        // Add more
////        // Add more items here
////
//        recyclerView3 = findViewById(R.id.recyclerview3);
//                List<cmeitem3> item = new ArrayList<>();
//
//                //.....
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                    Query query3=db.collection("CME").orderBy("Time", Query.Direction.DESCENDING);
//                    ((Query) query3)
//                            .get()
//                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                @Override
//                                public void onComplete(@NonNull Task<QuerySnapshot> task ) {
//
//                                    if (task.isSuccessful()) {
//                                        for (QueryDocumentSnapshot document : task.getResult()) {
//
//                                            Log.d(ContentValues.TAG, document.getId() + " => " + document.getData());
//
//                                            Map<String, Object> dataMap = document.getData();
//                                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
//                                            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//                                            String Time = document.getString("Selected Time");
//                                            String date =  document.getString("Selected Date");
//
////
//                                            LocalTime parsedTime = null;
//                                            LocalDate parsedDate = null;
//                                            try {
//                                                // Parse the time string into a LocalTime object
//                                                parsedTime = null;
//                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                                    parsedTime = LocalTime.parse(Time, formatter);
//
//
//
//                                                    Log.d("vivek", "0");
//                                                }
//
//                                                // Display the parsed time
//                                                System.out.println("Parsed Time: " + parsedTime);
//                                            } catch (DateTimeParseException e) {
//                                                // Handle parsing error, e.g., if the input string is in the wrong format
//                                                System.err.println("Error parsing time: " + e.getMessage());
//                                                Log.d("vivek", "Time");
//                                            }
//                                            parsedDate = LocalDate.parse(date, formatter1);
//                                            LocalTime currentTime = null;
//                                            LocalDate currentDate = null;
//                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                                currentTime = LocalTime.now();
//                                                currentDate = LocalDate.now();
//                                            }
//
//
//                                            int r = parsedTime.compareTo(currentTime);
//                                            int r1 = parsedDate.compareTo(currentDate);
//
//                                            Log.d("vivek1", String.valueOf(r1));
//                                            if ((r1>0)) {
//                                                String field3 = ((String) dataMap.get("CME Title"));
//                                                String field4 = ((String) dataMap.get("CME Presenter"));
//                                                String field1 = (String) dataMap.get("CME Organiser");
//                                                String field2;
//                                                field2 = ((String) dataMap.get("Speciality"));
//                                                String field5=((String ) dataMap.get("User"));
//                                                String Date=((String) dataMap.get("Selected Date"));
//                                                String time =((String) dataMap.get("Selected Time"));
//                                                String subspeciality=((String) dataMap.get("SubSpeciality"));
//                                                String mode=((String) dataMap.get("Mode"));
//                                                String documentid=((String) dataMap.get("documentId"));
//
//                                                cmeitem3 c = new cmeitem3(field1, field2, Date, field3, field4,5,time,field5,"UPCOMING",documentid);
//
//
//                                                int r4=Speciality.compareTo(field2);
//                                                int r3=SubSpeciality.compareTo(subspeciality);
//                                                int r2=Mode.compareTo(mode);
//
//                                                    if ((r2==0)&&(r3==0)&&(r4==0)) {
//                                                        item.add(c);
//                                                    }
//
//
//
//                                            } else {
//                                                if ((r>0)&&(r1==0)){
//                                                    String field3 = ((String) dataMap.get("CME Title"));
//                                                    String field4 = ((String) dataMap.get("CME Presenter"));
//                                                    String field1 = (String) dataMap.get("CME Organiser");
//                                                    String field2;
//                                                    field2 = ((String) dataMap.get("Speciality"));
//                                                    String Date=((String) dataMap.get("Selected Date"));
//                                                    String time =((String) dataMap.get("Selected Time"));
//                                                    String field5=((String ) dataMap.get("User"));
//                                                    String subspeciality=((String) dataMap.get("SubSpeciality"));
//                                                    String mode=((String) dataMap.get("Mode"));
//                                                    String documentid=((String) dataMap.get("documentId"));
//
//                                                    cmeitem3 c = new cmeitem3(field1, field2, Date, field3, field4,5,time,field5,"UPCOMING",documentid);
//
//                                                    int r4=Speciality.compareTo(field2);
//                                                    int r3=SubSpeciality.compareTo(subspeciality);
//                                                    int r2=Mode.compareTo(mode);
//
//                                                    if ((r2==0)&&(r3==0)&&(r4==0)) {
//                                                        item.add(c);
//                                                    }
//
//                                                }
//
//
//                                            }
//
//                                        }
//                                        recyclerView3.setLayoutManager(new LinearLayoutManager(getApplication()));
//                                        recyclerView3.setAdapter(new MyAdapter3(getApplication(), item));
//                                    } else {
//                                        Log.d(ContentValues.TAG, "Error getting documents: ", task.getException());
//                                    }
//                                }
//                            });
//        }
////        //.....
//        recyclerView2 = findViewById(R.id.recyclerview2);
////        List<cmeitem2> myitem = new ArrayList<cmeitem2>();
////        //......
////        db.collection("CME")
////                .get()
////                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
////                    @SuppressLint("RestrictedApi")
////                    @Override
////                    public void onComplete(@NonNull Task<QuerySnapshot> task ) {
////
////
////                        if (task.isSuccessful()) {
////                            for (QueryDocumentSnapshot document : task.getResult()) {
////                                Log.d(TAG, document.getId() + " => " + document.getData());
////                                Map<String, Object> dataMap = document.getData();
////
////
////
////
////
////                                recyclerView2.setLayoutManager(new LinearLayoutManager(getApplication(), LinearLayoutManager.HORIZONTAL, false));
////                                recyclerView2.setAdapter(new MyAdapter4(getApplication(), myitem));
////
////                            }
////                        } else {
////                            Log.d(TAG, "Error getting documents: ", task.getException());
////                        }
////                    }
////                });
//        //......
//    }
    class ViewPagerAdapter extends FragmentStateAdapter {

        private String speciality;
        private String subSpeciality;
        private String mode;

        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, String speciality, String subSpeciality, String mode) {
            super(fragmentActivity);
            this.speciality = speciality;
            this.subSpeciality = subSpeciality;
            this.mode = mode;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return OngoingFragmentSearch.newInstance(speciality, subSpeciality, mode);
                case 1:
                    return UpcomingFragmentSearch.newInstance(speciality, subSpeciality, mode);
                case 2:
                    return PastFragmentSearch.newInstance(speciality, subSpeciality, mode);
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }


}
