package com.example.macrosocietyapp.api;

import com.example.macrosocietyapp.models.Post;
import com.example.macrosocietyapp.models.PostDto;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IPostsAPI {
    @GET("api/posts/community/{encryptedCommunityId}")
    Call<List<Post>> getCommunityPosts(@Path("encryptedCommunityId") String encryptedId);

    @POST("api/posts/add")
    Call<JsonObject> addPost(@Body PostDto postDto);
}

