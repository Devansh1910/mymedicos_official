package com.example.my_medicos.adapter.ug;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_medicos.R;
import com.example.my_medicos.adapter.ug.items.ugitem1;

import java.util.List;

public class UgAdapter1 extends RecyclerView.Adapter<UgAdapter1.UgViewHolder1>{

    Context context;
    List<ugitem1> item;

    public UgAdapter1(Context context, List<ugitem1> item) {
        this.context = context;
        this.item = item;
    }

    @NonNull
    @Override
    public UgViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.ug_design1,parent,false);
        return new UgViewHolder1(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UgViewHolder1 holder, int position) {
        holder.name.setText(item.get(position).getDocname());
        holder.description.setText(item.get(position).getDocdescripiton());
        holder.title.setText(item.get(position).getDoctitle());
        holder.date.setText(item.get(position).getdate());
    }

    @Override
    public int getItemCount() {
        return item.size();
    }
    public static class UgViewHolder1 extends  RecyclerView.ViewHolder {

        TextView name,title,description,date;

        public UgViewHolder1(@NonNull View itemView) {
            super(itemView);

//            imageview = itemView.findViewById(R.id.cme_img);
            name=itemView.findViewById(R.id.dr_ug_name);
            description=itemView.findViewById(R.id.dr_ug_desciption);
            title=itemView.findViewById(R.id.dr_ug_title);
            date=itemView.findViewById(R.id.dr_ug_date);

        }
    }
}
