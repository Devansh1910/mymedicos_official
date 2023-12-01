package com.example.my_medicos.adapter.job;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.my_medicos.R;
import com.example.my_medicos.adapter.job.items.jobitem;

import java.util.List;

public class AdapterforApplicants extends RecyclerView.Adapter<AdapterforApplicants.MyViewHolder> {

    Context context;
    List<jobitem> joblistforapplicant;

    public AdapterforApplicants(Context context, List<jobitem> joblist) {
        this.context = context;
        this.joblistforapplicant = joblist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.job_design_for_applicants, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.applicantname.setText(joblistforapplicant.get(position).getTitle());
        holder.phoneapplicant.setText(joblistforapplicant.get(position).getHospital());
        holder.cover_letter.setText(joblistforapplicant.get(position).getLocation());
        holder.ageofapplicant.setText(joblistforapplicant.get(position).getDate());
        holder.phonepart.setText(joblistforapplicant.get(position).getCategory());
    }

    @Override
    public int getItemCount() {
        return joblistforapplicant.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView applicantname, phoneapplicant, cover_letter,ageofapplicant,downloadresume,phonepart;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            applicantname = itemView.findViewById(R.id.applicantname);
            phonepart = itemView.findViewById(R.id.phone_of_applicant);
            cover_letter = itemView.findViewById(R.id.cover_letter_content);
            ageofapplicant = itemView.findViewById(R.id.age_of_applicant);
            downloadresume = itemView.findViewById(R.id.DownloadResume);
            phoneapplicant = itemView.findViewById(R.id.modeiscoming);
        }
    }
}
