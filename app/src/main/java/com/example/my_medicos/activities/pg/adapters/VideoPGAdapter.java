package com.example.my_medicos.activities.pg.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.my_medicos.activities.pg.activites.detailed.VideoBankDetailedActivity;
import com.example.my_medicos.activities.pg.model.VideoPG;
import com.example.my_medicos.databinding.ItemProductBinding;

import java.util.ArrayList;

public class VideoPGAdapter extends RecyclerView.Adapter<VideoPGAdapter.VideoPGViewHolder> {

    Context context;
    ArrayList<VideoPG> videobankspg;

    public VideoPGAdapter(Context context, ArrayList<VideoPG> videobankspg) {

        this.context = context;
        this.videobankspg = videobankspg;
    }

    @NonNull
    @Override
    public VideoPGViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VideoPGViewHolder(LayoutInflater.from(context).inflate(com.example.my_medicos.R.layout.item_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoPGViewHolder holder, int position) {
        VideoPG videobankpg = videobankspg.get(position);
        Glide.with(context)
                .load(videobankpg.getImage())
                .into(holder.binding.image);
        holder.binding.label.setText(videobankpg.getName());
        holder.binding.price.setText("PKR " + videobankpg.getPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, VideoBankDetailedActivity.class);
                intent.putExtra("name", videobankpg.getName());
                intent.putExtra("image", videobankpg.getImage());
                intent.putExtra("id", videobankpg.getId());
                intent.putExtra("price", videobankpg.getPrice());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videobankspg.size();
    }

    public class VideoPGViewHolder extends RecyclerView.ViewHolder {

        ItemProductBinding binding;

        public VideoPGViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemProductBinding.bind(itemView);
        }
    }
}
