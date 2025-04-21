package com.example.macrosocietyapp.api.callbacks;

import com.example.macrosocietyapp.models.User;

import java.util.List;

public interface UsersCallback {
    void onSuccess(List<User> users);
    void onError(String error);
}
