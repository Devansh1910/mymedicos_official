package com.example.my_medicos.activities.pg.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.my_medicos.activities.pg.adapters.VideoPGAdapter;
import com.example.my_medicos.activities.pg.model.VideoPG;
import com.example.my_medicos.activities.utils.ConstantsDashboard;
import com.example.my_medicos.databinding.FragmentVideoBankBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VideoBankFragment extends Fragment {

    private FragmentVideoBankBinding binding;
    private VideoPGAdapter videosAdapter;
    private ArrayList<VideoPG> videoforpg;

    public VideoBankFragment() {
        // Required empty public constructor
    }

    public static VideoBankFragment newInstance() {
        return new VideoBankFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentVideoBankBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        videoforpg = new ArrayList<>();
        videosAdapter = new VideoPGAdapter(requireContext(), videoforpg);

        RecyclerView recyclerViewVideos = binding.videosListQuestion;
        LinearLayoutManager layoutManagerVideos = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewVideos.setLayoutManager(layoutManagerVideos);
        recyclerViewVideos.setAdapter(videosAdapter);

        initVideosBanks();

        return view;
    }

    void initVideosBanks() {
        videoforpg = new ArrayList<>();
        videosAdapter = new VideoPGAdapter(requireContext(), videoforpg);

        getRecentVideos();

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.videosListQuestion.setLayoutManager(layoutManager);
        binding.videosListQuestion.setAdapter(videosAdapter);
    }

    void getRecentVideos() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        String url = ConstantsDashboard.GET_PG_VIDEOS_URL;
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if(object.getString("status").equals("success")){
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject childObj = array.getJSONObject(i);
                        VideoPG videoItem = new VideoPG(
                                childObj.getString("Title"),
                                childObj.getString("Thumbnail"),
                                childObj.getString("Time"),
                                childObj.getString("URL")
                        );
                        videoforpg.add(videoItem);
                    }
                    videosAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> { });

        queue.add(request);
    }
}
