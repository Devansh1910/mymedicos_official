package com.medical.my_medicos.activities.slideshow.insider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.my_medicos.R;

import java.util.List;

public class SlideshowinsiderAdapter extends RecyclerView.Adapter<SlideshowinsiderAdapter.MyViewHolder2> {

    Context context;
    List<Slidershowinsideitem> item;

    public SlideshowinsiderAdapter(Context context, List<Slidershowinsideitem> item) {
        this.context = context;
        this.item = item;
    }

    @NonNull
    @Override
    public MyViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.slide_show,parent,false);
        return new com.medical.my_medicos.activities.slideshow.insider.SlideshowinsiderAdapter.MyViewHolder2(v);
    }

//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder2 holder, int position) {
//
//    }

    @Override
    public void onBindViewHolder(@NonNull com.medical.my_medicos.activities.slideshow.insider.SlideshowinsiderAdapter.MyViewHolder2 holder, @SuppressLint("RecyclerView") int position) {
//        holder.name.setText(item.get(position).getDocname());
//        holder.position.setText(item.get(position).getDocpos());
//        holder.title.setText(item.get(position).getDoctitle());
//        holder.presenters.setText(item.get(position).getDocpresenter());
//        holder.date.setText(item.get(position).getDate());
//        holder.time.setText(item.get(position).getTime());
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View a) {
//                Context context = a.getContext();
//                Intent i = new Intent(context, CmeDetailsActivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// Add this line
//                i.putExtra("documentid",item.get(position).getDocumentid());
//                i.putExtra("name",item.get(position).getEmail());
//                i.putExtra("time",item.get(position).getTime());
//                i.putExtra("type",item.get(position).getType());
//                context.startActivity(i);
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return item.size();
    }
    public static class MyViewHolder2 extends  RecyclerView.ViewHolder {

        TextView name,position,title,presenters,date,time;

        public MyViewHolder2(@NonNull View itemView) {
            super(itemView);

//            imageview = itemView.findViewById(R.id.cme_img);
//            name=itemView.findViewById(R.id.dr_name);
//            position=itemView.findViewById(R.id.dr_pos);
//            title=itemView.findViewById(R.id.dr_title);
//            presenters=itemView.findViewById(R.id.dr_presenters);
//            date=itemView.findViewById(R.id.dr_date);
//            time=itemView.findViewById(R.id.dr_time);

        }
    }
}
