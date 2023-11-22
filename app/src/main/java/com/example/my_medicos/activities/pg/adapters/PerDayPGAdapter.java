package com.example.my_medicos.activities.pg.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.my_medicos.R;
import com.example.my_medicos.activities.pg.animations.CorrectAnswerActivity;
import com.example.my_medicos.activities.pg.animations.WrongAnswerActivity;
import com.example.my_medicos.activities.pg.model.PerDayPG;
import com.example.my_medicos.databinding.QuestionPerDayDesignBinding;

import java.util.ArrayList;

public class PerDayPGAdapter extends RecyclerView.Adapter<PerDayPGAdapter.DailyQuestionViewHolder> {

    private Context context;
    private ArrayList<PerDayPG> dailyquestions;
    private String selectedOption;
    private long lastSelectionTimestamp; // to store the timestamp when an option is selected

    public PerDayPGAdapter(Context context, ArrayList<PerDayPG> questions) {
        this.context = context;
        this.dailyquestions = questions;
        this.lastSelectionTimestamp = 0; // Initialize to 0
    }

    @NonNull
    @Override
    public DailyQuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DailyQuestionViewHolder(LayoutInflater.from(context).inflate(R.layout.question_per_day_design, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DailyQuestionViewHolder holder, int position) {
        PerDayPG dailyquestion = dailyquestions.get(position);
        Glide.with(context);
        holder.binding.questionspan.setText(dailyquestion.getDailyQuestion());
        holder.binding.optionA.setText(dailyquestion.getDailyQuestionA());
        holder.binding.optionB.setText(dailyquestion.getDailyQuestionB());
        holder.binding.optionC.setText(dailyquestion.getDailyQuestionC());
        holder.binding.optionD.setText(dailyquestion.getDailyQuestionD());

        // Add OnClickListener to the options
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

        // Add OnClickListener to the "Finish" button
        holder.binding.submitanswerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedOption != null) {
                    // Save the current timestamp
                    lastSelectionTimestamp = System.currentTimeMillis();

                    // Hide the LinearLayout
                    holder.binding.questionsbox.setVisibility(View.GONE);

                    PerDayPG selectedQuestion = dailyquestions.get(holder.getAdapterPosition());
                    String correctAnswer = selectedQuestion.getSubmitDailyQuestion();

                    if (selectedOption.equals(correctAnswer)) {
                        showCorrectAnswerPopup();
                    } else {
                        showWrongAnswerPopup();
                    }
                } else {
                    showToast("Please select an option");
                }
            }
        });

        // Check if 24 hours have passed since the last selection
        if (System.currentTimeMillis() - lastSelectionTimestamp >= 24 * 60 * 60 * 1000) {
            holder.binding.questionsbox.setVisibility(View.VISIBLE);
        } else {
            holder.binding.questionsbox.setVisibility(View.GONE);
        }
    }

    private void handleOptionClick(DailyQuestionViewHolder holder, String selectedOption) {
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

    private void resetOptionStyle(DailyQuestionViewHolder holder) {
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
        // Launch the CorrectAnswerActivity using Intent
        Intent intent = new Intent(context, CorrectAnswerActivity.class);
        // Add FLAG_ACTIVITY_CLEAR_TOP flag
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    private void showWrongAnswerPopup() {
        // Launch the WrongAnswerActivity using Intent
        Intent intent = new Intent(context, WrongAnswerActivity.class);
        // Add FLAG_ACTIVITY_CLEAR_TOP flag
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }


    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return dailyquestions.size();
    }

    public class DailyQuestionViewHolder extends RecyclerView.ViewHolder {

        QuestionPerDayDesignBinding binding;

        public DailyQuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = QuestionPerDayDesignBinding.bind(itemView);
        }
    }
}
