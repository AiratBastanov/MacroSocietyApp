package com.example.macrosocietyapp.api;
import com.example.macrosocietyapp.models.User;
import com.example.macrosocietyapp.models.UserStats;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IUserAPI {
    @GET("api/users")
    Call<User> getUserByName(@Query("name") String name);

    @POST("api/users/register")
    Call<User> registerUser(@Body User user, @Query("code") String code); // теперь не шифруем на клиенте

    @POST("api/users/checkemail")
    Call<Void> sendVerificationCode(@Query("email") String encryptedEmail,@Query("state") String state); // шифруем email

    @POST("api/users/login")
    Call<User> loginWithCode(@Query("email") String encryptedEmail, @Query("code") String code);

    @GET("api/users/byid/{userIdEncrypted}")
    Call<User> getUserById(@Path("userIdEncrypted") String userIdEncrypted);

    @GET("api/users/stats/{userIdEncrypted}")
    Call<UserStats> getUserStats(@Path("userIdEncrypted") String userIdEncrypted);

    @GET("api/users/allusers")
    Call<List<User>> getAllUsers(@Query("myIdEncrypted") String myIdEncrypted);//кроме друзей и себя
}
