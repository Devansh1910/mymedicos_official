package com.medical.my_medicos.activities.pg.activites.internalfragments.Preprationindexing.adapter.Swgt;

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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.database.FirebaseDatabase;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.pg.model.QuizPGExam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ExamUpcomingadapter extends RecyclerView.Adapter<ExamUpcomingadapter.ExamUpcomingViewHolder> {
    private static final String TAG = "ExamQuizAdapter";  // Added for consistent logging
    private Context context;
    private ArrayList<QuizPGExam> quizList;

    public ExamUpcomingadapter(Context context, ArrayList<QuizPGExam> quizList) {
        this.context = context;
        this.quizList = quizList;
    }

    @NonNull
    @Override
    public ExamUpcomingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.quiz_upcoming_list_item_swgt, parent, false);
        return new ExamUpcomingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamUpcomingViewHolder holder, int position) {
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
        holder.timestart.setText(formatTimestamp(quiz.getTo()));
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

    public class ExamUpcomingViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, timestart, timeend;
        Button payforsets;
        LinearLayout pay;
        LinearLayout  lock, unlock;
        FirebaseDatabase database;
        String currentUid;
        int coins = 50;

        public ExamUpcomingViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize components
            titleTextView = itemView.findViewById(R.id.titleSets);
            lock = itemView.findViewById(R.id.lock);
            unlock = itemView.findViewById(R.id.unlock);
            timeend = itemView.findViewById(R.id.availablefromtime);
            timestart = itemView.findViewById(R.id.availabletilltime);
            payforsets = itemView.findViewById(R.id.paymentpart);
            pay = itemView.findViewById(R.id.schedulefortheexam);

            // Set click listener
            pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openCalendarToAddEvent();
                }
            });
        }

        private void openCalendarToAddEvent() {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                QuizPGExam quiz = quizList.get(position);
                Timestamp startTimestamp = quiz.getTo();
                String title = quiz.getTitle();

                // Create and show AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirm Event");
                builder.setMessage("Do you want to schedule this exam on your calendar?");

                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createCalendarEvent(startTimestamp, title);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }


        private void createCalendarEvent(Timestamp startTimestamp, String title) {
            Intent intent = new Intent(Intent.ACTION_INSERT);
            intent.setType("vnd.android.cursor.item/event");
            intent.putExtra(CalendarContract.Events.TITLE, title);
            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTimestamp.toDate().getTime());
            intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, startTimestamp.toDate().getTime() + 60 * 60 * 1000); // Assuming a 1-hour duration

            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            } else {
                Log.d(TAG, "No Calendar app found.");
            }
        }
    }
}
