package com.example.psyrq.runningtracker;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class TrackerService extends Service {

    Context mContext;
    private SQLiteDatabase db;
    private TrackerDBOpenHelper myDBHelper;
    private ContentResolver resolver;

    private final String tag = "running tracker service";
    private TrackerBinder binder = new TrackerBinder();

    int lastMarkers = 0;
    private double[] curLocationInfo = new double[2];

    LocationManager locationManager;
    MyLocationListener locationListener;

    @Override
    public void onCreate() {

        super.onCreate();

        resolver = this.getContentResolver();

        mContext = TrackerService.this;
        myDBHelper = new TrackerDBOpenHelper(mContext, "my.db", null, 1);
        db = myDBHelper.getWritableDatabase();

        //set the location listener when movement happened
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();

        try{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
        }catch(SecurityException se) {
            Log.i(tag, se.toString());
        }


        Log.i(tag, "onCreate");
    }

    public class TrackerBinder extends Binder {

        public int getLastMarkers() {
            return lastMarkers();
        }

        //get all records in the databas for some period of running
        public Location[] getMarkedLocations(int marker) {

            int i = 0;
            int markedRecord = getCount(marker);
            double[][] locationInfo = new double[markedRecord][2];
            long[] timeStamps = new long[markedRecord];
            Location[] locations = new Location[markedRecord];

            Cursor cursor = resolver.query(TrackerProviderContract.URI.ID_QUERY, null, "trackerMarker = ?", new String[]{String.valueOf(marker)}, null);
            if(cursor.moveToFirst()) {
                do{
                    //package the latitude, longitude and time into a location object
                    locationInfo[i][0] = cursor.getDouble(cursor.getColumnIndex("trackerLatitude"));
                    locationInfo[i][1] = cursor.getDouble(cursor.getColumnIndex("trackerLongitude"));
                    timeStamps[i] = cursor.getLong(cursor.getColumnIndex("trackerTime"));
                    locations[i] = coordinateToLocation(locationInfo[i][0], locationInfo[i][1], timeStamps[i]);
                    i++;
                }while(cursor.moveToNext());
            }
            cursor.close();

            return locations;
        }

        //calculate distances between two locations sequentially in some period
        public float[] calculateDistances(int marker) {

            Location[] markedLocations = getMarkedLocations(marker);
            float[] distances = new float[markedLocations.length-1];

            for(int i = 0; i < distances.length; i++) {
                distances[i] = getDistance(markedLocations[i], markedLocations[i+1]);
            }

            return distances;
        }

        //calculate time differences between two locations sequentially in some period
        public float[] calculateDurations(int marker) {

            Location[] markedLocations = getMarkedLocations(marker);
            float[] durations = new float[markedLocations.length-1];

            for(int i = 0; i < durations.length; i++) {
                durations[i] = getDuration(markedLocations[i], markedLocations[i+1]);
            }

            return durations;
        }
        //package a location with the saved latitude, longitude and durations
        private Location coordinateToLocation(double latitude, double longitude, long time) {

            Location location = new Location("point");
            location.setLatitude(latitude);
            location.setLongitude(longitude);
            location.setTime(time);

            return location;
        }

        //clear all the record in the databases
        public void DeleteRecords() {

            resolver.delete(TrackerProviderContract.URI.ID_DELETE, null, null);
            Toast.makeText(getApplicationContext(), "clear all records", Toast.LENGTH_SHORT).show();
        }

        //get the marker of last record in the database
        public int lastMarkers() {

            Cursor cursor = db.rawQuery("select * from tracker order by trackerId desc limit 0,1", null);
            if(cursor.moveToFirst()) {
                lastMarkers = cursor.getInt(cursor.getColumnIndex("trackerMarker"));
            }

            return lastMarkers;
        }

        //get how many records for a period of running marked by the marker
        private int getCount(int marker) {

            int result = 0;
            Cursor cursor = db.rawQuery("SELECT Count (*) FROM tracker where trackerMarker = ?", new String[]{String.valueOf(marker)});
            if(cursor.moveToFirst())
                result = cursor.getInt(0);
            cursor.close();

            return result;
        }

        //get distances between two different locations
        private float getDistance(Location lastLocation, Location curLocation) {
            return curLocation.distanceTo(lastLocation);
        }

        //get durations durations between two different locations
        private float getDuration(Location lastLocation, Location curLocation) {
            return (curLocation.getTime() - lastLocation.getTime()) / 1000;
        }
    }

    //implement the location listener
    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

            Intent locationIntent = new Intent();
            locationIntent.setAction("com.example.psyrq.runningtracker.MY_LOCATION_RECEIVER");

            curLocationInfo[0] = location.getLatitude();
            curLocationInfo[1] = location.getLongitude();
            long curTime = location.getTime();

            //send the broadcast, current location information included
            locationIntent.putExtra("locationMsg", curLocationInfo);
            sendBroadcast(locationIntent);

            //insert the location information into database
            ContentValues insertValues = new ContentValues();
            insertValues.put("trackerMarker", MainActivity.MARKER);
            insertValues.put("trackerLatitude", curLocationInfo[0]);
            insertValues.put("trackerLongitude", curLocationInfo[1]);
            insertValues.put("trackerTime", curTime);
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
            locationManager = null;
            locationListener = null;
            Log.d(tag, "onProviderDisabled: " + provider);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(tag, "onBind");
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        super.onUnbind(intent);
        //stop updating when the service is stopped
        locationManager.removeUpdates(locationListener);
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
