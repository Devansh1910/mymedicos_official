package com.medical.my_medicos.activities.fmge.activites.fragments.fragments.materials;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.fmge.adapters.MaterialQbFmgeAdapter;
import com.medical.my_medicos.activities.fmge.model.MaterialFMGE;
import com.medical.my_medicos.activities.pg.activites.insiders.SpecialityPGInsiderActivity;
import com.medical.my_medicos.activities.pg.activites.insiders.fragment.QuestionbankFragment;
import com.medical.my_medicos.activities.pg.adapters.QuestionBankPGAdapter;
import com.medical.my_medicos.activities.pg.model.QuestionPG;
import com.medical.my_medicos.activities.utils.ConstantsDashboard;
import com.medical.my_medicos.databinding.FragmentReadablesBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ReadablesFragment extends Fragment {

    private FragmentReadablesBinding binding;
    private MaterialQbFmgeAdapter questionsAdapterfmge;
    LottieAnimationView nodatafound;
    private ArrayList<MaterialFMGE> questionsforfmge;
    private int catId;

    public static ReadablesFragment newInstance(int catId, String title) {
        ReadablesFragment fragment = new ReadablesFragment();
        Bundle args = new Bundle();
        args.putInt("catId", catId);
        args.putString("title", title);
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
        binding = FragmentReadablesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        String title = getArguments().getString("title", "");
        if (getActivity() instanceof SpecialityPGInsiderActivity) {
            ((SpecialityPGInsiderActivity) getActivity()).setToolbarTitle(title);
        }

        questionsforfmge = new ArrayList<>();
        questionsAdapterfmge = new MaterialQbFmgeAdapter(requireContext(), questionsforfmge);

        RecyclerView recyclerViewQuestions = binding.readablesRecyclerviewFmge;
        LinearLayoutManager layoutManagerQuestions = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewQuestions.setLayoutManager(layoutManagerQuestions);
        recyclerViewQuestions.setAdapter(questionsAdapterfmge);
        getRecentQuestions(title);

        return view;
    }

    void getRecentQuestions(String title) {
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        String url = ConstantsDashboard.GET_PG_QUESTIONBANK_URL + "/" + title;
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getString("status").equals("success")) {
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject childObj = array.getJSONObject(i);
                        MaterialFMGE questionbankItem = new MaterialFMGE(
                                childObj.getString("Title"),
                                childObj.getString("Description"),
                                childObj.getString("Time"),
                                childObj.getString("file")
                        );
                        questionsforfmge.add(questionbankItem);
                    }
                    questionsAdapterfmge.notifyDataSetChanged();
                    updateNoDataVisibility();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
        });

        queue.add(request);
    }

    private void updateNoDataVisibility() {
        if (questionsforfmge.isEmpty()) {
            binding.nodatafound.setVisibility(View.VISIBLE);
        } else {
            binding.nodatafound.setVisibility(View.GONE);
        }
    }
}
