package com.medical.my_medicos.activities.news;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.medical.my_medicos.databinding.ItemNewsBinding;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    Context context;
    ArrayList<News> newses;

    public NewsAdapter(Context context, ArrayList<News> news) {
        this.context = context;
        this.newses = news;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsViewHolder(LayoutInflater.from(context).inflate(com.medical.my_medicos.R.layout.item_news, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News news = newses.get(position);
        Glide.with(context)
                .load(news.getThumbnail())
                .into(holder.binding.thumbnailnews);
        holder.binding.newslabel.setText(news.getLabel());
        holder.binding.newslabel.setText(news.getUrl());

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, NewsDetailedActivity.class);
//                intent.putExtra("Title", news.getLabel());
//                intent.putExtra("image", news.getThumbnail());
//                intent.putExtra("url", news.getUrl());
//                intent.putExtra("date", news.getDate());
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return newses.size();
    }
    public class NewsViewHolder extends RecyclerView.ViewHolder {
        ItemNewsBinding binding;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemNewsBinding.bind(itemView);

            binding.readmoreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        News clickedNews = newses.get(position);
                        openUrlInBrowser(clickedNews.getDate());
                    }
                }
            });
        }
    }
    private void openUrlInBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
    }
}
