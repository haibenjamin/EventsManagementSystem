package com.example.client.Model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Upcoming {
    private String _id;
    private String name;
    private String type;
    private String location;
    private long date;
    private String formattedDate;
    private Owner owner;

    public class Owner {
        public Owner(String email) {
            this.email = email;
        }

        private String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public Upcoming(String name, String type, String location, long date, String mail) {
        this.name = name;
        this.type = type;
        this.location = location;
        this.date = date;
        this.owner = new Owner(mail);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getDate() {
        Date date = new Date(this.date * 1000); // Convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        this.formattedDate=sdf.format(date);
        return formattedDate;

    }

    public void setDate(long date) {
        this.date = date;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }
}