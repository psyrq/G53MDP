package com.example.psyrq.mp3player;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

public class MusicService extends Service {

    private final String Tag = "G53MDP";
    private static final File musicPath = Environment.getExternalStorageDirectory();
    public List<String> musicList;
    public MediaPlayer player;
    public int songMum;
    public String songName;

    public MusicService() {
        super();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(Tag, "music service onBind");
        return null;
    }

    class MusicFilter implements FilenameFilter {

        public  boolean accept(File dir, String name) {

            return (name.endsWith(".mp3"));
        }
    }
}
