package com.example.macrosocietyapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "users")
public class User {
    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("createdAt")
    @Expose
    private String createdAt;

    @ColumnInfo(defaultValue = "0") // стандартное значение, чтобы самому не указывать false (0), когда вставляю нового пользователя
    @Expose(serialize = false, deserialize = false)
    private boolean isCurrent;

    // Для заявок в друзья
    @Expose(serialize = false, deserialize = false)
    private boolean isFriendRequest;

    // Для исходящих заявок
    @Expose(serialize = false, deserialize = false)
    private boolean isOutgoingRequest;

    public User() {}

    public User(Integer id, String name, String email, boolean isCurrent) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.isCurrent = isCurrent;
    }

    public User(String email) {
        this.email = email;
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // Геттеры и сеттеры для всех полей
    public Integer getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getCreatedAt() { return createdAt; }
    public boolean isCurrent() { return isCurrent; }
    public boolean isFriendRequest() { return isFriendRequest; }
    public boolean isOutgoingRequest() { return isOutgoingRequest; }

    public void setId(Integer id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setCurrent(boolean current) { isCurrent = current; }
    public void setFriendRequest(boolean friendRequest) { isFriendRequest = friendRequest; }
    public void setOutgoingRequest(boolean outgoingRequest) { isOutgoingRequest = outgoingRequest; }
}



