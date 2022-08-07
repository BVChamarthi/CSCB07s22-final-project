package com.example.cscb07s22finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class NewEventActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    DataBase db = DataBase.getInstance();

    Venue v;
    ArrayList<String> sports;
    String activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        Intent intent = getIntent();

        // Retrieving the venue object that was passed in
        Bundle args = intent.getBundleExtra("BUNDLE");
        v = (Venue)args.getSerializable("VENUE");
        initSpinner();
    }

    public void eventActivity(View view)
    {
        //gets all values from text boxes and stores as needed
        EditText editText = findViewById(R.id.editTextTextPersonName3);
        String eventName = editText.getText().toString();

        String venueName = v.getVenueName();

        editText = findViewById(R.id.editTextNumber5);
        String players = editText.getText().toString();

        editText = findViewById(R.id.editTextNumber);
        String date = editText.getText().toString();

        editText = findViewById(R.id.editTextNumber2);
        String startTime = editText.getText().toString();

        editText = findViewById(R.id.editTextNumber3);
        String endTime = editText.getText().toString();

        db.checkEventTimesAction(venueName, activity, date, startTime, endTime,
                (boolean eventOverlaps) ->
                {
                    System.out.println(eventOverlaps);
                });

        //gives message based on which error occurred from incorrect regex format, etc. - if everything is correct, it creates an event
        db.eventCreateActions(eventName, venueName, activity, players, date, startTime, endTime,
                () -> {     // incorrect start time format
                    Toast.makeText(NewEventActivity.this, "Invalid:format of start time is incorrect", Toast.LENGTH_LONG).show();
                    },
                () -> {     // incorrect end time format
                    Toast.makeText(NewEventActivity.this, "Invalid:format of end time is incorrect", Toast.LENGTH_LONG).show();
                },
                () -> {     // incorrect date format
                    Toast.makeText(NewEventActivity.this, "Invalid: format of date is incorrect", Toast.LENGTH_LONG).show();
                },
                () -> {     // incorrect event name format
                    Toast.makeText(NewEventActivity.this, "Invalid: event name must be more than 1 character", Toast.LENGTH_LONG).show();
                    },
                () -> {     // incorrect max players name format
                    Toast.makeText(NewEventActivity.this, "Invalid: max players must be a number greater than 0", Toast.LENGTH_LONG).show();
                },
                () -> {     // incorrect time period - the end time is before the start time
                    Toast.makeText(NewEventActivity.this, "Invalid: end time must be after the start time", Toast.LENGTH_LONG).show();
                },
                () -> {
                    System.out.println("Invalid: There exists an event at this venue that occurs at the same time and has the same activity");
                    Toast.makeText(NewEventActivity.this, "Invalid: There exists an event at this venue that occurs at the same time and has the same activity", Toast.LENGTH_LONG).show();
                },
                () -> {     // Event passes all checks
                    db.createEvent(venueName, eventName, activity, date, startTime, endTime, "0", players, v);
                    Intent intent = new Intent(this, UserHomeActivity.class);
                    startActivity(intent);

                });

    }

    //front end code
    //spinner that displays activities using the venue object passed in
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
        activity = text;
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}