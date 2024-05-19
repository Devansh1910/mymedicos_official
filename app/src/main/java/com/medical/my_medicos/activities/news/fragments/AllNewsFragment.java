package com.medical.my_medicos.activities.news.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.Query;
import com.medical.my_medicos.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.medical.my_medicos.activities.news.News;
import com.medical.my_medicos.activities.news.NewsAdapter;

import java.util.ArrayList;

public class AllNewsFragment extends Fragment {

    private ArrayList<News> news;
    private NewsAdapter newsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_news, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initNewsSlider();
    }

    void initNewsSlider() {
        news = new ArrayList<>();
        newsAdapter = new NewsAdapter(getActivity(), news); // Adjust context to getActivity()
        getRecentNews();
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1); // Adjust context
        RecyclerView newsList = getView().findViewById(R.id.recyclerview_all_news); // Adjusted to match your XML
        newsList.setLayoutManager(layoutManager);
        newsList.setAdapter(newsAdapter);
    }

    void getRecentNews() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("MedicalNews")
                .orderBy("Time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String newsType = document.getString("type");
                            if ("News".equals(newsType)) {
                                News newsItem = new News(
                                        document.getId(),
                                        document.getString("Title"),
                                        document.getString("thumbnail"),
                                        document.getString("Description"),
                                        document.getString("subject"),
                                        document.getString("Time"),
                                        document.getString("URL"),
                                        newsType
                                );
                                news.add(newsItem);
                            }
                        }
                        newsAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
