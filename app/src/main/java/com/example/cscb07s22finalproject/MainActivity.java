package com.example.cscb07s22finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.cscb07s22finalproject";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("first").setValue("My Sports App");
        ref.child("second").setValue("harry");

        ref.child("first").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    TextView tv = findViewById(R.id.textView1);
                    Log.e("demo", "Error getting data", task.getException());
                }
                else {
                    TextView tv = findViewById(R.id.textView1);
                    tv.setText(task.getResult().getValue().toString());
                }
            }
        });
    }

    public void signUpActivity(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void loginAdminActivity(View view) {
        Intent intent = new Intent(this, AdminHomeActivity.class);
        startActivity(intent);
    }

    public void loginUserActivity(View view) {

        EditText editText = (EditText) findViewById(R.id.editTextTextPersonName);
        String name = editText.getText().toString();
        editText = (EditText) findViewById(R.id.editTextTextPassword);
        String password = editText.getText().toString();

        Pattern pattern = Pattern.compile("\\w+");
        Matcher user_match = pattern.matcher(name);
        Matcher pass_match = pattern.matcher(password);

        if(!(user_match.matches()) || !(pass_match.matches())){
           //Display an error message - you must enter a username or password
        }
        //else if username and password is not in the data
            //display error message - this user does not exist in our data
        else
        {
            Intent intent = new Intent(this, UserHomeActivity.class);
            startActivity(intent);
        }
    }
}