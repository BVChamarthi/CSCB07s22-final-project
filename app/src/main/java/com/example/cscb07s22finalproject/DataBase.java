package com.example.cscb07s22finalproject;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DataBase {

    private static DataBase db;
    private final DatabaseReference ref;
    private static User user;

    private ArrayList<Venue> venues;
    private ArrayList<Event> events;

    private DataBase() {
        ref = FirebaseDatabase.getInstance().getReference();    // initialise ref to root of database
        user = null;                                            // main user of the app (initially empty)
        venues = new ArrayList<Venue>();
        events = new ArrayList<Event>();
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
    public ArrayList<Venue> getVenues() { return venues; }
    public ArrayList<Event> getEvents() { return events; }

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
        void onCallBack();
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
                String actualPassword = passwordFetch.getResult().getValue(String.class);
                if(!password.equals(actualPassword)) {
                    userExists_WrongPassword.onCallBack();
                } else userExists_RightPassword.onCallBack();
            });
        });

    }

    public void venueActions(String venueName, String[] activities,
                            callBack incorrectFormat,
                            callBack venueDoesNotExist,
                            callBack venueExists
    ) {

        Pattern pattern = Pattern.compile("(\\w+\\s?)+");
        Matcher matcher_venueName = pattern.matcher(venueName);

        // check formatting,
        if(!matcher_venueName.matches()){
            incorrectFormat.onCallBack();
            return;
        }

        for (String activity : activities) {
            Matcher matcher_activity = pattern.matcher(activity);
            if (!matcher_activity.matches()) {
                incorrectFormat.onCallBack();
                return;
            }
        }

        // if formatting is good, set up async. listener to check if venue exists
        ref.child("Venues").child(venueName).get().addOnCompleteListener(venueFetch -> {
            if (!venueFetch.getResult().exists()) {           // if venue doesn't exist
                venueDoesNotExist.onCallBack();  // call venueDoesNotExist
            }else{
                venueExists.onCallBack();
            }
        });
    }

    public void readVenuesAndEvents(callBack readSuccessful){

        // get events
        ref.child("Events").get().addOnCompleteListener(eventsFetch -> {
            if(eventsFetch.isSuccessful()) {
                for( DataSnapshot eventRef : eventsFetch.getResult().getChildren()) {
                    String eventName = eventRef.child("eventName").getValue(String.class);
                    String activity = eventRef.child("activity").getValue(String.class);
                    String date = eventRef.child("date").getValue(String.class);
                    String startTime = eventRef.child("startTime").getValue(String.class);
                    String endTime = eventRef.child("endTime").getValue(String.class);
                    int curParticipants = eventRef.child("curParticipants").getValue(Integer.class);
                    int maxParticipants = eventRef.child("maxParticipants").getValue(Integer.class);
                    events.add(new Event(eventName, null, activity, date, startTime,
                            endTime, curParticipants, maxParticipants));
                }
                // get venues, attach events
                ref.child("Venues").get().addOnCompleteListener(venuesFetch -> {
                    if(venuesFetch.isSuccessful()) {
                        for( DataSnapshot venueRef : venuesFetch.getResult().getChildren()) {
                            String venueName = venueRef.child("venueName").getValue(String.class);
                            ArrayList<String> activities = (ArrayList<String>) venueRef.child("activities").getValue();
                            venues.add(new Venue(venueName, activities));
                        }
                        readSuccessful.onCallBack();
                    }
                });
            }
        });

    }

    public void createUser(String username, String password) {

        ref.child("users").child(username).child("username").setValue(username);
        ref.child("users").child(username).child("password").setValue(password);
        ref.child("users").child(username).child("adminFlag").setValue(false);

        setUser(username, password, false);
    }

    public void createVenue(String venueName, String[] activities){
        ref.child("Venues").child(venueName).child("venueName").setValue(venueName);
        ref.child("Venues").child(venueName).child("sports").setValue(activities);
    }
}

