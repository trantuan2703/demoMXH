package com.example.demomxh.View.MainScreen.TabMenu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.demomxh.Common.BaseFragment;
import com.example.demomxh.R;
import com.example.demomxh.View.Authen.LoginActivity;
import com.example.demomxh.View.MainScreen.ProfileActivity;


import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MenuFragment extends BaseFragment {
    @BindView(R.id.civ_menu_avatar)
    CircleImageView civAvatar;
    @BindView(R.id.tv_menu_name)
    TextView tvName;
    @BindView(R.id.ll_Logout)
    LinearLayout llLogout;
    @BindView(R.id.ll_go_to_profile)
    LinearLayout llGoToProfile;
    @BindView(R.id.ll_friendslist)
    LinearLayout llFriends;

    public MenuFragment() {
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
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        init();
        registerEvent();
    }

    private void registerEvent() {
        llLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realmContext.DeleteUser(userInfo);
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finishAffinity();
                Log.d("TESTLOGOUT",realmContext.getUser()+"");
            }
        });

        llGoToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                intent.putExtra(ProfileActivity.KEY_GET_PROFILE,userInfo.getUsername());
                startActivity(intent);
            }
        });

        llFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),FriendsListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    private void init() {
        if(userInfo!=null){
            tvName.setText(userInfo.getFullName());
            Glide.with(getContext()).load(userInfo.getAvatarUrl()).into(civAvatar);
        }
    }
}