package com.medical.my_medicos.activities.pg.activites.internalfragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.pg.fragment.WeeklyQuizFragment;

public class PreparationPgFragment extends Fragment {

    public static PreparationPgFragment newInstance() {
        PreparationPgFragment fragment = new PreparationPgFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_preparation_pg, container, false);
    }
}