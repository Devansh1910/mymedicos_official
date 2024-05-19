package com.medical.my_medicos.activities.pg.activites.insiders;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.TypedValue;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.pg.activites.Neetexaminsider;
import com.medical.my_medicos.activities.pg.activites.PgprepActivity;
import com.medical.my_medicos.activities.pg.activites.ResultActivity;
import com.medical.my_medicos.activities.pg.activites.ResultActivityNeet;
import com.medical.my_medicos.activities.pg.adapters.WeeklyQuizAdapterinsider;
import com.medical.my_medicos.activities.pg.model.Neetpg;
import com.medical.my_medicos.activities.pg.model.QuizPGinsider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WeeklyQuizInsiderActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private WeeklyQuizAdapterinsider adapter;
    private ArrayList<QuizPGinsider> quizList;
    private LinearLayout questionNumberLayout;
    private boolean timerRunning = false;
    private ArrayList<QuizPGinsider> copy;
    private long remainingTimeInMillis;
    private int currentQuestionIndex = 0;
    private CountDownTimer countDownTimer;
    private TextView currentquestion;
    TextView timerTextView;
    private long timeLeftInMillis = 25 * 60 * 1000;
    private ArrayList<String> selectedOptionsList = new ArrayList<>();
    String id;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_quiz_insider);
        TextView title1=findViewById(R.id.setnamewillbehere);
        currentquestion = findViewById(R.id.currentquestion1);
        timerTextView = findViewById(R.id.timerTextView1);



        recyclerView = findViewById(R.id.recycler_view);
        Intent intent = getIntent();
        String str = intent.getStringExtra("Title1"); // Specialty
        String str1 = intent.getStringExtra("Title"); // Title
        quizList = new ArrayList<>();
        copy = new ArrayList<>();
        startTimer();


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference quizzCollection = db.collection("PGupload").document("Weekley").collection("Quiz");
        quizzCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                quizList.clear(); // Ensure this happens only once
                copy.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String speciality = document.getString("speciality");
                    String title = document.getString("title");
                    title1.setText(title);

                    if (str.equals(speciality) && str1.equals(title)) {
                        ArrayList<Map<String, Object>> dataList = (ArrayList<Map<String, Object>>) document.get("Data");
                        for (Map<String, Object> entry : dataList) {
                            String question = (String) entry.get("Question");

                            String correctAnswer = (String) entry.get("Correct");
                            String optionA = (String) entry.get("A");
                            String optionB = (String) entry.get("B");
                            String optionC = (String) entry.get("C");
                            String optionD = (String) entry.get("D");
                            String description = (String) entry.get("Description");
                            String imageUrl = (String) entry.get("Image");
                            id = document.getId();
                            QuizPGinsider quizQuestion;
                            if (imageUrl != null && !imageUrl.isEmpty()) {
                                quizQuestion = new QuizPGinsider(question, optionA, optionB, optionC, optionD, correctAnswer, imageUrl, description);
                            }else {
                                quizQuestion = new QuizPGinsider(question, optionA, optionB, optionC, optionD, correctAnswer, null, description);
                            }
                            copy.add(quizQuestion);
                            quizList.add(quizQuestion);
                            selectedOptionsList.add("");


                        }

                    }
                }
//                adapter.setQuizQuestions(quizList); // Set quiz questions to the adapter
                loadNextQuestion(quizList);
                updateQuestionNumber();
            } else {
                Log.e("FirestoreError", "Error getting documents: ", task.getException());
            }
        });
        TextView gotoQuestionButton = findViewById(R.id.Navigate);
        gotoQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeeklyQuizInsiderActivity.QuestionBottomSheetDialogFragment bottomSheetDialogFragment = new WeeklyQuizInsiderActivity.QuestionBottomSheetDialogFragment();
                bottomSheetDialogFragment.show(getSupportFragmentManager(), "QuestionNavigator");
            }
        });
        TextView prevButton = findViewById(R.id.BackButton);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPreviousQuestion();
            }
        });

       TextView nextButton = findViewById(R.id.nextButton2);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNextQuestion(quizList);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new WeeklyQuizAdapterinsider(WeeklyQuizInsiderActivity.this, quizList);
        recyclerView.setAdapter(adapter);


        TextView endButton = findViewById(R.id.endenabled);
        endButton.setOnClickListener(v -> handleEndButtonClick());

        LinearLayout toTheBackLayout = findViewById(R.id.totheback);
        toTheBackLayout.setOnClickListener(v -> showConfirmationDialog());

