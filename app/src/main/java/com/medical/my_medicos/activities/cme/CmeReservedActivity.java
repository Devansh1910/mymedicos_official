package com.medical.my_medicos.activities.cme;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.cme.fragment.OngoingFragmentReserved;
import com.medical.my_medicos.activities.cme.fragment.PastFragmentReserved;
import com.medical.my_medicos.activities.cme.fragment.UpcomingFragmentReserved;
import com.medical.my_medicos.list.subSpecialitiesData;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class CmeReservedActivity extends AppCompatActivity {
    private ViewPager2 pager, viewpager;
    private TabLayout tabLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private final String[][] subspecialities = subSpecialitiesData.subspecialities;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cme_reserved);

        viewpager = findViewById(R.id.view_pager1);
        viewpager.setAdapter(new ViewPagerAdapter(this));

        Toolbar toolbar = findViewById(R.id.cmetoolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.arrowbackforappbar);
        }

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

        pager.setAdapter(new ViewPagerAdapter(this));

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                swipeRefreshLayout.setRefreshing(false);
            }, 3000);
        });
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

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
