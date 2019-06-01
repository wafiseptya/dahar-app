package com.komsi.dahar.fragments;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.komsi.dahar.LoginActivity;
import com.komsi.dahar.MainActivity;
import com.komsi.dahar.PlacesDetailActivity;
import com.komsi.dahar.R;
import com.komsi.dahar.models.Places;
import com.komsi.dahar.viewholder.PlacesViewHolder;
import com.squareup.picasso.Picasso;

public class SettingFragment extends Fragment{

    private static final String TAG = "SettingFragment";
    private Button signout;

    public SettingFragment() {}

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.setting_page, container, false);

        signout = rootView.findViewById(R.id.sign_out);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);

                startActivity(intent);
                getActivity().finish();
            }
        });

        return rootView;
    }

}
