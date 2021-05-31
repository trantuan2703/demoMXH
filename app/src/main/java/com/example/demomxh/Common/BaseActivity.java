package com.example.demomxh.Common;

import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demomxh.Database.RealmContext;
import com.example.demomxh.Network.RetrofitService;
import com.example.demomxh.Network.RetrofitUtils;

public class BaseActivity extends AppCompatActivity {
    public RetrofitService retrofitService = RetrofitUtils.getInstance().createService();
    public RealmContext realmContext = RealmContext.getInstance();
    public void ShowToast(String s){
        Toast.makeText(this, ""+s, Toast.LENGTH_SHORT).show();
    }
}
