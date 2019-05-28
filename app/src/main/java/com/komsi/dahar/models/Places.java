package com.komsi.dahar.models;

public class Places {
    private String name;
    private String image;
    private String location;
    private String open_hour;
    private String close_hour;

    public Places(String name, String image, String location, String open_hour, String close_hour) {
        this.name = name;
        this.image = image;
        this.location = location;
        this.open_hour = open_hour;
        this.close_hour = close_hour;
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
