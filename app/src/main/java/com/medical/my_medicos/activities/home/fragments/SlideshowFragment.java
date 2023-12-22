package com.medical.my_medicos.activities.home.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.slideshow.SlideshareCategoryAdapter;
import com.medical.my_medicos.activities.slideshow.Slideshow;
import com.medical.my_medicos.activities.slideshow.SlideshowAdapter;
import com.medical.my_medicos.activities.slideshow.insider.SpecialitySlideshowInsiderActivity;
import com.medical.my_medicos.activities.slideshow.model.SlideshareCategory;
import com.medical.my_medicos.activities.utils.ConstantsDashboard;
import com.medical.my_medicos.databinding.FragmentSlideshowBinding;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    private SwipeRefreshLayout swipeRefreshLayout;
    SlideshareCategoryAdapter slideshareCategoryAdapterslideshow;
    ArrayList<SlideshareCategory> slidesharecategoriesslideshow;
    private SlideshowAdapter slideshowAdapter;
    private ArrayList<Slideshow> slideshows;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this::refreshData);

        initCategoriesSlideshow();
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

                        String fileUrl = slideshowObj.optString("file");
                        String title = slideshowObj.optString("title");

                        if (slideshowObj.has("images")) {
                            JSONArray imagesArray = slideshowObj.getJSONArray("images");
                            ArrayList<Slideshow.Image> images = new ArrayList<>();
                            for (int j = 0; j < imagesArray.length(); j++) {
                                JSONObject imageObj = imagesArray.getJSONObject(j);
                                String imageUrl = imageObj.optString("url");
                                String imageId = imageObj.optString("id");
                                images.add(new Slideshow.Image(imageId, imageUrl));
                            }
                            // Now you can create your Slideshow object with images
                            Slideshow slideshowItem = new Slideshow(title, images, fileUrl);
                            slideshows.add(slideshowItem);
                        } else {
                            // If "images" array does not exist, create Slideshow without images
                            Slideshow slideshowItem = new Slideshow(title, new ArrayList<>(), fileUrl);
                            slideshows.add(slideshowItem);
                        }
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
    void initCategoriesSlideshow() {
        slidesharecategoriesslideshow = new ArrayList<>();
        slideshareCategoryAdapterslideshow = new SlideshareCategoryAdapter(requireContext(), slidesharecategoriesslideshow);

        getCategoriesSlideshow();

        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2);
        binding.slideshowpptlistcategory.setLayoutManager(layoutManager);
        binding.slideshowpptlistcategory.setAdapter(slideshareCategoryAdapterslideshow);
    }
    void getCategoriesSlideshow() {

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        StringRequest request = new StringRequest(Request.Method.GET, ConstantsDashboard.GET_SPECIALITY, new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("err", response);
                    JSONObject mainObj = new JSONObject(response);
                    if (mainObj.getString("status").equals("success")) {
                        JSONArray slidesharecategoriesArray = mainObj.getJSONArray("data");
                        int categoriesCount = Math.min(slidesharecategoriesArray.length(), 40);
                        for (int i = 0; i < categoriesCount; i++) {
                            JSONObject object = slidesharecategoriesArray.getJSONObject(i);
                            SlideshareCategory slideshowcategory = new SlideshareCategory(
                                    object.getString("id"),
                                    object.getInt("priority")
                            );
                            slidesharecategoriesslideshow.add(slideshowcategory);
                            Log.e("Something went wrong..",object.getString("priority"));
                        }
                        slideshareCategoryAdapterslideshow.notifyDataSetChanged();
                        binding.slideshowpptlistcategory.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                            @Override
                            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                                View child = rv.findChildViewUnder(e.getX(), e.getY());
                                int position = rv.getChildAdapterPosition(child);

                                if (position != RecyclerView.NO_POSITION) {
                                    if (position == slidesharecategoriesslideshow.size() - 1 && slidesharecategoriesslideshow.get(position).getPriority() == -1) {
                                        Intent intent = new Intent(requireContext(), SpecialitySlideshowInsiderActivity.class);
                                        startActivity(intent);
                                    } else {

                                    }
                                }
                                return false;
                            }
                            @Override
                            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {}

                            @Override
                            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
                        });
                        slideshareCategoryAdapterslideshow.notifyDataSetChanged();
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
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