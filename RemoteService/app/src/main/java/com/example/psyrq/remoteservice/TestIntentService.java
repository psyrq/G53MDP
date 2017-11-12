package com.example.psyrq.remoteservice;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by qianruofei on 2017/11/10.
 */

public class TestIntentService extends IntentService {

    private final String Tag = "haha";

    public TestIntentService() {
        super("TestIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String action = intent.getExtras().getString("param");

        if(action.equals("s1"))
            Log.i(Tag, "start service 1");
        else if(action.equals("s2"))
            Log.i(Tag, "start service 2");
        else
            Log.i(Tag, "start service 3");

        try {
            Thread.sleep(2000);
        } catch(InterruptedException e) {e.printStackTrace();}
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(Tag,"intent service onBind");
        return super.onBind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(Tag,"intent service onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void setIntentRedelivery(boolean enabled) {
        super.setIntentRedelivery(enabled);
        Log.i(Tag,"intent service setIntentRedelivery");
    }

    @Override
    public void onDestroy() {
        Log.i(Tag,"intent service onDestroy");
        super.onDestroy();
    }
}
