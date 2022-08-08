package com.example.cscb07s22finalproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;


import android.widget.Button;
import android.widget.Switch;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
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
    Customer c;

    private SingleAdapter eventsAdapter;
    private Button btn;

/*    private String selectedFilter = "all";
    private String currentSearchText = "";
    private SearchView searchView;*/
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch upcomingEvents;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch joinedEvents;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch scheduledEvents;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        c = (Customer) db.getUser();

        TextView usernameDisplay = findViewById(R.id.textView2);
        usernameDisplay.setText(db.getUser().getUsername());

        btn = findViewById(R.id.buttonGetSelect);

//        initSearchWidget();
        initRecyclerView();

        //db.fetchUserScheduledEvents();

//        TextView usernameText = findViewById(R.id.textView4);
//        usernameText.setText(db.getUser().toString());

        //eventsAdapter.printEvents();

        // initialise filter
        Filter filter = new Filter(false);

        // initialise spinner
        VenuesSpinner.connectSpinner(this,                          // connect the spinner
                findViewById(R.id.venue_spinner),
                true,                                       // if you don't want "All venues" option, set this to false
                selectedVenue -> {                                          // callback with the selected venue whenever it changes
            filter.setCompareVenue(selectedVenue);
            eventsAdapter.SetEvents(filter.filterPass(db.constructEventsArray()));
        });

        // initialise upcoming events switch
        upcomingEvents = findViewById(R.id.switch3);
        upcomingEvents.setOnClickListener(view -> {
            filter.setUpcomingEvents(upcomingEvents.isChecked());
            eventsAdapter.SetEvents(filter.filterPass(db.constructEventsArray()));
        });

        // initialise scheduled and joined events switches
        scheduledEvents = findViewById(R.id.switch1);
        joinedEvents = findViewById(R.id.switch2);
        scheduledEvents.setOnClickListener( view -> {
            eventsAdapter.SetEvents(filter.filterPass(getScheduledEventsAndJoinedEvents()));
        });
        joinedEvents.setOnClickListener( view -> {
            eventsAdapter.SetEvents(filter.filterPass(getScheduledEventsAndJoinedEvents()));
        });

        btn.setOnClickListener(view -> {
            if(eventsAdapter.getSelected() != -1){
                //Change the following line to change what happens when join button is clicked
                int selectedEvent = eventsAdapter.getSelected();

                db.joinEvent(selectedEvent,
                        () -> {ShowToast("Already Joined Event");},
                        () -> {ShowToast("Max Participants reached");});

                // Starting the activity to create a new event
                // To do so, we need to pass the venue object that is selected, which is
                // retrieved from the adapter
                //startNewEventActivity(adapter.getSelected());
            }else{
                ShowToast("No Selection");
                //Update list but with sports from venue selected

            }
        });
    }

    private ArrayList<Integer> getScheduledEventsAndJoinedEvents() {
        ArrayList<Integer> displayEvents;
        if(scheduledEvents.isChecked()) displayEvents = new ArrayList<>(c.getScheduledEvents());
        else displayEvents = new ArrayList<>();
        if(joinedEvents.isChecked()) displayEvents.addAll(c.getJoinedEvents());
        return displayEvents;
    }


    //Front end - recycler view layout code
    private void initRecyclerView(){

        //Layout of recyclerview
        RecyclerView recyclerView = findViewById(R.id.singleRV);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // By setting the adapter to the recycleView, it is able to display all events after updating the event list of the adapter
        eventsAdapter = new SingleAdapter(this, db.constructEventsArray());
        recyclerView.setAdapter(eventsAdapter);

        //this updates the event list so that all events are in the array, ready to be displayed by adapter
//        updateEventsList();

    }

/*    // Gets snapshot of events at current time and gives it to adapter for displaying
    public void updateEventsList()
    {
//        Toast.makeText(this, String.valueOf(db.getDataFetched()), Toast.LENGTH_LONG).show();

        db.readVenuesAndEvents(
                events -> {
                    eventsAdapter.SetEvents(new ArrayList<>(events));
                },
                venues -> {}, str -> {});
    }*/

/*    //BILLYS WORK starts - filters and searches on User Home page
=======
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
>>>>>>> story-4
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
<<<<<<< HEAD
                        name = event.getParentVenue().getVenueName();
=======
                        name = event.getVenueName();
>>>>>>> story-4
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

<<<<<<< HEAD
    private void CreateList(){
        ArrayList<Event> events = new ArrayList<>();
        for(int i=0; i<10; i++) {
            Event event = new Event(
                    "Venue " + (i + 1) + " ",
                    "Name " + (i + 1) + " ",
                    "Activity " + (i + 1) + " ",
                    "Date " + (i + 1) + " ",
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
=======
>>>>>>> story-3

    private void filterList(String status)
    {
        selectedFilter = status;
        String name="";

        ArrayList<Event> filteredEvents = new ArrayList<Event>();

        for(Event event: events)
        {
            if(status == "Venue") {
<<<<<<< HEAD

                name = event.getParentVenue().getVenueName();

=======
                name = event.getVenueName();
>>>>>>> story-4
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

<<<<<<< HEAD
    public void eventFilterTapped(View view){
        filterList("Event");
=======
    public void eventFilterTapped(View view)
    {
        initRecyclerView();
>>>>>>> story-4
    }

    public void sportFilterTapped(View view){
        filterList("Sport");
    }

    public void joinedFilterTapped(View view)
    {

    }
<<<<<<< HEAD
    //Billy's work ends - filters*/


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
/*
    public void newMyEventActivity(View view) {
        Intent intent = new Intent(this, MyEventsActivity.class);
        startActivity(intent);
    }

 */
}