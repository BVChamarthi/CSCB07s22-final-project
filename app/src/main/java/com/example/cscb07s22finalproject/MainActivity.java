package com.example.cscb07s22finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;


public class MainActivity extends AppCompatActivity {
    DataBase db = DataBase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseReference ref = db.getRef();
        ref.child("first").setValue("Our Sports App");
        ref.child("first").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("demo", "Error getting data", task.getException());
            }
            else {
                TextView tv = findViewById(R.id.textView1);
                tv.setText(task.getResult().getValue().toString());
            }
        });
    }

    public void signUpActivity(View view) {
        EditText editText = findViewById(R.id.editTextTextPersonName);
        String username = editText.getText().toString();

        editText = findViewById(R.id.editTextTextPassword);
        String password = editText.getText().toString();


        db.userActions(username, password,
                () -> {     // incorrect format
                    Toast.makeText(MainActivity.this, "Invalid: username & password must be 1 or more word characters only", Toast.LENGTH_LONG).show();
                },
                () -> {     // correct format, user doesn't exist, signup user
                    db.createUser(username, password);
                    Intent intent = new Intent(this, UserHomeActivity.class);
                    startActivity(intent);
                },
                () -> {     // user exists, wrong password
                    Toast.makeText(MainActivity.this, "Username already exists w", Toast.LENGTH_LONG).show();
                },
                () -> {     // user exists, right password
                    Toast.makeText(MainActivity.this, "Username already exists r", Toast.LENGTH_LONG).show();
                });
    }

    public void loginUserActivity(View view) {


        EditText editText = findViewById(R.id.editTextTextPersonName);
        String username = editText.getText().toString();

        editText = findViewById(R.id.editTextTextPassword);
        String password = editText.getText().toString();


        db.userActions(username, password,
                () -> {     // incorrect format
                    Toast.makeText(MainActivity.this, "Invalid: username & password must be 1 or more word characters only", Toast.LENGTH_LONG).show();
                },
                () -> {     // correct format, user doesn't exist, signup user
                    Toast.makeText(MainActivity.this, "Username does not exist - please sign up", Toast.LENGTH_LONG).show();
                },
                () -> {     // user exists, wrong password
                    Toast.makeText(MainActivity.this, "Incorrect Password", Toast.LENGTH_LONG).show();
                },
                () -> {     // user exists, right password
                    // set up async. listener to get adminFlag
                    db.getRef().child("users").child(username).child("adminFlag").get().addOnCompleteListener(task -> {
                        if(!task.isSuccessful()) return;    // adminFlag fetch failed
                        // TODO: display some error message in the future

                        // adminFlag fetch successful
                        Object bool = task.getResult().getValue();
                        if(!(bool instanceof Boolean)) return;  // adminFlag is not boolean (failed)
                        // TODO: display some error message in the future

                        db.setUser(username, password, (Boolean) bool); // set user
                        // create intent
                        Intent intent;
                        if((Boolean)bool)
                            intent = new Intent(this, AdminHomeActivity.class);
                        else
                            intent = new Intent(this, UserHomeActivity.class);
                        startActivity(intent);
                    });
                });

    }

}