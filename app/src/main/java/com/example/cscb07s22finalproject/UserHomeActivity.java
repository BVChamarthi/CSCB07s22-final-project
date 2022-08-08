package com.example.cscb07s22finalproject;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class UserHomeActivity extends AppCompatActivity
{
    /*
    Important methods:
        CreateList(): fill Event items with info
        line 52: what happens after join button is clicked
     */
    DataBase db = DataBase.getInstance();

    private SingleAdapter eventsAdapter;
    private Button btn;

/*    private String selectedFilter = "all";
    private String currentSearchText = "";
    private SearchView searchView;*/
    private Switch upcomingEvents;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);


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
        Filter filter = new Filter(false, false, false);

        // initialise spinner
        VenuesSpinner.connectSpinner(this,                          // connect the spinner
                findViewById(R.id.venue_spinner),
                true,                                       // if you don't want "All venues" option, set this to false
                selectedVenue -> {                                          // callback with the selected venue whenever it changes
            filter.setCompareVenue(selectedVenue);
            eventsAdapter.SetEvents(filter.filterPass(db.getEvents()));
        });

        // initialise upcoming events switch
        upcomingEvents = findViewById(R.id.switch3);
        upcomingEvents.setOnClickListener(view -> {
            filter.setUpcomingEvents(upcomingEvents.isChecked());
            eventsAdapter.SetEvents(filter.filterPass(db.getEvents()));
        });

        btn.setOnClickListener(view -> {
            if(eventsAdapter.getSelected() != null){
                //Change the following line to change what happens when join button is clicked
                Event selectedEvent = eventsAdapter.getSelected();

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



    //Front end - recycler view layout code
    private void initRecyclerView(){

        //Layout of recyclerview
        RecyclerView recyclerView = findViewById(R.id.singleRV);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // By setting the adapter to the recycleView, it is able to display all events after updating the event list of the adapter
        eventsAdapter = new SingleAdapter(this, db.getEvents());
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
                        name = event.getParentVenue().getVenueName();
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

                name = event.getParentVenue().getVenueName();

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
    //Billy's work ends - filters*/


    private void ShowToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void logout(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

/*    public void newEventActivity(View view) {
        Intent intent = new Intent(this, ChooseVenueActivity.class);
        startActivity(intent);
    }

    public void newMyEventActivity(View view) {
        Intent intent = new Intent(this, MyEventsActivity.class);
        startActivity(intent);
    }*/
}