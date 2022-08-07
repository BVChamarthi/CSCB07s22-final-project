package com.example.cscb07s22finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.lang.reflect.Array;

import java.util.ArrayList;

public class UserHomeActivity extends AppCompatActivity
{
    /*
    Important methods:
        CreateList(): fill Event items with info
        line 52: what happens after join button is clicked
     */
    DataBase db = DataBase.getInstance();
    private RecyclerView recyclerView;
    private Button btn;

    private ArrayList<Event> events = new ArrayList<>();

    private SingleAdapter eventsAdapter;

    private String selectedFilter = "all";
    private String currentSearchText = "";
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        initSearchWidget();
        initRecyclerView();

        //db.fetchUserScheduledEvents();

//        TextView usernameText = findViewById(R.id.textView4);
//        usernameText.setText(db.getUser().toString());

        //eventsAdapter.printEvents();
    }


    //Front end - recycler view layout code
    private void initRecyclerView(){

        //Layout of recyclerview
        recyclerView = findViewById(R.id.singleRV);
        btn = findViewById(R.id.buttonGetSelect);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // By setting the adapter to the recycleView, it is able to display all events after updating the event list of the adapter
        eventsAdapter = new SingleAdapter(this, events);
        recyclerView.setAdapter(eventsAdapter);

        //this updates the event list so that all events are in the array, ready to be displayed by adapter
        updateEventsList();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (eventsAdapter.getSelected() != null) {
                    //Change the following line to change what happens when join button is clicked
                    ShowToast(eventsAdapter.getSelected().getActivity());
                } else {
                    ShowToast("No Selection");
                }
            }
        });
    }

    //Not used - created to add events
    private void createList(){
        events = new ArrayList<>();

//        //Change the following lines for change what is displayed in each item
//        for(int i = 0; i<20; i++){
//            Event event = new Event(
//                    "Name "+(i+1)+" ",
//                    "Venue Name: Pan Am",
//                    "Activity "+(i+1)+" ",
//                    "Date "+(i+1)+" ",
//                    "Start Time "+(i+1)+" ",
//                    "End Time "+(i+1)+" ",
//                    i+1,
//                    i+2
//            );
//            events.add(event);
//        }

        events.add(new Event("Event 1", "Pan Am", "Swimming", "2022-08-04", "12:00", "13:00", 2, 5));
        events.add(new Event("Event 2", "Pan Am", "Soccer", "2022-08-04", "13:00", "14:00", 1, 5));
        events.add(new Event("Event 3", "Pan Am", "Surfing", "2022-08-04", "14:00", "15:00", 8, 5));


    }

    // Gets snapshot of events at current time and gives it to adapter for displaying
    public void updateEventsList()
    {
        db.viewEventAction(
                (ArrayList<Event> events) ->
        {
            eventsAdapter.SetEvents(events);
        });
    }



    //BILLYS WORK starts - filters and searches on User Home page
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
                eventsAdapter.SetEvents(filteredEvents);
                return false;
            }
        });
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
        eventsAdapter.SetEvents(filteredEvents);
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

    public void joinedFilterTapped(View view)
    {

    }
    //Billy's work ends - filters


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

    public void newMyEventActivity(View view) {
        Intent intent = new Intent(this, MyEventsActivity.class);
        startActivity(intent);
    }
}