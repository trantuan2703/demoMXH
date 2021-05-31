package com.example.demomxh.View.MainScreen.TabChat;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.demomxh.Adapter.AddUserChatAdapter;
import com.example.demomxh.Common.BaseActivity;
import com.example.demomxh.Model.Friend;
import com.example.demomxh.Model.GroupChatResponse;
import com.example.demomxh.Model.SendformCreateGroup;
import com.example.demomxh.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddGroupChatActivity extends BaseActivity implements AddUserChatAdapter.onAddUserClickListener {

    @BindView(R.id.vf_create_groups)
    ViewFlipper vfMain;
    @BindView(R.id.imv_add_to_group)
    ImageView imvAddToGroup;
    @BindView(R.id.rv_add_user_to_group)
    RecyclerView rvAddToGroups;
    private ArrayList<Friend> friends;
    private AddUserChatAdapter addUserChatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group_chat);
        init();
        RegisterEvent();
    }

    private void RegisterEvent() {
        imvAddToGroup.setOnClickListener(v -> CreateGroupChat());

    }

    private void CreateGroupChat() {
        SendformCreateGroup sendformCreateGroup = new SendformCreateGroup(getFriendSelected());
        retrofitService.createGroupChat(sendformCreateGroup).enqueue(new Callback<GroupChatResponse>() {
            @Override
            public void onResponse(Call<GroupChatResponse> call, Response<GroupChatResponse> response) {
                GroupChatResponse groupChatResponse = response.body();
                if (response.code()==200 && groupChatResponse!=null){
                    ShowToast("Successful");
                    Intent intent = new Intent(AddGroupChatActivity.this,ChatActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                   ShowToast("Unsuccessful");
                }
            }

            @Override
            public void onFailure(Call<GroupChatResponse> call, Throwable t) {
                ShowToast("Unsuccessful");
            }
        });
    }

    private void init() {
        ButterKnife.bind(this);
        friends = new ArrayList<>();
        GetFriendList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetFriendList();
    }

    private void GetFriendList() {
        retrofitService.getAllFriends(realmContext.getUser().getUserId()).enqueue(new Callback<ArrayList<Friend>>() {
            @Override
            public void onResponse(Call<ArrayList<Friend>> call, Response<ArrayList<Friend>> response) {
                ArrayList<Friend> responseList = response.body();
                if (response.code()==200 && responseList!=null){
                    friends.clear();
                    friends.addAll(responseList);
                    BindGroups(friends);
                    vfMain.setDisplayedChild(3);
                }else{
                    vfMain.setDisplayedChild(1);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Friend>> call, Throwable t) {
                vfMain.setDisplayedChild(2);
            }
        });
    }

    private void BindGroups(ArrayList<Friend> friends) {
        addUserChatAdapter = new AddUserChatAdapter(friends,this);
        rvAddToGroups.setAdapter(addUserChatAdapter);
        rvAddToGroups.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        addUserChatAdapter.notifyDataSetChanged();
    }

    private ArrayList<String> getFriendSelected() {
        ArrayList<String> list = new ArrayList<>();
        for (Friend friend : friends) {
            if (friend.isSelected())
                list.add(friend.getUserId());
        }
        list.add(realmContext.getUser().getUserId());
        Toast.makeText(this, ""+list.size(), Toast.LENGTH_SHORT).show();
        return list;

    }

    @Override
    public void onClick() {
        boolean visible = false;
        for (Friend friend : friends) {
            if (friend.isSelected()) {
                visible = true;
                break;
            }
        }
        if (visible)
            imvAddToGroup.setVisibility(View.VISIBLE);
        else
            imvAddToGroup.setVisibility(View.GONE);
    }
}