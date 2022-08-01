package com.example.cscb07s22finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class NewVenueActivity extends AppCompatActivity {
    DataBase db = DataBase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_venue);
    }

    public void createVenue(View view) {
        EditText editText = findViewById(R.id.venueName);
        String venueName = editText.getText().toString();

        editText = findViewById(R.id.activities);
        String[] activities = editText.getText().toString().split("\\n");

        db.createVenue(venueName, activities);
    }
}