package com.example.aiot.ui.map;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.aiot.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MapViewModel extends AndroidViewModel {
    EventRetrievalService eventRetrievalService;
    public ArrayList<Event> eventList;
    MutableLiveData<ArrayList<Event>> liveEventList;

    public MapViewModel(@NonNull Application application) {
        super(application);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(application.getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        eventRetrievalService = retrofit.create(EventRetrievalService.class);
        eventList = new ArrayList<>();
        liveEventList = new MutableLiveData<>(eventList);
    }

    public MutableLiveData<ArrayList<Event>> getLiveEventList() {
        return liveEventList;
    }

    public void requestForEvents(){
        Call<ArrayList<Event>> call = eventRetrievalService.updateEvents();
        call.enqueue(new Callback<ArrayList<Event>>() {
            @Override
            public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
                eventList = response.body();
                liveEventList.postValue(response.body());
                for(Event e : eventList){
                    Log.d("EVENT LOG",String.format("Lat: %s, Lng: %s, Type: %d", e.getLocationLat(), e.getLocationLng(), e.getEventType()));
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Event>> call, Throwable t) {
                Log.d("EVENT LOG", "Something wrong");
            }
        });
    }

}
