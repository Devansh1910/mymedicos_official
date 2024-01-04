package com.medical.my_medicos.activities.pg.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.pg.activites.Neetexaminsider;
import com.medical.my_medicos.activities.pg.activites.ResultActivityNeet;
import com.medical.my_medicos.activities.pg.model.Neetpg;

import java.util.ArrayList;
import java.util.List;

public class neetexampadapter extends RecyclerView.Adapter<neetexampadapter.NeetQuizQuestionViewHolder> {

    private Context context;
    private ArrayList<Neetpg> quizquestionsweekly;
    private TextView textViewTimer;
    private boolean isOptionSelectionEnabled = true;

    private String selectedOption;

    private OnOptionSelectedListener onOptionSelectedListener;
    private int currentQuestionIndex = 0;

    public neetexampadapter(Neetexaminsider context, ArrayList<Neetpg> quizList1) {
        this.context = context;
        this.quizquestionsweekly = quizList1;
        this.selectedOption = null;
        this.currentQuestionIndex = 0;
    }

    @NonNull
    @Override
    public NeetQuizQuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.question_quiz_design_weekly1, parent, false);
//        textViewTimer = view.findViewById(R.id.textViewTimer);
        return new NeetQuizQuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NeetQuizQuestionViewHolder holder, int position) {
        Neetpg quizquestion = quizquestionsweekly.get(holder.getAdapterPosition());

        currentQuestionIndex = holder.getAdapterPosition();

        holder.questionspan.setText(quizquestion.getQuestion());
        holder.optionA.setText(quizquestion.getOptionA());
        holder.optionB.setText(quizquestion.getOptionB());
        holder.optionC.setText(quizquestion.getOptionC());
        holder.optionD.setText(quizquestion.getOptionD());
        resetOptionStyle(holder);
//        disableOptionSelection();
        isOptionSelectionEnabled = true;

        if (quizquestion.getImage() != null && !quizquestion.getImage().isEmpty()) {
            holder.ifthequestionhavethumbnail.setVisibility(View.VISIBLE);
            Glide.with(context).load(quizquestion.getImage()).into((ImageView) holder.ifthequestionhavethumbnail);
            holder.ifthequestionhavethumbnail.setOnClickListener(view -> showImagePopup(quizquestion.getImage()));
        } else {
            holder.ifthequestionhavethumbnail.setVisibility(View.GONE);
        }

        setOptionClickListeners(holder);
    }

    private void setOptionClickListeners(NeetQuizQuestionViewHolder holder) {
        holder.optionA.setOnClickListener(view -> handleOptionClick(holder, "A"));
        holder.optionB.setOnClickListener(view -> handleOptionClick(holder, "B"));
        holder.optionC.setOnClickListener(view -> handleOptionClick(holder, "C"));
        holder.optionD.setOnClickListener(view -> handleOptionClick(holder, "D"));
    }

    private void handleOptionClick(NeetQuizQuestionViewHolder holder, String selectedOption) {
        if (isOptionSelectionEnabled) {
//            if (selectedOption == null || selectedOption.isEmpty()) {
//                showNoOptionSelectedDialog(holder);
//                return;  // Do not proceed further
//            }
            resetOptionStyle(holder);
            setOptionSelectedStyle(holder, selectedOption);

            Neetpg quizquestion = quizquestionsweekly.get(holder.getAdapterPosition());
            quizquestion.setSelectedOption(selectedOption);


            // Show correct and selected options along with the description
//            showOptionsAndDescription(quizquestion, holder);
        }
    }

    public void disableOptionSelection() {
        isOptionSelectionEnabled = false;
    }

    public void setQuizQuestions(List<Neetpg> quizQuestions) {
        this.quizquestionsweekly = new ArrayList<>(quizQuestions);
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    private void showNoOptionSelectedDialog(NeetQuizQuestionViewHolder holder) {
        if (context instanceof Activity && !((Activity) context).isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            Neetpg quizquestion = quizquestionsweekly.get(currentQuestionIndex);

            String correctOption = quizquestion.getCorrectAnswer();
            String description = quizquestion.getDescription();

            String message = "Correct Option: " + correctOption + "\n";
            message += "You did not select any option.\n";
            message += "Description: " + description;

            builder.setTitle("No Option Selected");
            builder.setMessage(message);

            AlertDialog dialog = builder.create();
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

    private void showOptionsAndDescription(Neetpg quizQuestion, NeetQuizQuestionViewHolder holder) {
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
            selectedTextView.setTextColor(Color.WHITE);
            selectedTextView.setTypeface(null, Typeface.BOLD);
        }
    }

    private void resetOptionStyle(NeetQuizQuestionViewHolder holder) {
        if (holder != null) {
            holder.optionA.setBackgroundResource(R.drawable.categorynewbk);
            holder.optionA.setTextColor(Color.BLACK);
            holder.optionA.setTypeface(null, Typeface.NORMAL);

            holder.optionB.setBackgroundResource(R.drawable.categorynewbk);
            holder.optionB.setTextColor(Color.BLACK);
            holder.optionB.setTypeface(null, Typeface.NORMAL);

            holder.optionC.setBackgroundResource(R.drawable.categorynewbk);
            holder.optionC.setTextColor(Color.BLACK);
            holder.optionC.setTypeface(null, Typeface.NORMAL);

            holder.optionD.setBackgroundResource(R.drawable.categorynewbk);
            holder.optionD.setTextColor(Color.BLACK);
            holder.optionD.setTypeface(null, Typeface.NORMAL);
        }
    }

    @Override
    public int getItemCount() {
        return quizquestionsweekly.size();
    }

    public class NeetQuizQuestionViewHolder extends RecyclerView.ViewHolder {

        TextView questionspan;
        TextView optionA;
        TextView optionB;
        TextView optionC;
        TextView optionD;
        View ifthequestionhavethumbnail;

        public NeetQuizQuestionViewHolder(@NonNull View itemView) {
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
            // Handle case where imageUrl is null or empty
        }
    }

    private void showWrongAnswerPopup() {
        Intent intent = new Intent(context, ResultActivityNeet.class);
        intent.putExtra("SELECTED_OPTION", selectedOption);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}
