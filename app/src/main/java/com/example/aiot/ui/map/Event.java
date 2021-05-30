package com.example.aiot.ui.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

public class Event {
    @SerializedName("id")
    public Integer id;
    @SerializedName("name")
    public Integer type;
    @SerializedName("location")
    public _Location location;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public _Location getLocation() {
        return location;
    }

    public void setLocation(_Location location) {
        this.location = location;
    }

    public Event(Integer id, Integer type, _Location location) {
        this.id = id;
        this.type = type;
        this.location = location;
    }

    public class _Location {
        @SerializedName("latitude")
        public Double latitude;
        @SerializedName("longitude")
        public Double longtitude;

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Double getLongtitude() {
            return longtitude;
        }

        public void setLongtitude(Double longtitude) {
            this.longtitude = longtitude;
        }

        public _Location(Double latitude, Double longtitude) {
            this.latitude = latitude;
            this.longtitude = longtitude;
        }
    }
}
