package com.example.macrosocietyapp.api.callbacks;

import com.example.macrosocietyapp.models.User;

import java.util.List;

public interface FriendListCallback {
    void onSuccess(List<User> friends);
    void onError(String error);
}
