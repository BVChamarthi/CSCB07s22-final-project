package com.example.cscb07s22finalproject;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DataBase
{
    private static DataBase db;
    private final DatabaseReference ref;
    private User user;

    public static final int INCORRECT_FORMAT = -1;
    public static final int DOES_NOT_EXIST = -2;
    public static final int INCORRECT_PASSWORD = -3;
    public static final int CAN_LOGIN = 0;

    private DataBase()
    {
        ref = FirebaseDatabase.getInstance().getReference();
        user = null;                                            // main user of the app
    }

    // Using a singleton design pattern; Creating ONE instance of database
    public static DataBase getInstance()
    {
        if(db == null) db = new DataBase();
        return db;
    }

    // Getter for ref
    public DatabaseReference getRef()
    {
        return ref;
    }

    // Getter for user
    public User getUser()
    {
        return user;
    }

    private void setUser(String username, String password, boolean isAdmin)
    {
        if (isAdmin) user = new Admin(username, password);
        else user = new Customer(username, password);
    }

    public boolean correctUserFormat(String username, String password)
    {
        Pattern pattern = Pattern.compile("\\w+");
        Matcher matcher_user = pattern.matcher(username);
        Matcher matcher_pass = pattern.matcher(password);

        return (matcher_user.matches() && matcher_pass.matches());
    }

    // Verifying usernames
    public void readUsername(String username, usernameExistsCallback userCallback)
    {
        DatabaseReference userDB = FirebaseDatabase.getInstance().getReference().child("users");
        ValueEventListener eventListener = new ValueEventListener()
        {
            boolean userExists = false;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for(DataSnapshot data : snapshot.getChildren())
                {
                    String databaseUsername = data.getKey().toString();

                    if(databaseUsername.equals(username))
                    {
                        userExists = true;
                    }
                }

                userCallback.onCallback(userExists);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        userDB.addListenerForSingleValueEvent(eventListener);
    }

    public interface usernameExistsCallback
    {
        public void onCallback(boolean userExists);
    }

    // Verifying password
    public void readPassword(String username, String password, passwordMatchesCallback passCallback)
    {
        DatabaseReference userDB = FirebaseDatabase.getInstance().getReference().child("users").child(username);
        ValueEventListener eventListener = new ValueEventListener()
        {
            boolean passwordMatches = false;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.child("password").getValue().toString().equals(password))
                {
                    passwordMatches = true;
                }

                passCallback.onCallback(passwordMatches);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        userDB.addListenerForSingleValueEvent(eventListener);
    }

    public interface passwordMatchesCallback
    {
        public void onCallback(boolean passwordMatches);
    }

    public void createUser(String username, String password)
    {
        User newUser = new User(username, password);

        // Adding a new user to database, with a unique identifier
        ref.child("users").child("username").setValue(newUser);
        ref.child("users").child("username").setValue(false);

        setUser(username, password, false);
    }
}
