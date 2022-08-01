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

public class MainActivity extends AppCompatActivity {
    DataBase db = DataBase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseReference ref = db.getRef();
        ref.child("first").setValue(100);
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

        if(db.checkUser(username, password) == DataBase.INCORRECT_FORMAT){
            Toast.makeText(MainActivity.this, "Invalid: username & password must be 1 or more word characters only", Toast.LENGTH_LONG).show();
        }
        //Checks if user already is signed up - if not it signs them up and if they exist then it doesn't
        Query query = db.getRef().child("users").orderByChild("username").equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.hasChild(username))
                {
                    Toast.makeText(MainActivity.this, "Username already exists", Toast.LENGTH_LONG).show();

                }
                else
                {
                    db.createUser(username, password);
                    login();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void login ()
    {

        Intent intent = new Intent(this, UserHomeActivity.class);
        startActivity(intent);
    }


    public void loginAdminActivity(View view) {
        Intent intent = new Intent(this, UserHomeActivity.class);
        startActivity(intent);

    }

    public void loginUserActivity(View view) {


        EditText editText = findViewById(R.id.editTextTextPersonName);
        String username = editText.getText().toString();

        editText = findViewById(R.id.editTextTextPassword);
        String password = editText.getText().toString();

        if(db.checkUser(username, password) == DataBase.INCORRECT_FORMAT){
            Toast.makeText(MainActivity.this, "Invalid: username & password must be 1 or more word characters only", Toast.LENGTH_LONG).show();
        }
        //Checks if user already is signed up - if not it signs them up and if they exist then it doesn't
        Query query = db.getRef().child("users").orderByChild("username").equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!(snapshot.hasChild(username)))
                {
                    Toast.makeText(MainActivity.this, "Username does not exist - please sign up", Toast.LENGTH_LONG).show();

                }
                else
                {
                    //TO-DO: IMPLEMENT IF PASSWORD IS INCORRECT
                    db.createUser(username, password);
                    login();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}