package com.example.macrosocietyapp.models;

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

    @Expose(serialize = false, deserialize = false)
    private boolean isCurrent; // не отправляем на сервер, только локально

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

    public Integer getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public boolean isCurrent() { return isCurrent; }

    public void setId(Integer id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setCurrent(boolean current) { isCurrent = current; }
}



