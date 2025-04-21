package com.example.macrosocietyapp.models;

public class Friend {
    private int id;
    private String username;
    private String imageUrl; // если ты используешь аватарки(пока нет)

    public Friend(int id, String username, String imageUrl) {
        this.id = id;
        this.username = username;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

