package com.example.cscb07s22finalproject;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class VenuesSpinner {        // helper class to connect venue spinners

    interface singleVenueCallBack {
        void onCallBack(Venue venue);
    }

    public static void connectSpinner(Context context,          // takes in context
                         Spinner spinner,                       // spinner (findViewById)
                         boolean hasDefaultVenue,               // whether or not there should be a default venue option
                         singleVenueCallBack venueCallBack) {   // callback with selected venue, called whenever venue changes

        DataBase db = DataBase.getInstance();

        // initialise venues array
        ArrayList<Venue> venues = new ArrayList<>();
        if(hasDefaultVenue) venues.add(db.getDefaultEntry());
        for(Venue v : db.getVenues()) {
            venues.add(v);
        }

        ArrayAdapter<Venue> adapter = new ArrayAdapter<Venue>(context,
                android.R.layout.simple_spinner_item, venues);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                venueCallBack.onCallBack((Venue) adapterView.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
