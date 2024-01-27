package com.medical.my_medicos.activities.home.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.home.sidedrawer.extras.BottomSheetForCommunity;

public class ClubFragment extends Fragment {

    LottieAnimationView underconstructionanimclub;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_club, container, false);

        underconstructionanimclub = rootView.findViewById(R.id.underconstructionanim);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = requireActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(requireContext(), R.color.backgroundcolor));
        }

        Button telegramLayout = rootView.findViewById(R.id.communityjoinopener);
        telegramLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBottomSheetCommunity();
            }
        });

        return rootView;
    }

    private void openBottomSheetCommunity() {
        BottomSheetDialogFragment bottomSheetFragment = new BottomSheetForCommunity();

        bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
    }
}

