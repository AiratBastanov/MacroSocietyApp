package com.example.macrosocietyapp.models;

import com.google.gson.annotations.SerializedName;

public class SimpleUser {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    // Геттеры
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
}

