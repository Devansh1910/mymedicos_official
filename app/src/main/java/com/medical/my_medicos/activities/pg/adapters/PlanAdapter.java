package com.medical.my_medicos.activities.pg.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.pg.model.Plan;

import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {
    private List<Plan> plans;

    public PlanAdapter(List<Plan> plans) {
        this.plans = plans;
    }

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_card, parent, false);
        return new PlanViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        Plan plan = plans.get(position);
        holder.planTitle.setText(plan.getTitle());
//        holder.planDescription.setText(plan.getDescription());
        holder.planPrice.setText(plan.getPrice());
        holder.planButton.setOnClickListener(v -> {
            // Handle button click
            Toast.makeText(holder.itemView.getContext(), "Plan Selected: " + plan.getTitle(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

    static class PlanViewHolder extends RecyclerView.ViewHolder {
        TextView planTitle, planDescription, planPrice;
        Button planButton;

        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);
            planTitle = itemView.findViewById(R.id.planTitle);
//            planDescription = itemView.findViewById(R.id.planDescription);
            planPrice = itemView.findViewById(R.id.planPrice);
            planButton = itemView.findViewById(R.id.planButton);
        }
    }
}
