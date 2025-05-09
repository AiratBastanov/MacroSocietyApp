package com.example.macrosocietyapp.api.callbacks;

import com.example.macrosocietyapp.models.Post;

import java.util.List;

public interface PostsCallback {
    void onSuccess(List<Post> posts);
    void onError(String error);
}

