package com.example.retrofitassignment.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.retrofitassignment.Constant;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {
    private static Repository INSTANCE;
    private final String TAG = "Repository";
    private MarsAPIService marsAPIService;
    private FlickersAPIService flickersAPIService;
    private Call<FlickrResponse> flickerRequest;
    private Call<List<MarsProperty>> marsRequest;

    private Repository() {

    }


    public static void initialize( ) {
        if (INSTANCE == null) {
            INSTANCE = new Repository();
        }
    }

    public static Repository get() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Repository must be initialized!");
        }
        return INSTANCE;
    }

    public void setMarsAPIService(MarsAPIService baseAPIService){
       this.marsAPIService = baseAPIService;
    }

    public void setFlickersAPIService(FlickersAPIService flickersAPIService){
        this.flickersAPIService = flickersAPIService;
    }
    public LiveData<List<GalleryItem>> fetchFlickers() {
        MutableLiveData<List<GalleryItem>> responseLiveData = new MutableLiveData<>();
        flickerRequest = flickersAPIService.getFlickers();
        flickerRequest.enqueue(new Callback<FlickrResponse>() {

            @Override
            public void onResponse(Call<FlickrResponse> call, Response<FlickrResponse> response) {
                Log.i(TAG, "Response received");
                if (response != null) {
                     FlickrResponse flickrResponse = response.body();
                     PhotoResponse photoResponse = flickrResponse.getPhotos();
                     List<GalleryItem> galleryItemList = photoResponse.getGalleryItems();
                    if (photoResponse != null) {
                        responseLiveData.postValue(galleryItemList);
                    }
                }
            }

            @Override
            public void onFailure(Call<FlickrResponse> call, Throwable t) {
                Log.e(TAG, "Failed to fetch mars", t);
            }
        });
        return responseLiveData;
    }

    public LiveData<List<MarsProperty>> fetchMars() {
        MutableLiveData<List<MarsProperty>> responseLiveData = new MutableLiveData<>();
        marsRequest = marsAPIService.getMars();
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
    @WorkerThread
    public Bitmap fetchMarsPhoto(String url) throws IOException {
        Bitmap bitmap = null;
        Response<ResponseBody> response = marsAPIService.fetchUrlBytes(url).execute();
        InputStream inputStream = response.body().byteStream();
        if (inputStream != null) {
            bitmap = BitmapFactory.decodeStream(inputStream);
            Log.i(TAG, "Decoded bitmap = " + bitmap + " from Response = " + response);
        }
        inputStream.close();
        return bitmap;
    }

    @WorkerThread
    public Bitmap fetchFlickersPhoto(String url) throws IOException {
        Bitmap bitmap = null;
        Response<ResponseBody> response = marsAPIService.fetchUrlBytes(url).execute();
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
        if (flickerRequest != null)
            if (flickerRequest.isExecuted()) {
                flickerRequest.cancel();
            }

    }
}