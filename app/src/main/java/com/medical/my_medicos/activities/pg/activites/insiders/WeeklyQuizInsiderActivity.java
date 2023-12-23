package com.medical.my_medicos.activities.pg.activites.insiders;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.pg.adapters.WeeklyQuizAdapterinsider;
import com.medical.my_medicos.activities.pg.model.QuizPG;
import com.medical.my_medicos.activities.pg.model.QuizPGinsider;
import java.util.ArrayList;
import java.util.Map;

public class WeeklyQuizInsiderActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WeeklyQuizAdapterinsider adapter;
    private ArrayList<QuizPGinsider> quizList;
    String question ;
    String optionA ;
    String optionB ;
    String optionC ;
    String optionD;
    String correctAnswer;
    String idQuestion;
    String titleOfSet ;
    String description;

    @SuppressLint({"MissingInflatedId", "RestrictedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_quiz_insider);

        // Initialize your RecyclerView and layout manager
        recyclerView = findViewById(R.id.recycler_view); // Replace with your RecyclerView ID
        Intent intent = getIntent();

        String str = intent.getStringExtra("Title");

        // Initialize your ArrayList
        quizList = new ArrayList<>();



        // Create an instance of QuizPGinsider with the provided data

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference quizzCollection = db.collection("PGupload").document("Weekley").collection("Quiz");

        quizzCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String speciality=document.getString("speciality");
                        Log.d("speciality3",speciality);
                        Log.d("speciality3",str);

                        // Access the map "Data" inside the document
                        ArrayList<Map<String, Object>> dataList = (ArrayList<Map<String, Object>>) document.get("Data");
                        int r=speciality.compareTo(str);
                        Log.d("abcdef",String.valueOf(r));
                        if (r==0) {



                        if (dataList != null) {
                            for (Map<String, Object> entry : dataList) {
                                // Access the fields under each key (0, 1, 2, etc.)
                                String question = (String) entry.get("Question");
                                String correctAnswer = (String) entry.get("Correct");
                                String optionA = (String) entry.get("A");
                                String optionB = (String) entry.get("B");
                                String optionC = (String) entry.get("C");
                                String optionD = (String) entry.get("D");
                                String description = (String) entry.get("Desciption");


                                QuizPGinsider quizQuestion = new QuizPGinsider(question, optionA, optionB, optionC, optionD, correctAnswer, idQuestion, titleOfSet, description);

                                quizList.add(quizQuestion);
                            }
                        }






                                // Now, you can use question and description as needed
//                                QuizPG quizday = new QuizPG(question, "OptionA", "OptionB", "OptionC", "OptionD", "CorrectAnswer", "", title, description);
//                                quizpg.add(quizday);
                            }

                        }
                    }


                    recyclerView.setLayoutManager(new LinearLayoutManager(WeeklyQuizInsiderActivity.this));

                    // Initialize the adapter
                    adapter = new WeeklyQuizAdapterinsider(WeeklyQuizInsiderActivity.this, quizList);

                    // Set the adapter to the RecyclerView
                    recyclerView.setAdapter(adapter);



            }
        });

        // Add the quizQuestion to quizList

    }
}
