package com.example.retrofitassignment.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {
    private static Repository INSTANCE;
    private final String TAG = "Repository";
    private RetrofitAPIService retrofitAPIService;
    private Call<List<MarsProperty>> marsRequest;
    private Call<List<MarsProperty>> boughtMarsRequest;
    private Call<List<MarsProperty>> rentMarsRequest;


    private Repository(RetrofitAPIService retrofitAPIService) {
        this.retrofitAPIService = retrofitAPIService;
    }


    public static void initialize(RetrofitAPIService retrofitAPIService) {
        if (INSTANCE == null) {
            INSTANCE = new Repository(retrofitAPIService);
        }
    }

    public static Repository get() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Repository must be initialized!");
        }
        return INSTANCE;
    }

    public LiveData<List<MarsProperty>> fetchMars() {
        MutableLiveData<List<MarsProperty>> responseLiveData = new MutableLiveData<>();
        marsRequest = retrofitAPIService.getProperties();
        marsRequest.enqueue(new Callback<List<MarsProperty>>() {

            @Override
            public void onResponse(Call<List<MarsProperty>> call, Response<List<MarsProperty>> response) {
                Log.i(TAG, "Response received");
                if (response != null) {
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

    public LiveData<List<MarsProperty>> fetchBoughtMars() {
        MutableLiveData<List<MarsProperty>> responseLiveData = new MutableLiveData<>();
        List<MarsProperty> boughtMarsList = new ArrayList<>();
        boughtMarsRequest = retrofitAPIService.getProperties();
        boughtMarsRequest.enqueue(new Callback<List<MarsProperty>>() {

            @Override
            public void onResponse(Call<List<MarsProperty>> call, Response<List<MarsProperty>> response) {
                Log.i(TAG, "Response received");
                if (response != null) {
                    List<MarsProperty> marsResponse = response.body();
                    if (marsResponse != null) {
                        for (MarsProperty marsProperty : marsResponse) {
                            if ("buy".equals(marsProperty.getType())) {
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

    public LiveData<List<MarsProperty>> fetchRentMars() {
        MutableLiveData<List<MarsProperty>> responseLiveData = new MutableLiveData<>();
        rentMarsRequest = retrofitAPIService.getProperties();
        List<MarsProperty> rentMarsList = new ArrayList<>();
        rentMarsRequest.enqueue(new Callback<List<MarsProperty>>() {

            @Override
            public void onResponse(Call<List<MarsProperty>> call, Response<List<MarsProperty>> response) {
                Log.i(TAG, "Response received");
                if (response != null) {
                    List<MarsProperty> marsResponse = response.body();
                    if (marsResponse != null) {
                        for (MarsProperty marsProperty : marsResponse) {
                            if ("rent".equals(marsProperty.getType())) {
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

    @WorkerThread
    public Bitmap fetchPhoto(String url) throws IOException {
        Bitmap bitmap = null;
        Response<ResponseBody> response = retrofitAPIService.fetchUrlBytes(url).execute();
        InputStream inputStream = response.body().byteStream();
        if (inputStream != null) {
            bitmap = BitmapFactory.decodeStream(inputStream);
            Log.i(TAG, "Decoded bitmap = " + bitmap + " from Response = " + response);
        }
        inputStream.close();
        return bitmap;
    }

    public void cancelRequestInFlight() {
        if (marsRequest != null) {
            if (marsRequest.isExecuted())
                marsRequest.cancel();
        }
        if (boughtMarsRequest != null)
            if (boughtMarsRequest.isExecuted()) {
                boughtMarsRequest.cancel();
            }
        if (rentMarsRequest != null)
            if (rentMarsRequest.isExecuted()) {
                rentMarsRequest.cancel();
            }
    }
}