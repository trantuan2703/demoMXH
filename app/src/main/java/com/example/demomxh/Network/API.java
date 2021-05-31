package com.example.demomxh.Network;

public class API {
    public static String ROOT = "https://hubbook.herokuapp.com/api/";

    public static final String LOGIN = "user/login";
    public static final String REGISTER = "user/register";

    public static final String GETALLPOSTS = "post/get-all";
    public static final String GET_ALL_COMMENTS_FROM_POST="post/get-comment";
    public static final String COMMENT_POST="post/comment";

    public static final String GET_PROFILE_DETAIL="user/get-detail";
    public static final String UPDATE_PROFILE="user/update-profile";
    public static final String GET_ALL_FRIENDS="user/get-all";

    public static final String GET_MY_GROUPS_CHAT = "chat/all-group";
    public static final String LIKE="post/like";
    public static final String POST_STATUS="post";
    public static final String DELETE_EDIT_POST="post";

    public static final String CHANGE_AVATAR="user/update-avatar";
    public static final String CHANGE_WALLPAPER="user/update-coverphoto";
    public static final String GET_ALL_MESSAGE = "chat/all-message";

    public static final String CREATE_GROUP_CHAT="chat/create-group";
}
