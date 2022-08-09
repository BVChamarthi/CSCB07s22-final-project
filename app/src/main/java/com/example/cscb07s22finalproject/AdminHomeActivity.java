package com.example.cscb07s22finalproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

public class AdminHomeActivity extends AppCompatActivity {

    DataBase db = DataBase.getInstance();

    private SingleAdapter eventsAdapter;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch upcomingEvents;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        TextView usernameDisplay = findViewById(R.id.textView5);
        usernameDisplay.setText(db.getUser().getUsername());

        initRecyclerView();

        Filter filter = new Filter(false);

        // initialise spinner
        VenuesSpinner.connectSpinner(this,                          // connect the spinner
                findViewById(R.id.spinner2),
                true,                                       // if you don't want "All venues" option, set this to false
                selectedVenue -> {                                          // callback with the selected venue whenever it changes
                    filter.setCompareVenue(selectedVenue);
                    eventsAdapter.SetEvents(filter.filterPass(db.constructEventsArray()));
                });

        // initialise upcoming events switch
        upcomingEvents = findViewById(R.id.switch4);
        upcomingEvents.setOnClickListener(view -> {
            filter.setUpcomingEvents(upcomingEvents.isChecked());
            eventsAdapter.SetEvents(filter.filterPass(db.constructEventsArray()));
        });
    }

    private void initRecyclerView(){

        //Layout of recyclerview
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // By setting the adapter to the recycleView, it is able to display all events after updating the event list of the adapter
        eventsAdapter = new SingleAdapter(this, db.constructEventsArray());
        recyclerView.setAdapter(eventsAdapter);

    }

    public void logout(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void newVenueActivity(View view) {
        Intent intent = new Intent(this, NewVenueActivity.class);
        startActivity(intent);
    }
}