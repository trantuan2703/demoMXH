package com.example.demomxh.Model;

import com.google.gson.annotations.SerializedName;

public class SendformPost {
    @SerializedName("userId")
    private String userId;
    @SerializedName("content")
    private String content;
    @SerializedName("images")
    private String[] images;

    public SendformPost(String userId, String content, String[] images) {
        this.userId = userId;
        this.content = content;
        this.images = images;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }
}
