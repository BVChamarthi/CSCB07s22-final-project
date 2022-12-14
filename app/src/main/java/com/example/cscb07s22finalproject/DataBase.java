package com.example.cscb07s22finalproject;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DataBase {

    private static DataBase db;
    private final DatabaseReference ref;
    private static User user;
    private boolean dataFetched;

    private ArrayList<Venue> venues;
    private ArrayList<Event> events;

    private static Venue defaultEntry;                       // default selection for spinner

    private DataBase() {
        ref = FirebaseDatabase.getInstance().getReference();    // initialise ref to root of database
        user = null;                                            // main user of the app (initially empty)
        venues = new ArrayList<>();
        events = new ArrayList<>();
        dataFetched = false;
    }
    public static DataBase getInstance() {      // singleton getInstance()
        if(db == null) {
            db = new DataBase();
            defaultEntry = new Venue("All Venues", null);
        }
        return db;
    }

    public DatabaseReference getRef() {return ref;}             // getter for ref
    public User getUser() {return user;}                        // getter for user

    public void setUser(String username, String password, boolean isAdmin) {    // setter for user (unsafe code)
        if (isAdmin) user = new Admin(username, password);
        else {
            user = new Customer(username, password);
            Customer c = (Customer) user;
            ref.child("users").child(username).child("joinedEvents").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        c.setJoinedEvents(new ArrayList<>());
                        for (DataSnapshot snapshot1 : snapshot.getChildren())
                            c.getJoinedEvents().add(snapshot1.getValue(Integer.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            ref.child("users").child(username).child("scheduledEvents").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        c.setScheduledEvents(new ArrayList<>());
                        for (DataSnapshot snapshot1 : snapshot.getChildren())
                            c.getScheduledEvents().add(snapshot1.getValue(Integer.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    public ArrayList<Venue> getVenues() { return venues; }
    public ArrayList<Event> getEvents() { return events; }
    public void setDataFetched(boolean b) { dataFetched = b; }
    public boolean getDataFetched() { return dataFetched; }
    public Venue getDefaultEntry() { return defaultEntry; }

    public ArrayList<Integer> constructEventsArray() {
        ArrayList<Integer> ans = new ArrayList<>(events.size());
        for(int i = 0; i < events.size(); i++) {
            ans.add(i);
        }
        return ans;
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
        void onCallBack();
    }

    public interface stringCallBack {
        void onCallBack(String msg);
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
                String actualPassword = Objects.requireNonNull(passwordFetch.getResult().getValue()).toString();
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

        Pattern pattern = Pattern.compile("\\w+(\\s\\w+)*");
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

    // Copied from filter code
    int[] extractDate(String date) {
        String[] yyyymmddIntermadiate = date.split("-");
        int[] yyyymmdd = {0, 0, 0};
        for(int i =0; i < 3; i++) {
            yyyymmdd[i] = Integer.parseInt(yyyymmddIntermadiate[i]);
        }
        return yyyymmdd;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void eventCreateActions(String eventName,
                                   Venue parentVenue,
                                   String activity, String players,
                                   String date,
                                   String startTime,
                                   String endTime,
                                   callBack incorrectStartTimeFormat,
                                   callBack incorrectStartTimeFormat2,
                                   callBack incorrectEndTimeFormat,
                                   callBack incorrectEndTimeFormat2,
                                   callBack incorrectDateFormat,
                                   callBack incorrectNameFormat,
                                   callBack incorrectPlayersFormat,
                                   callBack incorrectDatePeriod,
                                   callBack incorrectTimePeriod,
                                   callBack eventsOverlap,
                                   callBack eventCreate
    ) {
        //checks regexes for creating new activity

        //time regex
        Pattern pattern = Pattern.compile("(((2[0-3])||([0-1][0-9])):([0-5][0-9]))");
        Matcher matcher_startTime = pattern.matcher(startTime);
        Matcher matcher_endTime = pattern.matcher(endTime);

        Pattern pattern5 = Pattern.compile("[0-9]:([0-5][0-9])");
        Matcher matcher_startTime2 = pattern5.matcher(startTime);
        Matcher matcher_endTime2 = pattern5.matcher(endTime);

        //date regex
        Pattern pattern2 = Pattern.compile("(((202[2-9])||(20[3-9][0-9])||(2[1-9][0-9][0-9]))-((0[1-9])||(1[0-2]))-(([0-2][0-9])||(3[0-1])))");
        Matcher matcher_date = pattern2.matcher(date);

        //event name regex
        Pattern pattern3 = Pattern.compile("\\w+(\\s\\w+)*");
        Matcher matcher_eventName = pattern3.matcher(eventName);

        //max players regex
        Pattern pattern4 = Pattern.compile("[1-9]([0-9]*)");
        Matcher matcher_Players = pattern4.matcher(players);

        // if start time doesn't match regex
        if(!matcher_startTime.matches())
        {
            if(matcher_startTime2.matches())
            {
                incorrectStartTimeFormat2.onCallBack();
                return;
            }

            incorrectStartTimeFormat.onCallBack();
            return;
        }

        // if end time doesn't match regex
        if(!matcher_endTime.matches())
        {
            if(matcher_endTime2.matches())
            {
                incorrectEndTimeFormat2.onCallBack();
                return;
            }

            incorrectEndTimeFormat.onCallBack();
            return;
        }

        // if date doesn't match regex
        if(!matcher_date.matches()){
            incorrectDateFormat.onCallBack();
            return;
        }

        // if date is in the past
        int[] eventDate = extractDate(date);
        int[] currentDate = extractDate(String.valueOf(java.time.LocalDate.now()));

        // If event is in a past year
        if((eventDate[0] < currentDate[0]) ||
                // If event is in a past month
            (eventDate[0] == currentDate[0] && eventDate[1] < currentDate[1]) ||
                    // If event is in a past day
                ((eventDate[0] == currentDate[0] && eventDate[1] == currentDate[1]) && eventDate[2] < currentDate[2])
        )
        {
            incorrectDatePeriod.onCallBack();
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

        // If events overlap at the same venue
        db.checkEventTimesAction(parentVenue, activity, date, startTime, endTime,
                (boolean eventOverlaps) ->
                {
                    if(eventOverlaps)
                    {
                        eventsOverlap.onCallBack();
                        return;
                    }
                    else
                    {
                        eventCreate.onCallBack();
                        return;
                    }
                });

    }

    public interface viewEventCallback
    {
        // Using a separate onCallBack so that events can be sent back to adapter
        void onCallBack(ArrayList<Event> events);
    }

/*    public void viewEventAction(viewEventCallback callback)
    {
        // Setting numEvents back to zero so it updates correctly

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
                }

                // Using callback to send back all events
                callback.onCallBack(allEvents);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/

/*    public void viewUserEventAction(String eventType, viewEventCallback scheduledEventCallback)
    {
        // Using a valueEventListener so we can loop through DataSnapshot
        ref.addValueEventListener(new ValueEventListener()
        {
            ArrayList<Integer> userEventCodes = new ArrayList<Integer>();
            ArrayList<Event> userEvents = new ArrayList<Event>();

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

                if(userTree.hasChild(eventType))
                {
                    for(DataSnapshot dSchedSnap : userTree.child(eventType).getChildren())
                    {
                        userEventCodes.add(Integer.parseInt(dSchedSnap.getKey()));
                    }
                }

                for(DataSnapshot dEventSnap : eventTree.getChildren())
                {
                    if(userEventCodes.contains(Integer.parseInt(dEventSnap.getKey())))
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
                        userEvents.add(new Event(eventName, null, activity, date, startTime, endTime, curParticipants, maxParticipants));
                    }
                }

                // Using callback to send back all events
                scheduledEventCallback.onCallBack(userEvents);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

 */

    public interface viewVenueCallback
    {
        // Using a separate onCallBack so that venues can be passed back to adapter
        void onCallBack(ArrayList<Venue> venues);
    }

    private static final class eventsConnectionCheck {
        private static eventsConnectionCheck ecc;
        private int count;
        private eventsConnectionCheck() { count = 0; }
        public static eventsConnectionCheck getInstance() {
            if(ecc == null) ecc = new eventsConnectionCheck();
            return ecc;
        }
        public void reset() { count = 0; }
        public int getCount() { return count; }
        public void checkAndCall(callBack success) {
            count ++;
            if(count >= DataBase.getInstance().getEvents().size()) {
                DataBase.getInstance().setDataFetched(true);
                success.onCallBack();
            }
        }
    }
/*
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

 */

    public void readVenuesAndEvents(viewEventCallback eventCallback,
                                    viewVenueCallback venueCallback,
                                    stringCallBack msg){                // only used for debugging

//        if(dataFetched) {
//            eventCallback.onCallBack(events);
//            venueCallback.onCallBack(venues);
//            return;
//        }

        // There was a bug where events and venues don't reset after the user logs out, and
        // another user logs in (ie, duplicates exist). Sometimes it crashes.
        // This way, venues and events won't have duplicates. Currently doesn't crash
        events.clear();
        venues.clear();

        // get events
 /*       ref.child("Events").get().addOnCompleteListener(eventsFetch -> {
            if(!eventsFetch.isSuccessful()) {
                msg.onCallBack("events failed");
                return; // if fetch failed, return
            }

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

            msg.onCallBack("Events " + events.size());

            eventsConnectionCheck ecc = eventsConnectionCheck.getInstance();
            ecc.reset();

            // get venues, attach events
            ref.child("Venues").get().addOnCompleteListener(venuesFetch -> {
                if(!venuesFetch.isSuccessful()) return; // if fetch failed, return

                for( DataSnapshot venueRef : venuesFetch.getResult().getChildren()) {
                    String venueName = venueRef.child("venueName").getValue(String.class);
                    ArrayList<String> activities = new ArrayList<>();

                    ref.child("Venues").child(venueName).child("sports").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                for(DataSnapshot snapshot1 : snapshot.getChildren())
                                    activities.add(snapshot1.getValue(String.class));

                                msg.onCallBack(venueName + ", " + activities);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    Venue venue = new Venue(venueName, activities);
                    venues.add(venue);

                    ref.child("Venues").child(venueName).child("Events").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()) {
                                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    int eventCode = dataSnapshot.getValue(Integer.class);
                                    venue.addEvent(eventCode);
                                    ecc.checkAndCall(() -> {
                                        venueCallback.onCallBack(venues);
                                        eventCallback.onCallBack(events);
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                msg.onCallBack("Venues " + venues.size());
            });
        });*/
        ref.child("Events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                msg.onCallBack("events");
                if(!snapshot.exists()) return; // if fetch failed, return

                for( DataSnapshot eventRef : snapshot.getChildren()) {
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

                msg.onCallBack("Events " + events.size());

                eventsConnectionCheck ecc = eventsConnectionCheck.getInstance();
                ecc.reset();

                // get venues, attach events
                ref.child("Venues").get().addOnCompleteListener(venuesFetch -> {
                    if(!venuesFetch.isSuccessful()) return; // if fetch failed, return

                    for( DataSnapshot venueRef : venuesFetch.getResult().getChildren()) {
                        String venueName = venueRef.child("venueName").getValue(String.class);
                        ArrayList<String> activities = new ArrayList<>();

                        ref.child("Venues").child(venueName).child("sports").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    for(DataSnapshot snapshot1 : snapshot.getChildren())
                                        activities.add(snapshot1.getValue(String.class));

                                    msg.onCallBack(venueName + ", " + activities);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        Venue venue = new Venue(venueName, activities);
                        venues.add(venue);

                        ref.child("Venues").child(venueName).child("Events").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()) {
                                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        int eventCode = dataSnapshot.getValue(Integer.class);
                                        venue.addEvent(eventCode);
                                        ecc.checkAndCall(() -> {
                                            venueCallback.onCallBack(venues);
                                            eventCallback.onCallBack(events);
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    msg.onCallBack("Venues " + venues.size());
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                msg.onCallBack("err");
            }
        });
    }

    public interface checkEventTimesCallback
    {
        // Using a separate onCallBack so that events can be sent back to adapter
        public void onCallBack(boolean eventTimeOverlaps);
    }

    public void checkEventTimesAction(Venue parentVenue, String activity, String date, String startTime, String endTime, checkEventTimesCallback callback)
    {
        // get events
        ref.child("Events").get().addOnCompleteListener(eventsFetch -> {
            if (!eventsFetch.isSuccessful()) return; // if fetch failed, return

            boolean eventTimeOverlaps = false;

            String startLoopedEventStr;
            String endLoopedEventStr;
            int startLoopedEvent;
            int endLoopedEvent;

            // Converting startTime and endTime to ints (using military time)
            // EX. 12:00 and 13:00 turn to 1200 and 1300
            int startPassedEvent = Integer.parseInt(startTime.substring(0,2))*100 + Integer.parseInt(startTime.substring(3));
            int endPassedEvent = Integer.parseInt(endTime.substring(0,2))*100 + Integer.parseInt(endTime.substring(3));

            for (DataSnapshot dSnap : eventsFetch.getResult().getChildren())
            {
                if(dSnap.child("parentVenue").child("venueName").getValue().toString().equals(parentVenue.getVenueName()) &&
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

            callback.onCallBack(eventTimeOverlaps);
        });
    }

    public void createUser(String username, String password)
    {
        ref.child("users").child(username).child("username").setValue(username);
        ref.child("users").child(username).child("password").setValue(password);
        ref.child("users").child(username).child("joinedEvents");
        ref.child("users").child(username).child("scheduledEvents");
        ref.child("users").child(username).child("adminFlag").setValue(false);

        setUser(username, password, false);
    }

    public void createVenue(String venueName, String[] activities){
        ref.child("Venues").child(venueName).child("venueName").setValue(venueName);
        for(int i = 0; i < activities.length; i++){
            ref.child("Venues").child(venueName).child("sports").child(String.valueOf(i)).setValue(activities[i]);
        }
        venues.add(new Venue(venueName, new ArrayList<String>(Arrays.asList(activities))));
    }

    public void joinEvent(int eventCode,
                          callBack alreadyJoinedCallBack,
                          callBack limitReachedCallBack){
        Customer c = (Customer) user;

        if(events.get(eventCode).getCurParticipants() < events.get(eventCode).getMaxParticipants()){
            if(c.getJoinedEvents().contains(eventCode)){
                alreadyJoinedCallBack.onCallBack();
                return;
            }
            events.get(eventCode).addParticipant();
            ref.child("users")
                    .child(user.getUsername())
                    .child("joinedEvents")
                    .child(String.valueOf(c.getJoinedEvents().size()))
                    .setValue(eventCode);
            ref.child("Events")
                    .child(String.valueOf(eventCode))
                    .child("curParticipants")
                    .setValue(events.get(eventCode).getCurParticipants());
            c.joinEvent(eventCode);
        } else limitReachedCallBack.onCallBack();
    }

/*    public void createEvent(String eventName, Venue parentVenue, String activity, String date, String startTime, String endTime, String curParticipants, String maxParticipants){

        // Creating an event object
        // When passing in an object to Firebase, it will automatically add
        // All fields as children. It is just easier this way.
        Event e = new Event(eventName, parentVenue, activity, date, startTime, endTime, Integer.parseInt(curParticipants), Integer.parseInt(maxParticipants));

        // adding event to firebase
        ref.child("Events").child(String.valueOf(events.size())).setValue(e);
        ref.child("Venues").child(parentVenue.getVenueName()).child("Events").child(String.valueOf(parentVenue.getEvents().size())).setValue(events.size());

        // adding event in code
        parentVenue.addEvent(e);
        events.add(e);

        // Adding eventCode to current user
        if(user instanceof Customer)
        {
            // Need to use map according to StackExchange to append to database (not override)
            // This will create a key:value pair, and appends it to the tree
            // TODO: May fix tomorrow
            HashMap<String, Object> map = new HashMap<>();
            map.put(String.valueOf(events.size()), eventName);
            ref.child("users").child(user.getUsername()).child("scheduledEvents").updateChildren(map);
            //ref.child("users").child(user.getUsername()).child("scheduledEvents").setValue(((Customer)user).getScheduledEvents());
        }
    }*/

    public void createEvent(String eventName, Venue parentVenue, String activity, String date, String startTime, String endTime, String curParticipants, String maxParticipants){

        // Adding eventCode to current user
        if(user instanceof Customer)            // assumed true
        {
            Customer c = (Customer) user;
            // Creating an event object
            // When passing in an object to Firebase, it will automatically add
            // All fields as children. It is just easier this way.
            Event e = new Event(eventName, parentVenue, activity, date, startTime, endTime, Integer.parseInt(curParticipants), Integer.parseInt(maxParticipants));

            // adding event to firebase
            ref.child("Events").child(String.valueOf(events.size())).setValue(e);
            ref.child("Venues").child(parentVenue.getVenueName()).child("Events").child(String.valueOf(parentVenue.getEvents().size())).setValue(events.size());

            // adding event in code
            //addEvent calls db.getEvent().get(eventCode) which is basically events.get(events.size)
            parentVenue.addEvent(events.size()-1);
            events.add(e);
            /*
             Need to use map according to StackExchange to append to database (not override)
             This will create a key:value pair, and appends it to the tree
             TODO: May fix tomorrow
            HashMap<String, Object> map = new HashMap<>();
            map.put(String.valueOf(events.size()), eventName);
            ref.child("users").child(user.getUsername()).child("scheduledEvents").updateChildren(map);
            ref.child("users").child(user.getUsername()).child("scheduledEvents").setValue(((Customer)user).getScheduledEvents());
            */

            // set scheduled events in database
            ref.child("users").
                    child(user.getUsername()).
                    child("scheduledEvents").
                    child(String.valueOf(c.getScheduledEvents().size())).
                    setValue(events.size()-1);

            // set scheduled events in code
            c.getScheduledEvents().add(events.size() - 1);

        }
/*
        // Adding eventCode to respective venue
        parentVenue.addEventCodeToVenue(events.size());
        ref.child("Venues").child(parentVenue.getVenueName()).child("Events").setValue(parentVenue.getCodes());
 */
    }
}

