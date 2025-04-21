package com.example.macrosocietyapp.api.callbacks;

import com.example.macrosocietyapp.models.FriendRequest;

import java.util.List;

public interface FriendRequestsCallback {
    void onSuccess(List<FriendRequest> requests);
    void onError(String error);
}
