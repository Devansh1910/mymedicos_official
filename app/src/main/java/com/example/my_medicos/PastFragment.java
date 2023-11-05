package com.example.my_medicos;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class PastFragment extends Fragment {
    RecyclerView recyclerView2;



    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_past, container, false);

        recyclerView2 = view.findViewById(R.id.cme_recyclerview_past);
        List<cmeitem2> myitem = new ArrayList<cmeitem2>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //......
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Query query=db.collection("CME").orderBy("Time", Query.Direction.DESCENDING);

            query.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task ) {

                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    Map<String, Object> dataMap = document.getData();
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                                    DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                    String Time = document.getString("Selected Time");
                                    String date =  document.getString("Selected Date");

                                    //
                                    LocalTime parsedTime = null;
                                    LocalDate parsedDate=null;
                                    try {
                                        // Parse the time string into a LocalTime object
                                        parsedTime = null;
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            parsedTime = LocalTime.parse(Time, formatter);
                                            parsedDate = LocalDate.parse(date, formatter1);

                                        }

                                        // Display the parsed time
                                        System.out.println("Parsed Time: " + parsedTime);
                                    } catch (DateTimeParseException e) {
                                        // Handle parsing error, e.g., if the input string is in the wrong format
                                        System.err.println("Error parsing time: " + e.getMessage());

                                    }
                                    LocalTime currentTime = null;
                                    LocalDate currentDate = null;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        currentTime = LocalTime.now();
                                        currentDate = LocalDate.now();
                                    }


                                    int r = parsedTime.compareTo(currentTime);

                                    Log.d("vivek", String.valueOf(r));
                                    int r1 = parsedDate.compareTo(currentDate);
                                    if ((r < 0) && (r1 <= 0)) {
                                        String field3 = ((String) dataMap.get("CME Title"));
                                        String field4 = ((String) dataMap.get("CME Presenter"));
                                        String field1 = (String) dataMap.get("CME Organiser");
                                        String field2;
                                        String field5=((String ) dataMap.get("User"));
                                        field2 = ((String) dataMap.get("Speciality"));
                                        String Date=((String) dataMap.get("Selected Date"));
                                        String time =((String) dataMap.get("Selected Time"));
                                        cmeitem2 c = new cmeitem2(field1, field2, Date, field3, field4,5,time,field5,"PAST");
                                        myitem.add(c);
                                        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                                        recyclerView2.setAdapter(new MyAdapter4(getContext(), myitem));


                                    } else {

                                    }

                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }

        // Query and display Today's CME events in the RecyclerView
        // Customize your logic to query and display data for today's events.
        // Set the appropriate adapter for the RecyclerView.

        return view;
    }
}