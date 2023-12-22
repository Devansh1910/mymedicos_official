package com.medical.my_medicos.activities.home.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.medical.my_medicos.R;

public class ClubFragment extends Fragment {

    LottieAnimationView underconstructionanimclub;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_club, container, false);

        underconstructionanimclub = rootView.findViewById(R.id.underconstructionanim);

        return rootView;
    }
}

