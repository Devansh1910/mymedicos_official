package com.medical.my_medicos.activities.pg.activites.internalfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.pg.activites.internalfragments.preprationinternalfragments.AllPgPrep;
import com.medical.my_medicos.activities.pg.activites.internalfragments.preprationinternalfragments.ClinicalPgPrep;
import com.medical.my_medicos.activities.pg.activites.internalfragments.preprationinternalfragments.ParaPgPrep;
import com.medical.my_medicos.activities.pg.activites.internalfragments.preprationinternalfragments.PrePgPrep;
import com.medical.my_medicos.databinding.FragmentPreparationPgBinding;

public class PreparationPgFragment extends Fragment {

    FragmentPreparationPgBinding binding;
    SwipeRefreshLayout swipeRefreshLayoutPreparation;

    public static PreparationPgFragment newInstance() {
        PreparationPgFragment fragment = new PreparationPgFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPreparationPgBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        TabLayout tabLayout = rootView.findViewById(R.id.tablayoutprep);
        ViewPager2 viewPager = rootView.findViewById(R.id.view_page_prep);

        FragmentStateAdapter adapter = new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position) {
                    case 1:
                        return new PrePgPrep();
                    case 2:
                        return new ParaPgPrep();
                    case 3:
                        return new ClinicalPgPrep();
                    case 0:
                    default:
                        return new AllPgPrep();
                }
            }

            @Override
            public int getItemCount() {
                return 4; // Adjust based on your tabs count
            }
        };
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("ALL");
                    break;
                case 1:
                    tab.setText("PRE");
                    break;
                case 2:
                    tab.setText("PARA");
                    break;
                case 3:
                    tab.setText("CLINICAL");
                    break;
            }
        }).attach();

        // Initialize SwipeRefreshLayout
        swipeRefreshLayoutPreparation = binding.getRoot().findViewById(R.id.swipeRefreshLayoutPreparation);

        // Disable the SwipeRefreshLayout
        swipeRefreshLayoutPreparation.setEnabled(false);

        return rootView;
    }
}
