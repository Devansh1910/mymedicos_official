package com.medical.my_medicos.activities.fmge.adapters;
import com.google.firebase.Timestamp;
import android.annotation.SuppressLint;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.esafirm.stubutton.StuButton;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.fmge.activites.FmgePrepPayement;
import com.medical.my_medicos.activities.fmge.model.QuizFmge;
import com.medical.my_medicos.activities.pg.activites.PgPrepPayement;
import com.medical.my_medicos.activities.pg.activites.insiders.WeeklyQuizInsiderActivity;
import com.medical.my_medicos.activities.pg.model.QuizPG;
import com.medical.my_medicos.activities.publications.activity.PaymentPublicationActivity;

//import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeeklyFmgeQuizAdapter extends RecyclerView.Adapter<WeeklyFmgeQuizAdapter.ViewHolder> {
    private Context context;
    private ArrayList<QuizFmge> quizList;

    public WeeklyFmgeQuizAdapter(Context context, ArrayList<QuizFmge> quizList) {
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
        QuizFmge quiz = quizList.get(position);
        String title=quiz.getTitle();
        if (title.length() > 23) {
            title = title.substring(0, 20) + "...";
        }
        if (quiz.getType()==true){
            holder.unlock.setVisibility(View.GONE);
            holder.lock.setVisibility(View.VISIBLE);
        }
        else{
            holder.lock.setVisibility(View.GONE);
            holder.unlock.setVisibility(View.VISIBLE);

        }
        holder.titleTextView.setText(title);
        holder.categorytextview.setText(quiz.getIndex());

        holder.time.setText(formatTimestamp(quiz.getTo()));
        holder.pay.setOnClickListener(v -> {
            if (quiz.getType()) {
                holder.showBottomSheet();
            } else {
                Intent intent = new Intent(context, FmgePrepPayement.class);
                intent.putExtra("Title1", quiz.getTitle1());
                intent.putExtra("Title", quiz.getTitle());
                intent.putExtra("Id",quiz.getId());
                intent.putExtra("Due", formatTimestamp(quiz.getTo()));
                context.startActivity(intent);
            }
        });
        holder.titleTextView.setText(quiz.getTitle());
        holder.time.setText(formatTimestamp(quiz.getTo()));
        holder.slot.setText(quiz.getSlot());

        holder.pay.setOnClickListener(v -> {
//            holder.showBottomSheet(quiz);
            Intent intent = new Intent(context, FmgePrepPayement.class);
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
        TextView titleTextView, time, categorytextview,slot;

        LinearLayout pay,lock,unlock;
        Button payforsets;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleSets);
            time=itemView.findViewById(R.id.availabletilltime);
            lock=itemView.findViewById(R.id.lock);
            unlock=itemView.findViewById(R.id.unlock);
            payforsets = itemView.findViewById(R.id.paymentpart);
            pay = itemView.findViewById(R.id.payfortheexam);
            slot = itemView.findViewById(R.id.slot);
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
                    // Your code to handle the purchase action
                    // For example, start an activity to purchase a plan or show a message
                    Toast.makeText(context, "Purchase action triggered", Toast.LENGTH_SHORT).show();
                }
            });

            bottomSheetDialog.show();
        }


    }
    private String formatTimestamp(Timestamp timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");
        Log.d("date",dateFormat.format(timestamp.toDate()));
        return dateFormat.format(timestamp.toDate());
    }
}
