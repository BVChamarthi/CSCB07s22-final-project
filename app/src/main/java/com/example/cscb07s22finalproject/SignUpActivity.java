package com.example.cscb07s22finalproject;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    /** Called when the user taps the Sign Up button */
    public void createAccount(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}