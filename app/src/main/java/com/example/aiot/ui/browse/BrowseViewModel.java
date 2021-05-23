package com.example.aiot.ui.browse;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.aiot.FileUtils;
import com.example.aiot.R;
import com.example.aiot.ui.map.EventRetrievalService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BrowseViewModel extends AndroidViewModel {
    EventRetrievalService eventRetrievalService;

    public BrowseViewModel(@NonNull Application application) {
        super(application);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(application.getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        eventRetrievalService = retrofit.create(EventRetrievalService.class);

    }

    public ArrayList<String> listOfImages(Context context) {
        Uri uri;
        Cursor cursor;
        int columnIndexData, columnIndexFolderName;
        ArrayList<String> listAllImage = new ArrayList<>();
        String absolutePath;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        String orderBy = MediaStore.Video.Media.DATE_TAKEN;
        cursor = context.getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
        columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

        while (cursor.moveToNext()) {
            absolutePath = cursor.getString(columnIndexData);
            listAllImage.add(absolutePath);
        }
        return listAllImage;
    }

    @SuppressLint("MissingPermission")
    public void uploadPhoto1(Uri uri) {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplication());
        Log.e("Upload", "No nen dc in ra");
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                RequestBody lat = RequestBody.create(MultipartBody.FORM, String.valueOf(location.getLatitude()));
                RequestBody lng = RequestBody.create(MultipartBody.FORM, String.valueOf(location.getLongitude()));

                File originalFile = FileUtils.getFile(getApplication(), uri);
                RequestBody filePart = RequestBody.create(
                        MediaType.parse(getApplication().getContentResolver().getType(uri)),originalFile);
                Log.e("Upload", "No khong nen dc in ra");

                MultipartBody.Part file = MultipartBody.Part.createFormData("photo", originalFile.getName(), filePart);
                Call<ResponseBody> call = eventRetrievalService.reportPhoto(lat, lng, file);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });

            }
        });
    }

    public void uploadPhoto(String path){
        //TODO: Code up photo
    }
}