package com.example.cscb07s22finalproject;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DataBase {

    private static DataBase db;
    private final DatabaseReference ref;
    private static User user;
    private boolean dataFetched;

    private ArrayList<Venue> venues;
    private ArrayList<Event> events;

    private DataBase() {
        ref = FirebaseDatabase.getInstance().getReference();    // initialise ref to root of database
        user = null;                                            // main user of the app (initially empty)
        venues = new ArrayList<>();
        events = new ArrayList<>();
        dataFetched = false;
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

        for (int i = 0; i < activities.length; i++) {
            Matcher matcher_activity = pattern.matcher(activities[i]);
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
            return;
        });
    }

    public void eventCreateActions(String eventName,
                                   String venueName,
                                   String players,
                                   String date,
                                   String startTime,
                                   String endTime,
                                   callBack incorrectstartTimeFormat,
                                   callBack incorrectendTimeFormat,
                                   callBack incorrectDateFormat,
                                   callBack incorrectNameFormat,
                                   callBack incorrectPlayersFormat,
                                   callBack incorrectTimePeriod,
                                   callBack eventCreate
    ) {

        //checks regexes for creating new activity

        //time regex
        Pattern pattern = Pattern.compile("(((2[0-3])||([0-1][0-9])):([0-5][0-9]))");
        Matcher matcher_startTime = pattern.matcher(startTime);
        Matcher matcher_endTime = pattern.matcher(endTime);

        //date regex
        Pattern pattern2 = Pattern.compile("(((202[2-9])||(20[3-9][0-9])||(2[1-9][0-9][0-9]))-((0[1-9])||(1[0-2]))-(([0-2][0-9])||(3[0-1])))");
        Matcher matcher_date = pattern2.matcher(date);

        //event name regex
        Pattern pattern3 = Pattern.compile(".+");
        Matcher matcher_eventName = pattern3.matcher(eventName);

        //max players regex
        Pattern pattern4 = Pattern.compile("[1-9]([0-9]*)");
        Matcher matcher_Players = pattern4.matcher(players);

        // if start time doesn't match regex
        if(!matcher_startTime.matches()){
            incorrectstartTimeFormat.onCallBack();
            return;
        }

        // if end time doesn't match regex
        if(!matcher_endTime.matches()){
            incorrectendTimeFormat.onCallBack();
            return;
        }

        // if date doesn't match regex
        if(!matcher_date.matches()){
            incorrectDateFormat.onCallBack();
            return;
        }

        // if event name doesn't match regex
        if(!matcher_eventName.matches()){
            incorrectNameFormat.onCallBack();
            return;
        }

        // if max players doesn't match regex
        if(!matcher_Players.matches()){
            incorrectPlayersFormat.onCallBack();
            return;
        }

        //changes the start and end time to numbers
        int start = Integer.parseInt(startTime.substring(0,2))*100 + Integer.parseInt(startTime.substring(3));

        int end = Integer.parseInt(endTime.substring(0,2))*100 + Integer.parseInt(endTime.substring(3));

        //if the end time is less than start time, then the time is not valid
        if(end<start){
            incorrectTimePeriod.onCallBack();
            return;
        }

        eventCreate.onCallBack();
        return;



    }

    public interface viewEventCallback
    {
        // Using a separate onCallBack so that events can be sent back to adapter
        public void onCallBack(ArrayList<Event> events);
    }

    public interface viewVenueCallback
    {
        // Using a separate onCallBack so that venues can be passed back to adapter
        public void onCallBack(ArrayList<Venue> venues);
    }

    public void readVenuesAndEvents(viewEventCallback eventCallback,
                                    viewVenueCallback venueCallback){



        // get events
        ref.child("Events").get().addOnCompleteListener(eventsFetch -> {
            if(!eventsFetch.isSuccessful()) return; // if fetch failed, return

            for( DataSnapshot eventRef : eventsFetch.getResult().getChildren()) {
                String eventName = eventRef.child("eventName").getValue(String.class);
                String activity = eventRef.child("activity").getValue(String.class);
                String date = eventRef.child("date").getValue(String.class);
                String startTime = eventRef.child("startTime").getValue(String.class);
                String endTime = eventRef.child("endTime").getValue(String.class);
                int curParticipants = eventRef.child("curParticipants").getValue(Integer.class);
                int maxParticipants = eventRef.child("maxParticipants").getValue(Integer.class);
                events.add(new Event(eventName, (Venue) null, activity, date, startTime,
                        endTime, curParticipants, maxParticipants));
            }

            eventsConnectionCheck ecc = eventsConnectionCheck.getInstance();
            ecc.reset();
            // get venues, attach events
            ref.child("Venues").get().addOnCompleteListener(venuesFetch -> {
                if(!venuesFetch.isSuccessful()) return; // if fetch failed, return

                for( DataSnapshot venueRef : venuesFetch.getResult().getChildren()) {
                    String venueName = venueRef.child("venueName").getValue(String.class);
                    ArrayList<String> activities = (ArrayList<String>) venueRef.child("activities").getValue();
                    Venue venue = new Venue(venueName, activities);
                    venues.add(venue);

                    ref.child("Venues").child(venueName).child("Events").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()) {
                                for(DataSnapshot eventCodeSnap : snapshot.getChildren()) {
                                    int eventCode = eventCodeSnap.getValue(Integer.class);
                                    venue.addEventNoCheck(events.get(eventCode));
                                    ecc.checkAndCall(readSuccessful);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
        });

    }

    public void createUser(String username, String password)
    {
        ref.child("users").child(username).child("username").setValue(username);
        ref.child("users").child(username).child("password").setValue(password);
        ref.child("users").child(username).child("adminFlag").setValue(false);

        setUser(username, password, false);
    }

    public void createVenue(String venueName, String[] activities){
        ref.child(venueName);
        ref.child("Venues").child(venueName).child("venueName").setValue(venueName);
        for(int i = 0; i < activities.length; i++){
            ref.child("Venues").child(venueName).child("sports").child("sport" + (i+1)).setValue(activities[i]);
        }
    }

    public void createEvent(String venueName, String eventName, String activity, String date, String startTime, String endTime, String curParticipants, String maxParticipants, Venue venue){

            // Creating an event object
            // When passing in an object to Firebase, it will automatically add
            // All fields as children. It is just easier this way.
            Event e = new Event(eventName, venueName, activity, date, startTime, endTime, Integer.parseInt(curParticipants), Integer.parseInt(maxParticipants));

            // When adding events to database, we use numEvents as the "EventCode"
            // This way, every event has a different "EventCode"
            ref.child("Events").child(String.valueOf(numEvents)).setValue(e);

            // Adding eventCode to current user
            if(user instanceof Customer)
            {
                // Need to use map according to StackExchange to append to database (not override)
                // This will create a key:value pair, and appends it to the tree
                // TODO: May fix tomorrow
                HashMap<String, Object> map = new HashMap<>();
                map.put(String.valueOf(numEvents), eventName);
                ref.child("users").child(user.getUsername()).child("scheduledEvents").updateChildren(map);
                //ref.child("users").child(user.getUsername()).child("scheduledEvents").setValue(((Customer)user).getScheduledEvents());
            }

            // Adding eventCode to respective venue
            venue.addEventCodeToVenue(numEvents);
            ref.child("Venues").child(venueName).child("Events").setValue(venue.getCodes());
    }
}

