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
        ref = FirebaseDatabase.getInstance().getReference();    // initialise ref to root of database
        user = null;                                            // main user of the app (initially empty)
    }
    public static DataBase getInstance() {      // singleton getInstance()
        if(db == null) db = new DataBase();
        return db;
    }
    public DatabaseReference getRef() {return ref;}             // getter for ref
    public User getUser() {return user;}                        // getter for user
    public void setUser(String username, String password, boolean isAdmin) {    // setter for user (unsafe code)
        if (isAdmin) user = new Admin(username, password);
        else user = new Customer(username, password);
    }

    /*
        userActions(), takes in username, password and 4 lambda functions to execute under 4
        different scenarios:
        1) either username or password has incorrect format
        2) username and password are correctly formatted, but user doesn't exist
        3) user exists, but the password is wrong
        4) user exists and the password is right

        format for lambda functions: () -> {...your code here...}
     */
    public interface callBack {         // interface to define lambda functions for userActions
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

        // check formatting,
        if(!(matcher_user.matches() && matcher_pass.matches())) {   // if pattern doesn't match
            incorrectFormat.onCallBack();                           // call incorrectFormat
            return;                                                 // and return
        }
/*        // if formatting is good, set up async. listener to check if user exists
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
                    // TODO: display some error message in the future

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




 */
        // if formatting is good, set up async. listener to check if user exists
        ref.child("users").child(username).get().addOnCompleteListener(userFetch -> {
            if (!userFetch.getResult().exists()) {           // if user doesn't exist
                userDoesNotExist.onCallBack();  // call userDoesNotExist
                return;                         // and return
            }

            // if user exists, set up async. listener to check password
            ref.child("users").child(username).child("password").get().addOnCompleteListener(passwordFetch -> {
                if(!passwordFetch.isSuccessful()) return;    // password fetch failed
                // TODO: display some error message in the future

                // password fetch successful
                String actualPassword = passwordFetch.getResult().getValue().toString();
                if(!password.equals(actualPassword)) {
                    userExists_WrongPassword.onCallBack();
                } else userExists_RightPassword.onCallBack();
            });
        });
    }

    public void createUser(String username, String password) {

        ref.child("users").child(username).child("username").setValue(username);
        ref.child("users").child(username).child("password").setValue(password);
        ref.child("users").child(username).child("adminFlag").setValue(false);

        setUser(username, password, false);
    }

}
