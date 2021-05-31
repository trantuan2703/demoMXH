package com.example.demomxh.Network;
import com.example.demomxh.Model.Comment;
import com.example.demomxh.Model.Friend;
import com.example.demomxh.Model.GroupChat;
import com.example.demomxh.Model.GroupChatResponse;
import com.example.demomxh.Model.Message;
import com.example.demomxh.Model.Post;
import com.example.demomxh.Model.Profile;
import com.example.demomxh.Model.SendFormComment;
import com.example.demomxh.Model.SendformAvatarChange;
import com.example.demomxh.Model.SendformCreateGroup;
import com.example.demomxh.Model.SendformEdit;
import com.example.demomxh.Model.SendformLike;
import com.example.demomxh.Model.SendformLogin;
import com.example.demomxh.Model.SendformPost;
import com.example.demomxh.Model.SendformRegister;
import com.example.demomxh.Model.SendformWallpaperChange;
import com.example.demomxh.Model.UserInfo;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface RetrofitService {
    @POST(API.LOGIN)
    Call<UserInfo> login(@Body SendformLogin sendformLogin);

    @POST(API.REGISTER)
    Call<UserInfo> register(@Body SendformRegister sendformRegister);

    @GET(API.GETALLPOSTS)
    Call<ArrayList<Post>> getAllPosts(@Query("userId") String userId);

    @GET(API.GET_ALL_COMMENTS_FROM_POST)
    Call<ArrayList<Comment>> getAllComment(@Query("postId") String postId);

    @POST(API.COMMENT_POST)
    Call<Comment> postComment(@Body SendFormComment sendFormComment);

    @GET(API.GET_PROFILE_DETAIL)
    Call<Profile> getProfileDetail(@Query("username") String username, @Header("userId") String userId);

    @PUT(API.UPDATE_PROFILE)
    Call<Void> editProfile(@Query("userId") String userId, @Body SendformEdit sendformEdit);

    @GET(API.GET_ALL_FRIENDS)
    Call<ArrayList<Friend>> getAllFriends(@Query("userId") String userId);

    @GET(API.GET_MY_GROUPS_CHAT)
    Call<ArrayList<GroupChat>> getMyGroups(@Query("userId") String userId);

    @POST(API.LIKE)
    Call<Void> likeStatus(@Body SendformLike sendformLike);

    @POST(API.POST_STATUS)
    Call<Post> postaStatus(@Body SendformPost sendformPost);

    @HTTP(method = "DELETE")
    Call<Void> deletePost(@Header("userId") String userId, @Url String urlWithPostId);

    @PUT(API.CHANGE_AVATAR)
    Call<Void> changeAvatar(@Query("userId") String userId, @Body SendformAvatarChange sendformAvatarChange);

    @PUT(API.CHANGE_WALLPAPER)
    Call<Void> changeWallpaper(@Query("userId") String userId, @Body SendformWallpaperChange sendformWallpaperChange);

    @GET(API.GET_ALL_MESSAGE)
    Call<ArrayList<Message>> getAllMessages(@Query("groupId") String groupId);

    @POST(API.CREATE_GROUP_CHAT)
    Call<GroupChatResponse> createGroupChat(@Body SendformCreateGroup sendformCreateGroup);
}
