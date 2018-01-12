package com.example.psyrq.runningtracker;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class TrackerService extends Service {

    Context mContext;
    private SQLiteDatabase db;
    private TrackerDBOpenHelper myDBHelper;
    private ContentResolver resolver;

    private final String tag = "running tracker service";
    private TrackerBinder binder = new TrackerBinder();

    private double myLatitude;
    private double myLongitude;
    private double[] curCoordinate = new double[2];

    LocationManager locationManager;
    MyLocationListener locationListener;

    public class TrackerBinder extends Binder {

        public double[] getCoordinate() {
            return curCoordinate;
        }

        public float getDistance(Location lastLocation, Location curLocation) {

            float distance = curLocation.distanceTo(lastLocation);
            return distance;
        }

        public Location CoordinateToLocation(double[] coordinate) {

            Location location = new Location("point");
            location.setLatitude(coordinate[0]);
            location.setLongitude(coordinate[1]);

            return location;
        }
    }

    public class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

            myLatitude = location.getLatitude();
            myLongitude = location.getLongitude();

            ContentValues insertValues = new ContentValues();
            insertValues.put("trackerLatitude", myLatitude);
            insertValues.put("trackerLongitude", myLongitude);
            insertValues.put("trackerLocation", location.toString());
            resolver.insert(TrackerProviderContract.URI.ID_INSERT, insertValues);

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

        mContext = TrackerService.this;
        myDBHelper = new TrackerDBOpenHelper(mContext, "tracker.db", null, 1);

        resolver = this.getContentResolver();
        //db = myDBHelper.getWritableDatabase();

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();

        try{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, locationListener);
            curCoordinate[0] = myLongitude;
            curCoordinate[1] = myLatitude;
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
