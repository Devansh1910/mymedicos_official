package com.example.my_medicos.activities.publications.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.my_medicos.activities.publications.activity.ProductDetailedActivity;
import com.example.my_medicos.activities.publications.model.Product;
import com.example.my_medicos.databinding.ItemProductBinding;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    Context context;
    ArrayList<Product> products;

    public ProductAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(context).inflate(com.example.my_medicos.R.layout.item_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        Glide.with(context)
                .load(product.getThumbnail())
                .into(holder.binding.image);
        holder.binding.label.setText(product.getTitle());
        holder.binding.price.setText("INR " + product.getPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetailedActivity.class);
                intent.putExtra("Title", product.getTitle());
                intent.putExtra("thumbnail", product.getThumbnail());
                intent.putExtra("id", product.getId());
                intent.putExtra("Subject",product.getSubject());
                intent.putExtra("Price", product.getPrice());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        ItemProductBinding binding;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemProductBinding.bind(itemView);
        }
    }
}
