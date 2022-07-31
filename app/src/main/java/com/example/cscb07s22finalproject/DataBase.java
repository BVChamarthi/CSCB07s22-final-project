package com.example.cscb07s22finalproject;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    // Creating a user
    private void setUser(String username, String password, boolean isAdmin)
    {
        if (isAdmin) user = new Admin(username, password);
        else user = new Customer(username, password);
    }

    /*
        TODO: public int checkUser(String username, String password)
        check if username & password are the right format: return -1 if not

        check if user exists: return -2 if user doesn't exist
                prompt signup

        if user exists: check if password is right: return -3 if password is incorrect
                prompt incorrect Password

        if user exists and password is right: return 0
     */
    public boolean correctUserFormat(String username, String password)
    {
        Pattern pattern = Pattern.compile("\\w+");
        Matcher matcher_user = pattern.matcher(username);
        Matcher matcher_pass = pattern.matcher(password);

        return (matcher_user.matches() && matcher_pass.matches());
    }

    public void createUser(String username, String password, boolean isAdmin)
    {
        User newUser = new User(username, password, isAdmin);

        // Adding a new user to database, with a unique identifier
        ref.child("users").push().setValue(newUser);

        setUser(username, password, false);
    }

}
