package com.medical.my_medicos.activities.pg.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
            String title = args.getString("title", "");

            if (getActivity() instanceof SpecialityPGInsiderActivity) {
                ((SpecialityPGInsiderActivity) getActivity()).setToolbarTitle(title);
            }

            quizpg = new ArrayList<>();
            quizAdapter = new WeeklyQuizAdapter(requireContext(), quizpg);

            RecyclerView recyclerViewVideos = binding.quizListWeekly;
            LinearLayoutManager layoutManagerVideos = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
            recyclerViewVideos.setLayoutManager(layoutManagerVideos);
            recyclerViewVideos.setAdapter(quizAdapter);

            getQuestions(title);
        } else {
            // Handle the case where arguments are null
            Log.e("ERROR", "Arguments are null in WeeklyQuizFragment");
        }

        return view;
    }

    void getQuestions(String title) {
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        String url = ConstantsDashboard.GET_QUIZ_QUESTIONS_URL + "?q=" + title;
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject responseObject = new JSONObject(response);
                if ("success".equals(responseObject.getString("status"))) {
                    JSONArray dataArray = responseObject.getJSONArray("data");

//                    quizpg.clear();

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject quizObject = dataArray.getJSONObject(i);
                        String quizTitle = quizObject.getString("title");
                        String speciality = quizObject.getString("speciality");

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
                                    "",
                                    quizTitle,
                                    speciality
                            );

                            quizpg.add(quizday);
                            Log.d("DEBUG", "getQuestions: Question added to the list - " + quizTitle);
                        }
                    }

                    Log.d("DEBUG", "getQuestions: Entire data - " + dataArray.toString());

                    if (!quizpg.isEmpty()) {
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

