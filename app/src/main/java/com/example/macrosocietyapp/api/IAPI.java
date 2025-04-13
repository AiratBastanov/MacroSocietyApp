package com.example.macrosocietyapp.api;
import com.example.macrosocietyapp.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IAPI {
    @GET("api/users")
    Call<User> getUserByName(@Query("name") String name);

    @POST("api/users/register")
    Call<User> registerUser(@Body User user, @Query("code") String code); // теперь не шифруем на клиенте

    @POST("api/users/checkemail")
    Call<Void> sendVerificationCode(@Query("email") String encryptedEmail); // шифруем email

    @POST("api/users/login")
    Call<Void> loginWithCode(@Query("email") String encryptedEmail, @Query("code") String code); // тоже
}
