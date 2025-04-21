package com.example.macrosocietyapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.macrosocietyapp.models.User;
import com.google.gson.Gson;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "macro_society_pref";
    private static final String KEY_USER = "key_user";

    private static SharedPrefManager instance;
    private final SharedPreferences sharedPreferences;
    private final Gson gson = new Gson();

    private SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context);
        }
        return instance;
    }

    public void saveUser(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER, gson.toJson(user));
        editor.apply();
    }

    public User getUser() {
        String userJson = sharedPreferences.getString(KEY_USER, null);
        if (userJson != null) {
            return gson.fromJson(userJson, User.class);
        }
        return null;
    }

    public int getUserId() {
        User user = getUser();
        return user != null ? user.getId() : -1;
    }

    public boolean isLoggedIn() {
        return getUser() != null;
    }

    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}

