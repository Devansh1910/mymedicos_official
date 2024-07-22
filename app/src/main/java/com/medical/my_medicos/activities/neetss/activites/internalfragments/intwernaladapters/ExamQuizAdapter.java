package com.medical.my_medicos.activities.neetss.activites.internalfragments.intwernaladapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.database.FirebaseDatabase;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.neetss.activites.NeetssExamPayment;
import com.medical.my_medicos.activities.pg.model.QuizPGExam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ExamQuizAdapter extends RecyclerView.Adapter<ExamQuizAdapter.ExamViewHolder> {
    private static final String TAG = "ExamQuizAdapter";  // Added for consistent logging
    private Context context;
    private ArrayList<QuizPGExam> quizList;

    public ExamQuizAdapter(Context context, ArrayList<QuizPGExam> quizList) {
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
        QuizPGExam quiz = quizList.get(position);
        holder.titleTextView.setText(quiz.getTitle());
        holder.timestart.setText(formatTimestamp(quiz.getFrom()));
        holder.timeend.setText(formatTimestamp(quiz.getTo()));

        Log.d(TAG, "Binding view holder for position: " + position);

        holder.pay.setOnClickListener(v -> {
            Log.d(TAG, "Click event triggered for position: " + position);
            Intent intent = new Intent(v.getContext(), NeetssExamPayment.class);
            intent.putExtra("Title1", quiz.getTitle1());
            intent.putExtra("Title", quiz.getTitle());
            intent.putExtra("From", formatTimestamp(quiz.getFrom()));
            intent.putExtra("Due", formatTimestamp(quiz.getTo()));
            intent.putExtra("qid", quiz.getId());
            v.getContext().startActivity(intent);
        });
    }

    private String formatTimestamp(Timestamp timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd, MMMM yyyy");
        return dateFormat.format(timestamp.toDate());
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public class ExamViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, timestart, timeend;
        Button payforsets;
        LinearLayout pay;
        FirebaseDatabase database;
        String currentUid;
        int coins = 50;

        public ExamViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleSets);
            timestart = itemView.findViewById(R.id.availablefromtime);
            timeend = itemView.findViewById(R.id.availabletilltime);
            payforsets = itemView.findViewById(R.id.paymentpart);
            pay = itemView.findViewById(R.id.payfortheexam);
        }
    }
}
