package com.example.psyrq.mp3player;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MusicService extends Service {

    MP3Player myPlayer;
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
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(tag, "onUnbind");
        myPlayer.stop();
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
