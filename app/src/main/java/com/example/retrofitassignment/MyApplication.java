package com.example.retrofitassignment;

import android.app.Application;

import com.example.retrofitassignment.network.FlickersAPIService;
import com.example.retrofitassignment.network.MarsAPIService;
import com.example.retrofitassignment.network.Repository;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initRepository();
    }

    private void initRepository() {
        Repository.initialize();
        Repository repository = Repository.get();

        Retrofit retrofit01 = new Retrofit.Builder()
                .baseUrl(Constant.MARS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MarsAPIService marsAPIService = retrofit01.create(MarsAPIService.class);

        Retrofit retrofit02 = new Retrofit.Builder()
                .baseUrl(Constant.FLICKER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FlickersAPIService flickersAPIService = retrofit02.create(FlickersAPIService.class);

            repository.setMarsAPIService(marsAPIService);
            repository.setFlickersAPIService(flickersAPIService);

    }


}
