package com.medical.my_medicos.activities.fmge.adapters.inside;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.fmge.model.examination.FmgeEXAM;
import com.medical.my_medicos.activities.pg.adapters.neetexampadapter;
import com.medical.my_medicos.activities.pg.model.Neetpg;

import java.util.ArrayList;
import java.util.List;

public class fmgeinsideradapter extends RecyclerView.Adapter<fmgeinsideradapter.FmgeQuestionViewHolder> {
    private Context context;
    private ArrayList<FmgeEXAM> quizquestionsfmge;
    private String selectedOption;
    private boolean isOptionSelectionEnabled = true;
    private int currentQuestionIndex = 0;
    private OnOptionSelectedListener onOptionSelectedListener;


    public fmgeinsideradapter(Context context, ArrayList<FmgeEXAM> quizList1) {
        this.context = context;
        this.quizquestionsfmge = quizList1;
        this.selectedOption = null;
    }

    @NonNull
    @Override
    public FmgeQuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fmge_quiz_design, parent, false);
        return new FmgeQuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull fmgeinsideradapter.FmgeQuestionViewHolder holder, int position) {
        FmgeEXAM quizQuestion = quizquestionsfmge.get(holder.getAdapterPosition());
        currentQuestionIndex = holder.getAdapterPosition();
        holder.questionspan.setText(quizQuestion.getQuestion());
        holder.optionA.setText(quizQuestion.getOptionA());
        holder.optionB.setText(quizQuestion.getOptionB());
        holder.optionC.setText(quizQuestion.getOptionC());
        holder.optionD.setText(quizQuestion.getOptionD());
        resetOptionStyle(holder);
        isOptionSelectionEnabled = true;

        if (quizQuestion.getImage() != null && !quizQuestion.getImage().isEmpty()) {
            holder.ifthequestionhavethumbnail.setVisibility(View.VISIBLE);
            Glide.with(context).load(quizQuestion.getImage()).into((ImageView) holder.ifthequestionhavethumbnail);
            holder.ifthequestionhavethumbnail.setOnClickListener(view -> showImagePopup(quizQuestion.getImage()));
        } else {
            holder.ifthequestionhavethumbnail.setVisibility(View.GONE);
        }

        // Check if an option was previously selected for this question
        String selectedOption = quizQuestion.getSelectedOption();
        if (selectedOption != null && !selectedOption.isEmpty()) {
            setOptionSelectedStyle(holder, selectedOption);
        }

        setOptionClickListeners(holder);
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }
    public void setOnOptionSelectedListener(fmgeinsideradapter.OnOptionSelectedListener listener) {
        this.onOptionSelectedListener = listener;
    }

    public interface OnOptionSelectedListener {
        void onOptionSelected(String selectedOption);
    }

    private void setOptionClickListeners(fmgeinsideradapter.FmgeQuestionViewHolder holder) {
        holder.optionA.setOnClickListener(view -> handleOptionClick(holder, "A"));
        holder.optionB.setOnClickListener(view -> handleOptionClick(holder, "B"));
        holder.optionC.setOnClickListener(view -> handleOptionClick(holder, "C"));
        holder.optionD.setOnClickListener(view -> handleOptionClick(holder, "D"));
    }

    private void handleOptionClick(fmgeinsideradapter.FmgeQuestionViewHolder holder, String selectedOption) {
        if (isOptionSelectionEnabled) {
            // Check if the clicked option is already selected
            FmgeEXAM quizquestion = quizquestionsfmge.get(holder.getAdapterPosition());
            String currentSelectedOption = quizquestion.getSelectedOption();
            if (currentSelectedOption != null && currentSelectedOption.equals(selectedOption)) {
                resetOptionStyle(holder);
                quizquestion.setSelectedOption(null);
            } else {
                resetOptionStyle(holder);
                setOptionSelectedStyle(holder, selectedOption);
                quizquestion.setSelectedOption(selectedOption);
            }
        }
    }


    public void disableOptionSelection() {
        isOptionSelectionEnabled = false;
    }

    public void setQuizQuestions(List<FmgeEXAM> quizQuestionsFmge) {
        this.quizquestionsfmge = new ArrayList<>(quizQuestionsFmge);
        notifyDataSetChanged();
    }

    private void setOptionSelectedStyle(fmgeinsideradapter.FmgeQuestionViewHolder holder, String selectedOption) {
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
            selectedTextView.setTextColor(Color.WHITE);
            selectedTextView.setTypeface(null, Typeface.BOLD);
        }
    }

    private void resetOptionStyle(fmgeinsideradapter.FmgeQuestionViewHolder holder) {
        holder.optionA.setBackgroundResource(R.drawable.questionsoptionbk);
        holder.optionA.setTextColor(Color.BLACK);
        holder.optionA.setTypeface(null, Typeface.NORMAL);

        holder.optionB.setBackgroundResource(R.drawable.questionsoptionbk);
        holder.optionB.setTextColor(Color.BLACK);
        holder.optionB.setTypeface(null, Typeface.NORMAL);

        holder.optionC.setBackgroundResource(R.drawable.questionsoptionbk);
        holder.optionC.setTextColor(Color.BLACK);
        holder.optionC.setTypeface(null, Typeface.NORMAL);

        holder.optionD.setBackgroundResource(R.drawable.questionsoptionbk);
        holder.optionD.setTextColor(Color.BLACK);
        holder.optionD.setTypeface(null, Typeface.NORMAL);
    }

    @Override
    public int getItemCount() {
        return quizquestionsfmge.size();
    }

    public static class FmgeQuestionViewHolder extends RecyclerView.ViewHolder {

        TextView questionspan;
        TextView optionA;
        TextView optionB;
        TextView optionC;
        TextView optionD;
        View ifthequestionhavethumbnail;

        public FmgeQuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            questionspan = itemView.findViewById(R.id.questionspan1);
            optionA = itemView.findViewById(R.id.optionA1);
            optionB = itemView.findViewById(R.id.optionB1);
            optionC = itemView.findViewById(R.id.optionC1);
            optionD = itemView.findViewById(R.id.optionD1);
            ifthequestionhavethumbnail = itemView.findViewById(R.id.ifthequestionhavethumbnail);
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
        } else {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
}
