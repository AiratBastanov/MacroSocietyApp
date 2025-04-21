package com.example.macrosocietyapp.models;

public class UserStats {
    private int friendsCount;
    private int postsCount;
    private int communitiesCount;

    // Геттеры и сеттеры
    public int getFriendsCount() { return friendsCount; }
    public int getPostsCount() { return postsCount; }
    public int getCommunitiesCount() { return communitiesCount; }

    public void setFriendsCount(int friendsCount) { this.friendsCount = friendsCount; }
    public void setPostsCount(int postsCount) { this.postsCount = postsCount; }
    public void setCommunitiesCount(int communitiesCount) { this.communitiesCount = communitiesCount; }
}
