package com.example.psyrq.runningtracker;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //indicate which period of running it is
    public static int MARKER;
    private final String tag = "tracker main";

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

        //open the access location permission
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        Log.d(tag, "main onCreate");
    }

    //bind the buttons with their view
    public void start(View v) {

        //if start button has not been pressed, the pause button cannot be press
        startBtn.setEnabled(false);
        pauseBtn.setEnabled(true);
        //press start button to connect to the service
        connectTrackerService();
        Toast.makeText(getApplicationContext(), "Recording start", Toast.LENGTH_SHORT).show();
        Log.d(tag, "start record");
    }

    public void pause(View v) {

        //
        startBtn.setEnabled(true);
        pauseBtn.setEnabled(false);

        //disconnect to the service
        unbindService(trackerConnection);
        stopService(intent);
        trackerConnection = null;
        binder = null;
        Toast.makeText(getApplicationContext(), "Recording stop", Toast.LENGTH_SHORT).show();
        Log.d(tag, "pause record, cur curMarker: " + MARKER);
    }

    public void showDetails(View v) {

        if(binder != null) {
            //send bundle to the details activity
            int numMarkers = binder.getLastMarkers();
            Intent detailsIntent = new Intent(this, Details.class);
            Bundle bundle = new Bundle();
            bundle.putInt("numMarkers", numMarkers);
            Log.i(tag, "current Marker number: " + numMarkers);

            for(int i = 0; i < numMarkers; i++) {
                bundle.putFloatArray("distance " + i, binder.calculateDistances(i+1));
                bundle.putFloatArray("duration " + i, binder.calculateDurations(i+1));
            }

            detailsIntent.putExtras(bundle);
            startActivity(detailsIntent);
        }
    }

    public void showRouteOnMap(View v) {
        //jump to map activity to show map
        Intent mapIntent = new Intent(this, MapsActivity.class);
        startActivity(mapIntent);
    }

    //connect to tracker service
    private void connectTrackerService() {

        if(trackerConnection == null) {

            trackerConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    binder = (TrackerService.TrackerBinder)service;
                    MARKER = binder.getLastMarkers()+1;
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (permissions.length == 1 &&
                permissions[0] == android.Manifest.permission.ACCESS_FINE_LOCATION &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            //mMap.setMyLocationEnabled(true);
        } else {
            // Permission was denied. Display an error message.
        }
    }

    @Override
    protected void onDestroy() {
        //stop service when activity is destroyed
        unbindService(trackerConnection);
        stopService(intent);
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
