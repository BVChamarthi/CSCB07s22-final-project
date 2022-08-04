package com.example.cscb07s22finalproject;

import java.util.ArrayList;

public class Venue {

    String venueName;
    ArrayList<String> allowedActivities;
    ArrayList<Event> events;
    ArrayList<Integer> codes;

    public Venue(String venueName, ArrayList<String> allowedActivities, ArrayList<Integer> codes) {
        this.venueName = venueName;
        this.allowedActivities = allowedActivities;
        this.codes = codes;
    }

    public void addEvent(Event event) {
        //Not sure if .contains() properly compares two objs, might have to change 2nd predicate
        if(allowedActivities.contains(event.getActivity()) && !(events.contains(event))){
            event.setVenueName(venueName);
            events.add(event);
        }
    }

    public void removeEvent(Event event){
        events.remove(event);
        //Even if event is not in events list still say its removed anyways cause technically it has been removed from list
    }

    public void addAllowedActivities(String activity){
        allowedActivities.add(activity);
    }

    public void removeAllowedActivities(String activity){
        allowedActivities.remove(activity);
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }
}
