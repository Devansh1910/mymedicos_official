package com.example.my_medicos.activities.home.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.my_medicos.R;
import com.example.my_medicos.activities.slideshow.Slideshow;
import com.example.my_medicos.activities.slideshow.SlideshowAdapter;
import com.example.my_medicos.activities.utils.ConstantsDashboard;
import com.example.my_medicos.databinding.FragmentSlideshowBinding;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SlideshowAdapter slideshowAdapter;
    private ArrayList<Slideshow> slideshows;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this::refreshData);

        initSlideshowSlider();
        initSliderContent();

        return rootView;
    }

    private void refreshData() {
        slideshows.clear();
        slideshowAdapter.notifyDataSetChanged();

        getSlideshowRecent();

        swipeRefreshLayout.setRefreshing(false);
    }
    void getSlideshowRecent() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        String url = ConstantsDashboard.GET_SLIDESHOW;
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if ("success".equals(object.optString("status"))) {
                    JSONArray dataArray = object.getJSONArray("data");
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject slideshowObj = dataArray.getJSONObject(i);

                        JSONArray imagesArray = slideshowObj.getJSONArray("images");
                        ArrayList<Slideshow.Image> images = new ArrayList<>();
                        for (int j = 0; j < imagesArray.length(); j++) {
                            JSONObject imageObj = imagesArray.getJSONObject(j);
                            String imageUrl = imageObj.optString("url");
                            String imageId = imageObj.optString("id");
                            images.add(new Slideshow.Image(imageId, imageUrl));
                        }

                        String fileUrl = slideshowObj.optString("file");
                        String title = slideshowObj.optString("title");

                        // Now you can create your Slideshow object
                        Slideshow slideshowItem = new Slideshow(title, images, fileUrl);
                        slideshows.add(slideshowItem);
                    }
                    slideshowAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            // Handle error
        });

        queue.add(request);
    }

    void getsliderSlideShow() {
        RequestQueue queue = Volley.newRequestQueue(requireContext()); // Use requireContext()

        StringRequest request = new StringRequest(Request.Method.GET, ConstantsDashboard.GET_SLIDESHOW_SLIDER_URL, response -> {
            try {
                JSONArray slideshowsliderArray = new JSONArray(response);
                for (int i = 0; i < slideshowsliderArray.length(); i++) {
                    JSONObject childObj = slideshowsliderArray.getJSONObject(i);
                    binding.slideshowcarousel.addData(
                            new CarouselItem(
                                    childObj.getString("url"),
                                    childObj.getString("action")
                            )
                    );
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            // Handle error if needed
        });

        queue.add(request);
    }

    private void initSlideshowSlider() {
        getsliderSlideShow();
    }

    void initSliderContent() {
        slideshows = new ArrayList<>();
        slideshowAdapter = new SlideshowAdapter(getActivity(), slideshows);
        getSlideshowRecent();

        // Use requireContext() or getContext() to get a valid context
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);

        binding.slideshowpptlist.setLayoutManager(layoutManager);
        binding.slideshowpptlist.setAdapter(slideshowAdapter);
    }


}
