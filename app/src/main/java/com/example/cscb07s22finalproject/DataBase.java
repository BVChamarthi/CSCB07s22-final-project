package com.example.cscb07s22finalproject;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DataBase {

    private static DataBase db;
    private final DatabaseReference ref;
    private User user;

/*    public static final int INCORRECT_FORMAT = -1;
    public static final int DOES_NOT_EXIST = -2;
    public static final int ALREADY_EXISTS = -4;
    public static final int INCORRECT_PASSWORD = -3;
    public static final int CAN_LOGIN = 0;*/

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
    public void setUser(String username, String password, boolean isAdmin) {
        if (isAdmin) user = new Admin(username, password);
        else user = new Customer(username, password);
    }


    /*
        TODO: public int userActions(String username, String password)
        check if username & password are the right format: do incorrectPassword

        check if user exists: return -2 if user doesn't exist
                prompt signup

        if user exists: check if password is right: return -3 if password is incorrect
                prompt incorrect Password

        if user exists and password is right: return 0
     */
    public interface callBack {
        public void onCallBack();
    }

    public void userActions(String username, String password,
                            callBack incorrectFormat,
                            callBack userDoesNotExist,
                            callBack userExists_WrongPassword,
                            callBack userExists_RightPassword
                           ) {

        Pattern pattern = Pattern.compile("\\w+");
        Matcher matcher_user = pattern.matcher(username);
        Matcher matcher_pass = pattern.matcher(password);

        // check formatting, call incorrectFormat and return
        if(!(matcher_user.matches() && matcher_pass.matches())) {
            incorrectFormat.onCallBack();
            return;
        }

        // if formatting is good, set up async. listener to check if user exists
        ref.child("users").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {           // if user doesn't exist
                    userDoesNotExist.onCallBack();  // call userDoesNotExist
                    return;                         // and return
                }

                // if user exists, set up async. listener to check password
                ref.child("users").child(username).child("password").get().addOnCompleteListener(task -> {
                    if(!task.isSuccessful()) return;    // password fetch failed
                    // display some error message in the future

                    // password fetch successful
                    String actualPassword = task.getResult().getValue().toString();
                    if(!password.equals(actualPassword)) {
                        userExists_WrongPassword.onCallBack();
                    } else userExists_RightPassword.onCallBack();
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    public void createUser(String username, String password) {

        ref.child("users").child(username).child("username").setValue(username);
        ref.child("users").child(username).child("password").setValue(password);
        ref.child("users").child(username).child("adminFlag").setValue(false);

        setUser(username, password, false);
    }

}
