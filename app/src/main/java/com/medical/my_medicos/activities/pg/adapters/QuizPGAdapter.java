package com.medical.my_medicos.activities.pg.adapters;

import static androidx.media3.common.MediaLibraryInfo.TAG;

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

import com.bumptech.glide.Glide;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.pg.animations.CorrectAnswerActivity;
import com.medical.my_medicos.activities.pg.animations.WrongAnswerActivity;
import com.medical.my_medicos.activities.pg.model.QuizPG;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.medical.my_medicos.databinding.QuestionPerDayDesignBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

public class QuizPGAdapter extends RecyclerView.Adapter<QuizPGAdapter.QuizQuestionViewHolder> {

    private Context context;
    private ArrayList<QuizPG> quizquestions;
    QuizPGAdapter  QuizPGAdapter;
    private String selectedOption;
    private long lastSelectionTimestamp;

    public QuizPGAdapter(Context context, ArrayList<QuizPG> quiz) {
        this.context = context;
        this.quizquestions = quiz;
        this.lastSelectionTimestamp = 0;
    }

    @NonNull
    @Override
    public QuizQuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuizQuestionViewHolder(LayoutInflater.from(context).inflate(R.layout.question_per_day_design, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull QuizQuestionViewHolder holder, int position) {
        QuizPG quizquestion = quizquestions.get(position);

        Glide.with(context);
        holder.binding.questionspan.setText(quizquestion.getDailyQuestion());
        holder.binding.optionA.setText(quizquestion.getDailyQuestionA());
        holder.binding.optionB.setText(quizquestion.getDailyQuestionB());
        holder.binding.optionC.setText(quizquestion.getDailyQuestionC());
        holder.binding.optionD.setText(quizquestion.getDailyQuestionD());

        holder.binding.optionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleOptionClick(holder, "A");
            }
        });

        holder.binding.optionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleOptionClick(holder, "B");
            }
        });

        holder.binding.optionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleOptionClick(holder, "C");
            }
        });

        holder.binding.optionD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleOptionClick(holder, "D");
            }
        });

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String user=currentUser.getPhoneNumber();
        holder.binding.submitanswerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedOption != null) {
                    // Save the current timestamp
                    lastSelectionTimestamp = System.currentTimeMillis();
                    holder.binding.questionsbox.setVisibility(View.GONE);
                    QuizPG selectedQuiz = quizquestions.get(holder.getAdapterPosition());
                    String correctAnswer = selectedQuiz.getSubmitDailyQuestion();
                    String docId = Preferences.userRoot().get("docId", "");

                    if (selectedOption.equals(correctAnswer)) {
                        showCorrectAnswerPopup();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        CollectionReference usersCollection = db.collection("users");

                        Query query = usersCollection.whereEqualTo("Phone Number", user);

                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @OptIn(markerClass = UnstableApi.class) @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        updateDocument(document.getId(), String.valueOf(quizquestion.getidQuestion()));
                                    }
                                } else {
                                    Log.w(TAG, "Error getting documents.", task.getException());
                                }
                            }
                        });

                    } else {
                        showWrongAnswerPopup();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        CollectionReference usersCollection = db.collection("users");

                        Query query = usersCollection.whereEqualTo("Phone Number", user); // Replace "phoneNumber" with the actual field name

                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @OptIn(markerClass = UnstableApi.class) @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        updateDocument1(document.getId(), String.valueOf(quizquestion.getidQuestion()));
                                    }
                                } else {
                                    Log.w(TAG, "Error getting documents.", task.getException());
                                }
                            }
                        });
                    }
                } else {
                    showToast("Please select an option");
                }
            }
        });

        if (System.currentTimeMillis() - lastSelectionTimestamp >= 24 * 60 * 60 * 1000) {
            holder.binding.questionsbox.setVisibility(View.VISIBLE);
        } else {
            holder.binding.questionsbox.setVisibility(View.GONE);
        }
    }

    private void handleOptionClick(QuizQuestionViewHolder holder, String selectedOption) {
        // Set the background color for the selected option
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
    private void updateDocument(String documentId, String QuizToday) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(documentId);

        Map<String, Object> updates = new HashMap<>();
        updates.put("QuizToday", QuizToday);
        updates.put("CurrentTime", System.currentTimeMillis());

        updates.put("Streak", FieldValue.increment(1));

        docRef.update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @OptIn(markerClass = UnstableApi.class) @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                        Toast.makeText(context.getApplicationContext(), "Successfully Ended", Toast.LENGTH_SHORT).show();

                        Log.d("abc", "bcd");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @OptIn(markerClass = UnstableApi.class) @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }
    private void updateDocument1(String documentId, String QuizToday) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(documentId);

        Map<String, Object> updates = new HashMap<>();
        updates.put("QuizToday", QuizToday);
        updates.put("CurrentTime", System.currentTimeMillis());

        docRef.update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @OptIn(markerClass = UnstableApi.class) @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                        Toast.makeText(context.getApplicationContext(), "Successfully Ended", Toast.LENGTH_SHORT).show();

                        Log.d("abc", "bcd");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @OptIn(markerClass = UnstableApi.class) @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }

    private void resetOptionStyle(QuizQuestionViewHolder holder) {
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
        return quizquestions.size();
    }

    public class QuizQuestionViewHolder extends RecyclerView.ViewHolder {

        QuestionPerDayDesignBinding binding;

        public QuizQuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = QuestionPerDayDesignBinding.bind(itemView);
        }
    }
}
