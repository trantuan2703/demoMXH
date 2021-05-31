package com.example.demomxh.View.MainScreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.demomxh.Common.BaseActivity;
import com.example.demomxh.Model.UserInfo;
import com.example.demomxh.R;
import com.example.demomxh.View.Authen.LoginActivity;

import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkAccount();
            }
        },1000);
    }

    private void checkAccount() {
        UserInfo userInfo = realmContext.getUser();
        if(userInfo == null){
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else{
            Intent intent = new Intent(SplashActivity.this, MainScreenActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void init() {
        ButterKnife.bind(this);
    }
}