package com.example.macrosocietyapp.models;

public class CreateCommentRequest {
    private String postId;
    private Integer userId;
    private String content;

    public CreateCommentRequest(String postId, Integer userId, String content) {
        this.postId = postId;
        this.userId = userId;
        this.content = content;
    }
}


