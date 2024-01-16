package com.medical.my_medicos.activities.publications.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.publications.activity.ProductDetailedActivity;
import com.medical.my_medicos.activities.publications.model.Product;

import java.util.ArrayList;
import java.util.List;

public class PurchasedAdapter extends RecyclerView.Adapter<PurchasedAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> products;
    private OnItemClickListener onItemClickListener; // Listener for item click

    public PurchasedAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_purchased_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        Log.d("PurchasedAdapter", "Title: " + product.getTitle() +
                ", Author: " + product.getAuthor() +
                ", Price: " + product.getPrice() +
                ", Thumbnail: " + product.getThumbnail());
        Glide.with(context)
                .load(product.getThumbnail())
                .into(holder.imageView);
        holder.labelTextView.setText(product.getTitle());
        holder.authorTextView.setText(product.getAuthor());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetailedActivity.class);
                intent.putExtra("Title", product.getTitle());
                intent.putExtra("thumbnail", product.getThumbnail());
                intent.putExtra("id", product.getId());
                intent.putExtra("Subject", product.getSubject());
                intent.putExtra("Price", product.getPrice());
                intent.putExtra("Author", product.getAuthor());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView labelTextView;
        TextView authorTextView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image1);
            labelTextView = itemView.findViewById(R.id.label1);
            authorTextView = itemView.findViewById(R.id.author1);

            // Add click listener to the itemView
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Notify the listener on item click
                    if (onItemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onItemClickListener.onItemClick(products.get(position));
                        }
                    }
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }
}
