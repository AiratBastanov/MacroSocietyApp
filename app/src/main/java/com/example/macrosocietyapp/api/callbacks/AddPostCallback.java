package com.example.macrosocietyapp.api.callbacks;

public interface AddPostCallback {
    void onSuccess(String encryptedPostId, String createdAt);
    void onError(String error);
}


