package com.example.socketio;

public class Message {
    private String sender; // "Manager" hoặc "Customer"
    private String content;

    public Message(String sender, String content) {
        this.sender = sender;
        this.content = content;
    }

    public String getSender() { return sender; }
    public String getContent() { return content; }
}