package com.example.cscb07s22finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;


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
        else if(db.checkUser(username, password) == DataBase.ALREADY_EXISTS){
            Toast.makeText(MainActivity.this, "User already exists in database", Toast.LENGTH_LONG).show();
        }
        else if (db.checkUser(username, password) == DataBase.DOES_NOT_EXIST){
            db.createUser(username, password);
            Intent intent = new Intent(this, UserHomeActivity.class);
            startActivity(intent);
        }
    }

    public void loginAdminActivity(View view) {
        Intent intent = new Intent(this, AdminHomeActivity.class);
        startActivity(intent);
    }

    public void loginUserActivity(View view) {
        Intent intent = new Intent(this, UserHomeActivity.class);
        startActivity(intent);
    }
}