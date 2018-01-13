package com.example.psyrq.runningtracker;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final String tag = "running tracker";

    TrackerService.TrackerBinder binder;

    private Intent intent;
    ServiceConnection trackerConnection = null;

    private Button startBtn, pauseBtn, detailBtn, mapBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = new Intent(this, TrackerService.class);

        startBtn = (Button)findViewById(R.id.start);
        pauseBtn = (Button)findViewById(R.id.pause);
        detailBtn = (Button)findViewById(R.id.showDetails);
        mapBtn = (Button)findViewById(R.id.ShowMap);

        pauseBtn.setEnabled(false);

        Log.d(tag, "main onCreate");
    }

    public void start(View v) {

        startBtn.setEnabled(false);
        pauseBtn.setEnabled(true);
        connectTrackerService();
        Toast.makeText(getApplicationContext(), "Recording start", Toast.LENGTH_SHORT).show();
        Log.d(tag, "start record");
    }

    public void pause(View v) {

        startBtn.setEnabled(true);
        pauseBtn.setEnabled(false);

        unbindService(trackerConnection);
        stopService(intent);
        trackerConnection = null;
        TrackerService.MARKER += 1;
        Toast.makeText(getApplicationContext(), "Recording stop", Toast.LENGTH_SHORT).show();
        Log.d(tag, "pause record");
    }

    public void showDetails(View v) {

        float[] distances;
        float[] lastDistances;

        long[] durations;
        long[] lastDurations;

        float[] speeds;
        float[] lastSpeeds;

        if(binder != null) {

            int numAllRecords = binder.getCount();
            int numMarkedRecords = binder.getCount(TrackerService.MARKER);

            binder.initialization();
            binder.getALlCoordinates();
            binder.getAllMarkedCoordinates(TrackerService.MARKER);
            binder.calculations();
            Log.d(tag, "number of records: " + numAllRecords);

            distances = new float[numAllRecords-1];
            durations = new long[numAllRecords-1];
            speeds = new float[numAllRecords-1];

            lastDistances = new float[numMarkedRecords-1];
            lastDurations = new long[numMarkedRecords-1];
            lastSpeeds = new float[numMarkedRecords-1];

            distances = binder.getAllDistances();
            durations = binder.getAllDurations();
            speeds = binder.getAllSpeeds();

            lastDistances = binder.getMarkedDistances();
            lastDurations = binder.getMarkedDurations();
            lastSpeeds = binder.getMarkedSpeeds();

            Intent detailsIntent = new Intent(this, Details.class);
            Bundle bundle = new Bundle();

            bundle.putFloatArray("distances", distances);
            bundle.putLongArray("durations", durations);
            bundle.putFloatArray("speeds", speeds);

            bundle.putFloatArray("lastDistances", lastDistances);
            bundle.putLongArray("lastDurations", lastDurations);
            bundle.putFloatArray("lastSpeeds", lastSpeeds);

            detailsIntent.putExtras(bundle);
            startActivity(detailsIntent);
        }
    }

    public void showRouteOnMap(View v) {

        Intent mapIntent = new Intent(this, MapsActivity.class);
        startActivity(mapIntent);
    }

    private void connectTrackerService() {

        if(trackerConnection == null) {

            trackerConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    binder = (TrackerService.TrackerBinder)service;
                    Log.d(tag, "connect to tracker service");
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    Log.d(tag, "disconnect to service");
                }
            };

            bindService(intent, trackerConnection, Service.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onDestroy() {
        unbindService(trackerConnection);
        super.onDestroy();
        Log.d(tag, "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(tag, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(tag, "onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(tag, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(tag, "onStop");
    }
}
