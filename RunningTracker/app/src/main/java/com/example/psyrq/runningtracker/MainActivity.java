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
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private final String tag = "running tracker";

    TextView longitude, latitude;
    TrackerService.TrackerBinder binder;

    double[] location = new double[2];

    private Intent intent;
    ServiceConnection trackerConnection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = new Intent(this, TrackerService.class);

        longitude = (TextView)findViewById(R.id.longitudeView);
        latitude = (TextView)findViewById(R.id.latitudeView);

        Log.d(tag, "main onCreate");
    }

    public void start(View v) {
        connectTrackerService();
        longitude.setText("longitude:" + location[0]);
        latitude.setText("latitude: " + location[1]);
        Log.d(tag, "press start button");
    }

    private void connectTrackerService() {

        trackerConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                binder = (TrackerService.TrackerBinder)service;
                location = binder.getLocation();
                Log.d(tag, "connect to tracker service");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d(tag, "disconnect to service");
            }
        };

        bindService(intent, trackerConnection, Service.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
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
