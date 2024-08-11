package com.medical.my_medicos.activities.pg.activites.internalfragments.intwernaladapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.neetss.activites.NeetssExamPayment;
import com.medical.my_medicos.activities.neetss.model.QuizSSExam;
import com.medical.my_medicos.activities.pg.activites.NeetExamPayment;
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
    public ExamQuizAdapter.ExamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.quiz_list_item, parent, false);
        return new ExamQuizAdapter.ExamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamQuizAdapter.ExamViewHolder holder, int position) {
        QuizPGExam quiz = quizList.get(position);
        String title = quiz.getTitle();
        if (title.length() > 23) {
            title = title.substring(0, 20) + "...";
        }
        if (quiz.getType() == true) {
            holder.unlock.setVisibility(View.GONE);
            holder.lock.setVisibility(View.VISIBLE);
        } else {
            holder.lock.setVisibility(View.GONE);
            holder.unlock.setVisibility(View.VISIBLE);
        }
        holder.titleTextView.setText(title);
        holder.categorytextview.setText(quiz.getIndex());
        holder.dueDateTextView.setText(formatTimestamp(quiz.getTo()));
//        holder.titleTextView.setText(quiz.getTitle());
//        holder.timestart.setText(formatTimestamp(quiz.getFrom()));
//        holder.timeend.setText(formatTimestamp(quiz.getTo()));

        Log.d(TAG, "Binding view holder for position: " + position);

        holder.pay.setOnClickListener(v -> {
            if (quiz.getType()) {
                holder.showBottomSheet();
            } else {
                Log.d(TAG, "Click event triggered for position: " + position);
                Intent intent = new Intent(v.getContext(), NeetssExamPayment.class);
                intent.putExtra("Title1", quiz.getTitle1());
                intent.putExtra("Title", quiz.getTitle());
                intent.putExtra("From", formatTimestamp(quiz.getTo()));
                intent.putExtra("Due", formatTimestamp(quiz.getTo()));
                intent.putExtra("qid", quiz.getId());
                v.getContext().startActivity(intent);
            }
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

        TextView dueDateTextView;
        TextView categorytextview;

        LinearLayout  lock, unlock;
        String currentUid;
        int coins = 50;

        public ExamViewHolder(@NonNull View itemView) {
            super(itemView);
            pay = itemView.findViewById(R.id.payfortheexam);
            lock = itemView.findViewById(R.id.lock);
            unlock = itemView.findViewById(R.id.unlock);
            categorytextview = itemView.findViewById(R.id.categoryTextView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            dueDateTextView = itemView.findViewById(R.id.dateTextView);


        }
        private void showBottomSheet() {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
            View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_up_paid, null);
            bottomSheetDialog.setContentView(bottomSheetView);

            @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button btnBuyPlan = bottomSheetView.findViewById(R.id.btnBuyPlan);
            btnBuyPlan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                    Toast.makeText(context, "Purchase action triggered", Toast.LENGTH_SHORT).show();
                }
            });

            bottomSheetDialog.show();
        }
    }
    private String formatTimestamp(String timestamp) {
        try {
            // Adjust the format here to match the actual input format
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); // Example format
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

            java.util.Date date = inputFormat.parse(timestamp);
            return outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return ""; // Return an empty string or handle the error as needed
        }
    }
}
