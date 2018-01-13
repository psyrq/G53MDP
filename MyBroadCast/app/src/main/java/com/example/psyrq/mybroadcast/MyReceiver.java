package com.example.psyrq.mybroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    private final String ACTION_CONFESSION = "com.example.psyrq.testbroadcast.MY_CONFESSION";
    private final String ACTION_GAN = "com.example.psyrq.testbroadcast.MY_GAN";

    @Override
    public void onReceive(Context context, Intent intent) {

        if(ACTION_CONFESSION.equals(intent.getAction()))
            Toast.makeText(context, "receive confession~", Toast.LENGTH_LONG).show();
        else if(ACTION_GAN.equals(intent.getAction()))
            Toast.makeText(context, "guna~", Toast.LENGTH_LONG).show();
    }
}
