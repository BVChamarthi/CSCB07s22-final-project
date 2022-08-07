package com.example.cscb07s22finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MyEventsActivity extends AppCompatActivity {

    DataBase db = DataBase.getInstance();
    private RecyclerView recyclerView;
    private Button btn;
    private Button btn2;
    private ArrayList<Event> events = new ArrayList<>();
    private SingleAdapter adapter;

    //page for user story 4
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);

        recyclerView = findViewById(R.id.singleRV);
        btn = findViewById(R.id.buttonGetSelect);
        btn2 = findViewById(R.id.button6);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        adapter = new SingleAdapter(this, events);
        recyclerView.setAdapter(adapter);

        updateEventsList();

        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

            }
        });

        btn2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                updateScheduledEventsList();
            }
        });
    }

    private void ShowToast(String msg)
    {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void updateEventsList()
    {
        db.viewEventAction(
                (ArrayList<Event> events) ->
                {
                    adapter.SetEvents(events);
                });
    }

    public void updateScheduledEventsList()
    {
        db.viewUserEventAction(
                (ArrayList<Event> events) ->
                {
                    adapter.SetEvents(events);
                    System.out.println(events);
                });
    }

}