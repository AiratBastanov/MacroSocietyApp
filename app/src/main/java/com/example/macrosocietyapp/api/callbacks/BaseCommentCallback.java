package com.example.macrosocietyapp.api.callbacks;

import java.util.Date;

public interface BaseCommentCallback {
    void onSuccess(Integer commentId, String createdAt);
    void onError(String error);
}



