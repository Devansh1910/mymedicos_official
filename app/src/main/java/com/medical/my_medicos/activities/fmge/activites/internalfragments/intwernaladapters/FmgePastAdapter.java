package com.medical.my_medicos.activities.fmge.activites.internalfragments.intwernaladapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.CalendarContract;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.fmge.model.QuizFmgeExam;
import com.medical.my_medicos.activities.pg.activites.NeetExamPayment;
import com.medical.my_medicos.activities.pg.model.QuizPGExam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FmgePastAdapter extends RecyclerView.Adapter<FmgePastAdapter.FmgePastViewHolder> {
    private static final String TAG = "ExamQuizAdapter";  // Added for consistent logging
    private Context context;
    private ArrayList<QuizFmgeExam> quizList;

    public FmgePastAdapter(Context context, ArrayList<QuizFmgeExam> quizList) {
        this.context = context;
        this.quizList = quizList;
    }

    @NonNull
    @Override
    public FmgePastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.quiz_past_item, parent, false);
        return new FmgePastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FmgePastViewHolder holder, int position) {
        QuizFmgeExam quiz = quizList.get(position);
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

    public class FmgePastViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, timestart, timeend;
        Button payforsets;
        LinearLayout pay;
        FirebaseDatabase database;
        String currentUid;
        int coins = 50;

        public FmgePastViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.titleSets);
            timestart = itemView.findViewById(R.id.availablefromtime);
            timeend = itemView.findViewById(R.id.availabletilltime);
            payforsets = itemView.findViewById(R.id.paymentpart);
            pay = itemView.findViewById(R.id.pastfortheexam);

            pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Quiz already Terminated, Checkout other quizes.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
