// WeeklyQuizAdapterinsider.java

package com.medical.my_medicos.activities.pg.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.pg.activites.ResultActivity;
import com.medical.my_medicos.activities.pg.model.QuizPGinsider;
import com.medical.my_medicos.databinding.QuestionQuizDesignWeeklyBinding;

import java.util.ArrayList;

public class WeeklyQuizAdapterinsider extends RecyclerView.Adapter<WeeklyQuizAdapterinsider.WeeklyQuizQuestionViewHolder> {

    private Context context;
    private ArrayList<QuizPGinsider> quizquestionsweekly;
    private String selectedOption;

    public WeeklyQuizAdapterinsider(Context context, ArrayList<QuizPGinsider> quiz) {
        this.context = context;
        this.quizquestionsweekly = quiz;
        this.selectedOption = null;
    }

    @NonNull
    @Override
    public WeeklyQuizQuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeeklyQuizQuestionViewHolder(LayoutInflater.from(context).inflate(R.layout.question_quiz_design_weekly, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WeeklyQuizQuestionViewHolder holder, int position) {
        QuizPGinsider quizquestion = quizquestionsweekly.get(position);

        holder.binding.questionspan.setText(quizquestion.getQuestion());
        holder.binding.optionA.setText(quizquestion.getOptionA());
        holder.binding.optionB.setText(quizquestion.getOptionB());
        holder.binding.optionC.setText(quizquestion.getOptionC());
        holder.binding.optionD.setText(quizquestion.getOptionD());

        if (quizquestion.getImage() != null && !quizquestion.getImage().isEmpty()) {
            holder.binding.ifthequestionhavethumbnail.setVisibility(View.VISIBLE);
            Glide.with(context).load(quizquestion.getImage()).into(holder.binding.ifthequestionhavethumbnail);
            holder.binding.ifthequestionhavethumbnail.setOnClickListener(view -> showImagePopup(quizquestion.getImage()));
        } else {
            holder.binding.ifthequestionhavethumbnail.setVisibility(View.GONE);
        }

        setOptionClickListeners(holder);
    }

    private void setOptionClickListeners(WeeklyQuizQuestionViewHolder holder) {
        holder.binding.optionA.setOnClickListener(view -> handleOptionClick(holder, "A"));
        holder.binding.optionB.setOnClickListener(view -> handleOptionClick(holder, "B"));
        holder.binding.optionC.setOnClickListener(view -> handleOptionClick(holder, "C"));
        holder.binding.optionD.setOnClickListener(view -> handleOptionClick(holder, "D"));
    }

    private void handleOptionClick(WeeklyQuizQuestionViewHolder holder, String selectedOption) {
        resetOptionStyle(holder);
        setOptionSelectedStyle(holder, selectedOption);

        QuizPGinsider quizquestion = quizquestionsweekly.get(holder.getAdapterPosition());
        quizquestion.setSelectedOption(selectedOption);
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

}
