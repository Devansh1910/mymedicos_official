package com.example.my_medicos;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CmeActivity extends AppCompatActivity {
    String field1;
    String  field2;


    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    RecyclerView recyclerView3;

    RecyclerView recyclerView2;

    private ViewPager2 pager;
    private TabLayout tabLayout;


    Toolbar toolbar;
    private FirebaseFirestore db;
    private RecyclerView courseRV;
    private ArrayList<Object> coursesArrayList;

    ProgressBar loadingPB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cme);

        toolbar = findViewById(R.id.cmetoolbar);
        recyclerView = findViewById(R.id.cme_recyclerview1);
        setSupportActionBar(toolbar);



        DatabaseReference mbase;
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
        mbase
                = FirebaseDatabase.getInstance().getReference();

        List<cmeitem1> items = new ArrayList<>();
        db.collection("CME")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task ) {


                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Map<String, Object> dataMap = document.getData();
                                field1 = (String) dataMap.get("CME Organiser");
                                field2 = ((String) dataMap.get("CME Place"));
                                Log.d(TAG,(String) dataMap.get("CME Organiser"));

                                cmeitem1 c = new cmeitem1(field1, field2, 5,"123",field1);
                                Log.d("vivek",field1);
                                Log.d("vivek","hello");
                                items.add(c);


                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplication(), LinearLayoutManager.HORIZONTAL, false));

                                recyclerView.setAdapter(new MyAdapter2(getApplication(), items));

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });













        recyclerView3 = findViewById(R.id.recyclerview3);

        List<cmeitem3> item = new ArrayList<>();
        db.collection("CME")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task ) {


                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Map<String, Object> dataMap = document.getData();
                                field1 = (String) dataMap.get("CME Organiser");
                                field2 = ((String) dataMap.get("CME Place"));
                                Log.d(TAG,(String) dataMap.get("CME Organiser"));

                                cmeitem3 c = new cmeitem3("abc", "field", field2,field1);
                                Log.d("vivek",field1);
                                Log.d("vivek","hello");
                                item.add(c);


                                recyclerView3.setLayoutManager(new LinearLayoutManager(getApplication()));
                                recyclerView3.setAdapter(new MyAdapter3(getApplication(), item));

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });






        recyclerView2 = findViewById(R.id.recyclerview2);

        List<cmeitem2> myitem = new ArrayList<cmeitem2>();


        db.collection("CME")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task ) {


                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Map<String, Object> dataMap = document.getData();
                                field1 = (String) dataMap.get("CME Organiser");
                                field2 = ((String) dataMap.get("CME Place"));
                                Log.d(TAG,(String) dataMap.get("CME Organiser"));

                                cmeitem2 c = new cmeitem2("abc", "field", field2);
                                Log.d("vivek",field1);
                                Log.d("vivek","hello");
                                myitem.add(c);


                                recyclerView2.setLayoutManager(new LinearLayoutManager(getApplication(), LinearLayoutManager.HORIZONTAL, false));
                                recyclerView2.setAdapter(new MyAdapter4(getApplication(), myitem));

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });



        pager = findViewById(R.id.view_pager);


        Spinner spinner = (Spinner) findViewById(R.id.speciality);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.speciality, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        Spinner spinner2 = (Spinner) findViewById(R.id.mode);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> myadapter = ArrayAdapter.createFromResource(this,
                R.array.mode, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner2.setAdapter(myadapter);

        pager.setAdapter(new ViewPagerAdapter(this));

//        new TabLayoutMediator(tabLayout, pager, new TabLayoutMediator.TabConfigurationStrategy() {
//            @Override
//            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
//                switch (position) {
//                    case 0:
//                        tab.setText("Todays");
//                        break;
//                    case 1:
//                        tab.setText("Upcoming");
//                        break;
//                    case 2:
//                        tab.setText("Past");
//                        break;
//                }
//            }
//        }).attach();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.chat) {
            Toast.makeText(this, "Chat clicked", Toast.LENGTH_SHORT).show();
        }
        else{
                Intent i = new Intent(CmeActivity.this, HomeActivity.class);
                startActivity(i);
            }

            return super.onOptionsItemSelected(item);
        }




    }

