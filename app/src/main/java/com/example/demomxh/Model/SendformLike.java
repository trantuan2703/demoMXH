package com.example.demomxh.Model;

import com.google.gson.annotations.SerializedName;

public class SendformLike {
    @SerializedName("userId")
    private String userId;
    @SerializedName("postId")
    private String postId;

    public SendformLike(String userId, String postId) {
        this.userId = userId;
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
