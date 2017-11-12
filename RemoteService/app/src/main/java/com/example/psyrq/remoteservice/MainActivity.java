package com.example.psyrq.remoteservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button start;
    private Button stop;

    private Button bind;
    private Button cancel;
    private Button status;

    BindService.MyBinder binder;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            System.out.println("------Service Connected-------");
            binder = (BindService.MyBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            System.out.println("------Service DisConnected-------");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = (Button)findViewById(R.id.startService);
        stop = (Button)findViewById(R.id.stopService);

        final Intent intent = new Intent(this, TestService.class);
        intent.setAction("com.example.psyrq.remoteservice.TEST_SERVICE");

        Intent it1 = new Intent(this, TestIntentService.class);
        it1.setAction("com.example.psyrq.remoteservice.Test_Intent_Service");
        Bundle b1 = new Bundle();
        b1.putString("param", "s1");
        it1.putExtras(b1);

        Intent it2 = new Intent(this, TestIntentService.class);
        it2.setAction("com.example.psyrq.remoteservice.Test_Intent_Service");
        Bundle b2 = new Bundle();
        b2.putString("param", "s2");
        it2.putExtras(b2);

        Intent it3 = new Intent(this, TestIntentService.class);
        it3.setAction("com.example.psyrq.remoteservice.Test_Intent_Service");
        Bundle b3 = new Bundle();
        b3.putString("param", "s3");
        it3.putExtras(b3);

        startService(it1);
        startService(it2);
        startService(it3);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(intent);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(intent);
            }
        });

        bind = (Button)findViewById(R.id.Lock);
        cancel = (Button)findViewById(R.id.Unlock);
        status = (Button)findViewById(R.id.GetStatus);

        final Intent bindIntent = new Intent(this, BindService.class);
        bindIntent.setAction("com.example.psyrq.remoteservice.Bind_Service");

        bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //绑定service
                bindService(bindIntent, conn, Context.BIND_AUTO_CREATE);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //解除service绑定
                unbindService(conn);
            }
        });

        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Service的count的值为:"
                        + binder.getCount(), Toast.LENGTH_SHORT).show();
            }
        });

        Log.i("G53MDP", "Main onCreate");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("G53MDP", "Main onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("G53MDP", "Main onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("G53MDP", "Main onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("G53MDP", "Main onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("G53MDP", "Main onStop");
    }
}
