package com.example.cscb07s22finalproject;


import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class Filter {
    private DataBase db = DataBase.getInstance();
    private Venue compareVenue;
    private boolean scheduledEvents;
    private boolean joinedEvents;
    private boolean upcomingEvents;

    public Filter(boolean scheduledEvents, boolean joinedEvents, boolean upcomingEvents) {
        this.compareVenue = db.getDefaultEntry();
        this.scheduledEvents = scheduledEvents;
        this.joinedEvents = joinedEvents;
        this.upcomingEvents = upcomingEvents;
    }

    public void setFilter(Venue v,
                          boolean scheduledEvents,
                          boolean joinedEvents,
                          boolean upcomingEvents) {
        compareVenue = v;
        this.scheduledEvents = scheduledEvents;
        this.joinedEvents = joinedEvents;
        this.upcomingEvents = upcomingEvents;
    }

    public void setCompareVenue(Venue v) { compareVenue = v; }
    public void setScheduledEvents(boolean b) { scheduledEvents = b; }
    public void setJoinedEvents(boolean b) { joinedEvents = b; }
    public void setUpcomingEvents(boolean b) { upcomingEvents = b; }

    int[] extractDate(String date) {
        String[] yyyymmddIntermadiate = date.split("-");
        int[] yyyymmdd = {0, 0, 0};
        for(int i =0; i < 3; i++) {
            yyyymmdd[i] = Integer.parseInt(yyyymmddIntermadiate[i]);
        }
        return yyyymmdd;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean filterPass(Event e) {
        if(compareVenue != db.getDefaultEntry() && e.getParentVenue() != compareVenue) return false;

        // TODO: check if event is scheduled or joined by customer (db.getUser())

        if(upcomingEvents) {
            int[] eventDate = extractDate(e.getDate());
            int[] currentDate = extractDate(String.valueOf(java.time.LocalDate.now()));
            if(eventDate[0] < currentDate[0]) return false;
            if(eventDate[0] == currentDate[0] &&
                    eventDate[1] < currentDate[1]) return false;
            if(eventDate[0] == currentDate[0] &&
                    eventDate[1] == currentDate[1] &&
                    eventDate[2] < currentDate[2]) return false;
            // weeded out all past events
            if(currentDate[1] < 12) {       // not currently December
                if(eventDate[0] > currentDate[0] ||
                eventDate[1] > currentDate[1] + 1) return false;    // if event is beyond next month, exclude it
            } else {
                if( !((eventDate[0] == currentDate[0] && eventDate[1] == 12) ||             // if it's not that it's this year's Dec
                    (eventDate[0] == currentDate[0]+1 && eventDate[1] == 1))) return false; // or next year's Jan, return false
            }
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<Event> filterPass(ArrayList<Event> events) {
        ArrayList<Event> filteredEvents = new ArrayList<>();
        for(Event e : events) {
            if(filterPass(e)) filteredEvents.add(e);
        }
        return filteredEvents;
    }
}
