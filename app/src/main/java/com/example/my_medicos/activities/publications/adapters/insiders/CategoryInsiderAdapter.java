package com.example.my_medicos.activities.publications.adapters.insiders;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.my_medicos.R;
import com.example.my_medicos.activities.publications.activity.CategoryPublicationActivity;
import com.example.my_medicos.activities.publications.model.Category;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class CategoryInsiderAdapter extends RecyclerView.Adapter<CategoryInsiderAdapter.CategoryInsiderViewHolder> {

    Context context;
    ArrayList<Category> categories;

    public CategoryInsiderAdapter(Context context, ArrayList<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryInsiderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_insider_categories, parent, false);
        return new CategoryInsiderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryInsiderViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.label.setText(category.getName());
        Glide.with(context)
                .load(category.getIcon())
                .into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CategoryPublicationActivity.class);
                intent.putExtra("catId", category.getId());
                intent.putExtra("categoryName", category.getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryInsiderViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView image;
        TextView label;

        public CategoryInsiderViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            label = itemView.findViewById(R.id.label);
        }
    }

}