package com.example.chatsdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class User {

    @JsonProperty("id")
    private String id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("registeredAt")
    private String registeredAt;

    @JsonProperty("chats")
    private List<String> chats;

    public User() {
        // Default constructor required for Jackson
    }

    public User(String id, String username, String registeredAt, List<String> chats) {
        this.id = id;
        this.username = username;
        this.registeredAt = registeredAt;
        this.chats = chats;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(String registeredAt) {
        this.registeredAt = registeredAt;
    }

    public List<String> getChats() {
        return chats;
    }

    public void setChats(List<String> chats) {
        this.chats = chats;
    }

    @Override
    public String toString() {
        return "🧍 User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", registeredAt='" + registeredAt + '\'' +
                ", chats=" + chats +
                '}';
    }
}
