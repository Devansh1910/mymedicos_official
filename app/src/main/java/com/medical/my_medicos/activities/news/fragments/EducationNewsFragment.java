package com.medical.my_medicos.activities.news.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.Query;
import com.medical.my_medicos.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.medical.my_medicos.activities.news.News;
import com.medical.my_medicos.activities.news.NewsAdapter;

import java.util.ArrayList;

public class EducationNewsFragment extends Fragment {

    private ArrayList<News> news;
    private NewsAdapter newsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_education_news, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initNewsSlider();
    }

    void initNewsSlider() {
        news = new ArrayList<>();
        newsAdapter = new NewsAdapter(getActivity(), news); // Use getActivity() for context
        getRecentNews();
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        RecyclerView newsList = getView().findViewById(R.id.recyclerview_education_news); // Adjust the ID to match your layout
        newsList.setLayoutManager(layoutManager);
        newsList.setAdapter(newsAdapter);
    }

    void getRecentNews() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("MedicalNews")
                .whereEqualTo("type", "News")
                .whereEqualTo("tag", "Education")
                .orderBy("Time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            News newsItem = new News(
                                    document.getString("Title"),
                                    document.getString("thumbnail"),
                                    document.getString("Description"),
                                    document.getString("Time"),
                                    document.getString("URL"),
                                    document.getString("tag") // Assuming you want to include the tag in your News model
                            );
                            news.add(newsItem);
                        }
                        newsAdapter.notifyDataSetChanged();
                    } else {
                        // Handle the failure
                    }
                });
    }
}
