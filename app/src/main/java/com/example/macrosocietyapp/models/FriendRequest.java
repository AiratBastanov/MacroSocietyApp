package com.example.macrosocietyapp.models;

import com.google.gson.annotations.SerializedName;

public class FriendRequest {
    @SerializedName("id")
    private int id;

    @SerializedName("senderId")
    private String senderId;

    @SerializedName("receiverId")
    private String receiverId;

    @SerializedName("sentAt")
    private String sentAt;

    @SerializedName("status")
    private String status;

    @SerializedName("sender")
    private SimpleUser sender;

    @SerializedName("receiver")
    private SimpleUser receiver;

    // Геттеры
    public int getId() { return id; }
    public String getSenderId() { return senderId; }
    public String getReceiverId() { return receiverId; }
    public String getSentAt() { return sentAt; }
    public String getStatus() { return status; }
    public SimpleUser getSender() { return sender; }
    public SimpleUser getReceiver() { return receiver; }
}

