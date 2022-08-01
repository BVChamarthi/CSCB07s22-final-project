package com.example.cscb07s22finalproject;

public class Event {

    int eventCode;
    String venueName;
    String sport;
    int maxPlayers;
    int numPlayers;
    String date; // YYYY-MM-DD format
    int startTime; // Stored as 24 hour time
    int endTime;

    public Event(int year, int month, int day, int maxPlayers, int numPlayers, int eventCode, String venueName, String sport, int startTime, int endTime){
        this.eventCode = eventCode;
        this.venueName = venueName;
        this.sport = sport;
        this.maxPlayers = maxPlayers;
        this.numPlayers = numPlayers;
        this.date = year + "-" + month + "-" + day;
        this.startTime = startTime;
        this.endTime = endTime;

    }

}
