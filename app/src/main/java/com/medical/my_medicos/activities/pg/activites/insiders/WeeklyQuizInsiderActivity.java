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
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
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
import com.medical.my_medicos.activities.pg.activites.PgprepActivity;
import com.medical.my_medicos.activities.pg.activites.ResultActivity;
import com.medical.my_medicos.activities.pg.adapters.WeeklyQuizAdapterinsider;
import com.medical.my_medicos.activities.pg.model.QuizPGinsider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WeeklyQuizInsiderActivity extends AppCompatActivity implements WeeklyQuizAdapterinsider.OnOptionInteractionListener {
    private RecyclerView recyclerView;
    private WeeklyQuizAdapterinsider adapter;
    private ArrayList<QuizPGinsider> quizList;
    private ArrayList<QuizPGinsider> copy;
    private int currentQuestionIndex = 0;
    private CountDownTimer countDownTimer;
    private TextView currentquestion;
    private TextView timerTextView;
    private long timeLeftInMillis = 25 * 60 * 1000;
    private long remainingTimeInMillis;
    public ArrayList<String> selectedOptionsList = new ArrayList<>();
    private String id;
    private CheckBox markForReviewCheckBox;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_quiz_insider);
        TextView title1 = findViewById(R.id.setnamewillbehere);
        currentquestion = findViewById(R.id.currentquestion1);
        timerTextView = findViewById(R.id.timerTextView1);
        markForReviewCheckBox = findViewById(R.id.markForReviewCheckBox);

        recyclerView = findViewById(R.id.recycler_view);
        Intent intent = getIntent();
        String str = intent.getStringExtra("Title1"); // Specialty
        String str1 = intent.getStringExtra("Title"); // Title
        title1.setText(str1);
        quizList = new ArrayList<>();
        copy = new ArrayList<>();
        startTimer();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference quizzCollection = db.collection("PGupload").document("Weekley").collection("Quiz");
        quizzCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                quizList.clear();
                copy.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String speciality = document.getString("speciality");
                    String title = document.getString("title");

                    if (str.equals(speciality) && str1.equals(title)) {
                        title1.setText(title);
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
                            } else {
                                quizQuestion = new QuizPGinsider(question, optionA, optionB, optionC, optionD, correctAnswer, null, description);
                            }
                            copy.add(quizQuestion);
                            quizList.add(quizQuestion);
                            selectedOptionsList.add(null); // Ensure selectedOptionsList is initialized
                        }
                    }
                }
                loadNextQuestion(copy);
                updateQuestionNumber();
            } else {
                Log.e("FirestoreError", "Error getting documents: ", task.getException());
            }
        });

        markForReviewCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            QuizPGinsider currentQuestion = copy.get(currentQuestionIndex);
            currentQuestion.setMarkedForReview(isChecked);
            refreshNavigationGrid();
        });

        TextView gotoQuestionButton = findViewById(R.id.Navigate);
        gotoQuestionButton.setOnClickListener(v -> {
            WeeklyQuizInsiderActivity.QuestionBottomSheetDialogFragment bottomSheetDialogFragment = WeeklyQuizInsiderActivity.QuestionBottomSheetDialogFragment.newInstance(selectedOptionsList, copy);
            bottomSheetDialogFragment.show(getSupportFragmentManager(), "QuestionNavigator");
        });

        TextView prevButton = findViewById(R.id.BackButton);
        prevButton.setOnClickListener(v -> loadPreviousQuestion());

        TextView nextButton = findViewById(R.id.nextButton2);
        nextButton.setOnClickListener(v -> loadNextQuestion(copy));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new WeeklyQuizAdapterinsider(this, quizList, this);
        Log.d("BottomSheetFragment 10", "Weeklyquizinsider activity" + currentQuestionIndex);
        recyclerView.setAdapter(adapter);

        TextView endButton = findViewById(R.id.endenabled);
        endButton.setOnClickListener(v -> handleEndButtonClick());

        LinearLayout toTheBackLayout = findViewById(R.id.totheback);
        toTheBackLayout.setOnClickListener(v -> showConfirmationDialog());
    }

    private void loadPreviousQuestion() {
        if (!selectedOptionsList.isEmpty()) {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--;
                adapter.setQuizQuestions(Collections.singletonList(copy.get(currentQuestionIndex)));
                adapter.setSelectedOption(selectedOptionsList.get(currentQuestionIndex));
                recyclerView.smoothScrollToPosition(currentQuestionIndex);
                adapter.setcurrentquestionindex(currentQuestionIndex);
                updateQuestionNumber();
                markForReviewCheckBox.setOnCheckedChangeListener(null);
                markForReviewCheckBox.setChecked(copy.get(currentQuestionIndex).isMarkedForReview());
                markForReviewCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    QuizPGinsider currentQuestion = copy.get(currentQuestionIndex);
                    currentQuestion.setMarkedForReview(isChecked);
                    refreshNavigationGrid();
                });
            } else {
                Toast.makeText(this, "This is the first question", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No selected options available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onOptionSelected(int questionIndex, String selectedOption) {
        selectedOptionsList.set(questionIndex, selectedOption);
        Log.d("BottomSheetFragment", "Selected options list size: " + selectedOptionsList.size());
        for (int i = 0; i < selectedOptionsList.size(); i++) {
            Log.d("BottomSheetFragment 5", "Index " + i + ": " + selectedOptionsList.get(i));
        }
        Log.d("WeeklyQuizInsiderActivity", "Selected option at index " + questionIndex + ": " + selectedOption);
        refreshNavigationGrid();
    }

    public void refreshNavigationGrid() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("QuestionNavigator");
        if (fragment instanceof QuestionBottomSheetDialogFragment) {
            Log.d("BottomSheetFragment", "Fragment found with tag 'QuestionNavigator', updating grid");
            ((QuestionBottomSheetDialogFragment) fragment).updateGrid(selectedOptionsList);
            for (int i = 0; i < selectedOptionsList.size(); i++) {
                Log.d("BottomSheetFragment 3", "Index " + i + ": " + selectedOptionsList.get(i));
            }
            Log.d("BottomSheetFragment", "Updated grid with selected options list size: " + selectedOptionsList.size());
        } else {
            Log.d("BottomSheetFragment", "No fragment found with tag 'QuestionNavigator'");
        }
    }

    private void navigateToQuestion(int questionNumber) {
        if (questionNumber >= 0 && questionNumber < copy.size()) {
            currentQuestionIndex = questionNumber;
            adapter.setQuizQuestions(Collections.singletonList(copy.get(currentQuestionIndex)));
            adapter.setSelectedOption(selectedOptionsList.get(currentQuestionIndex));
            adapter.setcurrentquestionindex(currentQuestionIndex);
            recyclerView.smoothScrollToPosition(currentQuestionIndex);
            updateQuestionNumber();
            markForReviewCheckBox.setOnCheckedChangeListener(null);
            markForReviewCheckBox.setChecked(copy.get(currentQuestionIndex).isMarkedForReview());
            markForReviewCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                QuizPGinsider currentQuestion = copy.get(currentQuestionIndex);
                currentQuestion.setMarkedForReview(isChecked);
                refreshNavigationGrid();
            });
        } else {
            Toast.makeText(this, "Invalid question number", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateQuestionNumber() {
        int totalQuestions = copy.size();
        String questionNumberText = (currentQuestionIndex + 1) + " / " + totalQuestions;
        currentquestion.setText(questionNumberText);
    }

    private void loadNextQuestion(ArrayList<QuizPGinsider> quizList) {
        int q1 = copy.size();
        if (currentQuestionIndex < q1) {
            Log.d("abasjdnajs", "Current Question Index: " + currentQuestionIndex);
            Log.d("abasjdnajs", "Quiz List Size: " + quizList.size());
            for (int i = 0; i < q1; i++) {
                QuizPGinsider quizQuestion = copy.get(i);
                Log.d("QuizPGinsider", "Question " + (i + 1) + ": " + quizQuestion.getQuestion());
            }
            adapter.setcurrentquestionindex(currentQuestionIndex);
            final QuizPGinsider currentQuestion = copy.get(currentQuestionIndex);
            adapter.setQuizQuestions(Collections.singletonList(currentQuestion));
            recyclerView.smoothScrollToPosition(currentQuestionIndex);
            updateQuestionNumber();
            currentQuestionIndex++;
            markForReviewCheckBox.setOnCheckedChangeListener(null);
            markForReviewCheckBox.setChecked(copy.get(currentQuestionIndex).isMarkedForReview());
            markForReviewCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {

                currentQuestion.setMarkedForReview(isChecked);
                refreshNavigationGrid();
            });
        } else {
            Log.d("abasjdnajs", "Current Question Index: " + currentQuestionIndex);
            Log.d("abasjdnajs", "Quiz List Size: " + quizList.size());
            for (int i = 0; i < quizList.size(); i++) {
                QuizPGinsider quizQuestion = quizList.get(i);
                Log.d("QuizPGinsider", "Question " + (i + 1) + ": " + quizQuestion.getQuestion());
            }
            showEndQuizConfirmation();
        }
    }

    private void showEndQuizConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("End Quiz");
        builder.setMessage("Are you sure you want to end the quiz?");
        builder.setPositiveButton("Confirm", (dialog, which) -> handleEndButtonClick());
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("End Quiz");
        builder.setMessage("Are you sure you want to end the quiz?");
        builder.setPositiveButton("Confirm", (dialog, which) -> handleEndButtonClick());
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
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
                boolean timerRunning = false;
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
        builder.setPositiveButton("OK", (dialog, which) -> navigateToResultActivity());
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
        loadNextQuestion(copy);
    }

    public static class QuestionBottomSheetDialogFragment extends BottomSheetDialogFragment {
        private GridView gridView;
        private QuestionNavigationAdapter adapter;

        public static QuestionBottomSheetDialogFragment newInstance(ArrayList<String> selectedOptionsList, ArrayList<QuizPGinsider> quizQuestions) {
            QuestionBottomSheetDialogFragment fragment = new QuestionBottomSheetDialogFragment();
            Bundle args = new Bundle();
            args.putStringArrayList("selectedOptionsList", selectedOptionsList);
            args.putSerializable("quizQuestions", quizQuestions);
            for (int i = 0; i < selectedOptionsList.size(); i++) {
                Log.d("BottomSheetFragment 2", "Index " + i + ": " + selectedOptionsList.get(i));
            }
            Log.d("BottomSheetFragment", "Selected options list size: " + selectedOptionsList.size());
            fragment.setArguments(args);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.bottom_sheet_layout_neet, container, false);
            gridView = view.findViewById(R.id.grid_view);
            ArrayList<String> selectedOptions = getArguments() != null ? getArguments().getStringArrayList("selectedOptionsList") : new ArrayList<>();
            Log.d("BottomSheetFragment", "Selected options list size: " + selectedOptions.size());
            ArrayList<QuizPGinsider> quizQuestions = (ArrayList<QuizPGinsider>) getArguments().getSerializable("quizQuestions");
            for (int i = 0; i < selectedOptions.size(); i++) {
                Log.d("BottomSheetFragment", "Index " + i + ": " + selectedOptions.get(i));
            }
            adapter = new QuestionNavigationAdapter(selectedOptions.size(), selectedOptions, quizQuestions, position -> ((WeeklyQuizInsiderActivity) requireActivity()).navigateToQuestion(position));
            gridView.setAdapter(adapter);
            return view;
        }

        public void updateGrid(ArrayList<String> selectedOptionsList) {
            Log.d("BottomSheetFragment", "Updating grid with options list size 2: " + selectedOptionsList.size());
            if (adapter != null) {
                adapter.setSelectedOptions(selectedOptionsList);
                adapter.notifyDataSetChanged();
            }
        }
    }

    public static class QuestionNavigationAdapter extends BaseAdapter {
        private final int itemCount;
        private List<String> selectedOptions;
        private final OnItemClickListener listener;
        private List<QuizPGinsider> quizQuestions;

        public QuestionNavigationAdapter(int itemCount, List<String> selectedOptions, List<QuizPGinsider> quizQuestions, OnItemClickListener listener) {
            this.itemCount = itemCount;
            this.selectedOptions = selectedOptions;
            this.listener = listener;
            this.quizQuestions = quizQuestions;
        }

        @Override
        public int getCount() {
            return itemCount;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_layout, parent, false);
                holder = new ViewHolder();
                holder.textView = convertView.findViewById(R.id.grid_item_text);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textView.setText(String.valueOf(position + 1));
            QuizPGinsider quizQuestion = quizQuestions.get(position);
            Log.d("QuestionNavigationAdapter", "Green color at index1" + position + ": " + selectedOptions.get(position));
            if (quizQuestion.isMarkedForReview()) {
                holder.textView.setBackgroundColor(ContextCompat.getColor(parent.getContext(), R.color.colorPrimary)); // Different color for review
            } else if (selectedOptions.get(position) != null) {
                holder.textView.setBackgroundColor(ContextCompat.getColor(parent.getContext(), R.color.green));
            } else {
                holder.textView.setBackgroundColor(ContextCompat.getColor(parent.getContext(), R.color.grey));
            }
            convertView.setOnClickListener(v -> listener.onItemClick(position));
            return convertView;
        }

        public void setSelectedOptions(List<String> selectedOptions) {
            this.selectedOptions = selectedOptions;
        }

        static class ViewHolder {
            TextView textView;
        }

        public interface OnItemClickListener {
            void onItemClick(int position);
        }
    }
}
