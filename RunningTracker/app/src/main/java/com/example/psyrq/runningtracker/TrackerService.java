package com.example.psyrq.runningtracker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class TrackerService extends Service {

    private final String tag = "running tracker service";
    private TrackerBinder binder = new TrackerBinder();

    private double myLatitude;
    private double myLongitude;
    private double[] curLocation = new double[2];

    LocationManager locationManager;
    MyLocationListener locationListener;

    public class TrackerBinder extends Binder {

//        public void start() {
//
//            locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//            locationListener = new MyLocationListener();
//
//            try{
//                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, locationListener);
//                curLocation[0] = myLongitude;
//                curLocation[1] = myLatitude;
//            }catch(SecurityException se) {
//                Log.i(tag, se.toString());
//            }
//        }

        public double[] getLocation() {
            return curLocation;
        }
    }

    public class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            myLatitude = location.getLatitude();
            myLongitude = location.getLongitude();
            Log.d(tag, location.getLatitude() + " " + location.getLongitude());
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(tag, "onStatusChanged: " + provider + " " + status);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d(tag, "onProviderEnabled: " + provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d(tag, "onProviderDisabled: " + provider);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(tag, "onBind");
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();

        try{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, locationListener);
            curLocation[0] = myLongitude;
            curLocation[1] = myLatitude;
        }catch(SecurityException se) {
            Log.i(tag, se.toString());
        }

        Log.i(tag, "onCreate");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        super.onUnbind(intent);
        Log.d(tag, "onUnbind");
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(tag, "onDestroy");
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d(tag, "onRebind");
    }
}
