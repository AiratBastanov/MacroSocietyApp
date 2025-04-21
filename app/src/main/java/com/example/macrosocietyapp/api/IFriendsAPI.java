package com.example.macrosocietyapp.api;

import com.example.macrosocietyapp.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IFriendsAPI {
    @GET("api/friends/{userIdEncrypted}")
    Call<List<String>> getFriendIds(@Path("userIdEncrypted") String userIdEncrypted);

    @GET("api/friends/details/{userIdEncrypted}")
    Call<List<User>> getFriendsWithDetails(@Path("userIdEncrypted") String userIdEncrypted);

    @GET("api/friends/mutual")
    Call<List<User>> getMutualFriends(@Query("user1Id") String user1Id,
                                      @Query("user2Id") String user2Id);

    @DELETE("api/friends")
    Call<Void> removeFriend(@Query("userIdEncrypted") String userIdEncrypted,
                            @Query("friendIdEncrypted") String friendIdEncrypted);
}
