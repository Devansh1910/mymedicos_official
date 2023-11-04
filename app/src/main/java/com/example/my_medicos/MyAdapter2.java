package com.example.my_medicos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
    public List<cmeitem1> getData() {
        return item;
    }

    @NonNull
    @Override
    public MyViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.cme_design1,parent,false);
        return new MyViewHolder2(v);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder2 holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(item.get(position).getDocname());
        holder.position.setText(item.get(position).getDocpos());
        holder.title.setText((item.get(position).getDoctitle()));
        holder.presenters.setText(item.get(position).getDocpresenter());
        holder.date.setText(item.get(position).getDate());
        holder.time.setText(item.get(position).getTime());

        Log.d(item.get(position).getEmail(),"vivek2");

//        int imageResource = item.get(position).getImage();
//        Log.d("Image Debug", "Image Resource: " + imageResource);
//
//        if (imageResource != 0) {
//            holder.imageview.setImageResource(imageResource);
//        } else {
//            holder.imageview.setImageResource(R.drawable.default_banner);
//        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View a) {
                Context context = a.getContext();
                Intent i = new Intent(context, CmeDetailsActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// Add this line
                i.putExtra("name",item.get(position).getEmail());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return item.size();
    }
    public static class MyViewHolder2 extends  RecyclerView.ViewHolder {


        TextView name,position,title,presenters,date,time,venue,email;
        ImageView imageview;
        public MyViewHolder2(@NonNull View itemView) {
            super(itemView);

//          imageview = itemView.findViewById(R.id.cme_img);
            name=itemView.findViewById(R.id.dr_name);
            title=itemView.findViewById(R.id.dr_title);
            date=itemView.findViewById(R.id.dr_date);
            presenters=itemView.findViewById((R.id.dr_presenters));
            position=itemView.findViewById(R.id.dr_pos);
            time=itemView.findViewById(R.id.dr_time);
        }
    }
}
