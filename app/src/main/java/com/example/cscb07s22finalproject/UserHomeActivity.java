package com.example.cscb07s22finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UserHomeActivity extends AppCompatActivity{
    /*
    Important methods:
        CreateList(): fill Event items with info
        line 52: what happens after join button is clicked
     */
    private RecyclerView recyclerView;
    private Button btn;
    private ArrayList<Event> events = new ArrayList<>();
    private SingleAdapter adapter;

    private String selectedFilter = "all";
    private String currentSearchText = "";
    private SearchView searchView;

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

        initSearchWidget();
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

    private void initSearchWidget(){
        SearchView searchView = (SearchView) findViewById(R.id.eventsListSearchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                currentSearchText = s;
                String name = "";

                ArrayList<Event> filteredEvents = new ArrayList<Event>();

                for (Event event : events) {
                    name = event.getEventName();
                    if (selectedFilter == "Venue") {
                        name = event.getVenueName();
                    } else if (selectedFilter == "Event") {
                        name = event.getEventName();
                    } else if (selectedFilter == "Sport") {
                        name = event.getActivity();
                    }
                    if (name.toLowerCase().contains(s.toLowerCase())) {
                            filteredEvents.add(event);
                    }
                }
                adapter.SetEvents(filteredEvents);
                return false;
            }
        });
    }

    private void CreateList(){
        ArrayList<Event> events = new ArrayList<>();
        for(int i=0; i<10; i++) {
            Event event = new Event(
                    "Venue " + (i + 1) + " ",
                    "Name " + (i + 1) + " ",
                    "Activity " + (i + 1) + " ",
                    "Start Time " + (i + 1) + " ",
                    "End Time " + (i + 1) + " ",
                    i + 1,
                    i + 2
            );
            events.add(event);
        }
        this.events = events;
        adapter.SetEvents(events);
    }

    private void filterList(String status)
    {
        selectedFilter = status;
        String name="";

        ArrayList<Event> filteredEvents = new ArrayList<Event>();

        for(Event event: events)
        {
            if(status == "Venue") {
                name = event.getVenueName();
            }else if(status == "Event"){
                name = event.getEventName();
            }else if(status == "Sport"){
                name = event.getActivity();
            }
            if(name.toLowerCase().contains(currentSearchText.toLowerCase())) {
                filteredEvents.add(event);
            }
        }
        adapter.SetEvents(filteredEvents);
    }

    public void venueFilterTapped(View view){
        filterList("Venue");
    }

    public void eventFilterTapped(View view){
        filterList("Event");
    }

    public void sportFilterTapped(View view){
        filterList("Sport");
    }

    public void joinedFilterTapped(View view){

    }

    private void ShowToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void logout(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void newEventActivity(View view) {
        Intent intent = new Intent(this, ChooseVenueActivity.class);
        startActivity(intent);
    }
}