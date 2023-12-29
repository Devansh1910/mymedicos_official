package com.medical.my_medicos.activities.pg.activites.internalfragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.news.News;
import com.medical.my_medicos.activities.pg.activites.PgprepActivity;
import com.medical.my_medicos.activities.pg.activites.extras.PreparationCategoryDisplayActivity;
import com.medical.my_medicos.activities.pg.activites.extras.adapter.ImportantPreprationAdapter;
import com.medical.my_medicos.activities.pg.activites.extras.adapter.RecentUpdatesAdapter;
import com.medical.my_medicos.activities.pg.activites.internalfragments.intwernaladapters.ExamQuizAdapter;
import com.medical.my_medicos.activities.pg.fragment.WeeklyQuizFragment;
import com.medical.my_medicos.activities.pg.model.QuizPG;
import com.medical.my_medicos.databinding.FragmentNeetExamBinding;
import com.medical.my_medicos.databinding.FragmentPreparationPgBinding;

import java.util.ArrayList;

public class PreparationPgFragment extends Fragment {

    FragmentPreparationPgBinding binding;
    private ProgressBar progressBar;
    private TextView progressText;
    SwipeRefreshLayout swipeRefreshLayoutPreparation;

    private ViewFlipper viewFlipperPrepration;
    private Handler handlerprepration;
    private LinearLayout dotsLayoutprepration;

    ImportantPreprationAdapter newsupdatespreparationAdapter;
    ArrayList<News> newsprepration;
    private final int AUTO_SCROLL_DELAY = 3000;

    CardView practivemcq;


    int i = 0;

    public static PreparationPgFragment newInstance() {
        PreparationPgFragment fragment = new PreparationPgFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPreparationPgBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        swipeRefreshLayoutPreparation = binding.getRoot().findViewById(R.id.swipeRefreshLayoutPreparation);
        swipeRefreshLayoutPreparation.setOnRefreshListener(this::refreshContent);

        progressBar = rootView.findViewById(R.id.progress_bar);
        progressText = rootView.findViewById(R.id.progress_text);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (i <= 100) {
                    progressText.setText("" + i);
                    progressBar.setProgress(i);
                    i++;
                    handler.postDelayed(this, 200);
                } else {
                    handler.removeCallbacks(this);
                }
            }
        }, 200);


        viewFlipperPrepration = rootView.findViewById(R.id.viewFlipperPrepration);
        dotsLayoutprepration = rootView.findViewById(R.id.dotsLayoutPrepration);
        handlerprepration = new Handler();

        addDotsPrepration();

        handlerprepration.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY);

        practivemcq = rootView.findViewById(R.id.practivemcq);
        practivemcq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), PreparationCategoryDisplayActivity.class);
                startActivity(i);
            }
        });


        initImportantUpdatesInPreparation();

        return rootView;
    }

    void initImportantUpdatesInPreparation() {
        newsprepration = new ArrayList<News>();
        newsupdatespreparationAdapter = new ImportantPreprationAdapter(getContext(), newsprepration);
        getRecentNewsUpdatesPrepration();

        // Use LinearLayoutManager with horizontal orientation
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.noticesexam.setLayoutManager(layoutManager);

        binding.noticesexam.setAdapter(newsupdatespreparationAdapter);
    }



    @SuppressLint("NotifyDataSetChanged")
    void getRecentNewsUpdatesPrepration() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("MedicalNews")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String newsType = document.getString("type");

                            if ("Notice".equals(newsType)) {
                                News newsItem = new News(
                                        document.getString("Title"),
                                        document.getString("thumbnail"),
                                        document.getString("Description"),
                                        document.getString("Time"),
                                        document.getString("URL"),
                                        newsType
                                );
                                newsprepration.add(newsItem);
                            }
                        }
                        newsupdatespreparationAdapter.notifyDataSetChanged();
                    } else {
                    }
                });
    }


    private void refreshContent() {
        swipeRefreshLayoutPreparation.setRefreshing(false);
    }


    private final Runnable autoScrollRunnable = new Runnable() {
        @Override
        public void run() {
            int currentChildIndex = viewFlipperPrepration.getDisplayedChild();
            int nextChildIndex = (currentChildIndex + 1) % viewFlipperPrepration.getChildCount();
            viewFlipperPrepration.setDisplayedChild(nextChildIndex);
            updateDotsPrepration(nextChildIndex);
            handlerprepration.postDelayed(this, AUTO_SCROLL_DELAY);
        }
    };

    private void addDotsPrepration() {
        for (int i = 0; i < viewFlipperPrepration.getChildCount(); i++) {
            ImageView dot = new ImageView(requireContext());
            dot.setImageDrawable(getResources().getDrawable(R.drawable.inactive_thumb));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 0, 8, 0);
            dotsLayoutprepration.addView(dot, params);
        }
        updateDotsPrepration(0);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        handlerprepration.removeCallbacksAndMessages(null);
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    private void updateDotsPrepration(int currentDotIndex) {
        if (!isAdded()) {
            return;
        }
        for (int i = 0; i < dotsLayoutprepration.getChildCount(); i++) {
            ImageView dot = (ImageView) dotsLayoutprepration.getChildAt(i);
            dot.setImageDrawable(getResources().getDrawable(
                    i == currentDotIndex ? R.drawable.custom_thumb : R.drawable.inactive_thumb
            ));
        }
    }
}
