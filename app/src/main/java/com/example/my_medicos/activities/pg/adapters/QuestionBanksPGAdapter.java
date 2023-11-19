package com.example.my_medicos.activities.pg.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.my_medicos.activities.pg.activites.detailed.QuestionBankDetailedActivity;
import com.example.my_medicos.activities.pg.model.QuestionsPG;
import com.example.my_medicos.activities.publications.activity.ProductDetailedActivity;
import com.example.my_medicos.activities.publications.model.Product;
import com.example.my_medicos.databinding.ItemProductBinding;

import java.util.ArrayList;

public class QuestionBanksPGAdapter extends RecyclerView.Adapter<QuestionBanksPGAdapter.QuestionBanksViewHolder> {

    Context context;
    ArrayList<QuestionsPG> questionbanks;

    public QuestionBanksPGAdapter(Context context, ArrayList<QuestionsPG> questionbanks) {
        this.context = context;
        this.questionbanks = questionbanks;
    }

    @NonNull
    @Override
    public QuestionBanksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuestionBanksViewHolder(LayoutInflater.from(context).inflate(com.example.my_medicos.R.layout.item_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionBanksViewHolder holder, int position) {
        QuestionsPG questionbankpg = questionbanks.get(position);
        Glide.with(context)
                .load(questionbankpg.getImage())
                .into(holder.binding.image);
        holder.binding.label.setText(questionbankpg.getName());
        holder.binding.price.setText("PKR " + questionbankpg.getPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, QuestionBankDetailedActivity.class);
                intent.putExtra("name", questionbankpg.getName());
                intent.putExtra("image", questionbankpg.getImage());
                intent.putExtra("id", questionbankpg.getId());
                intent.putExtra("price", questionbankpg.getPrice());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionbanks.size();
    }

    public class QuestionBanksViewHolder extends RecyclerView.ViewHolder {

        ItemProductBinding binding;

        public QuestionBanksViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemProductBinding.bind(itemView);
        }
    }
}
