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
import com.medical.my_medicos.activities.fmge.activites.fragments.fragments.materials.ReadablesFragment;
import com.medical.my_medicos.activities.fmge.activites.fragments.fragments.materials.VideosFragment;

public class MaterialFmgeFragment extends Fragment {

    private ViewPager2 pagerfmge, viewpagerfmgematerial;
    private TabLayout tabLayoutfmgematerial;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_material_fmge, container, false);

        ViewPager2 viewpagerfmgematerial = view.findViewById(R.id.view_pager_fmge_material);
        TabLayout tabLayoutfmgematerial = view.findViewById(R.id.tablayoutmaterial);

        viewpagerfmgematerial.setAdapter(new MaterialFmgeFragment.ViewPagerAdapterFmgeMaterial(getActivity(), "Title1")); // Assuming Title1 is a valid string you wanted to use

        new TabLayoutMediator(tabLayoutfmgematerial, viewpagerfmgematerial, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Readable");
                    break;
                case 1:
                    tab.setText("Videos");
                    break;
            }
        }).attach();

        return view;
    }


    class ViewPagerAdapterFmgeMaterial extends FragmentStateAdapter {
        private String title;

        public ViewPagerAdapterFmgeMaterial(@NonNull FragmentActivity fragmentActivity, String title) {
            super(fragmentActivity);
            this.title = title; // Store the title in the adapter
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    ReadablesFragment readablesFragment = new ReadablesFragment();
                    Bundle argsReadables = new Bundle();
                    argsReadables.putString("Title", title); // Pass Title1 to RegularFragment
                    readablesFragment.setArguments(argsReadables);
                    return readablesFragment;
                case 1:
                    VideosFragment videosFragment = new VideosFragment();
                    Bundle argsVideos = new Bundle();
                    argsVideos.putString("Title", title); // Pass Title1 to LocumFragment
                    videosFragment.setArguments(argsVideos);
                    return videosFragment;
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}