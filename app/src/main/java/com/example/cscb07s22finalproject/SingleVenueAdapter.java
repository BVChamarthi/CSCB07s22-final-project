/*
You can prob combine this with SingleAdapter but since they not looking at code, there are important things to work on

 */

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

public class SingleVenueAdapter extends RecyclerView.Adapter<SingleVenueAdapter.SingleViewHolder>{
    /*
    Important methods:
        line 59: Change which part of Event is shown is each box
     */

    private Context context;
    private ArrayList<Venue> venues;
    private int checkedPosition = 0; //-1: no default selection, 0: 1st item selected

    public SingleVenueAdapter(Context context, ArrayList<Venue> venues) {
        this.context = context;
        this.venues = venues;
    }

    //initializes and sets venues
    public void SetVenues(ArrayList<Venue> venues)
    {
        this.venues = venues;
       // System.out.println("venues address in Adapter" + this.venues);
        notifyDataSetChanged();
    }

    //BILLY'S WORK - for adapter till end
    class SingleViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;
        private ImageView imageView;

        public SingleViewHolder(@NonNull View itemView){
            super(itemView);
            textView = itemView.findViewById(R.id.event_name);
            imageView = itemView.findViewById(R.id.imageview);
        }

        void bind(final Venue venue){
            if(checkedPosition == -1){
                imageView.setVisibility(View.GONE);
            }else{
                if(checkedPosition == getAdapterPosition()){
                    imageView.setVisibility(View.VISIBLE);
                }else{
                    imageView.setVisibility(View.GONE);
                }
            }

            //Change which part of Venue is shown is each box. In this case, the toString()
            textView.setText(venue.toString());
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    imageView.setVisibility(View.VISIBLE);
                    if(checkedPosition != getAdapterPosition()){
                        notifyItemChanged(checkedPosition);
                        checkedPosition = getAdapterPosition();
                    }
                }
            });
        }
    }

    // Returns the venue object that is selected by the user (indicated by the checkmark)
    public Venue getSelected(){
        if(checkedPosition != -1){
            return venues.get(checkedPosition);
        }
        return null;
    }

    public void printVenues()
    {
        System.out.println(venues);
    }

    @NonNull
    @Override
    public SingleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
        return new SingleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleViewHolder holder, int position) {
        holder.bind(venues.get(position));
    }

    @Override
    public int getItemCount() {
        return venues.size();
    }
}

