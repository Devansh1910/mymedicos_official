package com.example.my_medicos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.example.my_medicos.jobs.LocumFragment;
import com.example.my_medicos.jobs.RegularFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        viewpagerjobs.setAdapter(new JobsActivity2.ViewPagerAdapterJobs(this));

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

        public ViewPagerAdapterJobs(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new RegularFragment();
                case 1:
                    return new LocumFragment();
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}
