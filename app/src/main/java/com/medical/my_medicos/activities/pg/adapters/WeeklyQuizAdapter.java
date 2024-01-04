package com.medical.my_medicos.activities.pg.adapters;
import com.google.firebase.Timestamp;
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

//import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
        holder.time.setText(formatTimestamp(quiz.getTo()));

        holder.pay.setOnClickListener(v -> {
            holder.showBottomSheet(quiz);
        });
    }


    private void showQuizInsiderActivity(QuizPG quiz) {
        Intent intent = new Intent(context, WeeklyQuizInsiderActivity.class);
        intent.putExtra("Title1", quiz.getTitle1());
        intent.putExtra("Title", quiz.getTitle());
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView,time;
        LinearLayout payforsets;
        LinearLayout demo;

        CardView pay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleSets);
            time=itemView.findViewById(R.id.availabletilltime);
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
    private String formatTimestamp(Timestamp timestamp) {
        // Format the Firebase Timestamp to a string
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log.d("date",dateFormat.format(timestamp.toDate()));
        return dateFormat.format(timestamp.toDate());
    }
//    private String formatTimestamp(Timestamp timestamp) {
//        // Format the timestamp to a string
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        return dateFormat.format(timestamp);
//    }
}
