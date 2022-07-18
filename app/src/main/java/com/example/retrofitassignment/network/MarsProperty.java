package com.example.retrofitassignment.network;

import com.squareup.moshi.Json;

public class MarsProperty {
    public String id;
    public String type;
    public @Json(name = "img_src") String imgSrcUrl;
    public Double price;

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getImgSrcUrl() {
        return imgSrcUrl;
    }

    public Double getPrice() {
        return price;
    }
}
