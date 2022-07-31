package com.example.cscb07s22finalproject;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DataBase {

    static DataBase db;
    private final DatabaseReference ref;
    private User user;

    public static final int INCORRECT_FORMAT = -1;
    public static final int DOES_NOT_EXIST = -2;
    public static final int INCORRECT_PASSWORD = -3;

    private DataBase() {
        ref = FirebaseDatabase.getInstance().getReference();
        user = null;                                            // main user of the app
    }
    public static DataBase getInstance() {
        if(db == null) db = new DataBase();
        return db;
    }
    public DatabaseReference getRef() {return ref;}             // getter for ref
    public User getUser() {return user;}                        // getter for user

    /*
        TODO: public int checkUser(String username, String password)
        check if username & password are the right format: return -1 if not

        check if user exists: return -2 if user doesn't exist
                prompt signup

        if user exists: check if password is right: return -3 if password is incorrect
                prompt incorrect Password

        if user exists and password is right: return 0
     */
    public int checkUser(String username, String password) {
        Pattern pattern = Pattern.compile("\\w+");
        Matcher matcher_user = pattern.matcher(username);
        Matcher matcher_pass = pattern.matcher(password);

        if(!(matcher_user.matches()) || !(matcher_pass.matches())) return -1;   // incorrect format

        //TODO: other checks

        return -2;
    }

    public void createUser(String username, String password) {

        ref.child("users").child(username).child("username").setValue(username);
        ref.child("users").child(username).child("password").setValue(password);
        ref.child("users").child(username).child("adminFlag").setValue(false);

        user = new Customer(username, password);   // initialise the user
    }

}
