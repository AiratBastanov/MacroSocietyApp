package com.example.macrosocietyapp.api;
import com.example.macrosocietyapp.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IAPI {
    @GET("api/users")
    Call<User> GetUser(@Query("name") String name);

    @POST("api/users/register")
    Call<String> registerUser(@Body String encryptedUserData);

    @POST("api/users/checkemail")
    Call<Void> sendVerificationCode(@Query("email") String email);

    @POST("api/users/login")
    Call<Void> loginWithCode(@Query("email") String email, @Query("code") String code);
}

