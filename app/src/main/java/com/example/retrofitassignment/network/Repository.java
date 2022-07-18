package com.example.retrofitassignment.network;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository {
    private final String TAG = this.getClass().getSimpleName();
    private static Repository INSTANCE;
    private Retrofit retrofit;
    private RetrofitAPIService retrofitAPIService;
    private Call<List<MarsProperty>> marsRequest = retrofitAPIService.getProperties();
    private Call<List<MarsProperty>> boughtMarsRequest = retrofitAPIService.getProperties();
    private Call<List<MarsProperty>> rentMarsRequest = retrofitAPIService.getProperties();



    private Repository (){
        init();
    }

    private void init() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://mars.udacity.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitAPIService = retrofit.create(RetrofitAPIService.class);
    }


    public static void initialize(){
        if(INSTANCE == null){
            INSTANCE = new Repository();
        }
    }
    public static Repository get(){
        if(INSTANCE ==null){
            throw new IllegalStateException("Repository must be initialized!");
        }
        return INSTANCE;
    }
    public LiveData<List<MarsProperty>> fetchMars(){
        MutableLiveData<List<MarsProperty>> responseLiveData = new MutableLiveData<>();
        marsRequest.enqueue(new Callback<List<MarsProperty>>(){

            @Override
            public void onResponse(Call<List<MarsProperty>> call, Response<List<MarsProperty>> response) {
                Log.i(TAG, "Response received");
                if(response != null) {
                    List<MarsProperty> marsResponse = response.body();
                    if (marsResponse != null) {
                     responseLiveData.postValue(marsResponse);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<MarsProperty>> call, Throwable t) {
                Log.e(TAG, "Failed to fetch mars", t);
            }
        });
        return responseLiveData;
    }

    public LiveData<List<MarsProperty>> fetchBoughtMars(){
        MutableLiveData<List<MarsProperty>> responseLiveData = new MutableLiveData<>();
        List<MarsProperty> boughtMarsList = new ArrayList<>();
        boughtMarsRequest.enqueue(new Callback<List<MarsProperty>>(){

            @Override
            public void onResponse(Call<List<MarsProperty>> call, Response<List<MarsProperty>> response) {
                Log.i(TAG, "Response received");
                if(response != null) {
                    List<MarsProperty> marsResponse = response.body();
                    if (marsResponse != null) {
                        for(MarsProperty marsProperty:marsResponse){
                            if("buy".equals(marsProperty.getType())){
                                boughtMarsList.add(marsProperty);
                            }
                        }
                        responseLiveData.postValue(boughtMarsList);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<MarsProperty>> call, Throwable t) {
                Log.e(TAG, "Failed to fetch mars", t);
            }
        });
        return responseLiveData;
    }

    public LiveData<List<MarsProperty>> fetchRentMars(){
        MutableLiveData<List<MarsProperty>> responseLiveData = new MutableLiveData<>();
        Call<List<MarsProperty>> rentMarsRequest = retrofitAPIService.getProperties();
        List<MarsProperty> rentMarsList = new ArrayList<>();
        rentMarsRequest.enqueue(new Callback<List<MarsProperty>>(){

            @Override
            public void onResponse(Call<List<MarsProperty>> call, Response<List<MarsProperty>> response) {
                Log.i(TAG, "Response received");
                if(response != null) {
                    List<MarsProperty> marsResponse = response.body();
                    if (marsResponse != null) {
                        for(MarsProperty marsProperty:marsResponse){
                            if("rent".equals(marsProperty.getType())){
                                rentMarsList.add(marsProperty);
                            }
                        }
                        responseLiveData.postValue(rentMarsList);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<MarsProperty>> call, Throwable t) {
                Log.e(TAG, "Failed to fetch mars", t);
            }
        });
        return responseLiveData;
    }

    public void cancelRequestInFlight() {
        if(marsRequest.isExecuted()){
            marsRequest.cancel();
        }
        if(boughtMarsRequest.isExecuted()){
            boughtMarsRequest.cancel();
        }
        if(rentMarsRequest.isExecuted()){
            rentMarsRequest.cancel();
        }
    }
}