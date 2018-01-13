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

    public static int MARKER;

    Context mContext;
    private SQLiteDatabase db;
    private TrackerDBOpenHelper myDBHelper;
    private ContentResolver resolver;

    private final String tag = "running tracker service";
    private TrackerBinder binder = new TrackerBinder();

    private double myLatitude;
    private double myLongitude;
    private long curTime;

    private int i;

    private float[] distances;
    private float[] markedDistances;

    private long[] durations;
    private long[] markedDurations;

    private long[] timeStamps;
    private long[] markedTimeStamps;

    private float[] speeds;
    private float[] markedSpeeds;

    private Location[] allLocations;
    private Location[] markedLocations;

    private double[][] allLocationInfo;
    private double[][] markedLocationInfo;

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

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();

        try{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, locationListener);
            curLocationInfo[0] = myLatitude;
            curLocationInfo[1] = myLongitude;
        }catch(SecurityException se) {
            Log.i(tag, se.toString());
        }

        Log.i(tag, "onCreate");
    }

    public class TrackerBinder extends Binder {

        //get current location
        public double[] getCurLocationInfo() {
            return curLocationInfo;
        }

        public double[][] getAllLocationInfo() {
            return allLocationInfo;
        }

        public long[] getAllTimeInfo() {
            return timeStamps;
        }

        public float[] getAllDistances() {
            return distances;
        }

        public long[] getAllDurations() {
            return durations;
        }

        public float[] getAllSpeeds() {
            return speeds;
        }

        public float[] getMarkedDistances() {
            return markedDistances;
        }

        public long[] getMarkedDurations() {
            return markedDurations;
        }

        public float[] getMarkedSpeeds() {
            return markedSpeeds;
        }

        public void initialization() {

            int numOfAllRecords = getCount();

            Cursor cursor = db.rawQuery("select * from tracker where trackerId = ?", new String[]{String.valueOf(numOfAllRecords)});
            if(cursor.moveToFirst())
                MARKER = cursor.getInt(cursor.getColumnIndex("trackerMarker"));
            cursor.close();

            distances = new float[numOfAllRecords-1];
            durations = new long[numOfAllRecords-1];
            speeds = new float[numOfAllRecords-1];
            timeStamps = new long[numOfAllRecords];
            allLocationInfo = new double[numOfAllRecords][2];
            allLocations = new Location[numOfAllRecords];

            int numOfMarkedRecords;
            if(MARKER != 0) {
                numOfMarkedRecords = getCount(MARKER-1);
                markedDistances = new float[numOfMarkedRecords-1];
                markedDurations = new long[numOfMarkedRecords-1];
                markedSpeeds = new float[numOfMarkedRecords-1];
                markedTimeStamps = new long[numOfMarkedRecords-1];
                markedLocationInfo = new double[numOfMarkedRecords][2];
                markedLocations = new Location[numOfMarkedRecords];
            }

        }

        //get all coordinates of locations in the database
        public void getALlCoordinates() {

            i = 0;
            Cursor cursor = resolver.query(TrackerProviderContract.URI.ID_QUERY, null, null, null, null);

            if(cursor.moveToFirst()) {
                do{
                    allLocationInfo[i][0] = cursor.getDouble(cursor.getColumnIndex("trackerLatitude"));
                    allLocationInfo[i][1] = cursor.getDouble(cursor.getColumnIndex("trackerLongitude"));
                    timeStamps[i] = cursor.getLong(cursor.getColumnIndex("trackerTime"));
                    i++;
                }while(cursor.moveToNext());
            }
        }

        public void getAllMarkedCoordinates(int marker) {

            i = 0;
            if(marker != 0) {
                Cursor cursor = resolver.query(TrackerProviderContract.URI.ID_QUERY, null, "trackerMarker = ?", new String[]{String.valueOf(marker)}, null);
                if(cursor.moveToFirst()) {
                    do{
                        markedLocationInfo[i][0] = cursor.getDouble(cursor.getColumnIndex("trackerLatitude"));
                        markedLocationInfo[i][1] = cursor.getDouble(cursor.getColumnIndex("trackerLongitude"));
                        markedTimeStamps[i] = cursor.getLong(cursor.getColumnIndex("trackerTime"));
                        i++;
                    }while(cursor.moveToNext());
                }
            }
        }

        //calculate distances and speeds between two locations sequentially in the database
        public void calculations() {

            for(int i = 0; i < allLocationInfo.length; i++) {
                allLocations[i] = coordinateToLocation(allLocationInfo[i][0], allLocationInfo[i][1], timeStamps[i]);
            }

            for(int i = 0; i < allLocations.length-1; i++) {

                distances[i] = getDistance(allLocations[i], allLocations[i+1]);
                durations[i] = getTime(allLocations[i], allLocations[i+1]);
                speeds[i] = distances[i] / durations[i];
                //Log.i(tag, "distances: " + String.valueOf(distances[i]) + " speed(m/s): " + String.valueOf(speeds[i]));
            }

            if(MARKER != 0) {
                for(int i = 0; i < markedLocationInfo.length; i++) {
                    markedLocations[i] = coordinateToLocation(markedLocationInfo[i][0], markedLocationInfo[i][1], markedTimeStamps[i]);
                }

                for(int i = 0; i < markedLocations.length-1; i++) {

                    markedDistances[i] = getDistance(markedLocations[i], markedLocations[i+1]);
                    markedDurations[i] = getTime(markedLocations[i], markedLocations[i+1]);
                    markedSpeeds[i] = markedDistances[i] / markedDurations[i];
                }
            }
        }

        //create a location with the saved latitude, longitude and durations
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

        //get the number of record in the database
        public int getCount() {

            Cursor cursor = db.rawQuery("SELECT COUNT (*) FROM tracker", null);
            cursor.moveToFirst();
            int result = cursor.getInt(0);
            cursor.close();

            return result;
        }

        public int getCount(int marker) {

            Cursor cursor = db.rawQuery("SELECT Count (*) FROM tracker where trackerMarker = ?", new String[]{String.valueOf(marker)});
            cursor.moveToFirst();
            int result = cursor.getInt(0);
            cursor.close();

            return result;
        }

        //get distances between two different locations
        private float getDistance(Location lastLocation, Location curLocation) {

            float distance = curLocation.distanceTo(lastLocation);
            return distance;
        }

        //get durations durations between two different locations
        private long getTime(Location lastLocation, Location curLocation) {

            long time = curLocation.getTime() - lastLocation.getTime();
            return time / 1000;
        }
    }

    public class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

            myLatitude = location.getLatitude();
            myLongitude = location.getLongitude();
            curTime = location.getTime();

            ContentValues insertValues = new ContentValues();
            insertValues.put("trackerMarker", MARKER);
            insertValues.put("trackerLatitude", myLatitude);
            insertValues.put("trackerLongitude", myLongitude);
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
