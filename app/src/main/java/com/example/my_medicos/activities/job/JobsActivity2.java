package com.example.my_medicos.activities.job;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.my_medicos.R;
import com.example.my_medicos.activities.job.fragments.LocumFragment;
import com.example.my_medicos.activities.job.fragments.RegularFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.FirebaseFirestore;

public class JobsActivity2 extends AppCompatActivity {
    String Title1;

    private ViewPager2 pagerjobs, viewpagerjobs;
    private TabLayout tabLayoutjobs;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs2);

        Toolbar toolbar = findViewById(R.id.jobstoolbar);
        setSupportActionBar(toolbar); // Set the toolbar as the ActionBar

        // Get the title from the intent
        Intent intent = getIntent();
        if (intent != null) {
            Title1 = intent.getStringExtra("Title");
        }

        if (Title1 != null) {
            toolbar.setTitle(Title1); // Set the title now that you have it
        }

        viewpagerjobs = findViewById(R.id.view_pager_jobs);
        viewpagerjobs.setAdapter(new ViewPagerAdapterJobs(this, Title1)); // Pass Title1 to the adapter

        pagerjobs = findViewById(R.id.view_pager_jobs);
        tabLayoutjobs = findViewById(R.id.tablayout);
        new TabLayoutMediator(tabLayoutjobs, viewpagerjobs, (tab, position) -> {
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

    class ViewPagerAdapterJobs extends FragmentStateAdapter {
        private String title;

        public ViewPagerAdapterJobs(@NonNull FragmentActivity fragmentActivity, String title) {
            super(fragmentActivity);
            this.title = title; // Store the title in the adapter
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    RegularFragment regularFragment = new RegularFragment();
                    Bundle args = new Bundle();
                    args.putString("Title", title); // Pass Title1 to RegularFragment
                    regularFragment.setArguments(args);
                    return regularFragment;
                case 1:
                    LocumFragment locumFragment = new LocumFragment();
                    Bundle argsLocum = new Bundle();
                    argsLocum.putString("Title", title); // Pass Title1 to LocumFragment
                    locumFragment.setArguments(argsLocum);
                    return locumFragment;
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}
