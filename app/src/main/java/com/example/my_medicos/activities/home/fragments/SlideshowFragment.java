package com.example.my_medicos.activities.home.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.my_medicos.R;

public class SlideshowFragment extends Fragment {

    LottieAnimationView underconstructionanimclub;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_club, container, false);

        underconstructionanimclub = rootView.findViewById(R.id.underconstructionanim);

        return rootView;
    }
}

