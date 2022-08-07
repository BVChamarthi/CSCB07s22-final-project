package com.example.cscb07s22finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NewVenueActivity extends AppCompatActivity {
    DataBase db = DataBase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_venue);
    }

/*    public void createVenue(View view) {
        EditText editText = findViewById(R.id.venueName);
        String venueName = editText.getText().toString();

        editText = findViewById(R.id.activities);
        String[] activities = editText.getText().toString().split("\\n");

        db.venueActions(venueName, activities,
                () -> {     // incorrect format
                    Toast.makeText(NewVenueActivity.this, "Invalid: venueName & activities must be 1 or more word characters only", Toast.LENGTH_LONG).show();
                },
                () -> {     // correct format, venue doesn't exist, create venue
                    db.createVenue(venueName, activities);
                    Intent intent = new Intent(this, AdminHomeActivity.class);
                    startActivity(intent);
                },
                () -> {     // venue exists
                    Toast.makeText(NewVenueActivity.this, "Venue already exists", Toast.LENGTH_LONG).show();
                });
    }*/
}