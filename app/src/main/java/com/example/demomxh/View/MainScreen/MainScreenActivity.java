package com.example.demomxh.View.MainScreen;
import androidx.viewpager.widget.ViewPager;

import android.graphics.PorterDuff;
import android.os.Bundle;

import com.example.demomxh.Adapter.MainScreenViewPagerAdapter;
import com.example.demomxh.Common.BaseActivity;
import com.example.demomxh.R;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainScreenActivity extends BaseActivity {
    @BindView(R.id.vp_main_screen)
    ViewPager vpScreen;
    @BindView(R.id.tl_main_screen)
    TabLayout tlScreen;

    MainScreenViewPagerAdapter viewPagerHomeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Init();
    }

    private void Init() {
        ButterKnife.bind(this);
        viewPagerHomeAdapter = new MainScreenViewPagerAdapter(getSupportFragmentManager());
        vpScreen.setAdapter(viewPagerHomeAdapter);
        vpScreen.setOffscreenPageLimit(4);
        tlScreen.setupWithViewPager(vpScreen);
        SetUpTabLayout();
    }
    private void SetUpTabLayout() {
        Objects.requireNonNull(tlScreen.getTabAt(0)).setIcon(R.drawable.ic_new_feeds);
        Objects.requireNonNull(tlScreen.getTabAt(1)).setIcon(R.drawable.ic_chat);
        Objects.requireNonNull(tlScreen.getTabAt(2)).setIcon(R.drawable.ic_youtube);
        Objects.requireNonNull(tlScreen.getTabAt(3)).setIcon(R.drawable.ic_menu);

        tlScreen.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.colorBlue), PorterDuff.Mode.SRC_IN);
        tlScreen.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.colorGray500), PorterDuff.Mode.SRC_IN);
        tlScreen.getTabAt(2).getIcon().setColorFilter(getResources().getColor(R.color.colorGray500), PorterDuff.Mode.SRC_IN);
        tlScreen.getTabAt(3).getIcon().setColorFilter(getResources().getColor(R.color.colorGray500), PorterDuff.Mode.SRC_IN);

        tlScreen.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(getResources().getColor(R.color.colorBlue), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(getResources().getColor(R.color.colorGray500), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}