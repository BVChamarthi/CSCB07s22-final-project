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

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    DataBase db = DataBase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

/*      // write array list to firebase
        ArrayList<Integer> intArray = new ArrayList<Integer>();
        intArray.add(1);
        intArray.add(2);
        intArray.add(4);
        intArray.add(8);
        db.getRef().child("intArray").setValue(intArray);
*/

/*        // read array list from firebase
        db.getRef().child("intArray").get().addOnCompleteListener(arrayFetch -> {
            if(arrayFetch.isSuccessful()){
                ArrayList<Integer> intArray = (ArrayList<Integer>) arrayFetch.getResult().getValue();
                Toast.makeText(MainActivity.this, "intArray :" + intArray.get(0), Toast.LENGTH_LONG).show();
            }
        });*/

/*        // read a user object from firebase: DOESN'T WORK (pleas fix this if you can)
        db.getRef().child("users").child("bharath").get().addOnCompleteListener(userFetch-> {
           if(userFetch.isSuccessful()) {
               Toast.makeText(MainActivity.this, "test", Toast.LENGTH_LONG).show();
               User user = userFetch.getResult().getValue(User.class);
               Toast.makeText(MainActivity.this,
                       "user: " + user.getUsername() +
                       ", password: " + user.getPassword(), Toast.LENGTH_LONG).show();
           }
        });*/

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