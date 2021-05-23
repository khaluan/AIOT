package com.example.aiot.ui.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

public class Event {
    @SerializedName("Lat")
    public Double locationLat;
    @SerializedName("Lng")
    public Double locationLng;
    @SerializedName("Type")
    public Integer eventType;

    public Double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(Double locationLat) {
        this.locationLat = locationLat;
    }

    public Double getLocationLng() {
        return locationLng;
    }

    public void setLocationLng(Double locationLng) {
        this.locationLng = locationLng;
    }

    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    public Event(Double locationLat, Double locationLng, Integer eventType) {
        this.locationLat = locationLat;
        this.locationLng = locationLng;
        this.eventType = eventType;
    }
}
