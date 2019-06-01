package com.komsi.dahar;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.komsi.dahar.fragments.HomeListFragment;
import com.komsi.dahar.fragments.PlacesListFragment;
import com.komsi.dahar.fragments.PlacesSearchFragment;
import com.komsi.dahar.fragments.SettingFragment;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    public String YOUR_STRING;


    LocationManager locationManager;
    String provider;
    String mParam1;
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_page);

        // Notification
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel =
                    new NotificationChannel("DaharNotifications","DaharNotifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>(){
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Successfull";
                        if(!task.isSuccessful()){
                            msg = "Failed";
                        }
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        locationManager =(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Criteria c=new Criteria();

        provider=locationManager.getBestProvider(c, false);

        if ((ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            location=locationManager.getLastKnownLocation(provider);
        }

        if(location!=null)
        {
            double lng=location.getLongitude();
            double lat=location.getLatitude();
            LocationAddress locationAddress = new LocationAddress();

            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            String result = null;
            try {
                List<Address> addressList = geocoder.getFromLocation(
                        lat, lng, 1);
                if (addressList != null && addressList.size() > 0) {
                    Address address = addressList.get(0);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        sb.append(address.getAddressLine(i)).append("\n");
                    }
                    sb.append(address.getLocality()).append("\n");
                    sb.append(address.getPostalCode()).append("\n");
                    sb.append(address.getCountryName());
                    result = sb.toString();
                }
            } catch (IOException e) {
                Log.e(TAG, "Unable connect to Geocoder", e);
            }

            YOUR_STRING = result;

        }
        else
        {
            YOUR_STRING = "Error Fetching Location";
        }


        // Create the adapter that will return a fragment for each section
        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            private final Fragment[] mFragments = new Fragment[] {
                    new HomeListFragment(),
                    new PlacesSearchFragment(),
                    new SettingFragment(),
            };
            private final String[] mFragmentNames = new String[] {
                    getString(R.string.tab_home),
                    getString(R.string.tab_search),
                    getString(R.string.tab_setting)
            };
            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }
            @Override
            public int getCount() {
                return mFragments.length;
            }
            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }
        };
        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    public String sendData() {
        return YOUR_STRING;
    }


}