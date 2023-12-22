package com.medical.my_medicos.activities.ug.insideractivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.ug.UgPostInsiderActivity;
import com.medical.my_medicos.activities.ug.model.UgFormViewModel;
import com.medical.my_medicos.databinding.FragmentUgformBinding;

public class UgFormFragment extends Fragment {

    Button movetoug; // Add this line

    private FragmentUgformBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        UgFormViewModel ugFormViewModel =
                new ViewModelProvider(this).get(UgFormViewModel.class);

        binding = FragmentUgformBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize movetoug CardView
        movetoug = root.findViewById(R.id.newupdatebtn); // Make sure to replace "R.id.movetoug" with the actual ID of your CardView

        // Set an OnClickListener for movetoug
        movetoug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent to start the JobsActivity
                Intent intent = new Intent(getActivity(), UgPostInsiderActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}