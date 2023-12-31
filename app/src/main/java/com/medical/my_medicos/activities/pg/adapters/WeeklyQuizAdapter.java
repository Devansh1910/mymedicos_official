package com.medical.my_medicos.activities.pg.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.esafirm.stubutton.StuButton;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.pg.activites.insiders.WeeklyQuizInsiderActivity;
import com.medical.my_medicos.activities.pg.model.QuizPG;
import com.medical.my_medicos.activities.publications.activity.PaymentPublicationActivity;

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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        LinearLayout payforsets;
        LinearLayout demo;

        CardView pay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleSets);
            payforsets = itemView.findViewById(R.id.paymentpart);
            demo = itemView.findViewById(R.id.demotest);
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
                    // Handle progress change
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // Handle tracking touch start
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    int progress = seekBar.getProgress();
                    int max = seekBar.getMax();

                    if (progress <= max * 0.75) {
                        // If thumb is scrolled to 75% or below, reset to 0
                        seekBar.setProgress(0);
                    } else {
                        // If it is more than 75%, proceed with the action
                        showQuizInsiderActivity(quiz);
                        bottomSheetDialog.dismiss();
                    }
                }
            });

            bottomSheetDialog.show();
        }


    }
}
