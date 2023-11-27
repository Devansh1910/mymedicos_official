package com.example.my_medicos.activities.drawer.drawercontent.logout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.my_medicos.R;

public class LogoutFragment extends Fragment {

    private TextView textView;

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_logout, container, false);
        Log.d("jabjbj","cbsakjbkb");

        textView = root.findViewById(R.id.text_slideshow); // Replace with the actual ID of your TextView

        LogoutViewModel logoutViewModel = new ViewModelProvider(this).get(LogoutViewModel.class);

        logoutViewModel.getText().observe(getViewLifecycleOwner(), text -> textView.setText(text));

        return root;
    }
}
