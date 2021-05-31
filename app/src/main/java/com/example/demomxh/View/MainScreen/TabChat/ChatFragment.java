package com.example.demomxh.View.MainScreen.TabChat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demomxh.Adapter.GroupsChatAdapter;
import com.example.demomxh.Common.BaseFragment;
import com.example.demomxh.Model.GroupChat;
import com.example.demomxh.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatFragment extends BaseFragment implements GroupsChatAdapter.onGroupChatClick {

    @BindView(R.id.rv_groups_chat)
    RecyclerView rvGroupsChat;
    @BindView(R.id.fab_add_groups_chat)
    FloatingActionButton fabAddGroup;

    private ArrayList<GroupChat> groupChats;
    private GroupsChatAdapter groupChatAdapter;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        Init();
        RegisterEvent();
    }

    private void RegisterEvent() {
        fabAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),AddGroupChatActivity.class);
                startActivity(intent);
            }
        });
    }

    private void Init() {
        groupChats = new ArrayList<>();
        groupChatAdapter = new GroupsChatAdapter(groupChats,this);
        rvGroupsChat.setAdapter(groupChatAdapter);
        rvGroupsChat.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
    }

    @Override
    public void onResume() {
        super.onResume();
        getGroupsChat();
    }

    private void getGroupsChat() {
        if(realmContext.getUser()!=null){
            retrofitService.getMyGroups(realmContext.getUser().getUserId()).enqueue(new Callback<ArrayList<GroupChat>>() {
                @Override
                public void onResponse(Call<ArrayList<GroupChat>> call, Response<ArrayList<GroupChat>> response) {
                    if (response.code()==200 && response.body()!=null){
                        groupChats.clear();
                        groupChats.addAll(response.body());
                        groupChatAdapter.notifyDataSetChanged();
                    }else{
                        ShowToast("Hiển thị danh sách thất bại");
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<GroupChat>> call, Throwable t) {
                    ShowToast("Hiển thị danh sách thất bại");
                }
            });
        }
    }

    @Override
    public void onGroupChatClick(int position) {
        Intent intent = new Intent(getContext(),ChatActivity.class);
        intent.putExtra(ChatActivity.KEY_GET_GROUP_CHAT,groupChats.get(position).getGroupId());
        startActivity(intent);
    }
}