package com.example.macrosocietyapp.models;

public class PostDto {
    private String postId;
    private String userId;       // зашифрованный userId
    private String communityId;  // зашифрованный communityId
    private String content;
    private String username;

    // Конструктор по умолчанию
    public PostDto() {}

    // Конструктор со всеми полями (если нужно)
    public PostDto(String userId, String communityId, String content) {
        this.userId = userId;
        this.communityId = communityId;
        this.content = content;
    }

    // Геттеры и сеттеры
    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

