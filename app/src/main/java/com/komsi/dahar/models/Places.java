package com.komsi.dahar.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Places {

    public String name;
    public String img;
    public String location;
    public Double lng;
    public Double lat;
    public String open_hour;
    public String close_hour;

//    public Places() {
//         Default constructor required for calls to DataSnapshot.getValue(Post.class)
//    }

    public Places(String name, String img, String location, String open_hour, String close_hour, Double lat, Double lng) {
        this.name = name;
        this.img = img;
        this.location = location;
        this.open_hour = open_hour;
        this.close_hour = close_hour;
        this.lat = lat;
        this.lng= lng;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("img", img);
        result.put("location", location);
        result.put("open_hour", open_hour);
        result.put("close_hour", close_hour);
        result.put("lat", lat);
        result.put("lng", lng);

        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getImage() {
        return img;
    }

    public void setImage(String img) {
        this.img = img;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOpen_hour() {
        return open_hour;
    }

    public void setOpen_hour(String open_hour) {
        this.open_hour = open_hour;
    }

    public String getClose_hour() {
        return close_hour;
    }

    public void setClose_hour(String close_hour) {
        this.close_hour = close_hour;
    }
    public Places() {

    }
}
