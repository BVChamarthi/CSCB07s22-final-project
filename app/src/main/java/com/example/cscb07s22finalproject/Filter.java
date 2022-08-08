package com.example.cscb07s22finalproject;


import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class Filter {
    private DataBase db = DataBase.getInstance();
    private Venue compareVenue;
    private boolean upcomingEvents;

    public Filter(boolean upcomingEvents) {
        this.compareVenue = db.getDefaultEntry();
        this.upcomingEvents = upcomingEvents;
    }

    public void setFilter(Venue v, boolean upcomingEvents) {
        compareVenue = v;
        this.upcomingEvents = upcomingEvents;
    }

    public void setCompareVenue(Venue v) { compareVenue = v; }
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
    private boolean filterPass(int eventCode) {
        Event e = db.getEvents().get(eventCode);
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
    public ArrayList<Integer> filterPass(ArrayList<Integer> events) {
        ArrayList<Integer> filteredEvents = new ArrayList<>();
        for(int code : events) {
            if(filterPass(code)) filteredEvents.add(code);
        }
        return filteredEvents;
    }
}
