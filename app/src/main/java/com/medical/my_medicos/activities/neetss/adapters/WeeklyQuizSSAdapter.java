package com.medical.my_medicos.activities.neetss.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.Timestamp;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.neetss.activites.SsPrepPayement;
import com.medical.my_medicos.activities.neetss.model.QuizSS;
import com.medical.my_medicos.activities.pg.model.QuizPG;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class WeeklyQuizSSAdapter extends RecyclerView.Adapter<WeeklyQuizSSAdapter.ViewHolder> {
    private Context context;
    private ArrayList<QuizSS> quizList;

    public WeeklyQuizSSAdapter(Context context, ArrayList<QuizSS> quizList) {
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
        QuizSS quiz = quizList.get(position);
        holder.titleTextView.setText(quiz.getTitle());
        holder.time.setText(formatTimestamp(quiz.getTo()));

        holder.pay.setOnClickListener(v -> {
//            holder.showBottomSheet(quiz);
            Intent intent = new Intent(context, SsPrepPayement.class);
            intent.putExtra("Title1", quiz.getTitle1());
            intent.putExtra("Title", quiz.getTitle());
            intent.putExtra("Due",formatTimestamp(quiz.getTo()));
            context.startActivity(intent);
        });
    }


//    private void showQuizInsiderActivity(QuizSS quiz) {
//        Intent intent = new Intent(context, WeeklyQuizInsiderActivity.class);
//        intent.putExtra("Title1", quiz.getTitle1());
//        intent.putExtra("Title", quiz.getTitle());
//        context.startActivity(intent);
//    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView,time;
        Button payforsets;
        LinearLayout pay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleSets);
            time=itemView.findViewById(R.id.availabletilltime);
            payforsets = itemView.findViewById(R.id.paymentpart);
            pay = itemView.findViewById(R.id.payfortheexam);
        }

        private void showBottomSheet(QuizPG quiz) {
            View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_for_prep, null);
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
            bottomSheetDialog.setContentView(bottomSheetView);

            SeekBar swipeSeekBar = bottomSheetView.findViewById(R.id.swipeSeekBar);
            swipeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    int progress = seekBar.getProgress();
                    int max = seekBar.getMax();

                    if (progress <= max * 0.75) {
                        seekBar.setProgress(0);
                    } else {
//                        showQuizInsiderActivity(quiz);
//                        bottomSheetDialog.dismiss();
                    }
                }
            });

            bottomSheetDialog.show();
        }


    }
    private String formatTimestamp(Timestamp timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log.d("date",dateFormat.format(timestamp.toDate()));
        return dateFormat.format(timestamp.toDate());
    }
}
