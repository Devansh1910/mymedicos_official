package com.medical.my_medicos.activities.job;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.job.fragments.LoccumSearchFragment;
import com.medical.my_medicos.activities.job.fragments.LocumFragment;
import com.medical.my_medicos.activities.job.fragments.RegularFragment;
import com.medical.my_medicos.activities.job.fragments.RegularSearchFragment;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class JobsSearchActivity extends AppCompatActivity {
    String selectedSpeciality;
    String Title1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_search);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.backgroundcolor));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.backgroundcolor));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }

        // Retrieve the selected speciality from the intent
        Intent intent = getIntent();
        if (intent != null) {
            selectedSpeciality = intent.getStringExtra("selectedSpeciality");
            Title1 = intent.getStringExtra("Title");
        }

        // Check if the selected speciality is not null or empty
        if (selectedSpeciality != null && !selectedSpeciality.isEmpty()) {
            // Your code to handle the selected speciality, if needed
        }

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ViewPager2 viewpagerjobs = findViewById(R.id.view_pager_jobs);
        viewpagerjobs.setAdapter(new ViewPagerAdapterJobsSearch(this, selectedSpeciality,Title1));

        TabLayout tabLayoutJobsSearch = findViewById(R.id.tablayout);
        new TabLayoutMediator(tabLayoutJobsSearch, viewpagerjobs, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Regular");
                    break;
                case 1:
                    tab.setText("Locum");
                    break;
            }
        }).attach();
    }

    static class ViewPagerAdapterJobsSearch extends FragmentStateAdapter {
        private String speciality;
        private String title;

        public ViewPagerAdapterJobsSearch(FragmentActivity fragmentActivity, String speciality,String Title) {
            super(fragmentActivity);
            this.speciality = speciality;
            this.title=Title;
        }

        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return RegularSearchFragment.newInstance(speciality,title);
                case 1:
                    return LoccumSearchFragment.newInstance(speciality,title);
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}
