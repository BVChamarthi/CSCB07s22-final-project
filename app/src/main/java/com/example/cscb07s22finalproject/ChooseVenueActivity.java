package com.example.cscb07s22finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ChooseVenueActivity extends AppCompatActivity implements Serializable {
    private RecyclerView recyclerView;
    private Button btn;
    private ArrayList<Venue> venues = new ArrayList<>();
    private SingleVenueAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_venue);

        recyclerView = findViewById(R.id.singleRV2);
        btn = findViewById(R.id.buttonNext);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapter = new SingleVenueAdapter(this, venues);
        recyclerView.setAdapter(adapter);

        CreateList();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adapter.getSelected() != null){
                    //Change the following line to change what happens when join button is clicked
                    ShowToast(adapter.getSelected().getVenueName());
                    startChooseSport(adapter.getSelected());
                }else{
                    ShowToast("No Selection");
                    //Update list but with sports from venue selected
                }
            }
        });
    }

    private void ShowToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void CreateList(){
        ArrayList<String> sports = new ArrayList<String>(){{
            add("Swimming");
            add("Soccer");
        }};

        ArrayList<Integer> code = new ArrayList<Integer>(){{
            add(123);
            add(423);
        }};

        ArrayList<Venue> venues = new ArrayList<>();
        for(int i=0; i<10; i++) {
            Venue venue = new Venue(
                    "Venue " + (i + 1) + " ",
                    sports
            );
            venues.add(venue);
        }

        adapter.SetVenues(venues);
    }

    public void startChooseSport(Venue venue){
        ArrayList<String> sports = venue.getActivities();
        Intent intent = new Intent(this, ChooseSportActivity.class);
        Bundle args = new Bundle();
        args.putSerializable("ARRAYLIST",(Serializable)sports);
        intent.putExtra("BUNDLE",args);
        startActivity(intent);
    }
}