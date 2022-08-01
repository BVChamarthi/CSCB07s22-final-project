package com.example.cscb07s22finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
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

    public void signUpActivity(View view)
    {
        // Getting username
        EditText editText = findViewById(R.id.editTextTextPersonName);
        String username = editText.getText().toString();

        // Getting password
        editText = findViewById(R.id.editTextTextPassword);
        String password = editText.getText().toString();

        // Check if format is correct
        if(!db.correctUserFormat(username, password))
        {
            printLoginError("Invalid: Username and/or password in incorrect format");
        }

        // Check if user exists
        db.readUsername(username, new DataBase.usernameExistsCallback()
        {
            @Override
            public void onCallback(boolean userExists)
            {
                if(!userExists)
                {
                    db.createUser(username, password);
                    login();
                }
                else
                {
                    printLoginError("Invalid: Username already taken");
                }
            }
        });
    }

    public void loginAdminActivity(View view)
    {
        Intent intent = new Intent(this, AdminHomeActivity.class);
        startActivity(intent);
    }

    public void loginUserActivity(View view) {
        // Getting username
        EditText editText = findViewById(R.id.editTextTextPersonName);
        String username = editText.getText().toString();

        // Getting password
        editText = findViewById(R.id.editTextTextPassword);
        String password = editText.getText().toString();

        // First, check if the user exists
        db.readUsername(username, new DataBase.usernameExistsCallback()
        {
            @Override
            public void onCallback(boolean userExists) {
                if (!userExists)
                {
                    // Do something, preferably send an error message and reset the main screen\
                    printLoginError("Invalid: Username does not exist");
                }
                else
                {
                    // If the user exists, check if the password is right
                    db.readPassword(username, password, new DataBase.passwordMatchesCallback()
                    {
                        @Override
                        public void onCallback(boolean passwordMatches)
                        {
                            if (!passwordMatches)
                            {
                                // Do something, preferably send an error message and reset the main screen
                                printLoginError("Invalid: Incorrect password");
                            }
                            else
                            {
                                login();
                            }
                        }
                    });
                }
            }
        });
    }

    public void printLoginError(String message)
    {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }

    public void login()
    {
        // Brings user to the home page
        Intent intent = new Intent(this, UserHomeActivity.class);
        startActivity(intent);
    }
}

