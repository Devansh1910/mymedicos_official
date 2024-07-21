package com.medical.my_medicos.activities.fmge.activites.internalfragments.Preprationindexing;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.fmge.activites.internalfragments.Preprationindexing.adapter.Twgt.PreprationTwgtPageAdapter;
import com.medical.my_medicos.activities.pg.adapters.WeeklyQuizAdapter;
import com.medical.my_medicos.activities.pg.model.QuizPG;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PreprationTWGT#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreprationTWGT extends Fragment {
    private WeeklyQuizAdapter quizAdapter;
    private ArrayList<QuizPG> quizpg;
    private static final String ARG_SPECIALITY = "speciality";
    private String speciality;
    String title1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public void PreprationSWGT() {
        // Required empty public constructor
    }

    public static PreprationTWGT newInstance(String speciality) {
        PreprationTWGT fragment = new PreprationTWGT();
        Bundle args = new Bundle();
        args.putString(ARG_SPECIALITY, speciality);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            speciality = getArguments().getString(ARG_SPECIALITY);
        }
    }
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prepration_t_w_g_t, container, false);

       @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TabLayout tabLayout = view.findViewById(R.id.tabLayout);
      ViewPager2 viewPager = view.findViewById(R.id.viewPager3);

        PreprationTwgtPageAdapter adapter = new PreprationTwgtPageAdapter(getActivity(),speciality);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("All");
                    break;
                case 1:
                    tab.setText("Hy");
                    break;

            }
        }).attach();

        return view;

    }
}