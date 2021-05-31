package com.example.demomxh.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GroupChatResponse {
    @SerializedName("name")
    private String groupName;
    @SerializedName("users")
    private ArrayList<String> users;
    @SerializedName("_id")
    private String groupId;
    @SerializedName("messages")
    private ArrayList<Message> messages;

    public GroupChatResponse(String groupName, ArrayList<String> users, String groupId, ArrayList<Message> messages) {
        this.groupName = groupName;
        this.users = users;
        this.groupId = groupId;
        this.messages = messages;
    }

    public GroupChatResponse() {
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
}
