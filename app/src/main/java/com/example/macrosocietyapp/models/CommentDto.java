package com.example.macrosocietyapp.models;

public class CommentDto {
    private int userId;
    private int postId;
    private String content;

    public CommentDto(int userId, int postId, String content) {
        this.userId = userId;
        this.postId = postId;
        this.content = content;
    }

    // Getters
    public int getUserId() { return userId; }
    public int getPostId() { return postId; }
    public String getContent() { return content; }
}

