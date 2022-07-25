package com.example.retrofitassignment.network;

import com.example.retrofitassignment.Constant;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface FlickersAPIService {

    final String KEY = Constant.FLICKER_GET_KEY;
    @GET(KEY)
    Call<FlickrResponse> getFlickers();

    @GET
    Call<ResponseBody> fetchUrlBytes(@Url String url);

}
