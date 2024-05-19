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
import com.medical.my_medicos.activities.fmge.adapters.VideoFmgeAdapter;
import com.medical.my_medicos.activities.fmge.model.VideoFMGE;
import com.medical.my_medicos.activities.pg.activites.insiders.SpecialityPGInsiderActivity;
import com.medical.my_medicos.activities.pg.activites.insiders.fragment.VideoBankFragment;
import com.medical.my_medicos.activities.pg.adapters.VideoPGAdapter;
import com.medical.my_medicos.activities.pg.model.VideoPG;
import com.medical.my_medicos.activities.utils.ConstantsDashboard;
import com.medical.my_medicos.databinding.FragmentVideoBankBinding;
import com.medical.my_medicos.databinding.FragmentVideosBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VideosFragment extends Fragment {

    private FragmentVideosBinding binding;
    private VideoFmgeAdapter videosAdapterfmge;
    LottieAnimationView nodatafound;
    private ArrayList<VideoFMGE> videosforfmge;
    private int catId;

    public static VideosFragment newInstance(int catId, String title) {
        VideosFragment fragment = new VideosFragment();
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
        binding = FragmentVideosBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        String title = getArguments().getString("title", "");

        if (getActivity() instanceof SpecialityPGInsiderActivity) {
            ((SpecialityPGInsiderActivity) getActivity()).setToolbarTitle(title);
        }

        videosforfmge = new ArrayList<>();
        videosAdapterfmge = new VideoFmgeAdapter(requireContext(), videosforfmge);

        RecyclerView recyclerViewQuestions = binding.videoRecyclerviewFmge;
        LinearLayoutManager layoutManagerQuestions = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewQuestions.setLayoutManager(layoutManagerQuestions);
        recyclerViewQuestions.setAdapter(videosAdapterfmge);
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
                        VideoFMGE videofmgeItem = new VideoFMGE(
                                childObj.getString("Title"),
                                childObj.getString("Description"),
                                childObj.getString("Time"),
                                childObj.getString("file")
                        );
                        videosforfmge.add(videofmgeItem);
                    }
                    videosAdapterfmge.notifyDataSetChanged();
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
        if (videosforfmge.isEmpty()) {
            binding.nodatafound.setVisibility(View.VISIBLE);
        } else {
            binding.nodatafound.setVisibility(View.GONE);
        }
    }
}
