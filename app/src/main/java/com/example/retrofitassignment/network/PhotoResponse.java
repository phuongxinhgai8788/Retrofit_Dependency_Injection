package com.example.retrofitassignment.network;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PhotoResponse {
    @SerializedName("photo")
    private List<GalleryItem> galleryItems = new ArrayList<>();

    public List<GalleryItem> getGalleryItems(){
        return this.galleryItems;
    }
}
