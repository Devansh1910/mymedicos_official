package com.medical.my_medicos.activities.pg.activites.internalfragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.medical.my_medicos.R;

public class NeetExamFragment extends Fragment {

    public static NeetExamFragment newInstance() {
        NeetExamFragment fragment = new NeetExamFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_neet_exam, container, false);
    }
}