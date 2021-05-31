package com.example.demomxh.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.demomxh.View.MainScreen.TabChat.ChatFragment;
import com.example.demomxh.View.MainScreen.TabMenu.MenuFragment;
import com.example.demomxh.View.MainScreen.TabNewFeeds.NewFeedsFragment;
import com.example.demomxh.View.MainScreen.TabYoutube.YoutubeFragment;

public class MainScreenViewPagerAdapter extends FragmentPagerAdapter {

    public MainScreenViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new NewFeedsFragment();
            case 1:
                return new ChatFragment();
            case 2:
                return new YoutubeFragment();
            default:
                return new MenuFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
