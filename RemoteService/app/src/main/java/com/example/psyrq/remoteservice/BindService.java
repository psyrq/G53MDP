package com.example.psyrq.remoteservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class BindService extends Service {

    private final String Tag = "BindService";
    private int count;
    private boolean quit;


    private MyBinder binder = new MyBinder();

    public class MyBinder extends Binder {

        public int getCount() {
            return count;
        }
    }

    public BindService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        Log.i(Tag, "service onBind");
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(Tag, "service onCreate");

        new Thread() {

            public void run() {

                while (!quit) {

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    count++;
                }
            }
        }.start();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(Tag, "service onUnbind");
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.quit = true;
        Log.i(Tag, "service onDestroy");
    }

    @Override
    public void onRebind(Intent intent) {
        Log.i(Tag, "service onRebind");
        super.onRebind(intent);
    }
}
