package com.example.demomxh.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SendformCreateGroup {
    @SerializedName("users")
    ArrayList<String> users;

    public SendformCreateGroup(ArrayList<String> users) {
        this.users = users;
    }
}
