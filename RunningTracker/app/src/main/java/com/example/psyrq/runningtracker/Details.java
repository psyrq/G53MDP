package com.example.psyrq.runningtracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class Details extends AppCompatActivity {

    Bundle bundle;

    float[] distances;
    float[] lastDistances;

    long[] durations;
    long[] lastDurations;

    float[] speeds;
    float[] lastSpeeds;

    TextView avgSpeed, fastestSpeed, totalDistance, lastDistance, lastAvgSpeed, lastFastestSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        bundle = getIntent().getExtras();

        distances = bundle.getFloatArray("distances");
        lastDistances = bundle.getFloatArray("lastDistances");

        durations = bundle.getLongArray("durations");
        lastDurations = bundle.getLongArray("lastDurations");

        speeds = bundle.getFloatArray("speeds");
        lastSpeeds = bundle.getFloatArray("lastSpeeds");

        avgSpeed = (TextView)findViewById(R.id.AvgSpeed);
        fastestSpeed = (TextView)findViewById(R.id.FastestSpeed);
        totalDistance = (TextView)findViewById(R.id.TotalDistances);

        lastDistance = (TextView)findViewById(R.id.LastDistance);
        lastAvgSpeed = (TextView)findViewById(R.id.LastAvgSpeed);
        lastFastestSpeed = (TextView)findViewById(R.id.LastFastestSpeed);

        avgSpeed.setText("    average speed in all records: " + avgSpeed(distances, durations) + "m/s");
        fastestSpeed.setText("    fastest speed in all records: " + fastestSpeed(speeds) + "m/s");
        totalDistance.setText("    total distance in all records: " + totalDistances(distances) + "m");

        lastAvgSpeed.setText("    average speed in last records: " + avgSpeed(lastDistances, lastDurations) + "m/s");
        lastFastestSpeed.setText("    fastest speed last all records: " + fastestSpeed(lastSpeeds) + "m/s");
        lastDistance.setText("    total distance in last records: " + totalDistances(lastDistances) + "m");
    }

    private float avgSpeed(float[] distances,long[] durations) {

        return totalDistances(distances) / totalDuration(durations);
    }

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

    private float totalDistances(float[] distances) {

        float totalDistance = 0;

        if(distances != null) {
            for(int i = 0; i < distances.length; i++) {
                totalDistance += distances[i];
            }
        }

        return totalDistance;
    }

    private long totalDuration(long[] durations) {

        long totalDuration = 0;

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
