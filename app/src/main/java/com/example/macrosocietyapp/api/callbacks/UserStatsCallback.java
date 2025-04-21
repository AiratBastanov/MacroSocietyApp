package com.example.macrosocietyapp.api.callbacks;

import com.example.macrosocietyapp.models.UserStats;

public interface UserStatsCallback {
    void onSuccess(UserStats stats);
    void onError(String error);
}
