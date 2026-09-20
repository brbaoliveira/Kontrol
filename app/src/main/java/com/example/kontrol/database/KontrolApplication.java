package com.example.kontrol.database;

import android.app.Application;

import com.example.kontrol.helper.DbHelper;

public class KontrolApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        new DbHelper(this).getWritableDatabase();
    }
}

