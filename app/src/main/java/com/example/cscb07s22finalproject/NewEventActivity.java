package com.example.cscb07s22finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class NewEventActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    DataBase db = DataBase.getInstance();

    Venue v;
    ArrayList<String> sports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        v = (Venue)args.getSerializable("VENUE");

        for(String sport : v.getActivities()){
            System.out.println(sport);
        }

        initSpinner();
    }

    private void initSpinner() {
        Spinner spinner = findViewById(R.id.spinner);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                v.getActivities()
        );

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);
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