package com.example.cscb07s22finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
<<<<<<< Updated upstream

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
=======
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

>>>>>>> Stashed changes

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
<<<<<<< Updated upstream
=======

    public void signUpActivity(View view)
    {
        // Getting username
        EditText editText = findViewById(R.id.editTextTextPersonName);
        String username = editText.getText().toString();

        // Getting password
        editText = findViewById(R.id.editTextTextPassword);
        String password = editText.getText().toString();

        // Going through all checks
        if(!db.correctUserFormat(username, password))
        {
            Toast.makeText(MainActivity.this, "Invalid: username & password must be 1 or more word characters only", Toast.LENGTH_LONG).show();
        }
        else
        {
            db.createUser(username, password, false);

            // Brings user to the home page
            Intent intent = new Intent(this, UserHomeActivity.class);
            startActivity(intent);
        }
    }

    public void loginAdminActivity(View view)
    {


        Intent intent = new Intent(this, AdminHomeActivity.class);
        startActivity(intent);
    }

    public void loginUserActivity(View view)
    {
        // Getting username
        EditText editText = findViewById(R.id.editTextTextPersonName);
        String username = editText.getText().toString();

        // Getting password
        editText = findViewById(R.id.editTextTextPassword);
        String password = editText.getText().toString();

        DatabaseReference userDB = FirebaseDatabase.getInstance().getReference();
    }
>>>>>>> Stashed changes
}