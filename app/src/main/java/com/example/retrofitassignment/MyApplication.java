package com.example.retrofitassignment;

import android.app.Application;

import com.example.retrofitassignment.network.Repository;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initRepository();
    }

    private void initRepository() {
        Repository.initialize();
    }
}
