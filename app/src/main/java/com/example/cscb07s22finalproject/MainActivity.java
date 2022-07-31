package com.example.cscb07s22finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("first").setValue(100);
        ref.child("third").setValue("harry");
        ref.child("third").setValue("notharry");

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
        EditText editText = (EditText) findViewById(R.id.editTextTextPersonName);
        String username = editText.getText().toString();

        editText = (EditText) findViewById(R.id.editTextTextPassword);
        String password = editText.getText().toString();

        Pattern pattern = Pattern.compile("\\w+");
        Matcher matcher_user = pattern.matcher(username);
        Matcher matcher_pass = pattern.matcher(password);

        if(!(matcher_user.matches()) || !(matcher_pass.matches())){
            Toast.makeText(MainActivity.this, "Invalid: username & password must be 1 or more word characters only", Toast.LENGTH_LONG).show();
        }else{
            ref.child("users").child(username).child("username").setValue(username);
            ref.child("users").child(username).child("password").setValue(password);
            ref.child("users").child(username).child("adminFlag").setValue(false);

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