package com.example.client.Model.Responses;

public class RefreshResponse {
    String accessToken;

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken(){
        return accessToken;
    }
}