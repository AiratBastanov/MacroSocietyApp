package com.example.macrosocietyapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.macrosocietyapp.models.User;
import com.example.macrosocietyapp.saveUserRoom.UserRepository;

import java.util.concurrent.Executors;

public class SharedViewModel extends AndroidViewModel {

    private final UserRepository userRepository;
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();

    public SharedViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        loadCurrentUser(); // Загружаем при старте
    }

    public LiveData<User> getUser() {
        return userLiveData;
    }

    public void setUser(User user) {
        userLiveData.setValue(user);
    }

    public void loadCurrentUser() {
        Executors.newSingleThreadExecutor().execute(() -> {
            User currentUser = userRepository.getCurrentUser();
            if (currentUser != null) {
                userLiveData.postValue(currentUser);
            }
        });
    }

    public void saveUserToDb(User user) {
        user.setCurrent(true);
        Executors.newSingleThreadExecutor().execute(() -> {
            userRepository.setCurrentUser(user);
            userLiveData.postValue(user);
        });
    }

    public void clearUser() {
        Executors.newSingleThreadExecutor().execute(() -> {
            userRepository.clearCurrentUserFlag(); // сброс текущего
            userLiveData.postValue(null);
        });
    }
}


