package com.example.macrosocietyapp.api.callbacks;

import com.example.macrosocietyapp.models.Comment;

import java.util.List;

public interface CommentsCallback {
    void onSuccess(List<Comment> comments);
    void onError(String error);
}
