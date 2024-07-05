package com.example.client.Model;

public class UserInfo {
    private String id;
    private String role;
    // Add other fields if necessary

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setName(String role) {
        this.role= role;
    }
    public String getRole(){
        return role;
    }
}