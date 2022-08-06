package com.example.cscb07s22finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.lang.reflect.Array;

import java.util.ArrayList;

public class UserHomeActivity extends AppCompatActivity
{
    /*
    Important methods:
        CreateList(): fill Event items with info
        line 52: what happens after join button is clicked
     */
    DataBase db = DataBase.getInstance();
    private RecyclerView recyclerView;
    private Button btn;

    private ArrayList<Event> events = new ArrayList<>();

    private String selectedFilter = "all";
    private String currentSearchText = "";
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        TextView usernameText = findViewById(R.id.textView4);
        usernameText.setText(db.getUser().toString());

        //eventsAdapter.printEvents();
    }
}