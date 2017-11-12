package com.example.psyrq.remoteservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

/**
 * Created by qianruofei on 2017/11/2.
 */

public class TestService extends Service {

    private final String Tag = "TestService";
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(Tag, "service onBind");
        return null;
    }

    @Override
    public void onCreate() {

        super.onCreate();

        Intent resultIntent = new Intent(this, MainActivity.class);
        NotificationManager notifyMamager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder localBuilder =
                new NotificationCompat.Builder(this)
                .setContentIntent(PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("My notification")
                .setContentText("running...");

        notifyMamager.notify(1, localBuilder.build());
        Log.i(Tag, "service onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(Tag, "service onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        Log.i(Tag, "service onDestroy");
    }
}
