package com.example.client.Model.Responses;

import com.example.client.Model.EventData;
import com.example.client.Model.Vendor;

import java.util.ArrayList;
import java.util.List;

public class EventResponse {
    private ArrayList<EventData> events;
    private ArrayList<Vendor> vendors;

    public ArrayList<Vendor> getVendors() {
        return vendors;
    }

    public void setVendors(ArrayList<Vendor> vendors) {
        this.vendors = vendors;
    }

    // Getters and Setters
    public ArrayList<EventData> getEvents() { return events; }
    public void setEvents(ArrayList<EventData> events) { this.events = events; }
}