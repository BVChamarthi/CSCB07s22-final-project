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

/*        if(db.checkUser(username, password) == DataBase.INCORRECT_FORMAT){
            Toast.makeText(MainActivity.this, "Invalid: username & password must be 1 or more word characters only", Toast.LENGTH_LONG).show();
        }
        //Checks if user already is signed up - if not it signs them up and if they exist then it doesn't
        Query query = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("username").equalTo(username);
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
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
*/
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

/*        if(db.checkUser(username, password) == DataBase.INCORRECT_FORMAT){
            Toast.makeText(MainActivity.this, "Invalid: username & password must be 1 or more word characters only", Toast.LENGTH_LONG).show();
        }
        //Checks if user already is signed up - if not it signs them up and if they exist then it doesn't
        Query query = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("username").equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!(snapshot.hasChild(username)))
                {
                    Toast.makeText(MainActivity.this, "Username does not exist - please sign up", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Intent intent = new Intent(this, UserHomeActivity.class);
        startActivity(intent);*/

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
                        // display some error message in the future

                        // adminFlag fetch successful
                        Object bool = task.getResult().getValue();
                        if(!(bool instanceof Boolean)) return;  // adminFlag is not boolean, abort

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