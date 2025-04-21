package com.example.macrosocietyapp.api;

import com.example.macrosocietyapp.models.FriendRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IFriendRequestsAPI {
    @POST("api/friendrequests/send")
    Call<Void> sendFriendRequest(@Query("senderIdEncrypted") String senderIdEncrypted,
                                 @Query("receiverIdEncrypted") String receiverIdEncrypted);

    @GET("api/friendrequests/incoming/{userIdEncrypted}")
    Call<List<FriendRequest>> getIncomingRequests(@Path("userIdEncrypted") String userIdEncrypted);

    @GET("api/friendrequests/details/incoming/{userIdEncrypted}")
    Call<List<FriendRequest>> getIncomingRequestsWithDetails(@Path("userIdEncrypted") String userIdEncrypted);

    @GET("api/friendrequests/outgoing/{userIdEncrypted}")
    Call<List<FriendRequest>> getOutgoingRequests(@Path("userIdEncrypted") String userIdEncrypted);

    @POST("api/friendrequests/accept")
    Call<Void> acceptRequest(@Query("senderIdEncrypted") String senderIdEncrypted,
                             @Query("receiverIdEncrypted") String receiverIdEncrypted);

    @POST("api/friendrequests/reject")
    Call<Void> rejectRequest(@Query("senderIdEncrypted") String senderIdEncrypted,
                             @Query("receiverIdEncrypted") String receiverIdEncrypted);
}
