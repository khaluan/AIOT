package com.example.aiot.ui.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

public class Event {
    @SerializedName("id")
    public Integer id;
    @SerializedName("label")
    public Integer type;
    @SerializedName("url")
    public String url;
    @SerializedName("time")
    public String time;
    @SerializedName("location")
    public _Location location;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Event(Integer id, Integer type, String url, _Location location, String time) {
        this.id = id;
        this.type = type;
        this.url = url;
        this.location = location;
        this.time = time;
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
