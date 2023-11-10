package com.example.my_medicos.adapter.cme;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_medicos.R;
import com.example.my_medicos.activities.cme.CmeDetailsActivity;
import com.example.my_medicos.adapter.cme.items.cmeitem3;

import java.util.List;

public class MyAdapter3 extends RecyclerView.Adapter<MyAdapter3.MyViewHolder3>{

    Context context;
    List<cmeitem3> item;


    public MyAdapter3(Context context, List<cmeitem3> item) {
        this.context = context;
        this.item = item;
    }

    @NonNull
    @Override
    public MyViewHolder3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.cme_design3,parent,false);
        return new MyViewHolder3(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder3 holder, @SuppressLint("RecyclerView") int position) {

        holder.name.setText(item.get(position).getDocname());
        holder.position.setText(item.get(position).getDocpos());
        holder.title.setText(item.get(position).getDoctitle());
        holder.presenters.setText(item.get(position).getDocpresenter());
        holder.date.setText(item.get(position).getDate());
        holder.time.setText(item.get(position).getTime());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View a) {
                Context context = a.getContext();
                Intent i = new Intent(context, CmeDetailsActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// Add this line
                i.putExtra("name",item.get(position).getEmail());
                i.putExtra("type",item.get(position).getType());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public static class MyViewHolder3 extends  RecyclerView.ViewHolder{

        TextView date,time,title,name,position,presenters;

        ImageView imageview;
        public MyViewHolder3(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.dr_name);
            position=itemView.findViewById(R.id.dr_pos);
            title=itemView.findViewById(R.id.dr_title);
            presenters=itemView.findViewById(R.id.dr_presenters);
            date=itemView.findViewById(R.id.dr_date);
            time=itemView.findViewById(R.id.dr_time);
        }
    }


}
