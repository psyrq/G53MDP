package com.example.psyrq.runningtracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class Details extends AppCompatActivity {

    private final String tag = "tracker details";

    Bundle bundle;
    ArrayList<float[]> allDistances;
    ArrayList<float[]> allDurations;
    int numMarkers;

    TextView avgSpeed, fastestSpeed, totalDistance, lastDistance, lastAvgSpeed, lastFastestSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        bundle = getIntent().getExtras();
        getAllInfo();
        Log.i(tag,"number of markers: " + numMarkers);

        avgSpeed = (TextView)findViewById(R.id.AvgSpeed);
        fastestSpeed = (TextView)findViewById(R.id.FastestSpeed);
        totalDistance = (TextView)findViewById(R.id.TotalDistances);

        lastDistance = (TextView)findViewById(R.id.LastDistance);
        lastAvgSpeed = (TextView)findViewById(R.id.LastAvgSpeed);
        lastFastestSpeed = (TextView)findViewById(R.id.LastFastestSpeed);

        //if only one record in the database, there will be no last record
        if(numMarkers <= 1 ) {
            lastAvgSpeed.setText("    average speed: No record");
            lastFastestSpeed.setText("    fastest speed: No record");
            lastDistance.setText("    total distance: No record");
        }

        else {
            lastAvgSpeed.setText("    average speed: " + avgSpeed(allDistances.get(allDistances.size()-2), allDurations.get(allDurations.size()-2)) + "m/s");
            lastFastestSpeed.setText("    fastest speed: " +
                    fastestSpeed(calculateSpeeds(allDistances.get(allDistances.size()-2), allDurations.get(allDurations.size()-2))) + "m/s");
            lastDistance.setText("    total distance: " + totalDistances(allDistances.get(allDistances.size()-2)) + "m");
        }

        avgSpeed.setText("    average speed: " + avgSpeed(allDistances.get(allDistances.size()-1), allDurations.get(allDistances.size()-1)) + "m/s");
        fastestSpeed.setText("    fastest speed: " +
                fastestSpeed(calculateSpeeds(allDistances.get(allDistances.size()-1), allDurations.get(allDurations.size()-1))) + "m/s");
        totalDistance.setText("    total distance: " + totalDistances(allDistances.get(allDistances.size()-1)) + "m");
    }

    //get all information sent from main activity
    private void getAllInfo() {

        numMarkers = bundle.getInt("numMarkers");
        allDistances = new ArrayList<>();
        allDurations = new ArrayList<>();

        for(int i = 0; i < numMarkers; i++) {
            allDistances.add(bundle.getFloatArray("distance " + i));
            allDurations.add(bundle.getFloatArray("duration " + i));
        }
    }

    //calculate average speed
    private float avgSpeed(float[] distances,float[] durations) {
        return totalDistances(distances) / totalDuration(durations);
    }

    //calculate all speeds at each time stamp
    private float[] calculateSpeeds(float[] distances,float[] durations) {

        int size = distances.length;
        float[] speeds = new float[size];

        for(int i = 0; i < size; i++) {
            speeds[i] = distances[i] / durations[i];
        }

        return speeds;
    }

    //get the fastest speed
    public float fastestSpeed(float[] speeds) {

        float fastestSpeed = 0;

        if(speeds != null) {
            for(int i = 0; i < speeds.length-1; i++) {
                if(speeds[i+1] < speeds[i]) {
                    float temp = speeds[i];
                    speeds[i] = speeds[i+1];
                    speeds[i+1] = temp;
                    fastestSpeed = speeds[i+1];
                }
            }
        }

        return fastestSpeed;
    }

    //get the total move distance
    private float totalDistances(float[] distances) {

        float totalDistance = 0;

        if(distances != null) {
            for(int i = 0; i < distances.length; i++) {
                totalDistance += distances[i];
            }
        }

        return totalDistance;
        //return (float)(Math.round(totalDistance*100)/100);
    }

    //get speeds at all time stamps
    private float totalDuration(float[] durations) {

        float totalDuration = 0;

        if(durations != null) {
            for(int i = 0; i < durations.length; i++) {
                totalDuration += durations[i];
            }
        }

        return totalDuration;
    }

    public void backToMain(View v) {
        finish();
    }
}
