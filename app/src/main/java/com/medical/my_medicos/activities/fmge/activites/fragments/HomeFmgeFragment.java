package com.medical.my_medicos.activities.fmge.activites.fragments;

import static android.content.Context.MODE_PRIVATE;
import static androidx.fragment.app.FragmentManager.TAG;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
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
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.fmge.adapters.FmgeAdapter;
import com.medical.my_medicos.activities.fmge.adapters.MaterialQbFmgeAdapter;
import com.medical.my_medicos.activities.fmge.adapters.VideoFmgeAdapter;
import com.medical.my_medicos.activities.fmge.model.MaterialFMGE;
import com.medical.my_medicos.activities.fmge.model.VideoFMGE;
import com.medical.my_medicos.activities.fmge.model.examination.QuizFMGE;
import com.medical.my_medicos.activities.home.HomeActivity;
import com.medical.my_medicos.activities.job.JobsApplyActivity;
import com.medical.my_medicos.activities.job.fragments.TermsandConditionDialogueFragment;
import com.medical.my_medicos.activities.login.bottom_controls.TermsandConditionsActivity;
import com.medical.my_medicos.activities.pg.activites.PgprepActivity;
import com.medical.my_medicos.activities.pg.activites.extras.RecetUpdatesNoticeActivity;
import com.medical.my_medicos.activities.pg.activites.extras.TermsandConditionsDialogueFragmentPg;
import com.medical.my_medicos.activities.pg.activites.internalfragments.intwernaladapters.ExamQuizAdapter;
import com.medical.my_medicos.activities.pg.adapters.PerDayPGAdapter;
import com.medical.my_medicos.activities.pg.adapters.QuestionBankPGAdapter;
import com.medical.my_medicos.activities.pg.adapters.VideoPGAdapter;
import com.medical.my_medicos.activities.pg.model.PerDayPG;
import com.medical.my_medicos.activities.pg.model.QuestionPG;
import com.medical.my_medicos.activities.pg.model.QuizPG;
import com.medical.my_medicos.activities.pg.model.VideoPG;
import com.medical.my_medicos.activities.utils.ConstantsDashboard;
import com.medical.my_medicos.databinding.FragmentHomeFmgeBinding;
import com.medical.my_medicos.databinding.FragmentHomePgBinding;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeFmgeFragment extends Fragment {

    LottieAnimationView timer;
    FragmentHomeFmgeBinding binding;
    SwipeRefreshLayout swipeRefreshLayout;
    String quiztiddaya;
    LinearLayout nocardp;
    FirebaseUser currentUser;
    MaterialQbFmgeAdapter materialQBFMGEAdapter;
    VideoFmgeAdapter videoFmgeAdapter;
    ArrayList<MaterialFMGE> questionsforfmge;
    ArrayList<VideoFMGE> videoforfmge;
    private FmgeAdapter fmgeAdapter;
    private ArrayList<QuizFMGE> quizpg;
    String title1;

    private TermsandConditionsDialogueFragmentPg dialog;

    public static HomeFmgeFragment newInstance() {
        HomeFmgeFragment fragment = new HomeFmgeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeFmgeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        showFirstTimePopup();

        Bundle args = getArguments();
        if (args != null) {

            quizpg = new ArrayList<>();
            fmgeAdapter = new FmgeAdapter(requireContext(), quizpg);

            RecyclerView recyclerViewVideos = binding.specialexamfmge;
            LinearLayoutManager layoutManagerVideos = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
            recyclerViewVideos.setLayoutManager(layoutManagerVideos);
            recyclerViewVideos.setAdapter(fmgeAdapter);

            getPaidExam(title1);
        } else {
            Log.e("ERROR", "Arguments are null in WeeklyQuizFragment");
        }

        return view;
    }

    private void showFirstTimePopup() {
        SharedPreferences prefs = getActivity().getSharedPreferences("FmgePrefs", MODE_PRIVATE);
        boolean isFirstLaunch = prefs.getBoolean("isFirstLaunch", true);

        if (isFirstLaunch) {
            View popupView = LayoutInflater.from(requireContext()).inflate(R.layout.custompopupforpg, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setView(popupView);

            Button agreeButton = popupView.findViewById(R.id.agreepg);
            Button disagreeButton = popupView.findViewById(R.id.visit);
            ImageView closeOpt = popupView.findViewById(R.id.closebtndialogue);

            AlertDialog dialog = builder.create();

            agreeButton.setOnClickListener(v -> {
                dialog.dismiss();
            });

            disagreeButton.setOnClickListener(v -> {
                dialog.dismiss();
                Intent intent = new Intent(requireContext(), TermsandConditionsActivity.class);
                Toast.makeText(requireContext(), "Redirecting..", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            });

            closeOpt.setOnClickListener(v -> {
                dialog.dismiss();
                Intent intent = new Intent(requireContext(), HomeActivity.class);
                Toast.makeText(requireContext(), "Returning back...", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                dialog.dismiss();
            });

            dialog.show();
            prefs.edit().putBoolean("isFirstLaunch", false).apply();
        }
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

        initSliderFMGE();
        initQuestionsBanksFMGE();
        initVideoBankFMGE();
    }

    // For the Slider ....
    private void initSliderFMGE() {
        getRecentPgSlider();
    }
    void getRecentPgSlider() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest request = new StringRequest(Request.Method.GET, ConstantsDashboard.GET_FMGE_SLIDER_URL, response -> {
            try {
                JSONArray fmgesliderArray = new JSONArray(response);
                for (int i = 0; i < fmgesliderArray.length(); i++) {
                    JSONObject childObj = fmgesliderArray.getJSONObject(i);
                    binding.carouselfmgehome.addData(
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

    // For the Suggested Exam ....
    void getPaidExam(String title) {
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
                                int r = speciality.compareTo(finalTitle);
                                if (r == 0) {
                                    QuizFMGE quizday = new QuizFMGE(quizTitle, title1, To);
                                    quizpg.add(quizday);
                                }
                            } else {
                                int r = speciality.compareTo(finalTitle);
                                if (r == 0) {
                                    QuizFMGE quizday = new QuizFMGE(quizTitle, finalTitle, To);
                                    quizpg.add(quizday);
                                }
                            }
                        }
                    }
                    fmgeAdapter.notifyDataSetChanged();
                } else {
                    Log.d(ContentValues.TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    // For the Question Bank ........
    void initQuestionsBanksFMGE() {
        questionsforfmge = new ArrayList<>();
        materialQBFMGEAdapter = new MaterialQbFmgeAdapter(getActivity(), questionsforfmge);

        getRecentQuestionsFMGE();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.QBMaterial.setLayoutManager(layoutManager);
        binding.QBMaterial.setAdapter(materialQBFMGEAdapter);
    }
    void getRecentQuestionsFMGE() {
        RequestQueue queue = Volley.newRequestQueue(requireActivity());

        String url = ConstantsDashboard.GET_FMGE_QUESTIONBANK_URL_HOME;
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if(object.getString("status").equals("success")){
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject childObj = array.getJSONObject(i);
                        MaterialFMGE materialFMGEItem = new MaterialFMGE(
                                childObj.getString("Title"),
                                childObj.getString("Description"),
                                childObj.getString("Time"),
                                childObj.getString("file")
                        );
                        questionsforfmge.add(materialFMGEItem);
                    }
                    materialQBFMGEAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> { });

        queue.add(request);
    }

    // For the Videos .........
    void initVideoBankFMGE() {
        videoforfmge = new ArrayList<>();
        videoFmgeAdapter = new VideoFmgeAdapter(getActivity(), videoforfmge);

        getRecentVideosFMGE();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.VideosFMGE.setLayoutManager(layoutManager);
        binding.VideosFMGE.setAdapter(videoFmgeAdapter);
    }
    void getRecentVideosFMGE() {

        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        String url = ConstantsDashboard.GET_FMGE_VIDEOS_URL_HOME;
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if(object.getString("status").equals("success")){
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject childObj = array.getJSONObject(i);
                        VideoFMGE videoFMGEItem = new VideoFMGE(
                                childObj.getString("Title"),
                                childObj.getString("Thumbnail"),
                                childObj.getString("Time"),
                                childObj.getString("URL")
                        );
                        videoforfmge.add(videoFMGEItem);
                    }
                    videoFmgeAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        });
        queue.add(request);
    }

    //... Refresh........
    private void refreshContent() {
        swipeRefreshLayout.setRefreshing(false);
    }

    //... Shimmer.....
    private void showShimmer(boolean show) {
        if (show) {
            binding.shimmercomeup.setVisibility(View.VISIBLE);
            binding.shimmercomeup.playAnimation();
        } else {
            binding.shimmercomeup.setVisibility(View.GONE);
            binding.shimmercomeup.cancelAnimation();
        }
    }
}
