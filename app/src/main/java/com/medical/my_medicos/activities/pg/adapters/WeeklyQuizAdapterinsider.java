package com.medical.my_medicos.activities.pg.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.pg.activites.ResultActivity;
import com.medical.my_medicos.activities.pg.activites.extras.InsiderDataBottomSheet;
import com.medical.my_medicos.activities.pg.model.QuizPGinsider;
import com.medical.my_medicos.databinding.QuestionQuizDesignWeeklyBinding;

import java.util.ArrayList;
import java.util.List;

public class WeeklyQuizAdapterinsider extends RecyclerView.Adapter<WeeklyQuizAdapterinsider.WeeklyQuizQuestionViewHolder> {

    private Context context;
    private ArrayList<QuizPGinsider> quizquestionsweekly;
    private TextView textViewTimer;
    private boolean isOptionSelectionEnabled = true;

    private String selectedOption;

    private OnOptionSelectedListener onOptionSelectedListener;

    private int currentQuestionIndex = 0;
    private CountDownTimer countDownTimer;
    private static final long TIME_LIMIT_MILLIS = 30000;

    private MediaPlayer mediaPlayer;

    private TextToSpeech textToSpeech;
    public WeeklyQuizAdapterinsider(Context context, ArrayList<QuizPGinsider> quiz) {
        this.context = context;
        this.quizquestionsweekly = quiz;
        this.selectedOption = null;
        this.currentQuestionIndex = 0;

        textToSpeech = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                // TTS initialization successful
            } else {
                // Handle TTS initialization failure
            }
        });

        mediaPlayer = MediaPlayer.create(context, R.raw.tictac); // Replace with your ticking sound resource
        mediaPlayer.setLooping(true);
    }

    @NonNull
    @Override
    public WeeklyQuizQuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.question_quiz_design_weekly, parent, false);
        // Initialize textViewTimer here
        textViewTimer = view.findViewById(R.id.textViewTimer); // Replace with your actual TextView ID
        return new WeeklyQuizQuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeeklyQuizQuestionViewHolder holder, @SuppressLint("RecyclerView") int position) {
        QuizPGinsider quizquestion = quizquestionsweekly.get(position);
        currentQuestionIndex = position;


        holder.binding.questionspan.setText(quizquestion.getQuestion());
        holder.binding.optionA.setText(quizquestion.getOptionA());
        holder.binding.optionB.setText(quizquestion.getOptionB());
        holder.binding.optionC.setText(quizquestion.getOptionC());
        holder.binding.optionD.setText(quizquestion.getOptionD());
        isOptionSelectionEnabled=true;

        if (quizquestion.getImage() != null && !quizquestion.getImage().isEmpty()) {
            holder.binding.ifthequestionhavethumbnail.setVisibility(View.VISIBLE);
            Glide.with(context).load(quizquestion.getImage()).into(holder.binding.ifthequestionhavethumbnail);
            holder.binding.ifthequestionhavethumbnail.setOnClickListener(view -> showImagePopup(quizquestion.getImage()));
        } else {
            holder.binding.ifthequestionhavethumbnail.setVisibility(View.GONE);
        }

        setOptionClickListeners(holder);
        startTimer();
    }

    private void setOptionClickListeners(WeeklyQuizQuestionViewHolder holder) {
        holder.binding.optionA.setOnClickListener(view -> handleOptionClick(holder, "A"));
        holder.binding.optionB.setOnClickListener(view -> handleOptionClick(holder, "B"));
        holder.binding.optionC.setOnClickListener(view -> handleOptionClick(holder, "C"));
        holder.binding.optionD.setOnClickListener(view -> handleOptionClick(holder, "D"));
    }

    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(TIME_LIMIT_MILLIS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;
                textViewTimer.setText(String.valueOf(secondsRemaining));

                if (secondsRemaining <= 16 && secondsRemaining > 10) {
                    textViewTimer.setBackgroundResource(R.drawable.counterbkfor16);
                } else if (secondsRemaining <= 10) {
                    textViewTimer.setBackgroundResource(R.drawable.counterforbk10);

                    // Announce remaining time
                    announceRemainingTime(secondsRemaining);

                    // Play ticking sound
                    if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                    }
                }
            }

            @Override
            public void onFinish() {
                disableOptionSelection();
                announceTimeUp();
                // Stop ticking sound when time is up
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
            }
        }.start();
    }

    private void announceRemainingTime(long secondsRemaining) {
        if (secondsRemaining > 0) {
            String announcement = String.valueOf(secondsRemaining);
            textToSpeech.speak(announcement, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            announceTimeUp();
        }
    }

    private void announceTimeUp() {
        String announcement = "Time is up!";
        textToSpeech.speak(announcement, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        // Release MediaPlayer resources
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void handleOptionClick(WeeklyQuizQuestionViewHolder holder, String selectedOption) {
        if (isOptionSelectionEnabled) {
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            if (selectedOption == null || selectedOption.isEmpty()) {
                showNoOptionSelectedDialog(holder);
                return;
            }
            resetOptionStyle(holder);
            setOptionSelectedStyle(holder, selectedOption);

            QuizPGinsider quizquestion = quizquestionsweekly.get(holder.getAdapterPosition());
            quizquestion.setSelectedOption(selectedOption);
            showOptionsAndDescription(quizquestion,holder);

        }
    }
    public void disableOptionSelection() {
        isOptionSelectionEnabled = false;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setQuizQuestions(List<QuizPGinsider> quizQuestions) {
        this.quizquestionsweekly = new ArrayList<>(quizQuestions);
        notifyDataSetChanged();
    }

    private void showNoOptionSelectedDialog(WeeklyQuizQuestionViewHolder holder) {
        if (context instanceof Activity && !((Activity) context).isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            QuizPGinsider quizquestion = quizquestionsweekly.get(currentQuestionIndex);

            String correctOption = quizquestion.getCorrectAnswer();
            String description = quizquestion.getDescription();

            String message = "Correct Option: " + correctOption + "\n";
            message += "You did not select any option within the time limit.\n";
            message += "Description: " + description;

            builder.setTitle("No Option Selected");
            builder.setMessage(message);

            AlertDialog dialog = builder.create();

            // Check if the activity is still running before showing the dialog
            if (context instanceof Activity && !((Activity) context).isFinishing()) {
                dialog.show();
                dialog.setOnDismissListener(dialogInterface -> {
                    resetOptionStyle(holder);
                    if (onOptionSelectedListener != null) {
                        onOptionSelectedListener.onOptionSelected();
                    }
                });
            }
        }
    }

    private void showOptionsAndDescription(QuizPGinsider quizQuestion,WeeklyQuizQuestionViewHolder holder) {
        String correctOption = quizQuestion.getCorrectAnswer();
        String selectedOption = quizQuestion.getSelectedOption();
        String description = quizQuestion.getDescription();

        String message = "Correct Option: " + correctOption + "\n";
        message += "Your Selected Option: " + selectedOption + "\n";
        message += "Description: " + description;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Options and Description");
        builder.setMessage(message);
        builder.setPositiveButton("OK", (dialog, which) -> {
            resetOptionStyle(holder);
            dialog.dismiss();
            if (onOptionSelectedListener != null) {
                onOptionSelectedListener.onOptionSelected();
            }
        });
        builder.show();
        disableOptionSelection();
    }

    public interface OnOptionSelectedListener {
        void onOptionSelected();
    }

    public void setOnOptionSelectedListener(OnOptionSelectedListener listener) {
        this.onOptionSelectedListener = listener;
    }

    private void setOptionSelectedStyle(WeeklyQuizQuestionViewHolder holder, String selectedOption) {
        TextView selectedTextView = null;

        switch (selectedOption) {
            case "A":
                selectedTextView = holder.binding.optionA;
                break;
            case "B":
                selectedTextView = holder.binding.optionB;
                break;
            case "C":
                selectedTextView = holder.binding.optionC;
                break;
            case "D":
                selectedTextView = holder.binding.optionD;
                break;
        }

        if (selectedTextView != null) {
            selectedTextView.setBackgroundResource(R.drawable.selectedoptionbk);
            selectedTextView.setTextColor(Color.WHITE);
            selectedTextView.setTypeface(null, Typeface.BOLD);
        }
    }

    private void resetOptionStyle(WeeklyQuizQuestionViewHolder holder) {
        if (holder != null && holder.binding != null) {
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

    private void showCorrectAnswerPopup() {
        Intent intent = new Intent(context, ResultActivity.class);
        intent.putExtra("SELECTED_OPTION", selectedOption);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @SuppressLint("MissingInflatedId")
    private void showImagePopup(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            View popupView = LayoutInflater.from(context).inflate(R.layout.popup_image, null);
            PhotoView photoView = popupView.findViewById(R.id.photoView);
            Glide.with(context).load(imageUrl).into(photoView);


            int dialogWidth = context.getResources().getDimensionPixelSize(R.dimen.popup_width);
            int dialogHeight = context.getResources().getDimensionPixelSize(R.dimen.popup_height);

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(popupView);
            AlertDialog dialog = builder.create();

            dialog.getWindow().setLayout(dialogWidth, dialogHeight);

            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            popupView.findViewById(R.id.btnClose).setOnClickListener(view -> dialog.dismiss());

            dialog.show();
        } else {

        }
    }

    private void showWrongAnswerPopup() {
        Intent intent = new Intent(context, ResultActivity.class);
        intent.putExtra("SELECTED_OPTION", selectedOption);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

//    private void showInsiderDataBottomSheet() {
//        InsiderDataBottomSheet bottomSheet = new InsiderDataBottomSheet();
//        bottomSheet.show(((AppCompatActivity) context).getSupportFragmentManager(), bottomSheet.getTag());
//    }
}
