package com.example.cscb07s22finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class NewEventActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    DataBase db = DataBase.getInstance();

    private SingleVenueAdapter venueAdapter;
    private ArrayList<Venue> venues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

    }

    private void initSpinner() {
        Spinner spinner = findViewById(R.id.spinner);
        String values[] = {"Three", "Two", "Three", "Four", "Five"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                values
        );

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);

        Spinner spinner2 = findViewById(R.id.spinner2);
        String values2[] = {"Three", "Two", "Three", "Four", "Five"};
        ArrayAdapter<String> spinnerAdapter2 = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                values2
        );

        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(spinnerAdapter2);
        spinner2.setOnItemSelectedListener(this);
    }

    private void updateVenuesList()
    {
        db.viewVenueAction(
                (ArrayList<Venue> venues) ->
                {
                    venueAdapter.SetVenues(venues);
                }
        );
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}