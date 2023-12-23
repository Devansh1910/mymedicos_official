package com.medical.my_medicos.activities.pg.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.pg.activites.insiders.WeeklyQuizInsiderActivity;
import com.medical.my_medicos.activities.pg.model.QuizPG;

import java.util.ArrayList;

public class WeeklyQuizAdapter extends RecyclerView.Adapter<WeeklyQuizAdapter.ViewHolder> {
    private Context context;
    private ArrayList<QuizPG> quizList;

    public WeeklyQuizAdapter(Context context, ArrayList<QuizPG> quizList) {
        this.context = context;
        this.quizList = quizList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.quiz_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuizPG quiz = quizList.get(position);
        holder.titleSets.setText(quiz.getTitle());
        holder.titleSets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WeeklyQuizInsiderActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleSets;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleSets = itemView.findViewById(R.id.titleSets);
        }
    }
}
