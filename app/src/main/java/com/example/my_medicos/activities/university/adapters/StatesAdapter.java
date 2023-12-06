package com.example.my_medicos.activities.university.adapters;

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
import com.example.my_medicos.activities.publications.activity.CategoryPublicationActivity;
import com.example.my_medicos.activities.publications.adapters.CategoryAdapter;
import com.example.my_medicos.activities.publications.model.Category;
import com.example.my_medicos.activities.university.model.States;
import com.example.my_medicos.activities.university.model.Universities;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class StatesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_NORMAL = 1;
    private static final int VIEW_TYPE_MORE = 2;
    Context context;
    ArrayList<States> states;

    public StatesAdapter(Context context, ArrayList<States> states) {
        this.context = context;
        this.states = states;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == VIEW_TYPE_NORMAL) {
            View view = inflater.inflate(R.layout.item_universities, parent, false);
            return new StatesAdapter.StatesViewHolder(view);
        } else {
            // Use a different layout for the "More" category
            View view = inflater.inflate(R.layout.more_universities, parent, false);

            return new StatesAdapter.MoreUniversityViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_NORMAL) {
            StatesAdapter.StatesViewHolder statesViewHolder = (StatesAdapter.StatesViewHolder) holder;
            States state = states.get(position);
            statesViewHolder.label.setText(state.getName());
            Glide.with(context)
                    .load(state.getName())
                    .into(statesViewHolder.image);

            statesViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CategoryPublicationActivity.class);
                    intent.putExtra("stateName", state.getName());
                    context.startActivity(intent);
                }
            });
        } else {
            // Handle "More" category if needed
        }
    }

    @Override
    public int getItemCount() {
        return states.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == getItemCount() - 1 && getItemCount() > 5) ? VIEW_TYPE_MORE : VIEW_TYPE_NORMAL;
    }

    public class StatesViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView image;
        TextView label;

        public StatesViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            label = itemView.findViewById(R.id.label);
        }
    }
    public class MoreUniversityViewHolder extends RecyclerView.ViewHolder {
        ImageView imagemore;
        TextView labelmore;

        public MoreUniversityViewHolder(@NonNull View itemView) {
            super(itemView);
            imagemore = itemView.findViewById(R.id.arrowformore);
            labelmore = itemView.findViewById(R.id.moretext);
        }
    }

    // You can add the MoreCategoryViewHolder class here as well if needed
}
