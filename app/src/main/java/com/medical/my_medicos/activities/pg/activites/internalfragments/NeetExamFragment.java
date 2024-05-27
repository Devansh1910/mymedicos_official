package com.medical.my_medicos.activities.pg.activites.internalfragments;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.pg.activites.internalfragments.intwernaladapters.ExamQuizAdapter;
import com.medical.my_medicos.activities.pg.model.QuizPG;
import com.medical.my_medicos.databinding.FragmentNeetExamBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NeetExamFragment extends Fragment {

    private FragmentNeetExamBinding binding;
    private ViewFlipper viewFlipperPg;
    private Handler handlerpg;
    private ArrayList<QuizPG> quizpgneet;
    private ExamQuizAdapter quizAdapterneet;
    SwipeRefreshLayout swipeRefreshLayoutPg;
    private LinearLayout dotsLayoutpg;
    String title2;
    FirebaseUser currentUser;

    private final int AUTO_SCROLL_DELAY = 3000;

    public static NeetExamFragment newInstance() {
        NeetExamFragment fragment = new NeetExamFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNeetExamBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        Bundle args = getArguments();
        if (args != null) {
            quizpgneet = new ArrayList<>();
            quizAdapterneet = new ExamQuizAdapter(requireContext(), quizpgneet);

            RecyclerView recyclerViewVideos = binding.specialexam;
            LinearLayoutManager layoutManagerVideos = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
            recyclerViewVideos.setLayoutManager(layoutManagerVideos);
            recyclerViewVideos.setAdapter(quizAdapterneet);

            getPaidExamNeet(title2);
        } else {
            Log.e("ERROR", "Arguments are null in WeeklyQuizFragment");
        }

        viewFlipperPg = rootView.findViewById(R.id.viewFlipperpg);
        dotsLayoutpg = rootView.findViewById(R.id.dotsLayoutpg);
        handlerpg = new Handler();

        addDotspg();

        handlerpg.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getPhoneNumber();
        }

        swipeRefreshLayoutPg = binding.getRoot().findViewById(R.id.swipeRefreshLayoutPg);
        swipeRefreshLayoutPg.setOnRefreshListener(this::refreshContent);

        RecyclerView perDayQuestionsRecyclerView = binding.getRoot().findViewById(R.id.perdayquestions);

        if (perDayQuestionsRecyclerView == null) {
            Log.e("Fragment", "Empty");
            return;
        }

        showShimmer(true);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = currentUser.getPhoneNumber();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    }
                });
    }

    void getPaidExamNeet(String title) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        List<String> subcollectionIds = new ArrayList<>();

        if (user != null) {
            String userId = user.getPhoneNumber();

            CollectionReference quizResultsCollection = db.collection("QuizResults").document(userId).collection("Exam");

            quizResultsCollection.get()
                    .addOnCompleteListener(subcollectionTask -> {
                        if (subcollectionTask.isSuccessful()) {
                            for (QueryDocumentSnapshot subdocument : subcollectionTask.getResult()) {
                                String subcollectionId = subdocument.getId();
                                subcollectionIds.add(subcollectionId);
                                Log.d("Subcollection ID", subcollectionId);
                            }
                            for (String id : subcollectionIds) {
                                Log.d("All Subcollection IDs", id);
                            }
                        } else {
                            Log.e("Subcollection ID", "Error fetching subcollections", subcollectionTask.getException());
                        }
                    });
        }

        if (title == null || title.isEmpty()) {
            title = "Exam";
        }
        CollectionReference quizzCollection = db.collection("PGupload").document("Weekley").collection("Quiz");
        Query query = quizzCollection;
        String finalTitle = title;
        query.orderBy("from",Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String id = document.getId();

                        // Check if the document ID is present in the subcollectionIds array
                        if (!subcollectionIds.contains(id)) {
                            String quizTitle = document.getString("title");
                            String speciality = document.getString("speciality");
                            Timestamp To = document.getTimestamp("to");
                            Log.d("title sdnvkjsbdv",quizTitle);

                            if (finalTitle.isEmpty() || finalTitle.equals("Exam")) {
                                if (finalTitle != null && speciality != null) {
                                    int r = speciality.compareTo(finalTitle);
                                    if (r == 0) {
                                        QuizPG quizday = new QuizPG(quizTitle, finalTitle, To);
                                        quizpgneet.add(quizday);
                                    }
                                    quizAdapterneet.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                    quizAdapterneet.notifyDataSetChanged();
                } else {
                    Log.d(ContentValues.TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private final Runnable autoScrollRunnable = new Runnable() {
        @Override
        public void run() {
            int currentChildIndex = viewFlipperPg.getDisplayedChild();
            int nextChildIndex = (currentChildIndex + 1) % viewFlipperPg.getChildCount();
            viewFlipperPg.setDisplayedChild(nextChildIndex);
            updateDotspg(nextChildIndex);
            handlerpg.postDelayed(this, AUTO_SCROLL_DELAY);
        }
    };

    private void addDotspg() {
        for (int i = 0; i < viewFlipperPg.getChildCount(); i++) {
            ImageView dot = new ImageView(requireContext());
            dot.setImageDrawable(getResources().getDrawable(R.drawable.inactive_thumb));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 0, 8, 0);
            dotsLayoutpg.addView(dot, params);
        }
        updateDotspg(0);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        handlerpg.removeCallbacksAndMessages(null);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void updateDotspg(int currentDotIndex) {
        if (!isAdded()) {
            return;
        }
        for (int i = 0; i < dotsLayoutpg.getChildCount(); i++) {
            ImageView dot = (ImageView) dotsLayoutpg.getChildAt(i);
            dot.setImageDrawable(getResources().getDrawable(
                    i == currentDotIndex ? R.drawable.custom_thumb : R.drawable.inactive_thumb
            ));
        }
    }

    private void showShimmer(boolean show) {
        if (show) {
            binding.shimmercomeup.setVisibility(View.VISIBLE);
            binding.shimmercomeup.playAnimation();
        } else {
            binding.shimmercomeup.setVisibility(View.GONE);
            binding.shimmercomeup.cancelAnimation();
        }
    }

    private void refreshContent() {
        swipeRefreshLayoutPg.setRefreshing(false);
    }
}
