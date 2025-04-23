package com.example.chatsdk.models;


import java.util.List;

public class Message {
    private String id;
    private String chatId;
    private String senderId;
    private String receiverId;
    private String content;
    private String timestamp;

    public Message() {}

    public Message(String chatId, String senderId, String receiverId, String content, String timestamp) {
        this.chatId = chatId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public Message setId(String id) {
        this.id = id;
        return this;
    }

    public String getSenderId() {
        return senderId;
    }

    public Message setSenderId(String senderId) {
        this.senderId = senderId;
        return this;
    }

    public String getChatId() {
        return chatId;
    }

    public Message setChatId(String chatId) {
        this.chatId = chatId;
        return this;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public Message setReceiverId(String receiverId) {
        this.receiverId = receiverId;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Message setContent(String content) {
        this.content = content;
        return this;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Message setTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}
