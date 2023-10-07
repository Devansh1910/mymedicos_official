package com.example.my_medicos.ui.ughome;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_medicos.CmeActivity;
import com.example.my_medicos.MyAdapter2;
import com.example.my_medicos.PostCmeActivity;
import com.example.my_medicos.R;
import com.example.my_medicos.UgAdapter1;
import com.example.my_medicos.cmeitem1;
import com.example.my_medicos.databinding.FragmentUgHomeBinding;
import com.example.my_medicos.ugitem1;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UgHomeFragment extends Fragment {
    private FragmentUgHomeBinding binding;
    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private String field1, field2, field3, field4;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        UgExamViewModel ugExamViewModel =
                new ViewModelProvider(this).get(UgExamViewModel.class);

        binding = FragmentUgHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.ug_recycleview);
        // Fetch data from Firestore and populate the RecyclerView
        fetchData();

        return root;
    }

    private void fetchData() {
        db = FirebaseFirestore.getInstance();
        List<ugitem1> items = new ArrayList<>();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Query query = db.collection("UG").orderBy("Date", Query.Direction.DESCENDING);

            query.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map<String, Object> dataMap = document.getData();
                                    String Time = document.getString("Selected Time");

                                    if (Time != null && !Time.isEmpty()) { // Check if Time is not null or empty
                                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                                        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                        String date = document.getString("Selected Date");

                                        LocalTime parsedTime = null;
                                        LocalDate parsedDate = null;
                                        try {
                                            parsedTime = LocalTime.parse(Time, formatter);
                                            parsedDate = LocalDate.parse(date, formatter1);
                                        } catch (java.time.format.DateTimeParseException e) {
                                            // Handle parsing error
                                            e.printStackTrace();
                                        }

                                        LocalTime currentTime = LocalTime.now();
                                        LocalDate currentDate = LocalDate.now();

                                        int r = parsedTime.compareTo(currentTime);
                                        int r1 = parsedDate.compareTo(currentDate);

                                        if ((r <= 0) || (r1 < 0)) {
                                            field3 = ((String) dataMap.get("UG Title"));
                                            field4 = ((String) dataMap.get("UG Description"));
                                            field1 = (String) dataMap.get("UG Organiser");
                                            field2 = ((String) dataMap.get("Speciality"));

                                            ugitem1 u = new ugitem1(field1, field2, 5, field3, field4);
                                            items.add(u);
                                        }
                                    }
                                }

                                // Set up RecyclerView after fetching data
                                recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
                                recyclerView.setAdapter(new UgAdapter1(requireContext(), items));
                            } else {
                                // Handle error
                            }
                        }
                    });
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
