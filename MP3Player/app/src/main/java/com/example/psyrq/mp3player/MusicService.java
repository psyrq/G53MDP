package com.example.psyrq.mp3player;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.File;

public class MusicService extends Service {

    MP3Player myPlayer;
    Intent notifyIntent;
    NotificationManager notifyManager;
    NotificationCompat.Builder notifyBuilder;

    private final String tag = "music service";
    private MusicBinder binder = new MusicBinder();

    public class MusicBinder extends Binder {

        public void load(String filePath) {
            myPlayer.load(filePath);
            Log.i(tag, filePath);
        }

        public void play() {
            myPlayer.play();
            Log.i(tag, "play music");
        }

        public void pause() {
            myPlayer.pause();
            Log.i(tag, "pause music");
        }

        public void stop() {
            myPlayer.stop();
            Log.i(tag, "stop music");
        }

        public int getPrevOrNext(File list[], int songIndex, String prevOrNext) {

            int sumSongs = list.length;
            Log.i(tag, "current song is " + list[songIndex]);

            if(prevOrNext.equals("prev")) {

                if((songIndex-1) < 0) songIndex = sumSongs - 1;
                else songIndex -= 1;
            }
            else {

                if((songIndex+1) > (sumSongs-1)) songIndex = 0;
                else songIndex += 1;
            }
            return songIndex;
        }

    }

    @Override
    public IBinder onBind(Intent intent) {

        Log.i(tag, "onBind");
        return binder;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        myPlayer = new MP3Player();

        notifyIntent = new Intent(this, MainActivity.class);
        notifyManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        notifyBuilder = new NotificationCompat.Builder(this)
                        .setContentIntent(PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle("My Music App")
                        .setContentText("running...");

        notifyManager.notify(1, notifyBuilder.build());
        Log.i(tag, "service onCreate");
        Log.i(tag, "notification created");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        notifyManager.cancel(1);
        myPlayer.stop();
        Log.i(tag, "onUnbind");
        Log.i(tag, "notification cancelled");
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(tag, "onDestroy");
    }

    @Override
    public void onRebind(Intent intent) {
        Log.i(tag, "onRebind");
        super.onRebind(intent);
    }
}
