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
    int numEvents;
    int scheduled;

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
        });*/
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

/*        // if formatting is good, set up async. listener to check if venue exists
        ref.child("Venues").child(venueName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {           // if venue doesn't exist
                    venueDoesNotExist.onCallBack();  // call venueDoesNotExist
                }else{
                    venueExists.onCallBack();
                }
                return;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
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
                                   String activity,
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
                                   callBack eventsOverlap,
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
        if(end<start)
        {
            incorrectTimePeriod.onCallBack();
            return;
        }

        // If events overlap at the same venue
        db.checkEventTimesAction(venueName, activity, date, startTime, endTime,
                (boolean eventOverlaps) ->
                {
                    if(eventOverlaps)
                    {
                        eventsOverlap.onCallBack();
                        return;
                    }
                    eventCreate.onCallBack();
                    return;
                });

    }

    public interface viewEventCallback
    {
        // Using a separate onCallBack so that events can be sent back to adapter
        public void onCallBack(ArrayList<Event> events);
    }

    public void viewEventAction(viewEventCallback callback)
    {
        // Setting numEvents back to zero so it updates correctly
        numEvents = 0;

        // Using a valueEventListener so we can loop through DataSnapshot
        ref.child("Events").addValueEventListener(new ValueEventListener()
        {
            // Storing all the events from the database in an ArrayList
            ArrayList<Event> allEvents = new ArrayList<Event>();

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String eventName;
                String venueName;
                String activity;
                String date;
                String startTime;
                String endTime;
                int curParticipants;
                int maxParticipants;

                for(DataSnapshot dSnap : snapshot.getChildren())
                {
                    // Getting all fields from a particular event
                    eventName = dSnap.child("eventName").getValue().toString();
                    venueName = dSnap.child("venueName").getValue().toString();
                    activity = dSnap.child("activity").getValue().toString();
                    date = dSnap.child("date").getValue().toString();
                    startTime = dSnap.child("startTime").getValue().toString();
                    endTime = dSnap.child("endTime").getValue().toString();
                    curParticipants = Integer.parseInt(dSnap.child("curParticipants").getValue().toString());
                    maxParticipants = Integer.parseInt(dSnap.child("maxParticipants").getValue().toString());

                    // Inserting event into list
                    allEvents.add(new Event(eventName, venueName, activity, date, startTime, endTime, curParticipants, maxParticipants));

                    // Incrementing numEvents
                    numEvents++;
                }

                // Using callback to send back all events
                callback.onCallBack(allEvents);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void viewUserEventAction(viewEventCallback scheduledEventCallback)
    {
        // Using a valueEventListener so we can loop through DataSnapshot
        ref.addValueEventListener(new ValueEventListener()
        {
            ArrayList<Integer> userScheduledEventCodes = new ArrayList<Integer>();
            ArrayList<Event> userScheduledEvents = new ArrayList<Event>();

            String eventName;
            String venueName;
            String activity;
            String date;
            String startTime;
            String endTime;
            int curParticipants;
            int maxParticipants;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                // Snapshot of user tree
                DataSnapshot userTree = snapshot.child("users").child(user.getUsername());

                // Snapshot of events
                DataSnapshot eventTree = snapshot.child("Events");

                if(userTree.hasChild("scheduledEvents"))
                {
                    System.out.println("has scheduled Events");
                    for(DataSnapshot dSchedSnap : userTree.child("scheduledEvents").getChildren())
                    {
                        userScheduledEventCodes.add(Integer.parseInt(dSchedSnap.getKey()));
                        System.out.println("In user tree" + Integer.parseInt(dSchedSnap.getKey()));
                    }
                }

                for(DataSnapshot dEventSnap : eventTree.getChildren())
                {
                    System.out.println("Events" + Integer.parseInt(dEventSnap.getKey()));
                    if(userScheduledEventCodes.contains(Integer.parseInt(dEventSnap.getKey())))
                    {
                        // Getting all fields from a particular event
                        eventName = dEventSnap.child("eventName").getValue().toString();
                        venueName = dEventSnap.child("venueName").getValue().toString();
                        activity = dEventSnap.child("activity").getValue().toString();
                        date = dEventSnap.child("date").getValue().toString();
                        startTime = dEventSnap.child("startTime").getValue().toString();
                        endTime = dEventSnap.child("endTime").getValue().toString();
                        curParticipants = Integer.parseInt(dEventSnap.child("curParticipants").getValue().toString());
                        maxParticipants = Integer.parseInt(dEventSnap.child("maxParticipants").getValue().toString());

                        // Inserting event into list
                        userScheduledEvents.add(new Event(eventName, venueName, activity, date, startTime, endTime, curParticipants, maxParticipants));
                    }
                }

                // Using callback to send back all events
                scheduledEventCallback.onCallBack(userScheduledEvents);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public interface viewVenueCallback
    {
        // Using a separate onCallBack so that venues can be passed back to adapter
        public void onCallBack(ArrayList<Venue> venues);
    }

    public void viewVenueAction(viewVenueCallback callback)
    {
        // Using a valueEventListener so we can loop through DataSnapshot
        ref.child("Venues").addValueEventListener(new ValueEventListener()
        {
            // Storing all the venues from the database in an ArrayList
            ArrayList<Venue> allVenues = new ArrayList<Venue>();
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                ArrayList<String> activities;
                ArrayList<Integer> eventCodes;
                String venueName;

                for(DataSnapshot dSnap : snapshot.getChildren())
                {
                    // Getting fields from a particular venue
                    venueName = dSnap.child("venueName").getValue().toString();
                    activities = new ArrayList<String>();
                    eventCodes = new ArrayList<Integer>();

                    // For every sport in that venue, add to an ArrayList
                    for(DataSnapshot sportsDSnap : dSnap.child("sports").getChildren())
                    {
                        activities.add(sportsDSnap.getValue().toString());
                    }

                    // If that venue has any events, add to an ArrayList
                    if(dSnap.hasChild("Events"))
                    {
                        for(DataSnapshot eventsDSnap : dSnap.child("Events").getChildren())
                        {
                            eventCodes.add(Integer.parseInt(eventsDSnap.getValue().toString()));
                        }
                    }

                    allVenues.add(new Venue(venueName, activities, eventCodes));
                }

                // Using callback to send back all venues
                callback.onCallBack(allVenues);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public interface checkEventTimesCallback
    {
        // Using a separate onCallBack so that events can be sent back to adapter
        public void onCallBack(boolean eventTimeOverlaps);
    }

    public void checkEventTimesAction(String venueName, String activity, String date, String startTime, String endTime, checkEventTimesCallback callback)
    {
        // Using a valueEventListener so we can loop through DataSnapshot
        ref.child("Events").addValueEventListener(new ValueEventListener()
        {
            boolean eventTimeOverlaps = false;

            String startLoopedEventStr;
            String endLoopedEventStr;
            int startLoopedEvent;
            int endLoopedEvent;

            // Converting startTime and endTime to ints (using military time)
            // EX. 12:00 and 13:00 turn to 1200 and 1300
            int startPassedEvent = Integer.parseInt(startTime.substring(0,2))*100 + Integer.parseInt(startTime.substring(3));
            int endPassedEvent = Integer.parseInt(endTime.substring(0,2))*100 + Integer.parseInt(endTime.substring(3));

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for(DataSnapshot dSnap : snapshot.getChildren())
                {
                    if(dSnap.child("venueName").getValue().toString().equals(venueName) &&
                            dSnap.child("activity").getValue().toString().equals(activity) &&
                            dSnap.child("date").getValue().toString().equals(date))
                    {
                        startLoopedEventStr = dSnap.child("startTime").getValue().toString();
                        startLoopedEvent = Integer.parseInt(startLoopedEventStr.substring(0,2))*100 + Integer.parseInt(startLoopedEventStr.substring(3));

                        endLoopedEventStr = dSnap.child("endTime").getValue().toString();
                        endLoopedEvent = Integer.parseInt(endLoopedEventStr.substring(0,2))*100 + Integer.parseInt(endLoopedEventStr.substring(3));

                        if((startPassedEvent < endLoopedEvent) && (startLoopedEvent < endPassedEvent))
                        {
                            eventTimeOverlaps = true;
                        }
                    }
                }

                // Using callback to send back check
                callback.onCallBack(eventTimeOverlaps);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
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

