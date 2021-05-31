package com.example.demomxh.Database;

import android.app.Application;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config =
                new RealmConfiguration.Builder()
                        .deleteRealmIfMigrationNeeded()//Migration alert
                        .build();
        Realm.setDefaultConfiguration(config);
    }
}
