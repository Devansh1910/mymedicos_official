package com.medical.my_medicos.activities.pg.activites.internalfragments;

import static androidx.fragment.app.FragmentManager.TAG;

import static org.slf4j.MDC.put;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.pg.adapters.PerDayPGAdapter;
import com.medical.my_medicos.activities.pg.model.PerDayPG;
import com.medical.my_medicos.activities.utils.ConstantsDashboard;
import com.medical.my_medicos.databinding.FragmentPreparationPgBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PreparationPgFragment extends Fragment {

    FragmentPreparationPgBinding binding;
    SwipeRefreshLayout swipeRefreshLayoutPreparation;
    LottieAnimationView timer;
    String quiztiddaya;
    PerDayPGAdapter perDayPGAdapter;
    ArrayList<PerDayPG> dailyquestionspg;
    FirebaseUser currentUser;
    LinearLayout nocardp;

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

        // Initialize currentUser
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getPhoneNumber();
        }

        swipeRefreshLayoutPreparation = binding.getRoot().findViewById(R.id.swipeRefreshLayoutPreparation);
        swipeRefreshLayoutPreparation.setOnRefreshListener(this::refreshContent);

        RecyclerView perDayQuestionsRecyclerView = binding.getRoot().findViewById(R.id.perdayquestions);

        if (perDayQuestionsRecyclerView == null) {
            Log.e("Fragment", "Empty");
            return rootView;
        }

        nocardp = binding.getRoot().findViewById(R.id.nocardpg);
        timer = binding.getRoot().findViewById(R.id.timer);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (currentUser != null) {
            String userId = currentUser.getPhoneNumber();
            db.collection("users")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @SuppressLint("RestrictedApi")
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
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
        } else {
            Log.e("Fragment", "CurrentUser is null");
        }

        initPerDayQuestions(quiztiddaya);
        getPerDayQuestions(quiztiddaya);

        return rootView;
    }

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

                                if (a != 0) {
                                    dailyquestionspg.add(perday);
                                    questionFound = true;

                                    // Update Firestore when the question is added
                                    updateFirestore(questionId);
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

    private void updateFirestore(String questionId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String phoneNumber = currentUser.getPhoneNumber();
            CollectionReference usersCollection = db.collection("users");

            // Query to find the document with matching phone number
            Query query = usersCollection.whereEqualTo("Phone Number", phoneNumber);

            query.get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            DocumentReference userDocRef = document.getReference();
                            DatabaseReference userRealtimeRef = database.getReference("profiles").child(phoneNumber);

                            Long currentStreak = document.getLong("Streak");
                            if (currentStreak == null) {
                                currentStreak = 0L;
                            }
                            long newStreakCount = currentStreak + 1;

                            // Update streak count and timestamp in Firestore
                            Map<String, Object> updateData = new HashMap<>();

                            updateData.put("lastQuizAttemptDate", Timestamp.now()); // Add timestamp field

                            userDocRef.update(updateData)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("Firestore", "Streak count updated successfully");
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("Firestore", "Error updating streak count", e);
                                    });

                            // Update streak count in Realtime Database
                            userRealtimeRef.child("Streak").setValue(newStreakCount)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("RealtimeDB", "Streak count updated successfully");
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("RealtimeDB", "Error updating streak count", e);
                                    });
                            userRealtimeRef.child("lastQuizAttemptDate").setValue(Timestamp.now())
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("RealtimeDB", "Streak count updated successfully");
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("RealtimeDB", "Error updating streak count", e);
                                    });
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firestore", "Error fetching user document", e);
                    });
        }
    }



    private boolean containsQuestionId(ArrayList<PerDayPG> list, String questionId) {
        for (PerDayPG question : list) {
            if (question.getidQuestion().equals(questionId)) {
                return true;
            }
        }
        return false;
    }

    private void refreshContent() {
        clearData();
        fetchData();
        swipeRefreshLayoutPreparation.setRefreshing(false);
    }

    private void clearData() {
        dailyquestionspg.clear();
    }

    void fetchData() {
        getPerDayQuestions(quiztiddaya);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
