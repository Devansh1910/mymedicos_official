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
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.Timestamp;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.pg.activites.Neetexaminsider;
import com.medical.my_medicos.activities.pg.activites.insiders.WeeklyQuizInsiderActivity;
import com.medical.my_medicos.activities.pg.model.QuizPG;
import com.medical.my_medicos.activities.publications.activity.PaymentPublicationActivity;

import java.text.SimpleDateFormat;
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
        holder.time.setText(formatTimestamp(quiz.getTo()));


        holder.pay.setOnClickListener(v -> {
            holder.showBottomSheet(quiz);
        });
    }
    private String formatTimestamp(Timestamp timestamp) {
        // Format the Firebase Timestamp to a string
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log.d("date",dateFormat.format(timestamp.toDate()));
        return dateFormat.format(timestamp.toDate());
    }

    private void showQuizInsiderActivity(QuizPG quiz) {
        Intent intent = new Intent(context, Neetexaminsider.class);
        intent.putExtra("Title1", quiz.getTitle1());
        intent.putExtra("Title", quiz.getTitle());
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public class ExamViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView,time;
        Button payforsets;
        CardView pay;

        public ExamViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleSets);
            time = itemView.findViewById(R.id.availabletilltime);
            payforsets = itemView.findViewById(R.id.paymentpart);
            pay = itemView.findViewById(R.id.payfortheexam);
        }

        private void showBottomSheet(QuizPG quiz) {
            View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_payment, null);
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
            bottomSheetDialog.setContentView(bottomSheetView);

            Button click = bottomSheetView.findViewById(R.id.paymentpart);

            final QuizPG finalQuiz = quiz;

            click.setOnClickListener(v -> {
                showQuizInsiderActivity(finalQuiz);
            });

            bottomSheetDialog.show();
        }

    }
}
