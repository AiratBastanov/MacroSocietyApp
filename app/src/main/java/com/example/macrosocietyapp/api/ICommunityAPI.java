package com.example.macrosocietyapp.api;

import com.example.macrosocietyapp.models.Community;
import com.example.macrosocietyapp.models.CommunityCreateDto;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ICommunityAPI {
    @GET("api/communities/all")
    Call<List<Community>> getAllCommunities();

    @POST("api/communities/create")
    Call<ResponseBody> createCommunity(@Body CommunityCreateDto dto);

    @GET("communities/user/{userIdEncrypted}")
    Call<List<Community>> getUserCommunities(@Path("userIdEncrypted") String userIdEncrypted);

    @DELETE("communities/delete/{communityIdEncrypted}")
    Call<Void> deleteCommunity(@Path("communityIdEncrypted") String communityIdEncrypted);

    @POST("communities/transfer/{communityId}")
    Call<Void> transferOwnership(@Path("communityId") int communityId);

    @POST("api/communities/subscribe")
    Call<Void> subscribeToCommunity(@Body String encryptedData);
}
