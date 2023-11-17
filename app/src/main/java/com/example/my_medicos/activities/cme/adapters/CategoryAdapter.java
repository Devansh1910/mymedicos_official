package com.example.my_medicos.activities.cme.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.my_medicos.R;
import com.example.my_medicos.activities.cme.model.Category;
import com.example.my_medicos.activities.publications.activity.CategoryPublicationActivity;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_NORMAL = 1;
    private static final int VIEW_TYPE_MORE = 2;

    Context context;
    ArrayList<Category> categories;

    public CategoryAdapter(Context context, ArrayList<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == VIEW_TYPE_NORMAL) {
            View view = inflater.inflate(R.layout.item_categories, parent, false);
            return new CategoryViewHolder(view);
        } else {
            // Use a different layout for the "More" category
            View view = inflater.inflate(R.layout.more_category, parent, false);

            return new MoreCategoryViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_NORMAL) {
            CategoryViewHolder categoryViewHolder = (CategoryViewHolder) holder;
            Category category = categories.get(position);
            categoryViewHolder.label.setText(category.getName());
            Glide.with(context)
                    .load(category.getIcon())
                    .into(categoryViewHolder.image);

            categoryViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CategoryPublicationActivity.class);
                    intent.putExtra("catId", category.getId());
                    intent.putExtra("categoryName", category.getName());
                    context.startActivity(intent);
                }
            });
        } else {
            // Handle "More" category if needed
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == getItemCount() - 1 && getItemCount() > 5) ? VIEW_TYPE_MORE : VIEW_TYPE_NORMAL;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView image;
        TextView label;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            label = itemView.findViewById(R.id.label);
        }
    }
    public class MoreCategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imagemore;
        TextView labelmore;

        public MoreCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imagemore = itemView.findViewById(R.id.arrowformore);
            labelmore = itemView.findViewById(R.id.moretext);
        }
    }

    // You can add the MoreCategoryViewHolder class here as well if needed
}
