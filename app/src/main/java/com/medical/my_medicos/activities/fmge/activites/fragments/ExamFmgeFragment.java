package com.medical.my_medicos.activities.fmge.activites.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.fmge.activites.fragments.fragments.exams.SlothAFragment;
import com.medical.my_medicos.activities.fmge.activites.fragments.fragments.exams.SlothBFragment;


public class ExamFmgeFragment extends Fragment {

    private ViewPager2 pagerfmge, viewpagerfmge;
    private TabLayout tabLayoutfmge;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exam_fmge, container, false);

        ViewPager2 viewpagerfmge = view.findViewById(R.id.view_pager_fmge);
        TabLayout tabLayoutfmge = view.findViewById(R.id.tablayout);

        viewpagerfmge.setAdapter(new ViewPagerAdapterFmge(getActivity(), "Title1")); // Assuming Title1 is a valid string you wanted to use

        new TabLayoutMediator(tabLayoutfmge, viewpagerfmge, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Sloth A");
                    break;
                case 1:
                    tab.setText("Sloth B");
                    break;
            }
        }).attach();

        return view;
    }


    class ViewPagerAdapterFmge extends FragmentStateAdapter {
        private String title;

        public ViewPagerAdapterFmge(@NonNull FragmentActivity fragmentActivity, String title) {
            super(fragmentActivity);
            this.title = title; // Store the title in the adapter
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    SlothAFragment slothAFragment = new SlothAFragment();
                    Bundle argsSlothA = new Bundle();
                    argsSlothA.putString("Title", title); // Pass Title1 to RegularFragment
                    slothAFragment.setArguments(argsSlothA);
                    return slothAFragment;
                case 1:
                    SlothBFragment slothBFragment = new SlothBFragment();
                    Bundle argsSlothB = new Bundle();
                    argsSlothB.putString("Title", title); // Pass Title1 to LocumFragment
                    slothBFragment.setArguments(argsSlothB);
                    return slothBFragment;
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}