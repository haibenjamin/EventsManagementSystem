package com.example.client.Model.Responses;

import com.example.client.Model.Data;
import com.example.client.Model.EventData;
import com.example.client.Model.Guest;

import java.util.ArrayList;

public class GuestResponse {


    private ArrayList<Guest> guestList;

    // Getters and Setters
    public ArrayList<Guest> getGuests() { return guestList; }
    public void setGuests(ArrayList<Guest> guests) { this.guestList = guests; }

}
