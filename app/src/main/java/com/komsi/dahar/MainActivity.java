package com.komsi.dahar;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.komsi.dahar.models.Places;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<Places,PlacesViewHolder> mPlacesRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("places");
        Query placesQuery = mDatabase.orderByKey();
        mDatabase.keepSynced(true);

        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_list_places_home);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        FirebaseRecyclerOptions placesOptions = new FirebaseRecyclerOptions.Builder<Places>()
                .setQuery(placesQuery, Places.class).build();

        mPlacesRVAdapter = new FirebaseRecyclerAdapter<Places, PlacesViewHolder>(placesOptions) {

            @Override
            protected void onBindViewHolder(@NonNull PlacesViewHolder holder, int position, @NonNull Places model) {
                holder.setName(model.getName());
                holder.setOpenHour(model.getOpen_hour());
            }

            @NonNull
            @Override
            public PlacesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_foods, viewGroup, false);

                return new PlacesViewHolder(view);
            }


        };
        mRecyclerView.setAdapter(mPlacesRVAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mPlacesRVAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPlacesRVAdapter.stopListening();
    }




}
