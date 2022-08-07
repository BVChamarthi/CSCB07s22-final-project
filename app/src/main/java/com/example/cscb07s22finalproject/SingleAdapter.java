package com.example.cscb07s22finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SingleAdapter extends RecyclerView.Adapter<SingleAdapter.SingleViewHolder>{
    /*
    Important methods:
        line 59: Change which part of Event is shown is each box
     */

    DataBase db;

    private Context context;
    private int checkedPosition = 0; //-1: no default selection, 0: 1st item selected

    public SingleAdapter(Context context) {
        this.context = context;
        db = DataBase.getInstance();
    }

    public void SetEvents(){
        notifyDataSetChanged();
    }

    class SingleViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;
        private ImageView imageView;

        public SingleViewHolder(@NonNull View itemView){
            super(itemView);
            textView = itemView.findViewById(R.id.event_name);
            imageView = itemView.findViewById(R.id.imageview);
        }

        void bind(final Event event){
            if(checkedPosition == -1){
                imageView.setVisibility(View.GONE);
            }else{
                if(checkedPosition == getAdapterPosition()){
                    imageView.setVisibility(View.VISIBLE);
                }else{
                    imageView.setVisibility(View.GONE);
                }
            }

            //Change which part of Event is shown is each box
            textView.setText(event.toString());
            itemView.setOnClickListener(v -> {
                imageView.setVisibility(View.VISIBLE);
                if(checkedPosition != getAdapterPosition()){
                    notifyItemChanged(checkedPosition);
                    checkedPosition = getAdapterPosition();
                }
            });
        }
    }

    public Event getSelected(){
        if(checkedPosition != -1){
            return db.getEvents().get(checkedPosition);
        }
        return null;
    }

    public void printEvents()
    {
        System.out.println(db.getEvents());
    }

    @NonNull
    @Override
    public SingleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
        return new SingleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleViewHolder holder, int position) {
        holder.bind(db.getEvents().get(position));
    }

    @Override
    public int getItemCount() {
        return db.getEvents().size();
    }
}
