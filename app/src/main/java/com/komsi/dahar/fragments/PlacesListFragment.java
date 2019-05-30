package com.komsi.dahar.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.komsi.dahar.MainActivity;
import com.komsi.dahar.PlacesDetailActivity;
import com.komsi.dahar.R;
import com.komsi.dahar.models.Places;
import com.komsi.dahar.viewholder.PlacesViewHolder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class PlacesListFragment extends Fragment{

    private static final String TAG = "PlacesListFragment";

    private FusedLocationProviderClient client;
    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]

    private FirebaseRecyclerAdapter<Places, PlacesViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    TextView textLocation;
    LocationManager locationManager;
    String provider;
    String mParam1;
    Location location;

    public PlacesListFragment() {}

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString("params");
        }

        MainActivity activity = (MainActivity) getActivity();
        String getData = activity.sendData();


        View rootView = inflater.inflate(R.layout.home_page, container, false);

        textLocation = rootView.findViewById(R.id.location_now);
        Log.v("Jancok", getData);

        if(location!=null)
        {
            textLocation.setText(getData);
        }
        else
        {
//            Log.v("Tests Debug", getData);
            textLocation.setText(getData);
        }



        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]

        mRecycler = rootView.findViewById(R.id.recycler_list_places_home);
//        mRecycler.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = getQuery(mDatabase);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Places>()
                .setQuery(postsQuery, Places.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<Places, PlacesViewHolder>(options) {


            @Override
            protected void onBindViewHolder(PlacesViewHolder viewHolder, int position,@NonNull Places model) {
                final DatabaseReference placesRef = getRef(position);

                viewHolder.namePlaceView.setText(model.getName());
                viewHolder.locationPlaceView.setText(model.getLocation());
                viewHolder.openPlaceView.setText(model.getOpen_hour());

                final String placesKey = placesRef.getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch PostDetailActivity
                        Intent intent = new Intent(getActivity(), PlacesDetailActivity.class);
                        intent.putExtra(PlacesDetailActivity.EXTRA_PLACES_KEY, placesKey);
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public PlacesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                View view = inflater.inflate(R.layout.item_places, viewGroup, false);
                return new PlacesViewHolder(view);
            }


        };
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public Query getQuery(DatabaseReference databaseReference) {
        // [START recent_posts_query]
        // Last 100 posts, these are automatically the 100 most recent
        // due to sorting by push() keys
        Query homeListQuery = databaseReference.child("places")
                .limitToFirst(100);
        // [END recent_posts_query]

        return homeListQuery;
    }

}
