package com.example.macrosocietyapp.api;

import com.example.macrosocietyapp.models.Comment;
import com.example.macrosocietyapp.models.CreateCommentRequest;
import com.example.macrosocietyapp.models.CreateCommentResponse;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ICommentsAPI {
    @GET("api/comments/post/{encryptedPostId}")
    Call<List<Comment>> getPostComments(@Path("encryptedPostId") String encryptedId);

    @POST("api/comments/add")
    Call<CreateCommentResponse> createComment(@Body CreateCommentRequest request);
}
