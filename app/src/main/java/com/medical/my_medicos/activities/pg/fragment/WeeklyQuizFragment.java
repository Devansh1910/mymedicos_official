package com.medical.my_medicos.activities.pg.fragment;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.medical.my_medicos.activities.pg.adapters.QuizPGAdapter;
import com.medical.my_medicos.activities.pg.model.PerDayPG;
import com.medical.my_medicos.activities.pg.model.QuizPG;
import com.medical.my_medicos.activities.utils.ConstantsDashboard;
import com.medical.my_medicos.databinding.FragmentWeeklyQuizBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WeeklyQuizFragment extends Fragment {

    private FragmentWeeklyQuizBinding binding;

    private QuizPGAdapter quizAdapter;

    CardView nocardpgquestion;
    private ArrayList<PerDayPG> dailyQuestionsPG;
    ArrayList<QuizPG> quizpg;
    String quiz;

    public static WeeklyQuizFragment newInstance(int catId) {
        WeeklyQuizFragment fragment = new WeeklyQuizFragment();
        Bundle args = new Bundle();
        args.putInt("catId", catId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWeeklyQuizBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        quizpg = new ArrayList<>();
        quizAdapter = new QuizPGAdapter(requireContext(), quizpg);

        RecyclerView recyclerView = binding.quizList;
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(quizAdapter);

        initQuestions(quiz);

        return view;
    }

    void initQuestions(String Quiz) {
        quizpg = new ArrayList<>();
        quizAdapter = new QuizPGAdapter(getContext(), quizpg);

        getQuestions(Quiz);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        binding.quizList.setLayoutManager(layoutManager);
        binding.quizList.setAdapter(quizAdapter);
    }

    void getQuestions(String quiz) {
        Log.d("DEBUG", "getQuestions: Making API request");
        RequestQueue queue = Volley.newRequestQueue(getContext());

        String url = ConstantsDashboard.GET_QUIZ_QUESTIONS_URL;
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                Log.d("DEBUG", "getQuestions: Response received");
                JSONObject responseObject = new JSONObject(response);
                if (responseObject.getString("status").equals("success")) {
                    JSONArray dataArray = responseObject.getJSONArray("data");

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject quizObject = dataArray.getJSONObject(i);
                        JSONArray quizDataArray = quizObject.getJSONArray("Data");

                        for (int j = 0; j < quizDataArray.length(); j++) {
                            JSONObject questionObject = quizDataArray.getJSONObject(j);
                            QuizPG quizday = new QuizPG(
                                    questionObject.getString("Question"),
                                    questionObject.getString("A"),
                                    questionObject.getString("B"),
                                    questionObject.getString("C"),
                                    questionObject.getString("D"),
                                    questionObject.getString("Correct"),
                                    questionObject.getString("id")
                            );

                            String questionId = questionObject.getString("id");
                            Log.d("questionid", questionId);

                            if ((questionId != null) && (quiz != null)) {
                                int compareResult = questionId.compareTo(quiz);
                                Log.d("questionid", String.valueOf(compareResult));

                                if (compareResult != 0) {
                                    quizpg.add(quizday);
                                    Log.d("DEBUG", "getQuestions: Question added to the list - " + quizday.getidQuestion());
                                }
                            }
                        }
                    }

                    if (quizpg != null && !quizpg.isEmpty()) {
                        quizAdapter.notifyDataSetChanged();
                        Log.d("DEBUG", "getQuestions: Data added to the list");
                    } else {
                        Log.d("DEBUG", "getQuestions: No data to add to the list");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.e("API_ERROR", "Error in API request: " + error.getMessage());
        });
        queue.add(request);
    }



}
