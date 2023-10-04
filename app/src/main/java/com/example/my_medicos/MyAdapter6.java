package com.example.my_medicos;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MyAdapter6 extends RecyclerView.Adapter<MyAdapter6.MyViewHolder6> {

    Context context; // Change to Context
    List<jobitem1> joblist;

    public MyAdapter6(Context context, List<jobitem1> joblist) { // Change to Context
        this.context = context;
        this.joblist = joblist;
    }

    @NonNull
    @Override
    public MyViewHolder6 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.job_design2, parent, false);
        return new MyViewHolder6(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder6 holder, int position) {
        holder.pos.setText(joblist.get(position).getPosition());
        holder.hosp.setText(joblist.get(position).getHospital());
        holder.loc.setText(joblist.get(position).getLocation());
        Log.d("12345678", joblist.get(position).getPosition());
        Log.d("12345678", joblist.get(position).getHospital());
        Log.d("12345678", joblist.get(position).getLocation());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View a) {
                Context context = a.getContext();
                Intent i = new Intent(context, JobDetailsActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// Add this line
                i.putExtra("name",joblist.get(position).getHospital());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return joblist.size();
    }

    public static class MyViewHolder6 extends RecyclerView.ViewHolder {
        TextView pos, hosp, loc;
        Button apply;

        public MyViewHolder6(@NonNull View itemView) {
            super(itemView);
            pos = itemView.findViewById(R.id.job_pos1);
            hosp = itemView.findViewById(R.id.hosp_name1);
            loc = itemView.findViewById(R.id.job_loc1);
            apply=itemView.findViewById(R.id.Apply1);
            apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent j=new Intent(context, JobsApplyActivity.class);
                    j.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(j);
                }


            });
        }
    }
}