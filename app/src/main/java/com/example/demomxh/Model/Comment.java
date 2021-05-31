package com.example.demomxh.Model;

import com.google.gson.annotations.SerializedName;

public class Comment {
    @SerializedName("userAvatar")
    private String userAvatar;
    @SerializedName("fullName")
    private String fullName;
    @SerializedName("content")
    private String content;
    @SerializedName("username")
    private String username;

    public Comment() {
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
