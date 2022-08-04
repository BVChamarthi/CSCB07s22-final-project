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

import java.util.ArrayList;

public class UserHomeActivity extends AppCompatActivity{
    /*
    Important methods:
        CreateList():  fill the list with Event items also decide format
        line 49: returns the selected Event info, choose what to return
     */
    private RecyclerView recyclerView;
    private Button btn;
    private ArrayList<Event> events = new ArrayList<>();
    private SingleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        recyclerView = findViewById(R.id.singleRV);
        btn = findViewById(R.id.buttonGetSelect);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapter = new SingleAdapter(this, events);
        recyclerView.setAdapter(adapter);

        CreateList();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adapter.getSelected() != null){
                    //Change the following line to change what happens when join button is clicked
                    ShowToast(adapter.getSelected().getActivity());
                }else{
                    ShowToast("No Selection");
                }
            }
        });
    }

    private void CreateList(){
        events = new ArrayList<>();
        //Change the following lines for change what is displayed in each item
        for(int i = 0; i<20; i++){
            Event event = new Event(
                    "Name "+(i+1)+" ",
                    "Activity "+(i+1)+" ",
                    "Date "+(i+1)+" ",
                    "Start Time "+(i+1)+" ",
                    "End Time "+(i+1)+" ",
                    i+1,
                    i+2
            );
            events.add(event);
        }
        adapter.SetEvents(events);
    }

    private void ShowToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void logout(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void newEventActivity(View view) {
        Intent intent = new Intent(this, NewEventActivity.class);
        startActivity(intent);
    }
}