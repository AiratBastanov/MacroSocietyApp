package com.example.macrosocietyapp.models;

public class CreateCommentResponse {
    private boolean success;
    private Integer commentId;
    private String createdAt;

    public boolean isSuccess() { return success; }
    public Integer getCommentId() { return commentId; }
    public String getCreatedAt() { return createdAt; }
}

