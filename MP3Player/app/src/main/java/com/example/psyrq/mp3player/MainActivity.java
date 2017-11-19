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
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private ListView lv;
    private Intent intent;
    MusicService.MusicBinder binder;
    private ServiceConnection musicConnection = null;

    private TextView musicInfo = null;
    private SeekBar musicProcess = null;

    private int songIndex = 0;

    private File list[];
    private File selectedFromList;
    private final String tag = "music main activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = new Intent(this, MusicService.class);

        musicInfo = (TextView) findViewById(R.id.musicInfo);
        musicProcess = (SeekBar) findViewById(R.id.musicProcess);

        trackSeekBar();

        setListViewAdapter();
    }

    private void setListViewAdapter() {

        lv = (ListView)findViewById(R.id.Playlist);

        File musicDir = new File(Environment.getExternalStorageDirectory().getPath()+"/Music/");
        list = musicDir.listFiles();

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
                musicProcess.postDelayed(updatePerSec, 1000);

                Log.i(tag, selectedFromList.getAbsolutePath());
            }
        });
    }

    public void play(View v) {

        if(binder != null)
            binder.play();
    }

    public void pause(View v) {
        if(binder != null)
            binder.pause();
    }

    public void stop(View v) {

        if(binder != null) {
            binder.stop();
            musicProcess.setProgress(0);
            musicInfo.setText("");
        }

    }

    public void prev(View v) {

        if(binder != null) {
            binder.stop();
            songIndex = binder.getPrevOrNext(list, songIndex, "prev");
            binder.load(list[songIndex].getAbsolutePath());
            musicProcess.postDelayed(updatePerSec, 1000);
        }
    }

    public void next(View v) {

        if(binder != null) {
            binder.stop();
            songIndex = binder.getPrevOrNext(list, songIndex, "next");
            binder.load(list[songIndex].getAbsolutePath());
            musicProcess.postDelayed(updatePerSec, 1000);
        }
    }

    public void getSongIndex() {

        for(int i = 0; i < list.length; i++) {
            if(list[i].getAbsolutePath().equals(selectedFromList.getAbsolutePath()))
                songIndex = i;
        }
    }

    private Runnable updatePerSec = new Runnable() {

        @Override
        public void run() {
            if(musicProcess != null) {
                if(binder.getDuration() != 0) {
                    musicProcess.setProgress(binder.getProgress() * musicProcess.getMax() / binder.getDuration());
                    setMusicInfo(selectedFromList.getAbsolutePath(), translateTimeFormat(binder.getProgress()), translateTimeFormat(binder.getDuration()));
                }

                if(binder.getState().equals(MP3Player.MP3PlayerState.PLAYING))
                    musicProcess.postDelayed(updatePerSec, 1000);
            }
        }
    };

    private void setMusicInfo(String songName, String songProgress, String songDuration) {

        songName = binder.setPlayName(binder.getFilePath());
        musicInfo.setText("current: " + songName + "  " + songProgress + "/" + songDuration);
    }

    private String translateTimeFormat(long msec) {

        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (msec / (1000 * 60 * 60));
        int minutes = (int) (msec % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((msec % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    private void trackSeekBar() {

        musicProcess.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                binder.seekTo(binder.getDuration() * musicProcess.getProgress() / seekBar.getMax());
            }
        });
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