//        adapter.setOnCountdownFinishedListener(this::onCountdownFinished);
    }

    private void loadPreviousQuestion() {
        if (!selectedOptionsList.isEmpty()) { // Check if the selectedOptionsList is not empty
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--;
                adapter.setQuizQuestions(Collections.singletonList(copy.get(currentQuestionIndex)));
                adapter.setSelectedOption(selectedOptionsList.get(currentQuestionIndex));
                recyclerView.smoothScrollToPosition(currentQuestionIndex);
                updateQuestionNumber();
            } else {
                Toast.makeText(this, "This is the first question", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No selected options available", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToQuestion(int questionNumber) {
        if (questionNumber >= 0 && questionNumber < copy.size()) {
            currentQuestionIndex = questionNumber;
            adapter.setQuizQuestions(Collections.singletonList(copy.get(currentQuestionIndex)));
            adapter.setSelectedOption(selectedOptionsList.get(currentQuestionIndex));
            recyclerView.smoothScrollToPosition(currentQuestionIndex);
            updateQuestionNumber();
        } else {
            Toast.makeText(this, "Invalid question number", Toast.LENGTH_SHORT).show();
        }
    }
    private void updateQuestionNumber() {
        int totalQuestions = copy.size();
        String questionNumberText = (currentQuestionIndex + 1) + " / " + totalQuestions;
//        for (int i = 0; i < copy.size(); i++) {
//            QuizPGinsider quizQuestion = copy.get(i);
//            if (quizQuestion.getSelectedOption() != null) {
//
//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.WRAP_CONTENT,
//                        LinearLayout.LayoutParams.WRAP_CONTENT
//                );
//                TextView questionNumberTextView = (TextView) questionNumberLayout.getChildAt(i);
//                layoutParams.setMargins(8, 0, 8, 0); // Set margins between TextViews
//                questionNumberTextView.setLayoutParams(layoutParams);
//
//                questionNumberTextView.setBackground(getResources().getDrawable(R.drawable.circle_answered));
//                questionNumberTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//                questionNumberTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
//                questionNumberTextView.setPadding(48, 24, 48, 24);
//
//            } else {
//                TextView questionNumberTextView = (TextView) questionNumberLayout.getChildAt(i);
//                questionNumberTextView.setBackground(getResources().getDrawable(R.drawable.circle_outline_unanswered));
//            }
//            Log.d("asdfghjklllllllllll", "aaafdsghjkl");
//        }


        currentquestion.setText(questionNumberText);
    }

    private void loadNextQuestion(ArrayList<QuizPGinsider> quizList) {
        int q1=copy.size();
        if (currentQuestionIndex < q1) {
            Log.d("abasjdnajs", "Current Question Index: " + currentQuestionIndex);
            Log.d("abasjdnajs", "Quiz List Size: " + quizList.size());

            // Log each quiz question in the quizList
            for (int i = 0; i < q1; i++) {
                QuizPGinsider quizQuestion = copy.get(i);
                Log.d("QuizPGinsider", "Question " + (i + 1) + ": " + quizQuestion.getQuestion());
                // Log other properties of the quiz question as needed
            }

            QuizPGinsider currentQuestion = copy.get(currentQuestionIndex);
            adapter.setQuizQuestions(Collections.singletonList(currentQuestion));
            recyclerView.smoothScrollToPosition(currentQuestionIndex);
            updateQuestionNumber();
            currentQuestionIndex++;
        } else {
            Log.d("abasjdnajs", "Current Question Index: " + currentQuestionIndex);
            Log.d("abasjdnajs", "Quiz List Size: " + quizList.size());
            for (int i = 0; i < quizList.size(); i++) {
                QuizPGinsider quizQuestion = quizList.get(i);
                Log.d("QuizPGinsider", "Question " + (i + 1) + ": " + quizQuestion.getQuestion());
                // Log other properties of the quiz question as needed
            }
            showEndQuizConfirmation();

        }
    }



    private void showEndQuizConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("End Quiz");
        builder.setMessage("Are you sure you want to end the quiz?");

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handleEndButtonClick();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("End Quiz");
        builder.setMessage("Are you sure you want to end the quiz?");

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handleEndButtonClick();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void navigateToPrepareActivity() {
        Intent intent = new Intent(WeeklyQuizInsiderActivity.this, PgprepActivity.class);
        startActivity(intent);
        finish();
    }
    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                handleQuizEnd();
            }
        }.start();
    }

    private void handleQuizEnd() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        remainingTimeInMillis = timeLeftInMillis;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Time's Up!");
        builder.setMessage("Sorry, you've run out of time. The quiz will be ended.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                navigateToResultActivity();
            }
        });

        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timerTextView.setText(timeFormatted);
    }

    private void handleEndButtonClick() {
        ArrayList<String> userSelectedOptions = new ArrayList<>();

        for (QuizPGinsider quizQuestion : quizList) {
            if (quizQuestion.getSelectedOption() == null || quizQuestion.getSelectedOption().isEmpty()) {
                userSelectedOptions.add("Skip");
            }
            userSelectedOptions.add(quizQuestion.getSelectedOption());
        }

        ArrayList<String> results = compareAnswers(userSelectedOptions);

        Intent intent = new Intent(WeeklyQuizInsiderActivity.this, ResultActivity.class);
        intent.putExtra("questions", copy);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    private void showCustomToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, findViewById(R.id.customToastLayout));

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView text = layout.findViewById(R.id.customToastText);
        text.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    private ArrayList<String> compareAnswers(ArrayList<String> userSelectedOptions) {
        ArrayList<String> results = new ArrayList<>();

        for (int i = 0; i < quizList.size(); i++) {
            QuizPGinsider quizQuestion = quizList.get(i);
            String correctAnswer = quizQuestion.getCorrectAnswer();
            String userAnswer = userSelectedOptions.get(i);

            boolean isCorrect = correctAnswer.equals(userAnswer);

            results.add("Answer of Question " + (i + 1) + " is " + (isCorrect ? "Correct" : "Wrong"));
        }

        return results;
    }
    private void navigateToResultActivity() {
        Intent intent = new Intent(WeeklyQuizInsiderActivity.this, ResultActivity.class);
        intent.putExtra("questions", quizList);
        intent.putExtra("remainingTime", remainingTimeInMillis);
        intent.putExtra("id", id);
        startActivity(intent);
        finish();
    }

    public void onCountdownFinished() {
        loadNextQuestion(quizList);
    }
    public static class QuestionBottomSheetDialogFragment extends BottomSheetDialogFragment {
        private RecyclerView recyclerView;
        private WeeklyQuizInsiderActivity.QuestionNavigationAdapter adapter;

        @SuppressLint("MissingInflatedId")
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.bottom_sheet_layout_neet, container, false);
            recyclerView = view.findViewById(R.id.questionListRecyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            // Assuming there are 200 questions, adjust according to your actual count
            adapter = new WeeklyQuizInsiderActivity.QuestionNavigationAdapter(25, position -> {
                ((WeeklyQuizInsiderActivity) getActivity()).navigateToQuestion(position);
                dismiss();
            });
            recyclerView.setAdapter(adapter);

            return view;
        }
    }
    public static class QuestionNavigationAdapter extends RecyclerView.Adapter<WeeklyQuizInsiderActivity.QuestionNavigationAdapter.ViewHolder> {
        private final int itemCount;
        private final WeeklyQuizInsiderActivity.QuestionNavigationAdapter.OnItemClickListener listener;

        public QuestionNavigationAdapter(int itemCount, WeeklyQuizInsiderActivity.QuestionNavigationAdapter.OnItemClickListener listener) {
            this.itemCount = itemCount;
            this.listener = listener;
        }

        @NonNull
        @Override
        public WeeklyQuizInsiderActivity.QuestionNavigationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new WeeklyQuizInsiderActivity.QuestionNavigationAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull WeeklyQuizInsiderActivity.QuestionNavigationAdapter.ViewHolder holder, int position) {
            holder.textView.setText(String.format("Question %d", position + 1));
            holder.itemView.setOnClickListener(v -> listener.onItemClick(position));
        }

        @Override
        public int getItemCount() {
            return itemCount;
        }

        public interface OnItemClickListener {
            void onItemClick(int position);
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            ViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(android.R.id.text1);
            }
        }
    }


}
