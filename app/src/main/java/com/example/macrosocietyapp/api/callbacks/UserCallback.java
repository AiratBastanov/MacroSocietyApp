package com.example.macrosocietyapp.api.callbacks;

import com.example.macrosocietyapp.models.User;

public interface UserCallback {
    void onSuccess(User user);
    void onError(String error);
}

