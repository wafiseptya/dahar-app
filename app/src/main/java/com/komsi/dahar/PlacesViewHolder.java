package com.komsi.dahar;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class PlacesViewHolder extends RecyclerView.ViewHolder {
    View view;

    public PlacesViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
    }

    public void setName(String name){
        TextView places_name = (TextView)view.findViewById(R.id.places_name);
        places_name.setText(name);
    }

    public void setOpenHour(String openHour){
        TextView places_open = (TextView)view.findViewById(R.id.open_place);
        places_open.setText(openHour);
    }

}
