package com.example.cscb07s22finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("first").setValue(100);
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
        Intent intent = new Intent(this, UserHomeActivity.class);
        startActivity(intent);
    }
}