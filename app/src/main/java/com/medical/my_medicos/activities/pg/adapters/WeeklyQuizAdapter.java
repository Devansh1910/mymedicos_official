package com.medical.my_medicos.activities.pg.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.media3.common.util.UnstableApi;
import androidx.recyclerview.widget.RecyclerView;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.pg.animations.CorrectAnswerActivity;
import com.medical.my_medicos.activities.pg.animations.WrongAnswerActivity;
import com.medical.my_medicos.activities.pg.model.QuizPG;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.medical.my_medicos.databinding.QuestionQuizDesignWeeklyBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

import static androidx.media3.common.MediaLibraryInfo.TAG;

public class WeeklyQuizAdapter extends RecyclerView.Adapter<WeeklyQuizAdapter.WeeklyQuizQuestionViewHolder> {

    private Context context;
    private ArrayList<QuizPG> quizquestionsweekly;
    private String quizTitle;
    private long lastSelectionTimestamp;
    private String selectedOption;

    public WeeklyQuizAdapter(Context context, ArrayList<QuizPG> quiz) {
        this.context = context;
        this.quizquestionsweekly = quiz;
        this.quizTitle = quizTitle;
        this.lastSelectionTimestamp = 0;
        this.selectedOption = null;
    }

    @NonNull
    @Override
    public WeeklyQuizQuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeeklyQuizQuestionViewHolder(LayoutInflater.from(context).inflate(R.layout.question_quiz_design_weekly, parent, false));
    }

    @UnstableApi @Override
    public void onBindViewHolder(@NonNull WeeklyQuizQuestionViewHolder holder, int position) {
        QuizPG quizquestion = quizquestionsweekly.get(position);

        holder.binding.questionspan.setText(quizquestion.getQuestion());
        holder.binding.questiontitle.setText(quizquestion.getTitleOfSet());
        holder.binding.optionA.setText(quizquestion.getOptionA());
        holder.binding.optionB.setText(quizquestion.getOptionB());
        holder.binding.optionC.setText(quizquestion.getOptionC());
        holder.binding.optionD.setText(quizquestion.getOptionD());

        holder.binding.optionA.setOnClickListener(view -> handleOptionClick(holder, "A"));
        holder.binding.optionB.setOnClickListener(view -> handleOptionClick(holder, "B"));
        holder.binding.optionC.setOnClickListener(view -> handleOptionClick(holder, "C"));
        holder.binding.optionD.setOnClickListener(view -> handleOptionClick(holder, "D"));

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String user = currentUser.getPhoneNumber();

        holder.binding.submitanswerbtn.setOnClickListener(view -> {
            if (selectedOption != null) {
                lastSelectionTimestamp = System.currentTimeMillis();
                holder.binding.questionsbox.setVisibility(View.GONE);
                QuizPG selectedQuiz = quizquestionsweekly.get(holder.getAdapterPosition());
                String correctAnswer = selectedQuiz.getCorrectAnswer();
                String docId = Preferences.userRoot().get("docId", "");

                if (selectedOption.equals(correctAnswer)) {
                    showCorrectAnswerPopup();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference usersCollection = db.collection("users");

                    Query query = usersCollection.whereEqualTo("Phone Number", user);

                    query.get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                updateDocument(document.getId(), String.valueOf(quizquestion.getIdQuestion()));
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    });
                } else {
                    showWrongAnswerPopup();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference usersCollection = db.collection("users");

                    Query query = usersCollection.whereEqualTo("Phone Number", user);

                    query.get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                updateDocument1(document.getId(), String.valueOf(quizquestion.getIdQuestion()));
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    });
                }
            } else {
                showToast("Please select an option");
            }
        });

        if (System.currentTimeMillis() - lastSelectionTimestamp >= 24 * 60 * 60 * 1000) {
            holder.binding.questionsbox.setVisibility(View.VISIBLE);
        } else {
            holder.binding.questionsbox.setVisibility(View.GONE);
        }
    }

    private void handleOptionClick(WeeklyQuizQuestionViewHolder holder, String selectedOption) {
        resetOptionStyle(holder);
        switch (selectedOption) {
            case "A":
                setOptionSelectedStyle(holder.binding.optionA);
                break;
            case "B":
                setOptionSelectedStyle(holder.binding.optionB);
                break;
            case "C":
                setOptionSelectedStyle(holder.binding.optionC);
                break;
            case "D":
                setOptionSelectedStyle(holder.binding.optionD);
                break;
        }
        this.selectedOption = selectedOption;
    }

    private void setOptionSelectedStyle(TextView option) {
        option.setBackgroundResource(R.drawable.selectedoptionbk);
        option.setTextColor(Color.WHITE);
        option.setTypeface(null, Typeface.BOLD);
    }

    @UnstableApi private void updateDocument(String documentId, String quizToday) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(documentId);

        Map<String, Object> updates = new HashMap<>();
        updates.put("WeeklyQuiz", quizToday);
        updates.put("CurrentTime", System.currentTimeMillis());
        updates.put("StreakWeekly", FieldValue.increment(1));

        docRef.update(updates)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                    Toast.makeText(context.getApplicationContext(), "Successfully Ended", Toast.LENGTH_SHORT).show();
                    Log.d("abc", "bcd");
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }

    @OptIn(markerClass = UnstableApi.class) private void updateDocument1(String documentId, String quizToday) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(documentId);

        Map<String, Object> updates = new HashMap<>();
        updates.put("WeeklyQuiz", quizToday);
        updates.put("CurrentTime", System.currentTimeMillis());

        docRef.update(updates)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                    Toast.makeText(context.getApplicationContext(), "Successfully Ended", Toast.LENGTH_SHORT).show();
                    Log.d("abc", "bcd");
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }

    private void resetOptionStyle(WeeklyQuizQuestionViewHolder holder) {
        holder.binding.optionA.setBackgroundResource(R.drawable.categorynewbk);
        holder.binding.optionA.setTextColor(Color.BLACK);
        holder.binding.optionA.setTypeface(null, Typeface.NORMAL);

        holder.binding.optionB.setBackgroundResource(R.drawable.categorynewbk);
        holder.binding.optionB.setTextColor(Color.BLACK);
        holder.binding.optionB.setTypeface(null, Typeface.NORMAL);

        holder.binding.optionC.setBackgroundResource(R.drawable.categorynewbk);
        holder.binding.optionC.setTextColor(Color.BLACK);
        holder.binding.optionC.setTypeface(null, Typeface.NORMAL);

        holder.binding.optionD.setBackgroundResource(R.drawable.categorynewbk);
        holder.binding.optionD.setTextColor(Color.BLACK);
        holder.binding.optionD.setTypeface(null, Typeface.NORMAL);
    }

    private void showCorrectAnswerPopup() {
        Intent intent = new Intent(context, CorrectAnswerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    private void showWrongAnswerPopup() {
        Intent intent = new Intent(context, WrongAnswerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return quizquestionsweekly.size();
    }

    public class WeeklyQuizQuestionViewHolder extends RecyclerView.ViewHolder {

        QuestionQuizDesignWeeklyBinding binding;

        public WeeklyQuizQuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = QuestionQuizDesignWeeklyBinding.bind(itemView);
        }
    }
}

