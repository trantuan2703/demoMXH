package com.example.demomxh.Database;

import com.example.demomxh.Model.UserInfo;

import io.realm.Realm;

public class RealmContext {
    private Realm realm;
    private  static RealmContext realmContext;
    private RealmContext(){
        realm = Realm.getDefaultInstance();
    }
    public static RealmContext getInstance(){
        if (realmContext == null){
            realmContext = new RealmContext();
        }
        return realmContext;
    }

    public void addUser(UserInfo userInfo){
        if (userInfo == null) return;

        realm.beginTransaction();
        realm.insertOrUpdate(userInfo);
        realm.commitTransaction();
    }

    public UserInfo getUser(){
        return realm.where(UserInfo.class).findFirst();
    }

    public void DeleteUser(UserInfo userInfo){
        realm.beginTransaction();
        userInfo.deleteFromRealm();
        realm.commitTransaction();
    }
}
