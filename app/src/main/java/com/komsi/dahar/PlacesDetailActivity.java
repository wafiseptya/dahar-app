package com.komsi.dahar;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.komsi.dahar.models.Foods;
import com.komsi.dahar.models.Places;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;

public class PlacesDetailActivity extends BaseActivity {

    private static final String TAG = "PlacesDetailActivity";

    public static final String EXTRA_PLACES_KEY = "places_key";

    private DatabaseReference mPlacesReference;
    private DatabaseReference mFoodsReference;
    private ValueEventListener mPlacesListener;
    private String mPlacesKey;
    private String openClosePlace;
    private Double lat;
    private Double lng;
    private FoodsAdapter mAdapter;

    private TextView namePlaceView;
    private TextView locationPlaceView;
    private TextView openPlaceView;
    private ImageView imagePlaceView;
    private RecyclerView mFoodsRecycler;

    private Button buttonMaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_places_page);

        // Get post key from intent
        mPlacesKey = getIntent().getStringExtra(EXTRA_PLACES_KEY);
        if (mPlacesKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_PLACES_KEY");
        }

        // Initialize Database
        mPlacesReference = FirebaseDatabase.getInstance().getReference()
                .child("places").child(mPlacesKey);
        mFoodsReference = FirebaseDatabase.getInstance().getReference()
                .child("places_foods").child(mPlacesKey);

        // Initialize Views
        namePlaceView = findViewById(R.id.placeName);
        locationPlaceView = findViewById(R.id.clockView);
        openPlaceView = findViewById(R.id.distanceView);
        mFoodsRecycler = findViewById(R.id.recycler_list_foods_place);
        imagePlaceView = findViewById(R.id.placeImage);
        buttonMaps = findViewById(R.id.go_to_map);
        buttonMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng location = new LatLng(lat, lng);

                Bundle args = new Bundle();
                args.putParcelable("location", location);

                Intent intent = new Intent(PlacesDetailActivity.this, PlacesMapsActivity.class);
                intent.putExtra("bundle", args);
                startActivity(intent);
            }
        });

        mFoodsRecycler.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onStart() {
        super.onStart();

        // Add value event listener to the post
        // [START post_value_event_listener]
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Places places = dataSnapshot.getValue(Places.class);
                // [START_EXCLUDE]
                namePlaceView.setText(places.getName());

                openClosePlace = places.getOpen_hour() + " - " + places.getClose_hour();

                openPlaceView.setText(openClosePlace);
                locationPlaceView.setText(places.getLocation());
                Picasso.get().load(places.getImage()).into(imagePlaceView);

                lat = places.getLat();
                lng = places.getLng();


                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(PlacesDetailActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        mPlacesReference.addValueEventListener(postListener);
        // [END post_value_event_listener]

        // Keep copy of post listener so we can remove it when app stops
        mPlacesListener = postListener;

        // Listen for comments
        mAdapter = new FoodsAdapter(this, mFoodsReference);
        mFoodsRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();

        // Remove post value event listener
        if (mPlacesListener != null) {
            mPlacesReference.removeEventListener(mPlacesListener);
        }

        // Clean up comments listener
        mAdapter.cleanupListener();
    }

    private static class FoodsViewHolder extends RecyclerView.ViewHolder {

        public TextView foodNameView;
        public TextView foodPriceView;
        public ImageView foodImageView;

        public FoodsViewHolder(View itemView) {
            super(itemView);

            foodNameView = itemView.findViewById(R.id.food_name);
            foodImageView = itemView.findViewById(R.id.food_image);
            foodPriceView = itemView.findViewById(R.id.food_price);
        }
    }

    private static class FoodsAdapter extends RecyclerView.Adapter<FoodsViewHolder> {

        private Context mContext;
        private DatabaseReference mDatabaseReference;
        private ChildEventListener mChildEventListener;

        private List<String> mFoodsIds = new ArrayList<>();
        private List<Foods> mFoods = new ArrayList<>();

        public FoodsAdapter(final Context context, DatabaseReference ref) {
            mContext = context;
            mDatabaseReference = ref;

            // Create child event listener
            // [START child_event_listener_recycler]
            ChildEventListener childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                    // A new comment has been added, add it to the displayed list
                    Foods foods = dataSnapshot.getValue(Foods.class);

                    // [START_EXCLUDE]
                    // Update RecyclerView
                    mFoodsIds.add(dataSnapshot.getKey());
                    mFoods.add(foods);
                    notifyItemInserted(mFoods.size() - 1);
                    // [END_EXCLUDE]
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                    // A comment has changed, use the key to determine if we are displaying this
                    // comment and if so displayed the changed comment.
                    Foods newFoods = dataSnapshot.getValue(Foods.class);
                    String foodsKey = dataSnapshot.getKey();

                    // [START_EXCLUDE]
                    int foodsIndex = mFoodsIds.indexOf(foodsKey);
                    if (foodsIndex > -1) {
                        // Replace with the new data
                        mFoods.set(foodsIndex, newFoods);

                        // Update the RecyclerView
                        notifyItemChanged(foodsIndex);
                    } else {
                        Log.w(TAG, "onChildChanged:unknown_child:" + foodsKey);
                    }
                    // [END_EXCLUDE]
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                    // A comment has changed, use the key to determine if we are displaying this
                    // comment and if so remove it.
                    String foodsKey = dataSnapshot.getKey();

                    // [START_EXCLUDE]
                    int foodsIndex = mFoodsIds.indexOf(foodsKey);
                    if (foodsIndex > -1) {
                        // Remove data from the list
                        mFoodsIds.remove(foodsIndex);
                        mFoods.remove(foodsIndex);

                        // Update the RecyclerView
                        notifyItemRemoved(foodsIndex);
                    } else {
                        Log.w(TAG, "onChildRemoved:unknown_child:" + foodsKey);
                    }
                    // [END_EXCLUDE]
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                    // A comment has changed position, use the key to determine if we are
                    // displaying this comment and if so move it.
                    Foods movedFoods = dataSnapshot.getValue(Foods.class);
                    String foodsKey = dataSnapshot.getKey();

                    // ...
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                    Toast.makeText(mContext, "Failed to load comments.",
                            Toast.LENGTH_SHORT).show();
                }
            };
            ref.addChildEventListener(childEventListener);
            // [END child_event_listener_recycler]

            // Store reference to listener so it can be removed on app stop
            mChildEventListener = childEventListener;
        }

        @Override
        public FoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.item_foods, parent, false);
            return new FoodsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(FoodsViewHolder holder, int position) {
            Foods foods = mFoods.get(position);
            holder.foodNameView.setText(foods.name);
            String priceFoods = "Rp. " + foods.price;
            holder.foodPriceView.setText(priceFoods);
            Picasso.get().load(foods.img).into(holder.foodImageView);
        }

        @Override
        public int getItemCount() {
            return mFoods.size();
        }

        public void cleanupListener() {
            if (mChildEventListener != null) {
                mDatabaseReference.removeEventListener(mChildEventListener);
            }
        }

    }
}
