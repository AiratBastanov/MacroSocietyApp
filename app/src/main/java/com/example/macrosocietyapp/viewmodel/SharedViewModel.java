package com.example.macrosocietyapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.macrosocietyapp.models.User;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();

    public void setUser(User user) {
        userLiveData.setValue(user);
    }

    public LiveData<User> getUser() {
        return userLiveData;
    }
}

