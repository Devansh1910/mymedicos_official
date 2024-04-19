package com.medical.my_medicos.activities.pg.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.pg.activites.insiders.optionsbottom.OptionsBottomSheetDialogueFragment;
import com.medical.my_medicos.activities.pg.model.Neetpg;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class neetexampadapter extends RecyclerView.Adapter<neetexampadapter.NeetQuizQuestionViewHolder> {

    private Context context;
    private ArrayList<Neetpg> quizquestionsweekly;
    private TextView textViewTimer;
    private boolean isOptionSelectionEnabled = true;
    private CountDownTimer countDownTimer;
    private OnOptionSelectedListener onOptionSelectedListener;
    private long remainingTimeMilli=210*60*1000;
    private boolean isTimePaused = false;
    private OnTimeUpListener onTimeUpListener;
    private TextToSpeech textToSpeech;
    private MediaPlayer mediaPlayer;

    public neetexampadapter(Context context, ArrayList<Neetpg> quiz) {
        this.context = context;
        this.quizquestionsweekly = quiz;
    }

    @NonNull
    @Override
    public NeetQuizQuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.question_quiz_design_weekly1, parent, false);
        return new NeetQuizQuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NeetQuizQuestionViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Neetpg quizquestion = quizquestionsweekly.get(position);


        holder.questionspan.setText(quizquestion.getQuestion());
        holder.optionA.setText(quizquestion.getOptionA());
        holder.optionB.setText(quizquestion.getOptionB());
        holder.optionC.setText(quizquestion.getOptionC());
        holder.optionD.setText(quizquestion.getOptionD());
        isOptionSelectionEnabled = true;


        String imageUrl = quizquestion.getImage();



        if (imageUrl != null) {
            holder.ifthequestionhavethumbnail.setVisibility(View.VISIBLE);
            Glide.with(context).load(imageUrl).into((ImageView) holder.ifthequestionhavethumbnail);
            holder.ifthequestionhavethumbnail.setOnClickListener(view -> showImagePopup(imageUrl));
        } else {
            holder.ifthequestionhavethumbnail.setVisibility(View.VISIBLE);
        }

        setOptionClickListeners(holder);

        startTimer(remainingTimeMilli);
        holder.setOnOptionSelectedListener((selectedOption, adapterPosition) -> {
//            disableOptionSelection();

            handleOptionClick(holder, selectedOption);

        });

        if (!isTimePaused) {
            startTimer(remainingTimeMilli);
        } else {
            // If time was paused, reset the flag
            isTimePaused = false;
        }
    }


    public void setQuizQuestions(List<Neetpg> quizQuestions) {
        this.quizquestionsweekly = new ArrayList<>(quizQuestions);
        notifyDataSetChanged();
    }


    private void setOptionClickListeners(NeetQuizQuestionViewHolder holder) {

        holder.optionA.setOnClickListener(view -> handleOptionClick(holder, "A"));
        holder.optionB.setOnClickListener(view -> handleOptionClick(holder, "B"));
        holder.optionC.setOnClickListener(view -> handleOptionClick(holder, "C"));
        holder.optionD.setOnClickListener(view -> handleOptionClick(holder, "D"));


    }
    public void resetButtonColors(NeetQuizQuestionViewHolder holder) {
        // Reset option A
        holder.optionA.setBackgroundResource(R.drawable.categorynewbk);
        holder.optionA.setTextColor(context.getResources().getColor(R.color.black));
        holder.optionA.setTypeface(null, Typeface.NORMAL);

        // Reset option B
        holder.optionB.setBackgroundResource(R.drawable.categorynewbk);
        holder. optionB.setTextColor(context.getResources().getColor(R.color.black));
        holder.optionB.setTypeface(null, Typeface.NORMAL);

        // Reset option C
        holder.optionC.setBackgroundResource(R.drawable.categorynewbk);
        holder.optionC.setTextColor(context.getResources().getColor(R.color.black));
        holder.optionC.setTypeface(null, Typeface.NORMAL);

        // Reset option D
        holder. optionD.setBackgroundResource(R.drawable.categorynewbk);
        holder. optionD.setTextColor(context.getResources().getColor(R.color.black));
        holder.optionD.setTypeface(null, Typeface.NORMAL);
    }

    public interface OnOptionSelectedListener {
        void onOptionSelected(String selectedOption, int position);

    }
    public void setOnOptionSelectedListener(OnOptionSelectedListener listener) {
        this.onOptionSelectedListener = listener;
//        disableOptionSelection();
    }

    private void handleOptionClick(NeetQuizQuestionViewHolder holder, String selectedOption) {
        if (isOptionSelectionEnabled) {
            if (countDownTimer != null) {
                countDownTimer.cancel();
                isTimePaused = true;
            }

            if (selectedOption == null || selectedOption.isEmpty()) {
                showNoOptionSelectedDialog(holder);
                return;
            }

            Neetpg quizquestion = quizquestionsweekly.get(holder.getAdapterPosition());
            if (quizquestion.getSelectedOption() != null && !quizquestion.getSelectedOption().isEmpty()) {
                // If an option is already selected, do nothing
                return;
            }

            resetOptionStyle(holder);
            resetButtonColors(holder);
            setOptionSelectedStyle(holder, selectedOption);
//            disableOptionSelection();


            quizquestion.setSelectedOption(selectedOption);

            if (onOptionSelectedListener != null) {
                onOptionSelectedListener.onOptionSelected(selectedOption, holder.getAdapterPosition());
            }

//            showOptionsAndDescription(quizquestion, holder);
            notifyDataSetChanged();

        }
    }


    private void showNoOptionSelectedDialog(NeetQuizQuestionViewHolder holder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("No Option Selected");
        builder.setMessage("You did not select any option.\nPlease select an option before moving to the next question.");

        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showOptionsAndDescription(Neetpg quizQuestion, NeetQuizQuestionViewHolder holder) {
        String correctOption = quizQuestion.getCorrectAnswer();
        String selectedOption = quizQuestion.getSelectedOption();
        String description = quizQuestion.getDescription();

        OptionsBottomSheetDialogueFragment bottomSheetDialogFragment = OptionsBottomSheetDialogueFragment.newInstance(correctOption, selectedOption, description);
        bottomSheetDialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "OptionsBottomSheetDialogFragment");


//        disableOptionSelection();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                resetButtonColors(holder);
            }
        }, 5000); // 5000 milliseconds = 5 seconds
    }
    public interface OnTimeUpListener {
        void onTimeUp();
    }



    public void setOnTimeUpListener(OnTimeUpListener listener) {
        this.onTimeUpListener = listener;
    }


    private void startTimer(long remainingTimeMilli) {
        if (countDownTimer != null) {

            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(remainingTimeMilli, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long totalSecondsRemaining = millisUntilFinished / 1000;
                long minutes = totalSecondsRemaining / 60;
                long seconds = totalSecondsRemaining % 60;

                String timeRemaining = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
            }

            @Override
            public void onFinish() {
//                disableOptionSelection();
                announceTimeUp();
                if (mediaPlayer.isPlaying()) {
                    onTimeUpListener.onTimeUp();
                    mediaPlayer.pause();
                }
            }
        }.start();
    }

    private void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    private void resumeTimer() {
        if (remainingTimeMilli > 0) {
            startTimer(remainingTimeMilli);
        }
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
    public int getItemCount() {
        return quizquestionsweekly.size();
    }

    public void disableOptionSelection() {
        isOptionSelectionEnabled = false;

    }

    private void setOptionSelectedStyle(NeetQuizQuestionViewHolder holder, String selectedOption) {
        TextView selectedTextView = null;

        switch (selectedOption) {
            case "A":
                selectedTextView = holder.optionA;
                break;
            case "B":
                selectedTextView = holder.optionB;
                break;
            case "C":
                selectedTextView = holder.optionC;
                break;
            case "D":
                selectedTextView = holder.optionD;
                break;
        }

        if (selectedTextView != null) {
            selectedTextView.setBackgroundResource(R.drawable.selectedoptionbk);
            selectedTextView.setTextColor(context.getResources().getColor(R.color.white));
            selectedTextView.setTypeface(null, Typeface.BOLD);
        }
    }

    private void resetOptionStyle(NeetQuizQuestionViewHolder holder) {
        if (holder != null && holder.itemView != null) {
            holder.optionA.setBackgroundResource(R.drawable.categorynewbk);
            holder.optionA.setTextColor(context.getResources().getColor(R.color.black));
            holder.optionA.setTypeface(null, Typeface.NORMAL);

            holder.optionB.setBackgroundResource(R.drawable.categorynewbk);
            holder.optionB.setTextColor(context.getResources().getColor(R.color.black));
            holder.optionB.setTypeface(null, Typeface.NORMAL);

            holder.optionC.setBackgroundResource(R.drawable.categorynewbk);
            holder.optionC.setTextColor(context.getResources().getColor(R.color.black));
            holder.optionC.setTypeface(null, Typeface.NORMAL);

            holder.optionD.setBackgroundResource(R.drawable.categorynewbk);
            holder.optionD.setTextColor(context.getResources().getColor(R.color.black));
            holder.optionD.setTypeface(null, Typeface.NORMAL);
        }
    }


    public class NeetQuizQuestionViewHolder extends RecyclerView.ViewHolder {

        TextView questionspan;
        TextView optionA;
        TextView optionB;
        TextView optionC;
        TextView optionD;
        View ifthequestionhavethumbnail;
        Button descriptionButton;
        private OnOptionSelectedListener onOptionSelectedListener;

        public void setOnOptionSelectedListener(OnOptionSelectedListener listener) {
            this.onOptionSelectedListener = listener;
        }

        public NeetQuizQuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            questionspan = itemView.findViewById(R.id.questionspan1);
            optionA = itemView.findViewById(R.id.optionA1);
            optionB = itemView.findViewById(R.id.optionB1);
            optionC = itemView.findViewById(R.id.optionC1);
            optionD = itemView.findViewById(R.id.optionD1);
            ifthequestionhavethumbnail = itemView.findViewById(R.id.ifthequestionhavethumbnail);
            descriptionButton = itemView.findViewById(R.id.desciption); // Initialize Button


            // Set OnClickListener for the button
            descriptionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Neetpg quizquestion = quizquestionsweekly.get(position);
                        showOptionsAndDescription(quizquestion, NeetQuizQuestionViewHolder.this);
                        resetButtonColors(NeetQuizQuestionViewHolder.this);
                    }

                }
            });
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onOptionSelectedListener.onOptionSelected(null, position);

//                    disableOptionSelection();
                }
            });

        }

    }

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
        }
    }


}
