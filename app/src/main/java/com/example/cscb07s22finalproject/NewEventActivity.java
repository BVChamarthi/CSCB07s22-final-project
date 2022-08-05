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

    public void createEvent(View view)
    {
        EditText editText = findViewById(R.id.editTextNumber5);
        String players = editText.getText().toString();

        editText = findViewById(R.id.editTextNumber);
        String date = editText.getText().toString();

        editText = findViewById(R.id.editTextNumber2);
        String startTime = editText.getText().toString();

        editText = findViewById(R.id.editTextNumber3);
        String endTime = editText.getText().toString();


//        db.eventCreateActions(players, date, startTime, endTime,
//                () -> {     // incorrect time format
//                    Toast.makeText(NewEventActivity.this, "Invalid: username & password must be 1 or more word characters only", Toast.LENGTH_LONG).show();
//                },
//                () -> {     // incorrect date format
//                    Toast.makeText(NewEventActivity.this, "Username does not exist - please sign up", Toast.LENGTH_LONG).show();
//                },
//                () -> {     // Event passes all checks
//                    // set up async. listener to get adminFlag
//                    db.getRef().child("users").child(username).child("adminFlag").get().addOnCompleteListener(task -> {
//                        if(!task.isSuccessful()) return;    // adminFlag fetch failed
//                        // TODO: display some error message in the future
//
//                        // adminFlag fetch successful
//                        Object bool = task.getResult().getValue();
//                        if(!(bool instanceof Boolean)) return;  // adminFlag is not boolean (failed)
//                        // TODO: display some error message in the future
//
//                        db.setUser(username, password, (Boolean) bool); // set user
//                        // create intent
//                        Intent intent;
//                        if((Boolean)bool)
//                            intent = new Intent(this, AdminHomeActivity.class);
//                        else
//                            intent = new Intent(this, UserHomeActivity.class);
//                        startActivity(intent);
//                    });
//                });

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