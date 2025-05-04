package com.example.macrosocietyapp.models;

public class CommunityMember {
    private int id;
    private int userId;
    private int communityId;
    private String role;
    private String joinedAt;

    public CommunityMember() {}

    public CommunityMember(int userId, int communityId) {
        this.userId = userId;
        this.communityId = communityId;
    }

    public CommunityMember(int id, int userId, int communityId, String role, String joinedAt) {
        this.id = id;
        this.userId = userId;
        this.communityId = communityId;
        this.role = role;
        this.joinedAt = joinedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(String joinedAt) {
        this.joinedAt = joinedAt;
    }
}


