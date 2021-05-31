package com.example.demomxh.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Profile {
    @SerializedName("avatarUrl")
    private String avatarUrl;
    @SerializedName("coverPhoto")
    private ArrayList<String> coverPhoto;
    @SerializedName("fullName")
    private String fullName;
    @SerializedName("username")
    private String username;
    @SerializedName("phone")
    private String phone;
    @SerializedName("birthday")
    private String birthday;
    @SerializedName("address")
    private String address;
    @SerializedName("postList")
    private ArrayList<Post> postList;

    public Profile() {
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public ArrayList<String> getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(ArrayList<String> coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<Post> getPostList() {
        return postList;
    }

    public void setPostList(ArrayList<Post> postList) {
        this.postList = postList;
    }
}
