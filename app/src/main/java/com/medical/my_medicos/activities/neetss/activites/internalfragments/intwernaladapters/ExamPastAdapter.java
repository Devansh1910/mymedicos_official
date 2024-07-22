package com.medical.my_medicos.activities.neetss.activites.internalfragments.intwernaladapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.database.FirebaseDatabase;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.pg.model.QuizPGExam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ExamPastAdapter extends RecyclerView.Adapter<ExamPastAdapter.ExamPastViewHolder> {
    private static final String TAG = "ExamQuizAdapter";  // Added for consistent logging
    private Context context;
    private ArrayList<QuizPGExam> quizList;

    public ExamPastAdapter(Context context, ArrayList<QuizPGExam> quizList) {
        this.context = context;
        this.quizList = quizList;
    }

    @NonNull
    @Override
    public ExamPastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.quiz_past_item, parent, false);
        return new ExamPastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamPastViewHolder holder, int position) {
        QuizPGExam quiz = quizList.get(position);
        holder.titleTextView.setText(quiz.getTitle());
        holder.timestart.setText(formatTimestamp(quiz.getFrom()));
        holder.timeend.setText(formatTimestamp(quiz.getTo()));

        Log.d(TAG, "Binding view holder for position: " + position);

    }

    private String formatTimestamp(Timestamp timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd, MMMM yyyy");
        return dateFormat.format(timestamp.toDate());
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public class ExamPastViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, timestart, timeend;
        Button payforsets;
        LinearLayout pay;
        FirebaseDatabase database;
        String currentUid;
        int coins = 50;

        public ExamPastViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize components
            titleTextView = itemView.findViewById(R.id.titleSets);
            timestart = itemView.findViewById(R.id.availablefromtime);
            timeend = itemView.findViewById(R.id.availabletilltime);
            payforsets = itemView.findViewById(R.id.paymentpart);
            pay = itemView.findViewById(R.id.pastfortheexam);

            // Set click listener
            pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Quiz already Terminated, Checkout other quizes.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
