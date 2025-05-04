package com.example.macrosocietyapp.models;

public class LeaveCommunityRequest {
    private int userId;
    private int communityId;

    public LeaveCommunityRequest(int userId, int communityId) {
        this.userId = userId;
        this.communityId = communityId;
    }

    public int getUserId() {
        return userId;
    }

    public int getCommunityId() {
        return communityId;
    }
}

