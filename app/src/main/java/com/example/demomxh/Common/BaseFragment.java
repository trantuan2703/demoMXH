package com.example.demomxh.Common;

import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.demomxh.Database.RealmContext;
import com.example.demomxh.Model.UserInfo;
import com.example.demomxh.Network.RetrofitService;
import com.example.demomxh.Network.RetrofitUtils;

public class BaseFragment extends Fragment {
    public RetrofitService retrofitService = RetrofitUtils.getInstance().createService();
    public RealmContext realmContext = RealmContext.getInstance();
    public UserInfo userInfo = realmContext.getUser();
    public void ShowToast(String s){
        Toast.makeText(getContext(), ""+s, Toast.LENGTH_SHORT).show();
    }
}
