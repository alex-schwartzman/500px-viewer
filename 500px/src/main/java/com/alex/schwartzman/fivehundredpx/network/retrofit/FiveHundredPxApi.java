package com.alex.schwartzman.fivehundredpx.network.retrofit;

import com.alex.schwartzman.fivehundredpx.model.PageWithPhotos;

import retrofit.http.GET;
import retrofit.http.Query;

public interface FiveHundredPxApi {
    @GET("/photos")
    PageWithPhotos getImagesList(@Query("feature") String feature, @Query("consumer_key") String consumer_key, @Query("image_size") int image_size, @Query("page") int page);
}
