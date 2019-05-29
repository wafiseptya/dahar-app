package com.komsi.dahar.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Places {

    public String name;
    public String image;
    public String location;
    public String open_hour;
    public String close_hour;

//    public Places() {
//         Default constructor required for calls to DataSnapshot.getValue(Post.class)
//    }

    public Places(String name, String image, String location, String open_hour, String close_hour) {
        this.name = name;
        this.image = image;
        this.location = location;
        this.open_hour = open_hour;
        this.close_hour = close_hour;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("image", image);
        result.put("location", location);
        result.put("open_hour", open_hour);
        result.put("close_hour", close_hour);

        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
