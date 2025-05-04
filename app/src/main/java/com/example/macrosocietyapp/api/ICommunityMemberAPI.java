package com.example.macrosocietyapp.api;

import com.example.macrosocietyapp.models.LeaveCommunityRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ICommunityMemberAPI {
    @POST("api/communitymember/join")
    Call<Void> subscribeToCommunity(@Query("encryptedUserId") String encryptedUserId, @Query("encryptedCommunityId") String encryptedCommunityId);
    @DELETE("api/communitymember/leave")
    Call<Void> unsubscribeFromCommunity(@Query("encryptedUserId") String encryptedUserId,
                                        @Query("encryptedCommunityId") String encryptedCommunityId);
}
