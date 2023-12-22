package com.medical.my_medicos.activities.pg.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.medical.my_medicos.activities.pg.adapters.QuestionBankPGAdapter;
import com.medical.my_medicos.activities.pg.adapters.insiders.SpecialitiesPGInsiderAdapter;
import com.medical.my_medicos.activities.pg.model.QuestionPG;
import com.medical.my_medicos.activities.pg.model.SpecialitiesPG;
import com.medical.my_medicos.activities.publications.adapters.ProductAdapter;
import com.medical.my_medicos.activities.publications.model.Product;
import com.medical.my_medicos.activities.utils.ConstantsDashboard;
import com.medical.my_medicos.databinding.FragmentQuestionbankBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuestionbankFragment extends Fragment {

    private FragmentQuestionbankBinding binding;
    private QuestionBankPGAdapter questionsAdapter;
    private ArrayList<QuestionPG> questionsforpg;
    SpecialitiesPGInsiderAdapter specialitiesPGInsiderAdapter;
    ArrayList<SpecialitiesPG> specialitiesPostGraduate;
    private ArrayList<Product> products;
    private ProductAdapter productAdapter;
    private int catId;

    public static QuestionbankFragment newInstance(int catId) {
        QuestionbankFragment fragment = new QuestionbankFragment();
        Bundle args = new Bundle();
        args.putInt("catId", catId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            catId = getArguments().getInt("catId", 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentQuestionbankBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        questionsforpg = new ArrayList<>();
        questionsAdapter = new QuestionBankPGAdapter(requireContext(), questionsforpg);

        RecyclerView recyclerViewQuestions = binding.questionsListQuestion;
        LinearLayoutManager layoutManagerQuestions = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewQuestions.setLayoutManager(layoutManagerQuestions);
        recyclerViewQuestions.setAdapter(questionsAdapter);

        initQuestionsBanks();

        return view;
    }

    void initQuestionsBanks() {
        questionsforpg = new ArrayList<>();
        questionsAdapter = new QuestionBankPGAdapter(requireContext(), questionsforpg);

        getRecentQuestions();

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.questionsListQuestion.setLayoutManager(layoutManager);
        binding.questionsListQuestion.setAdapter(questionsAdapter);
    }

    void getRecentQuestions() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        String url = ConstantsDashboard.GET_PG_QUESTIONBANK_URL;
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getString("status").equals("success")) {
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject childObj = array.getJSONObject(i);
                        QuestionPG questionbankItem = new QuestionPG(
                                childObj.getString("Title"),
                                childObj.getString("Description"),
                                childObj.getString("Time"),
                                childObj.getString("file")
                        );
                        questionsforpg.add(questionbankItem);
                    }
                    questionsAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
        });

        queue.add(request);
    }
}
