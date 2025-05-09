package com.example.macrosocietyapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Post {

    @SerializedName("id")
    private String id;  // Зашифрованная строка

    @SerializedName("userId")
    private String userId;  // Зашифрованная строка

    @SerializedName("communityId")
    private String communityId;  // Зашифрованная строка

    @SerializedName("content")
    private String content;

    @SerializedName("createdAt")
    private Date createdAt;

    @SerializedName("username")
    private String username;

    // ======== Геттеры и сеттеры =========

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

