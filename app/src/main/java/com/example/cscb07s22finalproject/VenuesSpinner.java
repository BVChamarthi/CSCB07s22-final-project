package com.example.cscb07s22finalproject;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class VenuesSpinner {
    private Spinner spinner;
    private Context context;
    private DataBase db = DataBase.getInstance();

    interface singleVenueCallBack {
        void onCallBack(Venue venue);
    }

    public VenuesSpinner(Context context,
                         Spinner spinner,
                         boolean hasDefaultVenue,
                         singleVenueCallBack venueCallBack) {
        this.context = context;
        this.spinner = spinner;

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
                Venue selectedVenue = (Venue) adapterView.getSelectedItem();
                venueCallBack.onCallBack(selectedVenue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
