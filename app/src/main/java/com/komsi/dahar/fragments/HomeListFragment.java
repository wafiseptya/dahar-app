package com.komsi.dahar.fragments;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class HomeListFragment extends PlacesListFragment {

    public HomeListFragment() {}

    @Override
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
