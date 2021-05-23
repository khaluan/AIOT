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
    @GET("/update")
    Call<ArrayList<Event>> updateEvents();

    @Multipart
    @POST("/report")
    Call<ResponseBody> reportPhoto(@Part("Lat") RequestBody Lat, @Part("Lng") RequestBody Lng, @Part MultipartBody.Part photo);
}
