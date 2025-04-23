package com.example.chatsdk.models;


public class RegisterUserRequest {
    private String username;

    public RegisterUserRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

