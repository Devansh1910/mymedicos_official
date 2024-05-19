package com.medical.my_medicos.activities.fmge.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.fmge.model.VideoFMGE;
import com.medical.my_medicos.activities.pg.model.VideoPG;
import com.medical.my_medicos.databinding.ItemVideFmgeBinding;
import com.medical.my_medicos.databinding.ItemVideosBinding;

import java.util.ArrayList;

public class VideoFmgeAdapter extends RecyclerView.Adapter<VideoFmgeAdapter.VideoFmgeViewHolder> {

    Context context;
    ArrayList<VideoFMGE> videobanksfmges;

    public VideoFmgeAdapter(Context context, ArrayList<VideoFMGE> videobanksfmge) {

        this.context = context;
        this.videobanksfmges = videobanksfmge;
    }

    @NonNull
    @Override
    public VideoFmgeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VideoFmgeViewHolder(LayoutInflater.from(context).inflate(R.layout.item_vide_fmge, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoFmgeViewHolder holder, int position) {
        VideoFMGE videobanksfmge = videobanksfmges.get(position);
        Glide.with(context)
                .load(videobanksfmge.getThumbnail())
                .into(holder.binding.thumbnailvideo);
        holder.binding.videoslabel.setText(videobanksfmge.getLabel());
        holder.binding.timeofvideo.setText(videobanksfmge.getUrl());

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, VideoBankDetailedActivity.class);
//                intent.putExtra("name", videobankpg.getLabel());
//                intent.putExtra("image", videobankpg.getThumbnail());
//                intent.putExtra("id", videobankpg.getDate());
//                intent.putExtra("price", videobankpg.getUrl());
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return videobanksfmges.size();
    }

    public class VideoFmgeViewHolder extends RecyclerView.ViewHolder {
        ItemVideFmgeBinding binding;

        public VideoFmgeViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemVideFmgeBinding.bind(itemView);
            binding.Videobtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        VideoFMGE clickedNews = videobanksfmges.get(position);
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
