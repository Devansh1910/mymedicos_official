package com.medical.my_medicos.activities.fmge.adapters;

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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.fmge.activites.FmgeExamActivity;
import com.medical.my_medicos.activities.fmge.model.examination.QuizFMGE;
import com.medical.my_medicos.activities.pg.activites.Neetexaminsider;
import com.medical.my_medicos.activities.pg.activites.insiders.WeeklyQuizInsiderActivity;
import com.medical.my_medicos.activities.pg.model.QuizPG;
import com.medical.my_medicos.activities.publications.activity.PaymentPublicationActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FmgeAdapter extends RecyclerView.Adapter<FmgeAdapter.FmgeViewHolder> {
    private Context context;
    private ArrayList<QuizFMGE> quizListfmge;

    public FmgeAdapter(Context context, ArrayList<QuizFMGE> quizList) {
        this.context = context;
        this.quizListfmge = quizList;
    }

    @NonNull
    @Override
    public FmgeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.quiz_fmge_exam_item, parent, false);
        return new FmgeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FmgeViewHolder holder, int position) {
        QuizFMGE quizFMGE = quizListfmge.get(position);
        holder.titleTextView.setText(quizFMGE.getTitle());
        holder.time.setText(formatTimestamp(quizFMGE.getTo()));


        holder.pay.setOnClickListener(v -> {
            holder.showBottomSheet(quizFMGE);
        });
    }
    private String formatTimestamp(Timestamp timestamp) {
        // Format the Firebase Timestamp to a string
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log.d("date",dateFormat.format(timestamp.toDate()));
        return dateFormat.format(timestamp.toDate());
    }

    private void showQuizInsiderActivity(QuizFMGE quiz) {
        Intent intent = new Intent(context, FmgeExamActivity.class);
        intent.putExtra("Title1", quiz.getTitle1());
        intent.putExtra("Title", quiz.getTitle());
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return quizListfmge.size();
    }

    public class FmgeViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView,time;
        Button payforsets;
        CardView pay;
        FirebaseDatabase database;
        String currentUid;
        int coins= 50;


        public FmgeViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleSets);
            time = itemView.findViewById(R.id.availabletilltime);
            payforsets = itemView.findViewById(R.id.paymentpart);
            pay = itemView.findViewById(R.id.payfortheexam);
        }

        private void showBottomSheet(QuizFMGE quiz) {
            View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_payment, null);
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
            database = FirebaseDatabase.getInstance();
            currentUid = FirebaseAuth.getInstance().getUid();
            bottomSheetDialog.setContentView(bottomSheetView);

            Button click = bottomSheetView.findViewById(R.id.paymentpart);

            String customButtonText = "Pay 50 MedCoins to Attend";
            click.setText(customButtonText);

            final QuizFMGE finalQuizfmge = quiz;

            click.setOnClickListener(v -> {
                database.getReference().child("profiles")
                        .child(currentUid)
                        .child("coins")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                Integer coinsValue = snapshot.getValue(Integer.class);
                                if (coinsValue != null) {
                                    int newCoinsValue = coinsValue - 30;
                                    if (newCoinsValue >= 0) {
                                        showQuizInsiderActivity(finalQuizfmge);
                                        Toast.makeText(context, "Welcome", Toast.LENGTH_SHORT).show();
                                        database.getReference().child("profiles")
                                                .child(currentUid)
                                                .child("coins")
                                                .setValue(newCoinsValue);
                                        coins = newCoinsValue;
                                    } else {
                                        Toast.makeText(context, "Insufficient Credits", Toast.LENGTH_SHORT).show();
                                        database.getReference().child("profiles")
                                                .child(currentUid)
                                                .child("coins")
                                                .setValue(coinsValue);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                            }
                        });
            });

            bottomSheetDialog.show();
        }
    }
}
