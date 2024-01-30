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
        import com.medical.my_medicos.activities.pg.model.Neetpg;
        import com.medical.my_medicos.activities.pg.model.QuizPGinsider;

        import java.util.ArrayList;

public class  ResultReportNeetAdapter extends RecyclerView.Adapter< ResultReportNeetAdapter.ResultViewHolder> {

    private Context context;
    private ArrayList<Neetpg> quizList;
    private int correctCount;

    public ResultReportNeetAdapter(Context context, ArrayList<Neetpg> quizList) {
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        Neetpg quizQuestion = quizList.get(position);

        holder.resultQuestion.setText(quizQuestion.getQuestion());
        holder.resultCorrectOption.setText(quizQuestion.getCorrectAnswer());
        holder.resultSelectedOption.setText(quizQuestion.getSelectedOption());
        holder.resultDescription.setText(quizQuestion.getDescription());

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
        TextView resultDescription;
        TextView showResultDescription;

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            resultQuestion = itemView.findViewById(R.id.resultQuestion);
            resultCorrectOption = itemView.findViewById(R.id.resultCorrectOption);
            resultSelectedOption = itemView.findViewById(R.id.resultSelectedOption);
            resultDescription = itemView.findViewById(R.id.resultdescription);
            showResultDescription = itemView.findViewById(R.id.showresultdescription);

            showResultDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Toggle visibility of resultDescription
                    if (resultDescription.getVisibility() == View.VISIBLE) {
                        resultDescription.setVisibility(View.GONE);
                    } else {
                        resultDescription.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }
}