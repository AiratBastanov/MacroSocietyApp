package com.example.macrosocietyapp.saveUserRoom;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.macrosocietyapp.models.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Query("SELECT * FROM users WHERE isCurrent = 1 LIMIT 1")
    User getCurrentUser();

    @Query("UPDATE users SET isCurrent = 0 WHERE isCurrent = 1")
    void clearCurrentFlag();

    @Query("UPDATE users SET isCurrent = 1 WHERE id = :userId")
    void setCurrentUser(int userId);

    @Transaction
    default void replaceCurrentUser(User user) {
        clearCurrentFlag();
        insertUser(user);
        setCurrentUser(user.getId());
    }

    @Query("SELECT * FROM users")
    List<User> getAllUsers();

    @Query("DELETE FROM users")
    void deleteAllUsers();
}



