package com.example.retrofitassignment.network;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RetrofitAPIService {
    @GET("services/rest/?method=flickr.interestingness.getList" +
            "&api_key=4f721bbafa75bf6d2cb5af54f937bb70" +
            "&format=json" +
            "&nojsoncallback=1" +
            "&extras=url_s")
    Call<FlickrResponse> getProperties();

    @GET
    Call<ResponseBody> fetchUrlBytes(@Url String url);
}
