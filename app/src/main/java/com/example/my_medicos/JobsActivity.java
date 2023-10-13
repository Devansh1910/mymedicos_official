package com.example.my_medicos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.FirebaseFirestore;


public class JobsActivity extends AppCompatActivity {


    RecyclerView recyclerView1;
    Button OK;
    private Spinner specialitySpinner;
    String selectedMode1,selectedMode2,selectedMode;

    private Spinner subspecialitySpinner;
    private ArrayAdapter<CharSequence> specialityAdapter;
    private ArrayAdapter<CharSequence> subspecialityAdapter;
    private final String[][] subspecialities = subSpecialitiesData.subspecialities;
    MyAdapter6 adapter1;

    FloatingActionButton floatingActionButton;

    private ViewPager2 pager;
    private TabLayout tabLayout;
    RecyclerView recyclerView2;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String speciality,Organiser,Location;
    Toolbar toolbar;
    TabLayout tab;
    ViewPager viewPager;
//    private ActivityYourActivityBinding binding;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);

        toolbar = findViewById(R.id.jobstoolbar);
        setSupportActionBar(toolbar);
        tab=findViewById(R.id.tabLayout);
//        viewPager=findViewById(R.id.view_pager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        floatingActionButton=findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(JobsActivity.this, PostJobActivity.class);
                startActivity(i);
            }
        });
        pager = findViewById(R.id.jobViewpager);
        tabLayout = findViewById(R.id.jobtablayout);
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
                            selectedMode1 = specialitySpinner.getSelectedItem().toString();
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

//
        OK = findViewById(R.id.ok);

        Spinner  spinner2= (Spinner) findViewById(R.id.city);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> myadapter = ArrayAdapter.createFromResource(this,
                R.array.indian_cities, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        myadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner2.setAdapter(myadapter);



        pager.setAdapter(new ViewPagerAdapter(this));

        new TabLayoutMediator(tabLayout, pager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0: tab.setText("LOCUM"); break;
                    case 1: tab.setText("REGULAR"); break;
                }
            }
        }).attach();
//        binding = ActivityYourActivityBinding.inflate(getLayoutInflater()); // Use the binding
//        setContentView(binding.getRoot());
//        recyclerView1 = findViewById(R.id.recyclerview5);
//        List<jobitem1> joblist = new ArrayList<jobitem1>();
//
//
//        FirebaseFirestore dc = FirebaseFirestore.getInstance();
//        //......
//        dc.collection("JOB")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task ) {
//
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//
//
//                                Map<String, Object> dataMap = document.getData();
//                                String speciality = "";
//                                String Organiser =((String) dataMap.get("JOB Organiser"));
//                                String Location =((String) dataMap.get("Location"));
//                                String date=((String) dataMap.get("date"));
//                                String user=((String) dataMap.get("User"));
//
////
//                                jobitem1 c = new jobitem1(speciality, Organiser, Location,date,user);
//                                joblist.add(c);
//                                Log.d("speciality2", speciality);
//                                Log.d("speciality2", Organiser);
//                                Log.d("speciality2", Location);
////
////                                // Pass the joblist to the adapter
////                                Log.d("speciality2", speciality);
//                                recyclerView1.setLayoutManager(new LinearLayoutManager(getApplication(),LinearLayoutManager.VERTICAL, false));
//                                recyclerView1.setAdapter( new MyAdapter6(getApplication(),joblist));
//                                Log.d("speciality2", speciality);
//
//
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
                    return new JobLoccumfragment(); // Display Regular data in this fragment
                case 1:
                    return new Jobregularfragment(); // Display Locum data in this fragment
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

            Intent i = new Intent(JobsActivity.this, JobsHistoryActivity.class);
            startActivity(i);

        return super.onOptionsItemSelected(item);
    }

}