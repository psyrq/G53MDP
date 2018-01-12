package com.example.psyrq.runningtracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;

/**
 * Created by qianruofei on 2018/1/10.
 */

public class TrackerDBOpenHelper extends SQLiteOpenHelper {

    private final String tag = "database open helper";

    long dateMili = System.currentTimeMillis();
    Date date = new Date(dateMili);

    public TrackerDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "my.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS tracker");
        db.execSQL("CREATE TABLE tracker(trackerId INTEGER PRIMARY KEY AUTOINCREMENT,trackerLatitude double, trackerLongitude double, trackerLocation VARCHAR(128))");
        Log.d(tag, date.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
