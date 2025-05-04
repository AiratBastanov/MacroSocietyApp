package com.example.macrosocietyapp.api.callbacks;

import com.example.macrosocietyapp.models.Community;

import java.util.List;

public interface CommunityListCallback {
    void onSuccess(List<Community> communities);
    void onError(String error);
}
