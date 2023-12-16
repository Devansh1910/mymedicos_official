package com.example.my_medicos.activities.ug;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.my_medicos.R;
import com.example.my_medicos.activities.ug.fragments.ResourcesFragment;
import com.example.my_medicos.activities.ug.fragments.TextBooksFragment1;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.FirebaseFirestore;

public class uginsiderActivty extends AppCompatActivity {
    String Title1;

    private ViewPager2 pagerjobs, viewpagerjobs;
    private TabLayout tabLayoutjobs;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ug);

        Toolbar toolbar = findViewById(R.id.ugtoolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.arrowbackforappbar);
        }


        // Get the title from the intent
        Intent intent = getIntent();
        if (intent != null) {
            Title1 = intent.getStringExtra("Title");
        }

        if (Title1 != null) {
            toolbar.setTitle(Title1); // Set the title now that you have it
        }

        viewpagerjobs = findViewById(R.id.view_pager_jobs);
        viewpagerjobs.setAdapter(new com.example.my_medicos.activities.ug.uginsiderActivty.ViewPagerAdapterJobs(this, Title1)); // Pass Title1 to the adapter

        pagerjobs = findViewById(R.id.view_pager_jobs);
        tabLayoutjobs = findViewById(R.id.tablayout);
        new TabLayoutMediator(tabLayoutjobs, viewpagerjobs, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("TextBook");
                    break;
                case 1:
                    tab.setText("Resources");
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
                    TextBooksFragment1 regularFragment = new TextBooksFragment1();
                    Bundle args = new Bundle();
                    args.putString("Title", title); // Pass Title1 to RegularFragment
                    regularFragment.setArguments(args);
                    return regularFragment;
                case 1:
                    ResourcesFragment locumFragment = new ResourcesFragment();
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
