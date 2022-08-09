package com.example.cscb07s22finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SingleAdapter extends RecyclerView.Adapter<SingleAdapter.SingleViewHolder>{
    /*
    Important methods:
        line 59: Change which part of Event is shown is each box
     */

    DataBase db = DataBase.getInstance();

    private Context context;
    private ArrayList<Integer> events;
    private int checkedPosition; //-1: no default selection, 0: 1st item selected

    public SingleAdapter(Context context, ArrayList<Integer> events) {    // pass in copy of events array when getting from db
        this.context = context;
        this.events = events;
        checkedPosition = -1;
    }

    //initializes array of events
    public void SetEvents(ArrayList<Integer> events){
        this.events = events;
        notifyDataSetChanged();
    }

    // Billy's work - for adapter till end
    class SingleViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;
        private ImageView imageView;

        public SingleViewHolder(@NonNull View itemView){
            super(itemView);
            textView = itemView.findViewById(R.id.event_name);
            imageView = itemView.findViewById(R.id.imageview);
        }

        void bind(final int eventCode){
            if (checkedPosition == getAdapterPosition()) {
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
            }

            //Change which part of Event is shown is each box. In this case, the toString()
            textView.setText(db.getEvents().get(eventCode).toString());

            // Only shows checkmark if a customer
            if(db.getUser() instanceof Customer) {
                itemView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        imageView.setVisibility(View.VISIBLE);
                        if(checkedPosition != getAdapterPosition()){
                            notifyItemChanged(checkedPosition);
                            checkedPosition = getAdapterPosition();
                        } else {
                            int i = checkedPosition;
                            checkedPosition = -1;
                            notifyItemChanged(i);
                        }
                    }
                });
            }
            else
            {
                imageView.setVisibility(View.GONE);
            }
        }
    }

    // Returns the event object that is selected by the user (indicated by the checkmark)
    public int getSelected(){
        return checkedPosition;
    }

    public void printEvents()
    {
        System.out.println(events);
    }

    @NonNull
    @Override
    public SingleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate the view, make it visible
        View view = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
        return new SingleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleViewHolder holder, int position) {
        // initialise a view
        holder.bind(events.get(position));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }   // get number of views
}
