package com.example.my_medicos.activities.slideshow;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.my_medicos.R;
import com.example.my_medicos.databinding.ItemSlideshowBinding;

import java.util.ArrayList;

public class SlideshowAdapter extends RecyclerView.Adapter<SlideshowAdapter.SlideshowViewHolder> {

    private final Context context;
    private final ArrayList<Slideshow> slideshows;

    public SlideshowAdapter(Context context, ArrayList<Slideshow> slideshows) {
        this.context = context;
        this.slideshows = slideshows;
    }

    @NonNull
    @Override
    public SlideshowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSlideshowBinding binding = ItemSlideshowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SlideshowViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SlideshowViewHolder holder, int position) {
        Slideshow slideshow = slideshows.get(position);
        Glide.with(context)
                .load(slideshow.getImages().get(0).getUrl()) // Assuming you want to load the first image
                .into(holder.binding.thumbnailslideshow);
        holder.binding.slideshowlabel.setText(slideshow.getTitle());
    }

    @Override
    public int getItemCount() {
        return slideshows.size();
    }

    public class SlideshowViewHolder extends RecyclerView.ViewHolder {
        ItemSlideshowBinding binding;

        public SlideshowViewHolder(@NonNull ItemSlideshowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.downloadpptbtn.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Slideshow clickedSlideshow = slideshows.get(position);
                    openUrlInBrowser(clickedSlideshow.getFile());
                }
            });
        }
    }

    private void openUrlInBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
    }
}
