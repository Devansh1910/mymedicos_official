package com.medical.my_medicos.activities.pg.adapters;

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
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.pg.activites.insiders.SpecialityPGInsiderActivity;
import com.medical.my_medicos.activities.pg.model.SpecialitiesPG;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class SpecialitiesPGAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_NORMAL = 1;
    private static final int VIEW_TYPE_MORE = 2;
    Context context;
    ArrayList<SpecialitiesPG> specialitiespost;

    public SpecialitiesPGAdapter(Context context, ArrayList<SpecialitiesPG> specialitiespgs) {
        this.context = context;
        this.specialitiespost = specialitiespgs;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_categories, parent, false);

        return new SpecialitiesPGViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_NORMAL) {
            SpecialitiesPGViewHolder specialitiespostViewHolder = (SpecialitiesPGViewHolder) holder;
            SpecialitiesPG specialitiespg = specialitiespost.get(position);
            specialitiespostViewHolder.label.setText(specialitiespg.getName());
            Glide.with(context)
                    .load(specialitiespg.getName())
                    .into(specialitiespostViewHolder.image);

            specialitiespostViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, SpecialityPGInsiderActivity.class);
                    intent.putExtra("pgId", specialitiespg.getPriority());
                    intent.putExtra("specialityPgName", specialitiespg.getName());
                    context.startActivity(intent);
                }
            });
        } else {

        }
    }

    @Override
    public int getItemCount() {
        return specialitiespost.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == getItemCount() - 1 && getItemCount() > 5) ? VIEW_TYPE_MORE : VIEW_TYPE_NORMAL;
    }

    public class SpecialitiesPGViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView image;
        TextView label;

        public SpecialitiesPGViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            label = itemView.findViewById(R.id.label);
        }
    }
    public class MoreSpecialitiesPGViewHolder extends RecyclerView.ViewHolder {
        ImageView imagemore;
        TextView labelmore;

        public MoreSpecialitiesPGViewHolder(@NonNull View itemView) {
            super(itemView);
            imagemore = itemView.findViewById(R.id.arrowformore);
            labelmore = itemView.findViewById(R.id.moretext);
        }
    }

    // You can add the MoreCategoryViewHolder class here as well if needed
}
