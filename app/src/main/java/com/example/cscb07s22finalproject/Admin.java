package com.example.cscb07s22finalproject;

import java.util.ArrayList;

public class Admin extends User
{
    private ArrayList<Integer> addedVenues;

    public Admin(String username, String password)
    {
        super(username, password);
        addedVenues = new ArrayList<Integer>();
    }

    // Suggestion: If the admin wants to add a venue, take in the following parameters:

    //             String venue, ArrayList<String> activities, int capacity

    //             Then, we generate an venueCode using how many venues are currently in the database

    //             Then, we upload the venue to the database, and under that "tree", we
    //             put all the parameters (venue, activities, etc.)
    public void addVenue()
    {

    }

    // Suggestion: If the admin wants to edit a venue, take in the following parameters:
    //             int venueCode, ArrayList<String> activities, int capacity
    //             Using the venueCode, search through the venues in the database,
    //             and replace the fields
    public void editVenue()
    {

    }
}
