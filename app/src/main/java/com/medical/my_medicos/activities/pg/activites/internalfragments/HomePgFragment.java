package com.medical.my_medicos.activities.pg.activites.internalfragments;

import static androidx.fragment.app.FragmentManager.TAG;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.media3.common.util.UnstableApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.home.HomeActivity;
import com.medical.my_medicos.activities.home.sidedrawer.NotificationActivity;
import com.medical.my_medicos.activities.pg.activites.extras.RecetUpdatesNoticeActivity;
import com.medical.my_medicos.activities.pg.activites.insiders.SpecialityPGInsiderActivity;
import com.medical.my_medicos.activities.pg.activites.internalfragments.intwernaladapters.ExamQuizAdapter;
import com.medical.my_medicos.activities.pg.adapters.PerDayPGAdapter;
import com.medical.my_medicos.activities.pg.adapters.QuestionBankPGAdapter;
import com.medical.my_medicos.activities.pg.adapters.WeeklyQuizAdapter;
import com.medical.my_medicos.activities.pg.model.PerDayPG;
import com.medical.my_medicos.activities.pg.model.QuestionPG;
import com.medical.my_medicos.activities.pg.model.QuizPG;
import com.medical.my_medicos.activities.utils.ConstantsDashboard;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.medical.my_medicos.databinding.FragmentHomePgBinding;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class HomePgFragment extends Fragment {

    LottieAnimationView timer;
    FragmentHomePgBinding binding;
    PerDayPGAdapter perDayPGAdapter;
    ArrayList<PerDayPG> dailyquestionspg;
    SwipeRefreshLayout swipeRefreshLayout;
    String quiztiddaya;
    LinearLayout nocardp;
    FirebaseUser currentUser;
    QuestionBankPGAdapter questionsAdapter;
    ArrayList<QuestionPG> questionsforpg;
    private ExamQuizAdapter quizAdapter;
    CardView gotoupdatesofpg;
    private ArrayList<QuizPG> quizpg;
    String title1;
    public static HomePgFragment newInstance() {
        HomePgFragment fragment = new HomePgFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomePgBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Bundle args = getArguments();
        if (args != null) {

            quizpg = new ArrayList<>();
            quizAdapter = new ExamQuizAdapter(requireContext(), quizpg);

            RecyclerView recyclerViewVideos = binding.specialexam;
            LinearLayoutManager layoutManagerVideos = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
            recyclerViewVideos.setLayoutManager(layoutManagerVideos);
            recyclerViewVideos.setAdapter(quizAdapter);

            getPaidExam(title1);
        } else {
            Log.e("ERROR", "Arguments are null in WeeklyQuizFragment");
        }

        gotoupdatesofpg = view.findViewById(R.id.gotoupdatesofpg);
        gotoupdatesofpg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), RecetUpdatesNoticeActivity.class);
                startActivity(i);
            }
        });

        TextView promoteBtn = view.findViewById(R.id.promotebtn);
        promoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Customize the content to share
                String appLink = "https://play.google.com/store/apps/details?id=com.medical.my_medicos";
                String message = "Check out our medical app!\nDownload now: " + appLink;

                // Create an Intent with ACTION_SEND
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, message);

                // Check if there's an app to handle the Intent
                if (shareIntent.resolveActivity(requireContext().getPackageManager()) != null) {
                    startActivity(Intent.createChooser(shareIntent, "Share via"));
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getPhoneNumber();
        }

        swipeRefreshLayout = binding.getRoot().findViewById(R.id.swipeRefreshLayoutPg);
        swipeRefreshLayout.setOnRefreshListener(this::refreshContent);

        RecyclerView perDayQuestionsRecyclerView = binding.getRoot().findViewById(R.id.perdayquestions);

        if (perDayQuestionsRecyclerView == null) {
            Log.e("Fragment", "Empty");
            return;
        }

        nocardp = binding.getRoot().findViewById(R.id.nocardpg);
        timer = binding.getRoot().findViewById(R.id.timer);

        showShimmer(true);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = currentUser.getPhoneNumber();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        showShimmer(false);
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                Map<String, Object> dataMap = document.getData();
                                String field1 = (String) dataMap.get("Phone Number");

                                if (field1 != null && currentUser.getPhoneNumber() != null) {
                                    int a = field1.compareTo(userId);
                                    Log.d("Issue with the userID", String.valueOf(a));

                                    if (a == 0) {
                                        Log.d("Can't get it", String.valueOf(a));
                                        quiztiddaya = ((String) dataMap.get("QuizToday"));
                                        break;
                                    } else {
                                        quiztiddaya = null;
                                    }
                                }
                            }
                        } else {
                            Log.d(ContentValues.TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        initPerDayQuestions(quiztiddaya);
        initSliderPg();
        initQuestionsBanks();
        getPerDayQuestions(quiztiddaya);
    }


    // For the Slider
    private void initSliderPg() {
        getRecentPgSlider();
    }

    void getRecentPgSlider() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest request = new StringRequest(Request.Method.GET, ConstantsDashboard.GET_PG_SLIDER_URL, response -> {
            try {
                JSONArray pgsliderArray = new JSONArray(response);
                for (int i = 0; i < pgsliderArray.length(); i++) {
                    JSONObject childObj = pgsliderArray.getJSONObject(i);
                    binding.carouselpghome.addData(
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

    // For the Per Day Questions
    void initPerDayQuestions(String QuiaToday) {
        dailyquestionspg = new ArrayList<>();
        perDayPGAdapter = new PerDayPGAdapter(getActivity(), dailyquestionspg);

        getPerDayQuestions(QuiaToday);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        binding.perdayquestions.setLayoutManager(layoutManager);
        binding.perdayquestions.setAdapter(perDayPGAdapter);
    }

    void getPerDayQuestions(String quiz) {
        Log.d("DEBUG", "getPerDayQuestions: Making API request");
        RequestQueue queue = Volley.newRequestQueue(getContext());

        String url = ConstantsDashboard.GET_DAILY_QUESTIONS_URL;
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            Log.d("VolleyResponse", response);
            try {
                Log.d("DEBUG", "getPerDayQuestions: Response received");
                JSONObject object = new JSONObject(response);
                if (object.getString("status").equals("success")) {
                    JSONArray perdayArray = object.getJSONArray("data");

                    boolean questionFound = false;

                    for (int i = 0; i < perdayArray.length(); i++) {
                        JSONObject childObj = perdayArray.getJSONObject(i);
                        PerDayPG perday = new PerDayPG(
                                childObj.getString("Question"),
                                childObj.getString("A"),
                                childObj.getString("B"),
                                childObj.getString("C"),
                                childObj.getString("D"),
                                childObj.getString("Correct"),
                                childObj.getString("id"),
                                childObj.getString("Description")
                        );
                        String questionId = childObj.getString("id");
                        Log.d("questionid", questionId);

                        if ((questionId != null) && (quiztiddaya != null)) {
                            if (!containsQuestionId(dailyquestionspg, questionId)) {
                                int a = questionId.compareTo(quiztiddaya);

                                if(a != 0){
                                    dailyquestionspg.add(perday);
                                    questionFound = true;
                                }

                            } else {
                                questionFound = true;
                            }
                        }
                    }

                    if (questionFound) {
                        nocardp.setVisibility(View.GONE);
                    } else {
                        nocardp.setVisibility(View.VISIBLE);
                    }

                    if (!dailyquestionspg.isEmpty()) {
                        perDayPGAdapter.notifyDataSetChanged();
                        Log.d("DEBUG", "getPerDayQuestions: Data added to the list");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.e("VolleyError", "Error: " + error.getMessage());
        });
        queue.add(request);
    }

    private boolean containsQuestionId(ArrayList<PerDayPG> list, String questionId) {
        for (PerDayPG question : list) {
            if (question.getidQuestion().equals(questionId)) {
                return true;
            }
        }
        return false;
    }

    // For the Suggested Exam
    void getPaidExam(String title) {
        showShimmer(true);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        List<String> subcollectionIds = new ArrayList<>();

        if (user != null) {
            showShimmer(false);
            String userId = user.getUid();

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
            title = "home";
        }

        CollectionReference quizzCollection = db.collection("PGupload").document("Weekley").collection("Quiz");
        Query query = quizzCollection;
        String finalTitle = title;

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String id = document.getId();

                        if (!subcollectionIds.contains(id)) {
                            String quizTitle = document.getString("title");
                            String speciality = document.getString("speciality");
                            Timestamp To = document.getTimestamp("to");

                            if (finalTitle.isEmpty() || finalTitle.equals("Home")) {
                                int r = speciality.compareTo(title1);
                                if (r == 0) {
                                    QuizPG quizday = new QuizPG(quizTitle, title1, To);
                                    quizpg.add(quizday);
                                }
                            } else {
                                int r = speciality.compareTo(finalTitle);
                                if (r == 0) {
                                    QuizPG quizday = new QuizPG(quizTitle, finalTitle, To);
                                    quizpg.add(quizday);
                                }
                            }
                        }
                    }
                    quizAdapter.notifyDataSetChanged();
                } else {
                    Log.d(ContentValues.TAG, "Error getting documents: ", task.getException());
                }
            }
        });

    }
    void initQuestionsBanks() {
        questionsforpg = new ArrayList<>();
        questionsAdapter = new QuestionBankPGAdapter(getActivity(), questionsforpg);

        getRecentQuestions();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.QB.setLayoutManager(layoutManager);
        binding.QB.setAdapter(questionsAdapter);
    }

    void getRecentQuestions() {
        RequestQueue queue = Volley.newRequestQueue(requireActivity());

        String url = ConstantsDashboard.GET_PG_QUESTIONBANK_URL_HOME;
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if(object.getString("status").equals("success")){
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
        }, error -> { });

        queue.add(request);
    }

    private void refreshContent() {
        clearData();
        fetchData();
        swipeRefreshLayout.setRefreshing(false);
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


    private void clearData() {
        dailyquestionspg.clear();
    }

    void fetchData() {
        getPerDayQuestions(quiztiddaya);
    }

}
