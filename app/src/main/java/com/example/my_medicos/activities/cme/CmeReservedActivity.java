package com.example.my_medicos.activities.cme;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.example.my_medicos.activities.cme.fragment.OngoingFragmentReserved;
import com.example.my_medicos.activities.cme.fragment.PastFragmentReserved;
import com.example.my_medicos.activities.cme.fragment.UpcomingFragmentReserved;
import com.example.my_medicos.databinding.ActivityCmeBinding;
import com.example.my_medicos.list.subSpecialitiesData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.FirebaseFirestore;

public class CmeReservedActivity extends AppCompatActivity {

    ActivityCmeBinding binding;
    String field1;
    String  field2,email,Date,Time1,venue;
    String field3;
    String field4;
    FloatingActionButton floatingActionButton;
    Button OK;
    RecyclerView recyclerView;
    //    RecyclerView recyclerView3;
//    RecyclerView recyclerView2;
//    RecyclerView recyclerView4;
    private ViewPager2 pager,viewpager;
    private TabLayout tabLayout;
    private Spinner specialitySpinner;
    String selectedMode1,selectedMode2,selectedMode;
    private FirebaseFirestore db;
    private Spinner subspecialitySpinner;
    private ArrayAdapter<CharSequence> specialityAdapter;
    private ArrayAdapter<CharSequence> subspecialityAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    Toolbar toolbar;
    private final String[][] subspecialities = subSpecialitiesData.subspecialities;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cme_reserved);


        viewpager = findViewById(R.id.view_pager1);
        viewpager.setAdapter(new ViewPagerAdapter(this)); // Make sure to initialize the ViewPager2 before using it


        toolbar = findViewById(R.id.cmetoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

// Existing code...



        pager.setAdapter(new ViewPagerAdapter(this));

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
                    return new OngoingFragmentReserved();
                case 1:
                    return new UpcomingFragmentReserved();
                case 2:
                    return new PastFragmentReserved();
            }
            return null;
        }
        @Override
        public int getItemCount() {
            return 3;
        }
    }

}
