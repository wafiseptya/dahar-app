package com.komsi.dahar.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.komsi.dahar.R;
import com.komsi.dahar.models.Places;

public class PlacesViewHolder extends RecyclerView.ViewHolder {

    public TextView namePlaceView;
    public TextView openPlaceView;
    public TextView locationPlaceView;



    public PlacesViewHolder(View itemView) {
        super(itemView);

        namePlaceView = itemView.findViewById(R.id.places_name);
        openPlaceView = itemView.findViewById(R.id.open_place);
        locationPlaceView = itemView.findViewById(R.id.dc_place);
    }

    public void bindToPost(Places places) {
        namePlaceView.setText(places.name);
        openPlaceView.setText(places.open_hour);
        locationPlaceView.setText(places.location);
    }
}
