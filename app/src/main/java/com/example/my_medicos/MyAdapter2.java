package com.example.my_medicos;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter2  extends RecyclerView.Adapter<MyAdapter2.MyViewHolder2>{

    Context context;
    List<cmeitem1> item;

    public MyAdapter2(Context context, List<cmeitem1> item) {
        this.context = context;
        this.item = item;
    }

    @NonNull
    @Override
    public MyViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.cme_design1,parent,false);
        return new MyViewHolder2(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder2 holder, int position) {
        holder.name.setText(item.get(position).getDocname());
        holder.position.setText(item.get(position).getDocpos());
        holder.title.setText(item.get(position).getDoctitle());
        holder.presenters.setText(item.get(position).getDocpresenter());

        int imageResource = item.get(position).getImage();
        Log.d("Image Debug", "Image Resource: " + imageResource);

        if (imageResource != 0) {
            holder.imageview.setImageResource(imageResource);
        } else {
            Log.d("Image Debug", "Default Image Set");
            holder.imageview.setImageResource(R.drawable.default_banner);
        }
    }

    @Override
    public int getItemCount() {
        return item.size();
    }
    public static class MyViewHolder2 extends  RecyclerView.ViewHolder {

        TextView name,position,title,presenters;
        ImageView imageview;
        public MyViewHolder2(@NonNull View itemView) {
            super(itemView);

            imageview = itemView.findViewById(R.id.cme_img);
            name=itemView.findViewById(R.id.dr_name);
            position=itemView.findViewById(R.id.dr_pos);
            title=itemView.findViewById(R.id.dr_title);
            presenters=itemView.findViewById(R.id.dr_presenters);

        }
    }
}
