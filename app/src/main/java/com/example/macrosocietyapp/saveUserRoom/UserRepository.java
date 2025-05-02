package com.example.macrosocietyapp.saveUserRoom;

import android.content.Context;

import com.example.macrosocietyapp.models.User;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {
    private final UserDao userDao;
    private final ExecutorService executorService;

    public UserRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        userDao = db.userDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insertUser(User user) {
        executorService.execute(() -> userDao.insertUser(user));
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public User getCurrentUser() {
        return userDao.getCurrentUser();
    }

    public void setCurrentUser(User user) {
        executorService.execute(() -> userDao.replaceCurrentUser(user));
    }

    public void clearCurrentUserFlag() {
        executorService.execute(userDao::clearCurrentFlag);
    }

    public void deleteAllUsers() {
        executorService.execute(userDao::deleteAllUsers);
    }
}

