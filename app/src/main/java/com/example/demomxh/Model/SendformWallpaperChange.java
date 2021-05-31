package com.example.demomxh.Model;

import com.google.gson.annotations.SerializedName;

public class SendformWallpaperChange {
    @SerializedName("coverPhoto")
    private String[] coverPhoto;

    public SendformWallpaperChange() {
    }

    public String[] getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String[] coverPhoto) {
        this.coverPhoto = coverPhoto;
    }
}
