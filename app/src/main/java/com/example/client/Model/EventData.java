package com.example.client.Model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EventData {
    private String _id;
    private String name;
    private int budget;
    private long date; // Changed data type to long for epoch time
    private String location;
    private String type;
    private ArrayList<Collaborator> collaborators;
    private String formattedDate;
    private ArrayList<Guest> guests;
    private ArrayList<Task> tasks;

    // Constructor
    public EventData(String name, int budget, long date, String location, String type, ArrayList<Collaborator> collab) {
        this.name = name;
        this.budget = budget;
        this.date = date; // Assigning epoch time directly
        this.location = location;
        this.type = type;
        this.collaborators = collab;
        tasks=new ArrayList<>();
        guests=new ArrayList<>();
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getId() {
        return _id;
    }

    public void setId(String eventId) {
        this._id = eventId;
    }
    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public String getDate() {
        return formattedDate;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Collaborator> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(ArrayList<Collaborator> collaborators) {
        this.collaborators = collaborators;
    }

    public void convertEpochToFormattedDate() {

            Date date = new Date(this.date * 1000); // Convert seconds to milliseconds
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            this.formattedDate= sdf.format(date);

    }
}
