package com.medical.my_medicos.activities.pg.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.pg.model.QuizPGinsider;

import java.util.ArrayList;

public class ResultReportAdapter extends RecyclerView.Adapter<ResultReportAdapter.ResultViewHolder> {

    private Context context;
    private ArrayList<QuizPGinsider> quizList;
    private int correctCount;  // Add this variable

    public ResultReportAdapter(Context context, ArrayList<QuizPGinsider> quizList) {
        this.context = context;
        this.quizList = quizList;
        this.correctCount = 0;  // Initialize the count
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.result_report_item, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        QuizPGinsider quizQuestion = quizList.get(position);

        holder.resultQuestion.setText(quizQuestion.getQuestion());
        holder.resultCorrectOption.setText("" + quizQuestion.getCorrectAnswer());

        if (quizQuestion.getSelectedOption().equals(quizQuestion.getCorrectAnswer())) {
            holder.resultSelectedOption.setText("" + quizQuestion.getSelectedOption());
            holder.resultSelectedOption.setBackgroundResource(R.drawable.correct_option_selected);
            holder.resultCorrectOption.setVisibility(View.GONE);
        } else {
            holder.resultSelectedOption.setText("" + quizQuestion.getSelectedOption());
            holder.resultSelectedOption.setBackgroundResource(R.drawable.wrong_option_selected);
            holder.resultCorrectOption.setVisibility(View.VISIBLE);
        }

        holder.resultStatus.setText("Result: " + (quizQuestion.isCorrect() ? "Correct" : "Wrong"));
        holder.resultDescription.setText("Description: " + quizQuestion.getDescription());

        if (quizQuestion.isCorrect()) {
            correctCount++;
        }
    }


    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public static class ResultViewHolder extends RecyclerView.ViewHolder {
        TextView resultQuestion;
        TextView resultCorrectOption;
        TextView resultSelectedOption;
        TextView resultStatus;

        TextView resultDescription;

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            resultQuestion = itemView.findViewById(R.id.resultQuestion);
            resultCorrectOption = itemView.findViewById(R.id.resultCorrectOption);
            resultSelectedOption = itemView.findViewById(R.id.resultSelectedOption);
            resultStatus = itemView.findViewById(R.id.resultstatus);
            resultDescription = itemView.findViewById(R.id.resultdescription);
        }
    }
}
