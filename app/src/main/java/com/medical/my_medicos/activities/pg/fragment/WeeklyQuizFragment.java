package com.medical.my_medicos.activities.pg.fragment;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.medical.my_medicos.activities.pg.activites.insiders.SpecialityPGInsiderActivity;
import com.medical.my_medicos.activities.pg.adapters.VideoPGAdapter;
import com.medical.my_medicos.activities.pg.adapters.WeeklyQuizAdapter;
import com.medical.my_medicos.activities.pg.model.QuizPG;
import com.medical.my_medicos.activities.utils.ConstantsDashboard;
import com.medical.my_medicos.databinding.FragmentVideoBankBinding;
import com.medical.my_medicos.databinding.FragmentWeeklyQuizBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WeeklyQuizFragment extends Fragment {

    private FragmentWeeklyQuizBinding binding;
    private WeeklyQuizAdapter quizAdapter;
    private ArrayList<QuizPG> quizpg;

    String title1;
    private String quizTitle;

    public static WeeklyQuizFragment newInstance(int catId, String title) {
        WeeklyQuizFragment fragment = new WeeklyQuizFragment();
        Bundle args = new Bundle();
        args.putInt("catId", catId);
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWeeklyQuizBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Bundle args = getArguments();
        if (args != null) {
            title1= args.getString("title", "");

            if (getActivity() instanceof SpecialityPGInsiderActivity) {
                ((SpecialityPGInsiderActivity) getActivity()).setToolbarTitle(title1);
            }

            quizpg = new ArrayList<>();
            quizAdapter = new WeeklyQuizAdapter(requireContext(), quizpg);

            RecyclerView recyclerViewVideos = binding.quizListWeekly;
            LinearLayoutManager layoutManagerVideos = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
            recyclerViewVideos.setLayoutManager(layoutManagerVideos);
            recyclerViewVideos.setAdapter(quizAdapter);

            getQuestions(title1);
        } else {
            // Handle the case where arguments are null
            Log.e("ERROR", "Arguments are null in WeeklyQuizFragment");
        }

        return view;
    }

    void getQuestions(String title) {
//        RequestQueue queue = Volley.newRequestQueue(requireContext());
//
//        String url = ConstantsDashboard.GET_QUIZ_QUESTIONS_URL + "?q=" + title;
//        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
//            try {
//                JSONObject responseObject = new JSONObject(response);
//                if ("success".equals(responseObject.getString("status"))) {
//                    JSONArray dataArray = responseObject.getJSONArray("data");
//
////                    quizpg.clear();
//
//                    for (int i = 0; i < dataArray.length(); i++) {
//                        JSONObject quizObject = dataArray.getJSONObject(i);
//                        String quizTitle = quizObject.getString("title");
//                        String speciality = quizObject.getString("speciality");
//
//                        JSONArray quizDataArray = quizObject.getJSONArray("Data");
//
//                        for (int j = 0; j < quizDataArray.length(); j++) {
//                            JSONObject questionObject = quizDataArray.getJSONObject(j);
//                            QuizPG quizday = new QuizPG(
//                                    questionObject.getString("Question"),
//                                    questionObject.getString("A"),
//                                    questionObject.getString("B"),
//                                    questionObject.getString("C"),
//                                    questionObject.getString("D"),
//                                    questionObject.getString("Correct"),
//                                    "",
//                                    quizTitle,
//                                    speciality
//                            );
//
//                            quizpg.add(quizday);
//                            Log.d("DEBUG", "getQuestions: Question added to the list - " + quizTitle);
//                        }
//                    }
//
//                    Log.d("DEBUG", "getQuestions: Entire data - " + dataArray.toString());
//
//                    if (!quizpg.isEmpty()) {
//                        quizAdapter.notifyDataSetChanged();
//                        Log.d("DEBUG", "getQuestions: Data added to the list");
//                    } else {
//                        Log.d("DEBUG", "getQuestions: No data to add to the list");
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }, error -> {
//            Log.e("API_ERROR", "Error in API request: " + error.getMessage());
//        });
//        queue.add(request);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // Assuming "pg upload" is your collection, "weekly" is the document, and "quizz" is the subcollection
            CollectionReference quizzCollection = db.collection("PGupload").document("Weekley").collection("Quiz");

            Query query = quizzCollection;

            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Access "title" and "description" fields
                            String title = document.getString("title");
                            String speciality=document.getString("speciality");
                            int r=speciality.compareTo(title1);
                            if (r==0) {
                                QuizPG quizday = new QuizPG(title,title1);
                                quizpg.add(quizday);
                            }


                            // Your existing code logic goes here...
                            // You can use "title" and "description" as needed
                        }
                        quizAdapter.notifyDataSetChanged();

//                        adapter.notifyDataSetChanged();
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
        }

    }
}

