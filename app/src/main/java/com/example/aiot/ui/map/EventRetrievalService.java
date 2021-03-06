package com.example.aiot.ui.map;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface EventRetrievalService {
    @GET("/events")
    Call<ArrayList<Event>> updateEvents();

    @Multipart
    @POST("/upload")
    Call<ResponseBody> reportPhoto(@Part("latitude") RequestBody Lat, @Part("longitude") RequestBody Lng, @Part MultipartBody.Part photo);
}
