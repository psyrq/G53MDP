package com.example.psyrq.mp3player;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Environment;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private ListView lv;
    private Intent intent;
    MusicService.MusicBinder binder;
    private ServiceConnection musicConnection = null;

    private int songIndex = 0;
    private File list[];
    private File selectedFromList;
    private final String tag = "music main activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = new Intent(this, MusicService.class);

        setListViewAdapter();
    }

    private void setListViewAdapter() {

        lv = (ListView)findViewById(R.id.Playlist);

        File musicDir = new File(Environment.getExternalStorageDirectory().getPath()+"/Music/");
        list = musicDir.listFiles();
        Log.i(tag, "there are " + list.length + " songs in playlist");

        lv.setAdapter(new ArrayAdapter<File>(this, android.R.layout.simple_list_item_1, list));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {

                selectedFromList =(File) (lv.getItemAtPosition(myItemInt));
                getSongIndex();

                if(musicConnection != null) unbindService(musicConnection);

                musicConnection = new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        binder = (MusicService.MusicBinder)service;
                        binder.load(selectedFromList.getAbsolutePath());
                        Log.i(tag, "connect to service");
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {

                        Log.i(tag, "disconnect to service");
                    }
                };

                bindService(intent, musicConnection, Service.BIND_AUTO_CREATE);

                Log.i(tag, selectedFromList.getAbsolutePath());
            }
        });
    }

    public void play(View v) {
        binder.play();
        Log.i(tag, "play music");
    }

    public void pause(View v) {
        binder.pause();
        Log.i(tag, "pause music");
    }

    public void stop(View v) {
        binder.stop();
        unbindService(musicConnection);
        Log.i(tag, "stop music and disconnect to the service");
    }

    public void prev(View v) {
        binder.stop();
        songIndex = binder.getPrevOrNext(list, songIndex, "prev");
        binder.load(list[songIndex].getAbsolutePath());
    }

    public void next(View v) {
        binder.stop();
        songIndex = binder.getPrevOrNext(list, songIndex, "next");
        binder.load(list[songIndex].getAbsolutePath());
    }

    public void getSongIndex() {
        for(int i = 0; i < list.length; i++) {
            if(list[i].getAbsolutePath().equals(selectedFromList.getAbsolutePath()))
                songIndex = i;
        }
    }

    @Override
    protected void onDestroy() {
        unbindService(musicConnection);
        super.onDestroy();
        Log.i(tag, "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(tag, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(tag, "onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(tag, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(tag, "onStop");
    }
}
