package com.example.psyrq.runningtracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by qianruofei on 2018/1/13.
 */

public class TrackerReceiver extends BroadcastReceiver {

    private final String tag = "tracker receiver";

    private final String ACTION_LOCATION_RECEIVER = "com.example.psyrq.runningtracker.MY_LOCATION_RECEIVER";

    //Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {

        //get content from the tracker servic and resend the location information to the map activity
        if(ACTION_LOCATION_RECEIVER.equals(intent.getAction())) {
            double[] locationInfo = intent.getDoubleArrayExtra("locationMsg");

            Intent mapIntent = new Intent();
            mapIntent.setAction("com.example.psyrq.runningtracker.MY_MAP_RECEIVER");
            mapIntent.putExtra("locationMsg", locationInfo);
            context.sendBroadcast(mapIntent);
        }
    }
}
