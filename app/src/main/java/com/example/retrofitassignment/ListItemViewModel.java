package com.example.retrofitassignment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.retrofitassignment.network.FlickersAPIService;
import com.example.retrofitassignment.network.MarsAPIService;
import com.example.retrofitassignment.network.GalleryItem;
import com.example.retrofitassignment.network.MarsProperty;
import com.example.retrofitassignment.network.Repository;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListItemViewModel extends ViewModel {
    private Repository repository = Repository.get();

    public LiveData<List<MarsProperty>> marsList = repository.fetchMars();
    public LiveData<List<GalleryItem>> flickersList = repository.fetchFlickers();

    @Override
    protected void onCleared() {
        super.onCleared();
        repository.cancelRequestInFlight();
    }
}
