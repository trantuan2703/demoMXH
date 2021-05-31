package com.example.demomxh.Model;

import com.google.gson.annotations.SerializedName;

public class SendformAvatarChange {
    @SerializedName("avatarUrl")
    private String avatarUrl;

    public SendformAvatarChange(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
