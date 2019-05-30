package com.komsi.dahar.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Foods {

    public String name;
    public long price;
    public String image;

    public Foods() {
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }

    public Foods(String name, long price, String image) {
        this.name = name;
        this.price = price;
        this.image = image;
    }

}
// [END comment_class]
