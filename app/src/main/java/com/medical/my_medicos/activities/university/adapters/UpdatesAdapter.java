package com.medical.my_medicos.activities.university.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.publications.activity.ProductDetailedActivity;
import com.medical.my_medicos.activities.university.model.Updates;
import com.medical.my_medicos.databinding.ItemProductBinding;

import java.util.ArrayList;

public class UpdatesAdapter extends RecyclerView.Adapter<UpdatesAdapter.UpdatesViewHolder> {
    Context context;
    ArrayList<Updates> updates;

    public UpdatesAdapter(Context context, ArrayList<Updates> updates) {
        this.context = context;
        this.updates = updates;
    }

    @NonNull
    @Override
    public UpdatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UpdatesViewHolder(LayoutInflater.from(context).inflate(R.layout.item_updates, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UpdatesViewHolder holder, int position) {
        Updates update = updates.get(position);
        Glide.with(context)
                .load(update.getImage())
                .into(holder.binding.image);
        holder.binding.label.setText(update.getName());
        holder.binding.price.setText("PKR " + update.getPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetailedActivity.class);
                intent.putExtra("name", update.getName());
                intent.putExtra("image", update.getImage());
                intent.putExtra("id", update.getId());
                intent.putExtra("price", update.getPrice());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return updates.size();
    }

    public class UpdatesViewHolder extends RecyclerView.ViewHolder {

        ItemProductBinding binding;

        public UpdatesViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemProductBinding.bind(itemView);
        }
    }
}
