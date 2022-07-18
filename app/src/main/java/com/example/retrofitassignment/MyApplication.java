package com.example.retrofitassignment;

import android.app.Application;

import com.example.retrofitassignment.network.Repository;
import com.example.retrofitassignment.network.RetrofitAPIService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initRepository();
    }

    private void initRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://mars.udacity.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPIService retrofitAPIService = retrofit.create(RetrofitAPIService.class);
        Repository.initialize(retrofitAPIService);
    }
}
