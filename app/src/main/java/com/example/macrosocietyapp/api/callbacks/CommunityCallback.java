package com.example.macrosocietyapp.api.callbacks;

import com.example.macrosocietyapp.models.Community;

public interface CommunityCallback {
    void onSuccess(Community community);
    void onError(String error);
}

