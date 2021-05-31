package com.example.demomxh.View.MainScreen.TabMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ViewFlipper;

import com.example.demomxh.Adapter.FriendAdapter;
import com.example.demomxh.Common.BaseActivity;
import com.example.demomxh.Database.RealmContext;
import com.example.demomxh.Model.Friend;
import com.example.demomxh.Model.UserInfo;
import com.example.demomxh.R;
import com.example.demomxh.View.MainScreen.ProfileActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendsListActivity extends BaseActivity implements FriendAdapter.onFriendClickListener{

    @BindView(R.id.rv_friends)
    RecyclerView rvFriendsList;
    @BindView(R.id.vf_friends)
    ViewFlipper vfFriends;

    private UserInfo userInfo;
    private ArrayList<Friend> friends;
    private FriendAdapter friendAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);
        ButterKnife.bind(this);
        Init();
    }

    private void Init() {
        userInfo = RealmContext.getInstance().getUser();
        friends = new ArrayList<>();
        friendAdapter = new FriendAdapter(friends,this);
        rvFriendsList.setAdapter(friendAdapter);
        rvFriendsList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        getAllFriends();
    }

    private void getAllFriends() {
        retrofitService.getAllFriends(userInfo.getUserId()).enqueue(new Callback<ArrayList<Friend>>() {
            @Override
            public void onResponse(Call<ArrayList<Friend>> call, Response<ArrayList<Friend>> response) {
                ArrayList<Friend> responseList = response.body();
                if (response.code() == 200 && responseList!= null){
                    friends.clear();
                    friends.addAll(responseList);
                    friendAdapter.notifyDataSetChanged();
                    vfFriends.setDisplayedChild(3);
                }else {
                    vfFriends.setDisplayedChild(1);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Friend>> call, Throwable t) {
                vfFriends.setDisplayedChild(2);
            }
        });
    }

    @Override
    public void onClick(int pos) {
        Intent intent = new Intent(FriendsListActivity.this, ProfileActivity.class);
        intent.putExtra(ProfileActivity.KEY_GET_PROFILE,friends.get(pos).getUsername());
        startActivity(intent);
    }
}