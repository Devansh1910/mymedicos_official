package com.medical.my_medicos.activities.pg.activites.internalfragments.intwernaladapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.pg.activites.insiders.WeeklyQuizInsiderActivity;
import com.medical.my_medicos.activities.pg.model.QuizPG;
import com.medical.my_medicos.activities.publications.activity.PaymentPublicationActivity;

import java.util.ArrayList;

public class ExamQuizAdapter extends RecyclerView.Adapter<ExamQuizAdapter.ExamViewHolder> {
    private Context context;
    private ArrayList<QuizPG> quizList;

    public ExamQuizAdapter(Context context, ArrayList<QuizPG> quizList) {
        this.context = context;
        this.quizList = quizList;
    }

    @NonNull
    @Override
    public ExamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.quiz_list_item, parent, false);
        return new ExamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamViewHolder holder, int position) {
        QuizPG quiz = quizList.get(position);
        holder.titleTextView.setText(quiz.getTitle());

        holder.pay.setOnClickListener(v -> {
            holder.showBottomSheet(quiz);
        });
    }

    private void showQuizInsiderActivity(QuizPG quiz) {
        Intent intent = new Intent(context, WeeklyQuizInsiderActivity.class);
        intent.putExtra("Title", quiz.getTitle1());
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public class ExamViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        LinearLayout payforsets;
        LinearLayout demo;

        CardView pay;

        public ExamViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleSets);
            payforsets = itemView.findViewById(R.id.paymentpart);
            demo = itemView.findViewById(R.id.demotest);
            pay = itemView.findViewById(R.id.payfortheexam);
        }

        private void showBottomSheet(QuizPG quiz) {
            View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_payment, null);
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
            bottomSheetDialog.setContentView(bottomSheetView);

            @SuppressLint({"MissingInflatedId", "LocalSuppress"})
            LinearLayout textClickMe = bottomSheetView.findViewById(R.id.paymentpart);
            LinearLayout demoClickMe = bottomSheetView.findViewById(R.id.demotest);

            final QuizPG finalQuiz = quiz; // Declare a final variable

            textClickMe.setOnClickListener(v -> {
                Intent intent = new Intent(context, PaymentPublicationActivity.class);
                context.startActivity(intent);
            });

            demoClickMe.setOnClickListener(v -> {
                showQuizInsiderActivity(finalQuiz);
            });

            bottomSheetDialog.show();
        }
    }
}
