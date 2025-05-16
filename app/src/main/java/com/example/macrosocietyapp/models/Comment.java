package com.example.macrosocietyapp.models;

import com.example.macrosocietyapp.utils.AesEncryptionService;
import com.example.macrosocietyapp.utils.DateUtils;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class Comment {
    // Encrypted fields from server
    @SerializedName("id")
    private String encryptedId;
    @SerializedName("userId")
    private String encryptedUserId;
    @SerializedName("postId")
    private String encryptedPostId;
    @SerializedName("createdAt")
    private String encryptedCreatedAt;

    // Decrypted fields
    private transient int id;
    private transient int userId;
    private transient int postId;
    private transient Date createdAt;

    private String username;
    private String content;

    public Comment() {}

    public Comment(String username, String content, Date createdAt) {
        this.username = username;
        this.content = content;
        this.createdAt = createdAt;
    }

    public void decryptFields() throws Exception {
        this.id = Integer.parseInt(AesEncryptionService.decrypt(encryptedId));
        this.userId = Integer.parseInt(AesEncryptionService.decrypt(encryptedUserId));
        this.postId = Integer.parseInt(AesEncryptionService.decrypt(encryptedPostId));

        this.createdAt = DateUtils.parseUtc(encryptedCreatedAt);
    }

    // Getters & Setters
    public String getEncryptedId() { return encryptedId; }
    public String getEncryptedUserId() { return encryptedUserId; }
    public String getEncryptedPostId() { return encryptedPostId; }
    public String getEncryptedCreatedAt() { return encryptedCreatedAt; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getText() {
        return content;
    }

    public String getFormattedDate() {
        if (createdAt == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
        return sdf.format(createdAt);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Comment)) return false;
        Comment other = (Comment) obj;
        return userId == other.userId &&
                postId == other.postId &&
                content.equals(other.content) &&
                createdAt != null &&
                createdAt.equals(other.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, postId, content, createdAt);
    }
}

