package com.example.my_medicos.activities.cme;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.my_medicos.R;
import com.example.my_medicos.activities.cme.fragment.OngoingFragmentCreated;
import com.example.my_medicos.activities.cme.fragment.PastFragmentCreated;
import com.example.my_medicos.activities.cme.fragment.UpcomingFragmentCreated;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class CmeCreatedActivity extends AppCompatActivity {


    private ViewPager2 pager,viewpager;
    private TabLayout tabLayout;

    Toolbar toolbar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cme_created);


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
                    return new OngoingFragmentCreated();
                case 1:
                    return new UpcomingFragmentCreated();
                case 2:
                    return new PastFragmentCreated();
            }
            return null;
        }
        @Override
        public int getItemCount() {
            return 3;
        }
    }



}

